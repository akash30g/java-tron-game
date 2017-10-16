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
		String[] data = object.split(">");
		if (data[0].equals("ENTITIES")) {
			processEntitiesData(data);
		}
	}

	private static void processEntitiesData(String[] data) {
		for (int i = 1; i < data.length; i++) {
			String entity = data[i];
			String[] entityData = entity.split(";");
			createEntityFromData(entityData);
		}
	}

	private static void createEntityFromData(String[] entityData) {
		if (entityData[0].equals("Wall")) {
			// TODO
		}
		if (entityData[0].equals("LightCycle")) {
			String x = entityData[1];
			String y = entityData[2];
			String nickname = entityData[3];
			if (getPlayerByNickname(nickname) != null) {
				LightCycle lightCycle = getPlayerByNickname(nickname);
				lightCycle.setX(Integer.valueOf(x));
				lightCycle.setY(Integer.valueOf(y));
			} else {
				LightCycle lightCycle = new LightCycle(Integer.valueOf(x), Integer.valueOf(y), Color.BLUE, Color.BLUE,
						nickname);
				entities.add(lightCycle);
			}
		}
	}

	private static LightCycle getPlayerByNickname(String nickname) {
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
		return getPlayerByNickname(nickname);
	}

}
