package nova.setting;

import com.google.gson.JsonElement;
import nova.util.Anim;

public abstract class Setting {
	public final String name;
	public final Anim anim = new Anim(0, 16);
	protected Setting(String name) { this.name = name; }
	public abstract JsonElement save();
	public abstract void load(JsonElement e);
}
