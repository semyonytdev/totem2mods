package com.totemcuk;

import java.util.LinkedHashMap;
import java.util.Map;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/**
 * Totemcuk: цветные варианты тотема бессмертия.
 * Каждый цвет это встроенный ресурспак. Включается в Настройки, Ресурспаки.
 */
public class TotemcukClient implements ClientModInitializer {
	public static final String MOD_ID = "totemcuk";

	@Override
	public void onInitializeClient() {
		Map<String, String> colors = new LinkedHashMap<>();
		colors.put("red", "красный");
		colors.put("orange", "оранжевый");
		colors.put("cyan", "голубой");
		colors.put("blue", "синий");
		colors.put("purple", "фиолетовый");
		colors.put("pink", "розовый");
		colors.put("white", "белый");
		colors.put("black", "чёрный");

		FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container -> {
			for (Map.Entry<String, String> entry : colors.entrySet()) {
				ResourceLoader.registerBuiltinPack(
						Identifier.fromNamespaceAndPath(MOD_ID, "totem_" + entry.getKey()),
						container,
						Component.literal("Totemcuk: узор тотема " + entry.getValue()),
						PackActivationType.NORMAL);
			}
		});
	}
}
