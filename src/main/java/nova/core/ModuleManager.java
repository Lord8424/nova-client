package nova.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import nova.NovaClient;
import nova.modules.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
	public final List<Module> all = new ArrayList<>();

	public ModuleManager() {
		all.add(new KillAura());
		all.add(new AutoTotem());
		all.add(new Flight());
		all.add(new Speed());
		all.add(new Sprint());
		all.add(new Step());
		all.add(new NoFall());
		all.add(new Scaffold());
		all.add(new GlowEsp());
		all.add(new Fullbright());
		all.add(new ThemeModule());
		all.add(new HudModule());
	}

	public <T extends Module> T get(Class<T> c) {
		for (Module m : all) if (m.getClass() == c) return c.cast(m);
		return null;
	}

	public List<Module> in(Category c) {
		List<Module> l = new ArrayList<>();
		for (Module m : all) if (m.category == c) l.add(m);
		return l;
	}

	public void tick(MinecraftClient c) {
		if (c.player == null || c.world == null) return;
		for (Module m : all) {
			if (m.key != -1 && c.currentScreen == null) {
				boolean down = InputUtil.isKeyPressed(c.getWindow().getHandle(), m.key);
				if (down && !m.keyDown) m.toggle();
				m.keyDown = down;
			}
			if (m.isEnabled()) {
				try { m.onTick(); }
				catch (Throwable t) { NovaClient.LOG.error("Tick error in {}", m.name, t); m.setEnabled(false); }
			}
		}
	}

	public void worldLeft() {
		for (Module m : all) if (m.category != Category.CLIENT) m.forceOff();
	}
}
