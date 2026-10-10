package nova.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public final class MoveUtil {
	private MoveUtil() {}
	private static final MinecraftClient mc = MinecraftClient.getInstance();

	public static boolean isMoving() {
		var in = mc.player.input;
		return in.movementForward != 0 || in.movementSideways != 0;
	}

	/** Horizontal velocity (blocks/tick) in the direction the player is steering. */
	public static Vec3d dir(double speed) {
		var in = mc.player.input;
		float f = in.movementForward, s = in.movementSideways;
		if (f == 0 && s == 0) return Vec3d.ZERO;
		double yaw = Math.toRadians(mc.player.getYaw());
		double sin = Math.sin(yaw), cos = Math.cos(yaw);
		double x = s * cos - f * sin, z = f * cos + s * sin;
		double len = Math.sqrt(x * x + z * z);
		return new Vec3d(x / len * speed, 0, z / len * speed);
	}
}
