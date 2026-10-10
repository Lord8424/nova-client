package nova.modules;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import nova.core.Category;
import nova.core.Module;
import nova.setting.BoolSetting;
import java.util.HashSet;
import java.util.Set;

public class GlowEsp extends Module {
	private final BoolSetting players = add(new BoolSetting("Players", true));
	private final BoolSetting hostile = add(new BoolSetting("Hostile", true));
	private final BoolSetting passive = add(new BoolSetting("Passive", false));
	private final Set<Entity> glowing = new HashSet<>();

	public GlowEsp() { super("GlowESP", "Outlines entities through walls.", Category.RENDER); }

	@Override public void onTick() {
		Set<Entity> seen = new HashSet<>();
		for (Entity e : mc.world.getEntities()) {
			if (e == mc.player || !(e instanceof LivingEntity)) continue;
			boolean match = (e instanceof PlayerEntity && players.get())
				|| (e instanceof HostileEntity && hostile.get())
				|| (e instanceof AnimalEntity && passive.get());
			if (match) { e.setGlowing(true); seen.add(e); }
		}
		for (Entity e : glowing) if (!seen.contains(e)) e.setGlowing(false);
		glowing.clear();
		glowing.addAll(seen);
	}

	@Override public void onDisable() {
		for (Entity e : glowing) e.setGlowing(false);
		glowing.clear();
	}
}
