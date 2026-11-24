package com.totemcuk.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.totemcuk.TotemcukConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

/** Добавляет выбранный значок перед ником (только твоим) над головой. */
@Mixin(EntityRenderer.class)
public abstract class NameTagMixin {
	@Inject(method = "getNameTag", at = @At("RETURN"), cancellable = true)
	private void totemcuk$icon(Entity entity, CallbackInfoReturnable<Component> cir) {
		String icon = TotemcukConfig.icon();
		if (!icon.isEmpty() && entity == Minecraft.getInstance().player && cir.getReturnValue() != null) {
			cir.setReturnValue(Component.literal(icon + " ").append(cir.getReturnValue()));
		}
	}
}
