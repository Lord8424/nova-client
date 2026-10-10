package nova.modules;

import net.minecraft.entity.attribute.EntityAttributes;
import nova.core.Category;
import nova.core.Module;
import nova.setting.NumSetting;

public class Step extends Module {
	private final NumSetting height = add(new NumSetting("Height", 2, 1, 5, 0.5));

	public Step() { super("Step", "Walk up blocks without jumping.", Category.MOVEMENT); }

	@Override public String suffix() { return height.display(); }

	@Override public void onTick() {
		var attr = mc.player.getAttributeInstance(EntityAttributes.STEP_HEIGHT);
		if (attr != null) attr.setBaseValue(height.get());
	}

	@Override public void onDisable() {
		if (mc.player == null) return;
		var attr = mc.player.getAttributeInstance(EntityAttributes.STEP_HEIGHT);
		if (attr != null) attr.setBaseValue(0.6);
	}
}
