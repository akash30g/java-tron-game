package game.utils;

import java.awt.Color;

/*
 * This class helps to convert string to color and vice versa
 */

public class ColorUtils {
	public static Color stringToColor(String color) {
		switch (color) {
		case "Blue":
			return Color.BLUE;
		case "Red":
			return Color.RED;
		case "Green":
			return Color.GREEN;
		default:
			return Color.RED;
		}
	}

	public static String colorToString(Color color) {
		switch (color.toString()) {
		case "java.awt.Color[r=255,g=0,b=0]":
			return "Red";
		case "java.awt.Color[r=0,g=255,b=0]":
			return "Green";
		case "java.awt.Color[r=0,g=0,b=255]":
			return "Blue";
		default:
			return "Red";
		}
	}
}
