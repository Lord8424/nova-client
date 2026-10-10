package nova.util;

/** Frame-rate independent exponential easing toward a target. */
public class Anim {
	private float value, target;
	private final float speed;
	private long last = System.nanoTime();

	public Anim(float initial, float speed) { this.value = initial; this.target = initial; this.speed = speed; }

	public void set(float t) { target = t; }
	public void snap(float v) { value = v; target = v; }
	public float target() { return target; }

	public float get() {
		long now = System.nanoTime();
		float dt = Math.min((now - last) / 1_000_000_000f, 0.1f);
		last = now;
		value += (target - value) * (1f - (float) Math.exp(-speed * dt));
		if (Math.abs(target - value) < 0.0015f) value = target;
		return value;
	}
}
