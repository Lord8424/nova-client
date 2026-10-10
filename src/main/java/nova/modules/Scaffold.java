package nova.modules;

import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import nova.core.Category;
import nova.core.Module;

public class Scaffold extends Module {
	public Scaffold() { super("Scaffold", "Places blocks under your feet.", Category.PLAYER); }

	@Override public void onTick() {
		var p = mc.player;
		BlockPos below = p.getBlockPos().down();
		if (!mc.world.getBlockState(below).isReplaceable()) return;

		int slot = -1;
		for (int i = 0; i < 9; i++) if (p.getInventory().getStack(i).getItem() instanceof BlockItem) { slot = i; break; }
		if (slot == -1) return;
		p.getInventory().selectedSlot = slot;

		for (Direction d : Direction.values()) {
			if (d == Direction.UP) continue;
			BlockPos n = below.offset(d);
			var st = mc.world.getBlockState(n);
			if (st.isAir() || !st.getFluidState().isEmpty()) continue;
			Direction face = d.getOpposite();
			Vec3d hitPos = Vec3d.ofCenter(n).offset(face, 0.5);
			mc.interactionManager.interactBlock(p, Hand.MAIN_HAND, new BlockHitResult(hitPos, face, n, false));
			p.swingHand(Hand.MAIN_HAND);
			return;
		}
	}
}
