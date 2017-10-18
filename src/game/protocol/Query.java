package game.protocol;

import java.util.List;

import game.entities.LightCycle;

public class Query {
	
	public final static String USER = "USER";
	public final static String ADD = "ADD";
	public final static String PLAYERS = "PLAYERS";
	public final static String REPLY = "REPLY";
	public final static String NEW_PLAYER = "NEW PLAYER";
	public final static String OTHER_PLAYERS = "OTHER PLAYERS";

	/*
	 * Client request
	 */

	public static String turnLeft(String name) {
		return new StringBuilder().append(USER).append(" ").append(name).append(" TURN left").toString();
	}

	public static String turnRight(String name) {
		return new StringBuilder().append(USER).append(" ").append(name).append(" TURN right").toString();
	}

	public static String jetOn(String name) {
		return new StringBuilder().append(USER).append(" ").append(name).append(" JETWALL true").toString();
	}

	public static String jetOff(String name) {
		return new StringBuilder().append(USER).append(" ").append(name).append(" JETWALL false").toString();
	}

	public static String add(String name, String cycleColor, String wallColor) {
		return new StringBuilder()
				.append(ADD)
				.append(" ")
				.append(USER)
				.append(" ")
				.append(name)
				.append(" ")
				.append(cycleColor)
				.append(" ")
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
	
	/*
	 * Server queries
	 */

	public static String sendPlayers(List<LightCycle> lightCycles) {
		StringBuilder sb = new StringBuilder(PLAYERS).append(" ");
		for (LightCycle lightCycle : lightCycles) {
			if (!lightCycle.isDerezzed()) {
				sb
				.append(lightCycle.getPlayer().getNickname())
				.append(",")
				.append(lightCycle.getX())
				.append(",")
				.append(lightCycle.getY())
				.append(",")
				.append(lightCycle.isJetWallOn())
				.append(",");
			}
		}
		return sb.substring(0, sb.length() - 1);
	}
	
	public static String sendNewPlayer(String nickname, String cycleColor, String jetColor) {
		return new StringBuilder(NEW_PLAYER)
				.append(" ")
				.append(cycleColor)
				.append(",")
				.append(jetColor)
				.append(",")
				.append(nickname)
				.toString();
	}
	
}
