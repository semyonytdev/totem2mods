package com.totemcuk.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.totemcuk.TotemcukConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerSkin;

/** Подставляет свой плащ только для тебя (работает даже без записи в списке игроков). */
@Mixin(AbstractClientPlayer.class)
public abstract class PlayerInfoMixin {
	private static final Identifier CAPE = Identifier.fromNamespaceAndPath("totemcuk", "cape");

	@Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
	private void totemcuk$cape(CallbackInfoReturnable<PlayerSkin> cir) {
		if (!TotemcukConfig.capeEnabled()) {
			return;
		}
		if ((Object) this != Minecraft.getInstance().player) {
			return;
		}
		PlayerSkin skin = cir.getReturnValue();
		cir.setReturnValue(skin.with(PlayerSkin.Patch.create(
				Optional.empty(),
				Optional.of(new ClientAsset.ResourceTexture(CAPE)),
				Optional.empty(),
				Optional.empty())));
	}
}
