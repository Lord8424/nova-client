package nova.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import nova.NovaClient;
import nova.core.Category;
import nova.core.Module;
import nova.core.Theme;
import nova.util.Anim;
import java.util.ArrayList;
import java.util.List;

public class Panel {
	public static final float W = 118, HEAD = 18;

	public final Category cat;
	public float x, y;
	public boolean open = true;
	public final Anim openAnim = new Anim(1, 14);
	private final List<ModuleButton> buttons = new ArrayList<>();
	private boolean dragging;
	private float ox, oy, bodyH;

	Panel(Category cat, float x, float y) {
		this.cat = cat; this.x = x; this.y = y;
		for (Module m : NovaClient.modules.in(cat)) buttons.add(new ModuleButton(m));
	}

	void render(DrawContext ctx, int mx, int my, float yOff) {
		TextRenderer tr = MinecraftClient.getInstance().textRenderer;
		float ry = y + yOff;
		float total = 0;
		for (ModuleButton b : buttons) { b.update(); total += b.height(); }
		float o = openAnim.get();
		bodyH = total * o;

		ctx.fill((int) x, (int) ry, (int) (x + W), (int) (ry + HEAD), 0xF00B0B0F);
		for (int i = 0; i < W; i += 2)
			ctx.fill((int) x + i, (int) ry, (int) x + i + 2, (int) ry + 2, Theme.accent(i / W * 0.6f));
		int tw = tr.getWidth(cat.title);
		ctx.drawText(tr, cat.title, (int) (x + (W - tw) / 2f), (int) ry + 6, 0xFFFFFFFF, true);

		if (bodyH > 0.5f) {
			float top = ry + HEAD, bottom = top + bodyH;
			ctx.enableScissor((int) x, (int) top, (int) (x + W), (int) Math.ceil(bottom));
			float yy = top;
			for (ModuleButton b : buttons) { b.render(ctx, mx, my, x, yy, W, bottom); yy += b.height(); }
			ctx.disableScissor();
			ctx.fill((int) x, (int) bottom, (int) (x + W), (int) bottom + 1, Theme.accent(0.3f));
		}
	}

	boolean mouseClicked(double mx, double my, int button) {
		if (mx < x || mx >= x + W) return false;
		if (my >= y && my < y + HEAD) {
			if (button == 0) { dragging = true; ox = (float) (mx - x); oy = (float) (my - y); }
			else if (button == 1) { open = !open; openAnim.set(open ? 1 : 0); }
			return true;
		}
		float bottom = y + HEAD + bodyH;
		if (bodyH > 0.5f && my >= y + HEAD && my < bottom) {
			for (ModuleButton b : buttons) if (b.click(mx, my, button, bottom)) return true;
			return true;
		}
		return false;
	}

	void drag(double mx, double my, int sw, int sh) {
		if (!dragging) return;
		x = (float) Math.max(0, Math.min(sw - W, mx - ox));
		y = (float) Math.max(0, Math.min(sh - HEAD, my - oy));
	}

	void release() { dragging = false; }
}
