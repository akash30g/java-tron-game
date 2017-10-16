package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import game.entities.Entity;
import game.entities.LightCycle;
import protocol.Query;

public class KryoClient {

	private static Client client;
	private static String nickname;
	private static List<Entity> entities = new ArrayList<>();

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

	private static void processData(String object) {
		String[] data = object.split(" ");
		if (data[0].equals(Query.PLAYERS)) {
			processEntitiesData(data);
		}
	}

	private static void processEntitiesData(String[] data) {
		data = data[1].split(",");
		for (int i = 0; i < data.length; i += 4) {
			String nickname = data[i];
			Integer x = Integer.valueOf(data[i + 1]);
			Integer y = Integer.valueOf(data[i + 2]);
			Boolean isJetOn = Boolean.valueOf(data[i + 3]);

			LightCycle lightCycle = getLightCycleByName(nickname);
			lightCycle.setX(x);
			lightCycle.setY(y);
			lightCycle.setJetWallOn(isJetOn);
		}
	}

	private static LightCycle getLightCycleByName(String nickname) {
		for (Entity entity : entities) {
			if (entity instanceof LightCycle) {
				LightCycle lightCycle = (LightCycle) entity;
				if (lightCycle.getPlayer().getNickname().equals(nickname)) {
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
