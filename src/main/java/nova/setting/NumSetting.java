package nova.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.Locale;

public class NumSetting extends Setting {
	public final double min, max, step;
	private double value;
	/** Slider geometry, written by the GUI each frame. */
	public float sx, sw;

	public NumSetting(String name, double def, double min, double max, double step) {
		super(name); this.min = min; this.max = max; this.step = step;
		this.value = def; anim.snap((float) ((def - min) / (max - min)));
	}

	public double get() { return value; }
	public float f() { return (float) value; }
	public int i() { return (int) Math.round(value); }

	public void set(double v) {
		v = Math.max(min, Math.min(max, v));
		v = Math.round((v - min) / step) * step + min;
		value = Math.max(min, Math.min(max, v));
	}

	public String display() { return step >= 1 ? String.valueOf((int) Math.round(value)) : String.format(Locale.ROOT, "%.2f", value); }

	@Override public JsonElement save() { return new JsonPrimitive(value); }
	@Override public void load(JsonElement e) { if (e.isJsonPrimitive()) { set(e.getAsDouble()); anim.snap((float) ((value - min) / (max - min))); } }
}
