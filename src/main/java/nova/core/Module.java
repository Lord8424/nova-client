package nova.core;

import net.minecraft.client.MinecraftClient;
import nova.NovaClient;
import nova.setting.Setting;
import nova.util.Anim;
import java.util.ArrayList;
import java.util.List;

public abstract class Module {
	protected static final MinecraftClient mc = MinecraftClient.getInstance();

	public final String name, description;
	public final Category category;
	public final List<Setting> settings = new ArrayList<>();
	public int key = -1;
	public boolean keyDown;
	/** HUD slide animation. */
	public final Anim slide = new Anim(0, 14);
	private boolean enabled;

	protected Module(String name, String description, Category category) {
		this.name = name; this.description = description; this.category = category;
	}

	protected <T extends Setting> T add(T s) { settings.add(s); return s; }

	public boolean isEnabled() { return enabled; }
	public void toggle() { setEnabled(!enabled); }

	public void setEnabled(boolean v) {
		if (v == enabled) return;
		enabled = v;
		try { if (v) onEnable(); else onDisable(); }
		catch (Throwable t) { NovaClient.LOG.error("Module {} failed", name, t); }
	}

	/** Reset state without running onDisable (used when leaving a world). */
	public void forceOff() { enabled = false; }

	/** Short text shown after the name in the HUD list. */
	public String suffix() { return ""; }

	public void onEnable() {}
	public void onDisable() {}
	public void onTick() {}
}
