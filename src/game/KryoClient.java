package game;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
			}
			
			lightCycle.setX(x);
			lightCycle.setY(y);
			lightCycle.setJetWallOn(isJetOn);
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

	public static LightCycle getThisPlayer() {
		return getLightCycleByName(nickname);
	}

}
