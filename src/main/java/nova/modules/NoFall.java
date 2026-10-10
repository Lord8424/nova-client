package nova.modules;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import nova.core.Category;
import nova.core.Module;
import nova.setting.NumSetting;

public class NoFall extends Module {
	private final NumSetting distance = add(new NumSetting("Min Distance", 2.5, 1, 10, 0.5));

	public NoFall() { super("NoFall", "Cancels fall damage by spoofing ground state.", Category.PLAYER); }

	@Override public void onTick() {
		var p = mc.player;
		if (p.fallDistance > distance.get() && !p.isOnGround() && mc.getNetworkHandler() != null)
			mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true, p.horizontalCollision));
	}
}
