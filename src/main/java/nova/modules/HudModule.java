package nova.modules;

import nova.core.Category;
import nova.core.Module;
import nova.setting.BoolSetting;

public class HudModule extends Module {
	public final BoolSetting watermark = add(new BoolSetting("Watermark", true));
	public final BoolSetting moduleList = add(new BoolSetting("Module List", true));
	public final BoolSetting info = add(new BoolSetting("Coordinates", true));

	public HudModule() {
		super("HUD", "On-screen overlay.", Category.CLIENT);
		setEnabled(true);
	}
}
