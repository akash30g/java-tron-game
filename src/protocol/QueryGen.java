package protocol;

import java.util.List;

import game.entities.Entity;

public class QueryGen {

	/*
	 * Client request
	 */

	public static String turnLeft(String name) {
		return new StringBuilder().append("USER ").append(name).append(" TURN LEFT").toString();
	}

	public static String turnRight(String name) {
		return new StringBuilder().append("USER ").append(name).append(" TURN RIGHT").toString();
	}

	public static String jetOn(String name) {
		return new StringBuilder().append("USER ").append(name).append(" JETWALL ON").toString();
	}

	public static String jetOff(String name) {
		return new StringBuilder().append("USER ").append(name).append(" JETWALL OFF").toString();
	}

	public static String add(String name, String cycleColor, String wallColor) {
		return new StringBuilder().append("ADD USER ").append(name).append(" ").append(cycleColor).append(" ")
				.append(wallColor).toString();
	}

	public static String remove(String name) {
		return new StringBuilder().append("REMOVE USER ").append(name).toString();
	}

	public static String getGridSize() {
		return new StringBuilder().append("GRID SIZE").toString();
	}

	public static String getGameState() {
		return new StringBuilder().append("GAME STATE").toString();
	}

	public static String saveScore(String name, int score) {
		return new StringBuilder().append("SAVE SCORE ").append(name).append(" ").append(score).toString();
	}

	public static String sendPlayers(List<Entity> players) {
		StringBuilder sb = new StringBuilder("PLAYERS ");
		for (Entity entities : players) {
			sb.append(str)
		}
	}

}
