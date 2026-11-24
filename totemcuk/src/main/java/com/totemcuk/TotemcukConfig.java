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

	private static final Map<String, Character> ICONS = new LinkedHashMap<>();
	static {
		ICONS.put("круг", '\uE001');
		ICONS.put("звезда", '\uE002');
		ICONS.put("сердце", '\uE003');
		ICONS.put("корона", '\uE004');
		ICONS.put("снежинка", '\uE005');
		ICONS.put("пингвин", '\uE006');
		ICONS.put("алмаз", '\uE007');
		ICONS.put("молния", '\uE008');
	}
	private static final Map<String, String> ICONS_EN = Map.of(
			"circle", "круг", "star", "звезда", "heart", "сердце", "crown", "корона",
			"snowflake", "снежинка", "penguin", "пингвин", "diamond", "алмаз", "bolt", "молния");

	private static boolean cape = true;
	private static String iconName = "";
	private static String icon = "";

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
				String[] lines = Files.readString(f, StandardCharsets.UTF_8).split("\\R");
				if (lines.length > 0 && !lines[0].isBlank()) {
					set(lines[0].trim());
				}
				if (lines.length > 1 && !lines[1].isBlank()) {
					setIcon(lines[1].trim());
				}
				if (lines.length > 2 && !lines[2].isBlank()) {
					cape = !lines[2].trim().equals("off");
				}
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
			Files.writeString(file(), text + "\n" + iconName + "\n" + (cape ? "on" : "off") + "\n", StandardCharsets.UTF_8);
		} catch (IOException ignored) {
			// не страшно
		}
	}

	/** Возвращает true, если значок понятен. «выкл» убирает значок. */
	public static boolean setIcon(String input) {
		String s = input.trim().toLowerCase(Locale.ROOT);
		if (s.equals("выкл") || s.equals("off") || s.equals("нет")) {
			iconName = "";
			icon = "";
			save();
			return true;
		}
		String ru = ICONS_EN.getOrDefault(s, s);
		Character ch = ICONS.get(ru);
		if (ch == null) {
			return false;
		}
		iconName = ru;
		icon = String.valueOf(ch);
		save();
		return true;
	}

	/** Символ значка или пустая строка. */
	public static String icon() {
		return icon;
	}

	public static String iconNames() {
		return String.join(", ", ICONS.keySet()) + " или выкл";
	}

	public static boolean capeEnabled() {
		return cape;
	}

	public static boolean setCape(String input) {
		String s = input.trim().toLowerCase(Locale.ROOT);
		if (s.equals("вкл") || s.equals("on")) {
			cape = true;
		} else if (s.equals("выкл") || s.equals("off")) {
			cape = false;
		} else {
			return false;
		}
		save();
		return true;
	}

	public static float[] color() {
		return color;
	}

	public static String names() {
		return String.join(", ", NAMES.keySet()) + " или #RRGGBB";
	}
}
