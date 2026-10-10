package nova.modules;

import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import nova.core.Category;
import nova.core.Module;
import nova.setting.NumSetting;

public class AutoTotem extends Module {
	private final NumSetting health = add(new NumSetting("Health Below", 20, 1, 20, 1));
	private final NumSetting delay = add(new NumSetting("Delay (ticks)", 2, 0, 10, 1));
	private int wait;

	public AutoTotem() { super("AutoTotem", "Keeps a totem in your offhand.", Category.COMBAT); }

	@Override public void onTick() {
		var p = mc.player;
		if (mc.currentScreen != null || p.getHealth() > health.get()) return;
		if (p.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)) { wait = 0; return; }
		if (++wait <= delay.i()) return;
		var handler = p.playerScreenHandler;
		for (int i = 9; i <= 44; i++) {
			if (handler.getSlot(i).getStack().isOf(Items.TOTEM_OF_UNDYING)) {
				mc.interactionManager.clickSlot(handler.syncId, i, 40, SlotActionType.SWAP, p);
				wait = 0;
				return;
			}
		}
	}
}
