package nova.modules;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import nova.core.Category;
import nova.core.Module;
import nova.setting.BoolSetting;
import nova.setting.ModeSetting;
import nova.setting.NumSetting;

public class KillAura extends Module {
	private final NumSetting range = add(new NumSetting("Range", 3.5, 2, 6, 0.1));
	private final ModeSetting priority = add(new ModeSetting("Priority", "Distance", "Health"));
	private final BoolSetting players = add(new BoolSetting("Players", true));
	private final BoolSetting hostile = add(new BoolSetting("Hostile", true));
	private final BoolSetting passive = add(new BoolSetting("Passive", false));
	private final BoolSetting rotate = add(new BoolSetting("Rotate", true));
	private final NumSetting smooth = add(new NumSetting("Smoothing", 0.45, 0.05, 1.0, 0.05));
	private final BoolSetting cooldown = add(new BoolSetting("Wait For Cooldown", true));

	public KillAura() { super("KillAura", "Automatically attacks nearby targets.", Category.COMBAT); }

	@Override public String suffix() { return range.display(); }

	@Override public void onTick() {
		var p = mc.player;
		if (mc.currentScreen != null) return;
		LivingEntity best = null;
		double bestScore = Double.MAX_VALUE;
		for (Entity e : mc.world.getEntities()) {
			if (!(e instanceof LivingEntity le) || e == p || !le.isAlive()) continue;
			boolean match = (le instanceof PlayerEntity && players.get())
				|| (le instanceof HostileEntity && hostile.get())
				|| (le instanceof AnimalEntity && passive.get());
			if (!match) continue;
			double d = p.distanceTo(le);
			if (d > range.get()) continue;
			double score = priority.is("Health") ? le.getHealth() : d;
			if (score < bestScore) { bestScore = score; best = le; }
		}
		if (best == null) return;

		boolean aimed = true;
		if (rotate.get()) {
			Vec3d t = best.getBoundingBox().getCenter(), eye = p.getEyePos();
			double dx = t.x - eye.x, dy = t.y - eye.y, dz = t.z - eye.z;
			double h = Math.sqrt(dx * dx + dz * dz);
			float yaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
			float pitch = (float) -Math.toDegrees(Math.atan2(dy, h));
			float dYaw = MathHelper.wrapDegrees(yaw - p.getYaw());
			float dPitch = pitch - p.getPitch();
			p.setYaw(p.getYaw() + dYaw * smooth.f());
			p.setPitch(MathHelper.clamp(p.getPitch() + dPitch * smooth.f(), -90f, 90f));
			aimed = Math.abs(dYaw) < 20f && Math.abs(dPitch) < 25f;
		}
		if (!aimed) return;
		if (cooldown.get() && p.getAttackCooldownProgress(0.5f) < 0.95f) return;
		mc.interactionManager.attackEntity(p, best);
		p.swingHand(Hand.MAIN_HAND);
	}
}
