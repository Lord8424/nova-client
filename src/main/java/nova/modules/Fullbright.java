package nova.modules;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import nova.core.Category;
import nova.core.Module;

public class Fullbright extends Module {
	public Fullbright() { super("Fullbright", "See clearly in the dark.", Category.RENDER); }

	@Override public void onTick() {
		if (!mc.player.hasStatusEffect(StatusEffects.NIGHT_VISION))
			mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, StatusEffectInstance.INFINITE, 0, false, false, false));
	}

	@Override public void onDisable() {
		if (mc.player != null) mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
	}
}
