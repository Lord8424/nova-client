package nova.core;

import nova.setting.BoolSetting;
import nova.setting.NumSetting;
import java.awt.Color;

/** Global look-and-feel settings, edited through the "Theme" module in the GUI. */
public final class Theme {
	private Theme() {}
	public static final NumSetting hue = new NumSetting("Hue", 205, 0, 360, 1);
	public static final NumSetting saturation = new NumSetting("Saturation", 0.65, 0.0, 1.0, 0.05);
	public static final NumSetting spread = new NumSetting("Gradient Spread", 0.15, 0.0, 0.6, 0.01);
	public static final BoolSetting rainbow = new BoolSetting("Rainbow", false);
	public static final NumSetting rainbowSpeed = new NumSetting("Rainbow Speed", 1.0, 0.2, 4.0, 0.1);
	public static final BoolSetting dim = new BoolSetting("Dim Background", true);

	/** Accent color (opaque ARGB). {@code offset} shifts hue to create gradients. */
	public static int accent(float offset) {
		float h;
		if (rainbow.get()) h = (System.currentTimeMillis() % 10000L) / 10000f * rainbowSpeed.f() * 2f + offset;
		else h = hue.f() / 360f + offset * (spread.f() / 0.15f) * 0.15f;
		h -= (float) Math.floor(h);
		return 0xFF000000 | (Color.HSBtoRGB(h, saturation.f(), 1f) & 0xFFFFFF);
	}
}
