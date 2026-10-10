package nova.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import nova.core.Module;
import nova.core.Theme;
import nova.setting.*;
import nova.util.Anim;
import nova.util.Colors;

public class ModuleButton {
	public static final float H = 15, SH = 13;
	public static Module binding, hovered;
	public static NumSetting dragNum;

	private static final int TXT = 0xFF9A9AA6, WHITE = 0xFFFFFFFF, TRACK = 0xFF2A2A34;

	final Module m;
	boolean expanded;
	private final Anim expand = new Anim(0, 14), hover = new Anim(0, 20), on = new Anim(0, 16);
	private float exp, hov, onv;
	private float x, y, w;

	ModuleButton(Module m) { this.m = m; }

	void update() { exp = expand.get(); hov = hover.get(); onv = on.get(); }
	float settingsH() { return m.settings.size() * SH; }
	float height() { return H + settingsH() * exp; }

	void render(DrawContext ctx, int mx, int my, float x, float y, float w, float clipBottom) {
		this.x = x; this.y = y; this.w = w;
		TextRenderer tr = MinecraftClient.getInstance().textRenderer;
		int ix = (int) x, iy = (int) y, ir = (int) (x + w);

		boolean isHover = mx >= x && mx < x + w && my >= y && my < y + H && my < clipBottom;
		hover.set(isHover ? 1 : 0);
		on.set(m.isEnabled() ? 1 : 0);
		if (isHover) hovered = m;

		ctx.fill(ix, iy, ir, iy + (int) H, Colors.lerp(0xF0121216, 0xF01C1C24, hov));
		if (onv > 0.01f) {
			float off = y / 300f;
			ctx.fillGradient(ix, iy, ir, iy + (int) H, Colors.alpha(Theme.accent(off), 0.5f * onv), Colors.alpha(Theme.accent(off + 0.12f), 0.22f * onv));
			ctx.fill(ix, iy, ix + Math.max(1, (int) (2 * onv + 0.5f)), iy + (int) H, Theme.accent(off));
		}

		String label = binding == m ? "Press a key..." : m.name;
		ctx.drawText(tr, label, ix + 6 + (int) (onv * 2), iy + 4, Colors.lerp(TXT, WHITE, Math.max(onv, hov * 0.6f)), true);

		int rx = ir - 6;
		if (!m.settings.isEmpty()) {
			String s = expanded ? "-" : "+";
			rx -= tr.getWidth(s);
			ctx.drawText(tr, s, rx, iy + 4, 0xFF6A6A78, true);
		}
		if (m.key != -1 && binding != m) {
			String k = "[" + InputUtil.fromKeyCode(m.key, 0).getLocalizedText().getString() + "]";
			rx -= tr.getWidth(k) + 4;
			ctx.drawText(tr, k, rx, iy + 4, 0xFF6A6A78, true);
		}

		float sh = settingsH() * exp;
		if (sh > 0.5f) {
			ctx.enableScissor(ix, iy + (int) H, ir, iy + (int) (H + sh));
			float yy = y + H;
			for (Setting s : m.settings) { renderSetting(ctx, tr, s, x, yy, w); yy += SH; }
			ctx.disableScissor();
		}
	}

	private void renderSetting(DrawContext ctx, TextRenderer tr, Setting s, float x, float yy, float w) {
		int ix = (int) x, iy = (int) yy, ir = (int) (x + w);
		ctx.fill(ix, iy, ir, iy + (int) SH, 0xF00D0D11);
		if (s instanceof BoolSetting b) {
			b.anim.set(b.get() ? 1 : 0);
			float a = b.anim.get();
			ctx.drawText(tr, b.name, ix + 8, iy + 3, Colors.lerp(TXT, WHITE, a), true);
			int bx = ir - 15, by = iy + 3;
			ctx.fill(bx, by, bx + 8, by + 7, TRACK);
			if (a > 0.01f) {
				int inset = (int) ((1 - a) * 3.5f);
				ctx.fill(bx + inset, by + inset, bx + 8 - inset, by + 7 - inset, Theme.accent(yy / 300f));
			}
		} else if (s instanceof NumSetting n) {
			n.anim.set((float) ((n.get() - n.min) / (n.max - n.min)));
			float r = n.anim.get();
			n.sx = x + 8; n.sw = w - 16;
			ctx.drawText(tr, n.name, ix + 8, iy + 1, TXT, true);
			String v = n.display();
			ctx.drawText(tr, v, ir - 8 - tr.getWidth(v), iy + 1, WHITE, true);
			int ty = iy + (int) SH - 3, sx = (int) n.sx, sw = (int) n.sw;
			ctx.fill(sx, ty, sx + sw, ty + 2, TRACK);
			int fw = (int) (sw * r);
			for (int i = 0; i < fw; i += 2)
				ctx.fill(sx + i, ty, Math.min(sx + i + 2, sx + fw), ty + 2, Theme.accent(i / (float) Math.max(1, sw) * 0.8f));
			ctx.fill(sx + fw - 1, ty - 1, sx + fw + 1, ty + 3, WHITE);
		} else if (s instanceof ModeSetting md) {
			ctx.drawText(tr, md.name, ix + 8, iy + 3, TXT, true);
			String v = md.get();
			ctx.drawText(tr, v, ir - 8 - tr.getWidth(v), iy + 3, Theme.accent(yy / 300f), true);
		}
	}

	boolean click(double mx, double my, int button, float clipBottom) {
		if (my >= clipBottom || mx < x || mx >= x + w) return false;
		if (my >= y && my < y + H) {
			if (button == 0) m.toggle();
			else if (button == 1 && !m.settings.isEmpty()) { expanded = !expanded; expand.set(expanded ? 1 : 0); }
			else if (button == 2) binding = binding == m ? null : m;
			return true;
		}
		if (expanded && exp > 0.5f) {
			float yy = y + H;
			for (Setting s : m.settings) {
				if (my >= yy && my < yy + SH) {
					if (s instanceof BoolSetting b && button == 0) b.toggle();
					else if (s instanceof ModeSetting md) { if (button == 0) md.next(); else if (button == 1) md.prev(); }
					else if (s instanceof NumSetting n && button == 0) { dragNum = n; applySlider(mx); }
					return true;
				}
				yy += SH;
			}
		}
		return false;
	}

	static void applySlider(double mx) {
		NumSetting n = dragNum;
		if (n == null || n.sw <= 0) return;
		double r = Math.max(0, Math.min(1, (mx - n.sx) / n.sw));
		n.set(n.min + r * (n.max - n.min));
	}
}
