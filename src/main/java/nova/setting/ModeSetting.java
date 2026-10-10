package nova.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class ModeSetting extends Setting {
	public final String[] modes;
	private int index;
	public ModeSetting(String name, String... modes) { super(name); this.modes = modes; }
	public String get() { return modes[index]; }
	public boolean is(String m) { return modes[index].equalsIgnoreCase(m); }
	public void next() { index = (index + 1) % modes.length; }
	public void prev() { index = (index - 1 + modes.length) % modes.length; }
	@Override public JsonElement save() { return new JsonPrimitive(get()); }
	@Override public void load(JsonElement e) {
		if (!e.isJsonPrimitive()) return;
		for (int i = 0; i < modes.length; i++) if (modes[i].equalsIgnoreCase(e.getAsString())) index = i;
	}
}
