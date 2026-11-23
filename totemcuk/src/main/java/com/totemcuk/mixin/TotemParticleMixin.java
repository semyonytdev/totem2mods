package com.totemcuk.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.totemcuk.TotemcukConfig;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.TotemParticle;

@Mixin(TotemParticle.class)
public abstract class TotemParticleMixin {
	@Inject(method = "<init>", at = @At("TAIL"))
	private void totemcuk$recolor(CallbackInfo ci) {
		float[] c = TotemcukConfig.color();
		if (c != null) {
			float k = 0.7F + (float) Math.random() * 0.3F;
			((Particle) (Object) this).setColor(c[0] * k, c[1] * k, c[2] * k);
		}
	}
}
