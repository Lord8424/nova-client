package nova.modules;

import net.minecraft.util.math.Vec3d;
import nova.core.Category;
import nova.core.Module;
import nova.setting.ModeSetting;
import nova.setting.NumSetting;
import nova.util.MoveUtil;

public class Flight extends Module {
	private final ModeSetting mode = add(new ModeSetting("Mode", "Creative", "Velocity"));
	private final NumSetting speed = add(new NumSetting("Speed", 2, 0.5, 10, 0.5));

	public Flight() { super("Flight", "Fly around freely.", Category.MOVEMENT); }

	@Override public String suffix() { return mode.get(); }

	@Override public void onTick() {
		var p = mc.player;
		var ab = p.getAbilities();
		if (mode.is("Creative")) {
			ab.allowFlying = true;
			ab.flying = true;
			ab.setFlySpeed(0.05f * speed.f());
		} else {
			if (!p.isCreative() && !p.isSpectator()) { ab.flying = false; ab.setFlySpeed(0.05f); }
			Vec3d v = MoveUtil.dir(speed.get() * 0.5);
			double y = 0;
			if (mc.options.jumpKey.isPressed()) y += speed.get() * 0.5;
			if (mc.options.sneakKey.isPressed()) y -= speed.get() * 0.5;
			p.setVelocity(v.x, y, v.z);
			p.fallDistance = 0;
		}
	}

	@Override public void onDisable() {
		if (mc.player == null) return;
		var p = mc.player;
		var ab = p.getAbilities();
		ab.setFlySpeed(0.05f);
		if (!p.isCreative() && !p.isSpectator()) { ab.allowFlying = false; ab.flying = false; }
	}
}
