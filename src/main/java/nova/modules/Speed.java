package nova.modules;

import net.minecraft.util.math.Vec3d;
import nova.core.Category;
import nova.core.Module;
import nova.setting.ModeSetting;
import nova.setting.NumSetting;
import nova.util.MoveUtil;

public class Speed extends Module {
	private final ModeSetting mode = add(new ModeSetting("Mode", "Strafe", "Bhop"));
	private final NumSetting speed = add(new NumSetting("Speed", 1.4, 0.5, 5, 0.1));

	public Speed() { super("Speed", "Move faster on the ground.", Category.MOVEMENT); }

	@Override public String suffix() { return mode.get(); }

	@Override public void onTick() {
		var p = mc.player;
		if (!MoveUtil.isMoving() || p.isSneaking() || p.isTouchingWater() || p.isInLava()) return;
		if (mode.is("Bhop") && p.isOnGround()) p.jump();
		Vec3d v = MoveUtil.dir(0.28 * speed.get());
		p.setVelocity(v.x, p.getVelocity().y, v.z);
	}
}
