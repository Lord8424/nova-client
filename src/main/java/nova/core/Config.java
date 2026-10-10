package nova.core;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import nova.NovaClient;
import nova.setting.Setting;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class Config {
	private Config() {}
	/** category name -> {x, y, open(0/1)} */
	public static final Map<String, float[]> panels = new HashMap<>();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private static Path file() { return FabricLoader.getInstance().getConfigDir().resolve("nova.json"); }

	public static void save() {
		try {
			JsonObject root = new JsonObject(), mods = new JsonObject(), pan = new JsonObject();
			for (Module m : NovaClient.modules.all) {
				JsonObject o = new JsonObject(), s = new JsonObject();
				o.addProperty("key", m.key);
				for (Setting st : m.settings) s.add(st.name, st.save());
				o.add("settings", s);
				mods.add(m.name, o);
			}
			for (var e : panels.entrySet()) {
				JsonArray a = new JsonArray();
				for (float f : e.getValue()) a.add(f);
				pan.add(e.getKey(), a);
			}
			root.add("modules", mods);
			root.add("panels", pan);
			Files.writeString(file(), GSON.toJson(root));
		} catch (Exception ex) { NovaClient.LOG.error("Config save failed", ex); }
	}

	public static void load() {
		try {
			if (!Files.exists(file())) return;
			JsonObject root = JsonParser.parseString(Files.readString(file())).getAsJsonObject();
			JsonObject mods = root.getAsJsonObject("modules");
			if (mods != null) for (Module m : NovaClient.modules.all) {
				if (!mods.has(m.name)) continue;
				JsonObject o = mods.getAsJsonObject(m.name);
				if (o.has("key")) m.key = o.get("key").getAsInt();
				JsonObject s = o.getAsJsonObject("settings");
				if (s != null) for (Setting st : m.settings) if (s.has(st.name)) st.load(s.get(st.name));
			}
			JsonObject pan = root.getAsJsonObject("panels");
			if (pan != null) for (var e : pan.entrySet()) {
				JsonArray a = e.getValue().getAsJsonArray();
				panels.put(e.getKey(), new float[]{a.get(0).getAsFloat(), a.get(1).getAsFloat(), a.get(2).getAsFloat()});
			}
		} catch (Exception ex) { NovaClient.LOG.error("Config load failed", ex); }
	}
}
