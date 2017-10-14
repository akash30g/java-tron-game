package game;

import java.awt.Color;

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
			return Color.CYAN;
		}
	}
}
