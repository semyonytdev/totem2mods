package com.totemcuk.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.totemcuk.TotemcukConfig;

import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.TotemParticle;

@Mixin(TotemParticle.class)
public abstract class TotemParticleMixin {
	@Inject(method = "<init>", at = @At("TAIL"))
	private void totemcuk$recolor(CallbackInfo ci) {
		float[] c = TotemcukConfig.color();
		if (c != null) {
			float k = 0.7F + (float) Math.random() * 0.3F;
			int rgb = ((int) (c[0] * k * 255) << 16) | ((int) (c[1] * k * 255) << 8) | (int) (c[2] * k * 255);
			((SimpleAnimatedParticle) (Object) this).setColor(rgb);
		}
	}
}
