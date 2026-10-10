package nova.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import nova.NovaClient;
import nova.core.Category;
import nova.core.Module;
import nova.core.Theme;
import nova.modules.HudModule;
import java.util.ArrayList;
import java.util.List;

public final class Hud {
	private Hud() {}

	public static void render(DrawContext ctx, RenderTickCounter tick) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc.options.hudHidden || mc.player == null || NovaClient.modules == null) return;
		HudModule hud = NovaClient.modules.get(HudModule.class);
		if (hud == null || !hud.isEnabled()) return;
		TextRenderer tr = mc.textRenderer;
		int sw = ctx.getScaledWindowWidth(), sh = ctx.getScaledWindowHeight();

		if (hud.watermark.get()) {
			String fps = "  \u00a77" + mc.getCurrentFps() + " fps";
			int w = tr.getWidth("Nova") + tr.getWidth(fps) + 10;
			ctx.fill(4, 4, 4 + w, 17, 0xA0000000);
			for (int i = 0; i < w; i += 2) ctx.fill(4 + i, 4, 6 + i, 5, Theme.accent(i / (float) w * 0.6f));
			int x = 8, idx = 0;
			for (char c : "Nova".toCharArray()) {
				String s = String.valueOf(c);
				ctx.drawText(tr, s, x, 7, Theme.accent(idx++ * 0.08f), true);
				x += tr.getWidth(s);
			}
			ctx.drawText(tr, fps, x, 7, 0xFFFFFFFF, true);
		}

		if (hud.moduleList.get()) {
			List<Module> rows = new ArrayList<>();
			for (Module m : NovaClient.modules.all) {
				if (m.category == Category.CLIENT) continue;
				m.slide.set(m.isEnabled() ? 1f : 0f);
				if (m.slide.get() > 0.01f) rows.add(m);
			}
			rows.sort((a, b) -> tr.getWidth(label(b)) - tr.getWidth(label(a)));
			float y = 4;
			int i = 0;
			for (Module m : rows) {
				float a = m.slide.get();
				String text = label(m);
				int w = tr.getWidth(text) + 8;
				int x0 = (int) (sw - (w + 2) * a);
				int col = Theme.accent(i++ * 0.07f);
				ctx.fill(x0, (int) y, x0 + w, (int) y + 11, 0x90000000);
				ctx.fill(x0 + w, (int) y, x0 + w + 2, (int) y + 11, col);
				ctx.drawText(tr, text, x0 + 4, (int) y + 2, col, true);
				y += 11 * a;
			}
		}

		if (hud.info.get()) {
			String s = String.format("XYZ \u00a7f%d %d %d", (int) Math.floor(mc.player.getX()), (int) Math.floor(mc.player.getY()), (int) Math.floor(mc.player.getZ()));
			ctx.drawText(tr, s, 4, sh - 12, Theme.accent(0), true);
		}
	}

	private static String label(Module m) {
		String s = m.suffix();
		return s.isEmpty() ? m.name : m.name + " \u00a77" + s;
	}
}
