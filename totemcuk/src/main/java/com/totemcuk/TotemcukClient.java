package com.totemcuk;

import java.util.LinkedHashMap;
import java.util.Map;

import com.mojang.brigadier.arguments.StringArgumentType;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
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
		TotemcukConfig.load();
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
				dispatcher.register(ClientCommandManager.literal("totemcuk")
						.executes(ctx -> {
							ctx.getSource().sendFeedback(Component.literal("Totemcuk: /totemcuk <цвет>. Цвета: " + TotemcukConfig.names() + ". Значок: /totemcuk значок <имя>. Значки: " + TotemcukConfig.iconNames()));
							return 1;
						})
						.then(ClientCommandManager.literal("значок")
								.then(ClientCommandManager.argument("name", StringArgumentType.greedyString())
										.executes(ctx -> setIcon(ctx.getSource(), StringArgumentType.getString(ctx, "name")))))
						.then(ClientCommandManager.literal("icon")
								.then(ClientCommandManager.argument("name", StringArgumentType.greedyString())
										.executes(ctx -> setIcon(ctx.getSource(), StringArgumentType.getString(ctx, "name")))))
						.then(ClientCommandManager.argument("color", StringArgumentType.greedyString())
								.executes(ctx -> {
									String value = StringArgumentType.getString(ctx, "color");
									if (TotemcukConfig.set(value)) {
										ctx.getSource().sendFeedback(Component.literal("Totemcuk: цвет узора теперь " + value));
										return 1;
									}
									ctx.getSource().sendError(Component.literal("Не знаю такой цвет. Цвета: " + TotemcukConfig.names()));
									return 0;
								}))));

		Map<String, String> colors = new LinkedHashMap<>();
		colors.put("red", "красный");
		colors.put("orange", "оранжевый");
		colors.put("yellow", "жёлтый");
		colors.put("green", "зелёный");
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
						Component.literal("Totemcuk: контур тотема " + entry.getValue()),
						PackActivationType.NORMAL);
			}
		});
	}

	private static int setIcon(net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource source, String name) {
		if (TotemcukConfig.setIcon(name)) {
			source.sendFeedback(Component.literal("Totemcuk: значок рядом с ником: " + name + " (его видно в режиме от третьего лица, F5)"));
			return 1;
		}
		source.sendError(Component.literal("Не знаю такой значок. Значки: " + TotemcukConfig.iconNames()));
		return 0;
	}
}
