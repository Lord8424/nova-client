package nova.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class BoolSetting extends Setting {
	private boolean value;
	public BoolSetting(String name, boolean def) { super(name); this.value = def; anim.snap(def ? 1 : 0); }
	public boolean get() { return value; }
	public void set(boolean v) { value = v; }
	public void toggle() { value = !value; }
	@Override public JsonElement save() { return new JsonPrimitive(value); }
	@Override public void load(JsonElement e) { if (e.isJsonPrimitive()) { value = e.getAsBoolean(); anim.snap(value ? 1 : 0); } }
}
