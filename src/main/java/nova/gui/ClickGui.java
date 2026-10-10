package nova.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import nova.core.Category;
import nova.core.Config;
import nova.core.Theme;
import nova.util.Anim;
import nova.util.Colors;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;

public class ClickGui extends Screen {
	private final List<Panel> panels = new ArrayList<>();
	private final Anim fade = new Anim(0, 11);
	private boolean closing;

	public ClickGui() {
		super(Text.literal("Nova"));
		fade.set(1);
		int i = 0;
		for (Category c : Category.values()) {
			Panel p = new Panel(c, 14 + i * (Panel.W + 8), 14);
			float[] s = Config.panels.get(c.name());
			if (s != null) { p.x = s[0]; p.y = s[1]; p.open = s[2] > 0.5f; p.openAnim.snap(p.open ? 1 : 0); }
			panels.add(p);
			i++;
		}
	}

	@Override public boolean shouldPause() { return false; }
	@Override public boolean shouldCloseOnEsc() { return false; }

	@Override public void close() {
		if (closing) return;
		closing = true;
		fade.set(0);
		ModuleButton.binding = null;
		ModuleButton.dragNum = null;
	}

	private void finish() {
		for (Panel p : panels) Config.panels.put(p.cat.name(), new float[]{p.x, p.y, p.open ? 1f : 0f});
		Config.save();
		super.close();
	}

	@Override public void render(DrawContext ctx, int mx, int my, float delta) {
		float a = fade.get();
		if (closing && a < 0.03f) { finish(); return; }
		TextRenderer tr = MinecraftClient.getInstance().textRenderer;

		if (Theme.dim.get()) ctx.fill(0, 0, width, height, Colors.alpha(0x000000, 0.55f * a));

		ModuleButton.hovered = null;
		int i = 0;
		for (Panel p : panels) {
			float t = Math.max(0f, Math.min(1f, a * 1.6f - panels.indexOf(p) * 0.12f));
			p.render(ctx, mx, my, (1f - t) * -26f);
			i++;
		}

		if (ModuleButton.hovered != null && !closing) {
			String d = ModuleButton.hovered.description;
			int w = tr.getWidth(d);
			int tx = Math.min(mx + 10, width - w - 8), ty = my + 12;
			ctx.fill(tx - 3, ty - 3, tx + w + 3, ty + 11, 0xE0101014);
			ctx.fill(tx - 3, ty - 3, tx + w + 3, ty - 2, Theme.accent(0));
			ctx.drawText(tr, d, tx, ty, 0xFFFFFFFF, true);
		}
		if (ModuleButton.binding != null) {
			String s = "Press a key to bind " + ModuleButton.binding.name + "  (ESC = clear)";
			ctx.drawCenteredTextWithShadow(tr, s, width / 2, height - 24, Theme.accent(0));
		} else {
			String s = "Left click: toggle   Right click: settings   Middle click: bind key   Drag header: move";
			ctx.drawCenteredTextWithShadow(tr, s, width / 2, height - 16, Colors.alpha(0xAAAAAA, 0.7f * a));
		}
	}

	@Override public boolean mouseClicked(double mx, double my, int button) {
		for (int i = panels.size() - 1; i >= 0; i--) {
			if (panels.get(i).mouseClicked(mx, my, button)) {
				Panel p = panels.remove(i);
				panels.add(p);
				return true;
			}
		}
		return super.mouseClicked(mx, my, button);
	}

	@Override public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
		if (ModuleButton.dragNum != null) { ModuleButton.applySlider(mx); return true; }
		for (Panel p : panels) p.drag(mx, my, width, height);
		return true;
	}

	@Override public boolean mouseReleased(double mx, double my, int button) {
		ModuleButton.dragNum = null;
		for (Panel p : panels) p.release();
		return true;
	}

	@Override public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (ModuleButton.binding != null) {
			ModuleButton.binding.key = keyCode == GLFW.GLFW_KEY_ESCAPE ? -1 : keyCode;
			ModuleButton.binding = null;
			return true;
		}
		if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) { close(); return true; }
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
}
