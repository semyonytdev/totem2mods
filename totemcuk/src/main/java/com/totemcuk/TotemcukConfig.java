package com.totemcuk;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import net.fabricmc.loader.api.FabricLoader;

/** Цвет узора тотема. Хранится в config/totemcuk.txt одной строкой (название цвета или #RRGGBB). */
public final class TotemcukConfig {
	private static final Map<String, Integer> NAMES = new LinkedHashMap<>();
	static {
		NAMES.put("красный", 0xFF2020);
		NAMES.put("оранжевый", 0xFF8A00);
		NAMES.put("жёлтый", 0xFFE600);
		NAMES.put("зелёный", 0x30E050);
		NAMES.put("голубой", 0x20D0FF);
		NAMES.put("синий", 0x2050FF);
		NAMES.put("фиолетовый", 0xA040FF);
		NAMES.put("розовый", 0xFF40B0);
		NAMES.put("белый", 0xFFFFFF);
		NAMES.put("чёрный", 0x202020);
	}
	private static final Map<String, String> ENGLISH = Map.of(
			"red", "красный", "orange", "оранжевый", "yellow", "жёлтый", "green", "зелёный",
			"cyan", "голубой", "blue", "синий", "purple", "фиолетовый", "pink", "розовый",
			"white", "белый", "black", "чёрный");

	/** null значит оставить оригинальный цвет игры. */
	private static float[] color = null;
	private static String text = "";

	private TotemcukConfig() {
	}

	private static Path file() {
		return FabricLoader.getInstance().getConfigDir().resolve("totemcuk.txt");
	}

	public static void load() {
		try {
			Path f = file();
			if (Files.exists(f)) {
				set(Files.readString(f, StandardCharsets.UTF_8).trim());
			}
		} catch (IOException | RuntimeException ignored) {
			// без конфига остаётся оригинальный цвет
		}
	}

	/** Возвращает true, если цвет понятен. */
	public static boolean set(String input) {
		String s = input.trim().toLowerCase(Locale.ROOT).replace('ё', 'е');
		Integer rgb = null;
		if (s.startsWith("#") && s.length() == 7) {
			try {
				rgb = Integer.parseInt(s.substring(1), 16);
			} catch (NumberFormatException e) {
				return false;
			}
		} else {
			for (Map.Entry<String, Integer> e : NAMES.entrySet()) {
				String ru = e.getKey().replace('ё', 'е');
				if (ru.equals(s)) {
					rgb = e.getValue();
				}
			}
			for (Map.Entry<String, String> e : ENGLISH.entrySet()) {
				if (e.getKey().equals(s)) {
					rgb = NAMES.get(e.getValue());
				}
			}
		}
		if (rgb == null) {
			return false;
		}
		text = input.trim();
		boolean original = s.equals("зеленый") || s.equals("green");
		color = original ? null : new float[] {((rgb >> 16) & 255) / 255f, ((rgb >> 8) & 255) / 255f, (rgb & 255) / 255f};
		save();
		return true;
	}

	private static void save() {
		try {
			Files.writeString(file(), text, StandardCharsets.UTF_8);
		} catch (IOException ignored) {
			// не страшно
		}
	}

	public static float[] color() {
		return color;
	}

	public static String names() {
		return String.join(", ", NAMES.keySet()) + " или #RRGGBB";
	}
}
