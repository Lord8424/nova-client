package nova.modules;

import nova.core.Category;
import nova.core.Module;
import nova.setting.BoolSetting;
import nova.util.MoveUtil;

public class Sprint extends Module {
	private final BoolSetting omni = add(new BoolSetting("Omni-directional", false));

	public Sprint() { super("Sprint", "Always sprint while moving.", Category.MOVEMENT); }

	@Override public void onTick() {
		var p = mc.player;
		if (p.isSneaking() || p.horizontalCollision || p.getHungerManager().getFoodLevel() <= 6) return;
		if (p.input.movementForward > 0 || (omni.get() && MoveUtil.isMoving())) p.setSprinting(true);
	}
}
