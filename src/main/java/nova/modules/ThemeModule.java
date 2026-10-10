package nova.modules;

import nova.core.Category;
import nova.core.Module;
import nova.core.Theme;

/** Exposes the global theme settings in the GUI. */
public class ThemeModule extends Module {
	public ThemeModule() {
		super("Theme", "Customize colors and GUI look.", Category.CLIENT);
		add(Theme.hue); add(Theme.saturation); add(Theme.spread);
		add(Theme.rainbow); add(Theme.rainbowSpeed); add(Theme.dim);
		setEnabled(true);
	}
}
