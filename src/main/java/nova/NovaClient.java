package nova;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import nova.core.Config;
import nova.core.ModuleManager;
import nova.gui.ClickGui;
import nova.gui.Hud;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NovaClient implements ClientModInitializer {
	public static final Logger LOG = LoggerFactory.getLogger("Nova");
	public static ModuleManager modules;
	private static KeyBinding guiKey;

	@Override public void onInitializeClient() {
		modules = new ModuleManager();
		Config.load();

		guiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.nova.gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "category.nova"));

		ClientTickEvents.END_CLIENT_TICK.register(c -> {
			while (guiKey.wasPressed()) if (c.currentScreen == null) c.setScreen(new ClickGui());
			modules.tick(c);
		});
		HudRenderCallback.EVENT.register(Hud::render);
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> modules.worldLeft());
		ClientLifecycleEvents.CLIENT_STOPPING.register(c -> Config.save());
		LOG.info("Nova loaded: {} modules", modules.all.size());
	}
}
