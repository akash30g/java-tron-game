package game;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import game.entities.Entity;
import game.entities.LightCycle;
import game.entities.Wall;
import game.protocol.Query;
import game.utils.ColorUtils;

public class KryoClient {

	private static Client client;
	private static String nickname;
	private static List<Entity> entities = new ArrayList<>();

	private static boolean waitingForReply = false;
	private static boolean isGameReady = false;
	private static String lastReply = "";

	public static void connect(String host, int tcpPort, int udpPort) throws IOException {
		client = new Client();
		client.start();
		client.connect(10000, host, tcpPort, udpPort);
		client.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				if (object instanceof String) {
					processData((String) object);
				}
			}
		});
	}

	public static List<Entity> getEntities() {
		return entities;
	}

	public static String getNickname() {
		return nickname;
	}

	public static void setNickname(String nickname) {
		KryoClient.nickname = nickname;
	}

	public static void send(String data) {
		client.sendTCP(data);
	}

	public static void sendAndWait(String data) {
		client.sendTCP(data);
		waitingForReply = true;
	}

	public static boolean isWaitingForReply() {
		return waitingForReply;
	}

	public static String getLastReply() {
		return lastReply;
	}

	private static void processData(String object) {
		String[] data = object.split(" ");
		String keyword = data[0];
		System.out.println(object);
		if (keyword.equals(Query.PLAYERS)) {
			processEntitiesData(data);
		}
		if (keyword.equals(Query.REPLY)) {
			lastReply = object;
			waitingForReply = false;
		}
		if (object.contains(Query.NEW_PLAYER)) {
			processNewPlayerData(data[2]);
		}
		if (object.contains("GAME IS READY")) {
			isGameReady = true;
		}
		if (object.contains("GAME OVER")) {
			JOptionPane.showMessageDialog(null, "Game is over");
			System.exit(0);
		}
	}

	public static boolean isGameReady() {
		return isGameReady;
	}

	private static void processNewPlayerData(String data) {
		String[] newPlayerData = data.split(",");
		Color cycleColor = ColorUtils.stringToColor(newPlayerData[0]);
		Color jetColor = ColorUtils.stringToColor(newPlayerData[1]);
		String nickname = newPlayerData[2];
		LightCycle lightCycle = new LightCycle(cycleColor, jetColor, nickname);
		entities.add(lightCycle);
	}

	private static void processEntitiesData(String[] data) {
		if (data.length == 1) {
			return;
		}
		data = data[1].split(",");

		for (int i = 0; i < data.length; i += 4) {
			String nickname = data[i];
			Integer x = Integer.valueOf(data[i + 1]);
			Integer y = Integer.valueOf(data[i + 2]);
			Boolean isJetOn = Boolean.valueOf(data[i + 3]);

			LightCycle lightCycle = getLightCycleByName(nickname);
			if (lightCycle == null) {
				return;
			}

			if (lightCycle.isJetWallOn()) {
				int wallX = lightCycle.getX();
				int wallY = lightCycle.getY();
				entities.add(new Wall(wallX, wallY, lightCycle));
				lightCycle.getPlayer().incrementScore();
			}

			lightCycle.setX(x);
			lightCycle.setY(y);
			lightCycle.setJetWallOn(isJetOn);

			if (isCrashed(lightCycle)) {
				send(Query.remove(nickname));
				send("USER " + lightCycle.getPlayer().getNickname() + " SCORE " + lightCycle.getPlayer().getScore());
			}
		}
	}

	private static LightCycle getLightCycleByName(String nickname) {
		for (Entity entity : entities) {
			if (entity instanceof LightCycle) {
				LightCycle lightCycle = (LightCycle) entity;
				if (lightCycle.getPlayer().getNickname().contains(nickname)) {
					return lightCycle;
				}
			}
		}
		return null;
	}

	private static boolean isCrashed(LightCycle lightCycle) {
		for (Entity entity : entities) {
			if (entity.getX() == lightCycle.getX() && entity.getY() == lightCycle.getY()) {
				if (!entity.equals(lightCycle)) {
					return true;
				}
			}
		}
		return false;
	}

}
