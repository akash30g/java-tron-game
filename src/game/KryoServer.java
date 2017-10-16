package game;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import game.entities.Entity;
import game.entities.LightCycle;
import game.entities.Wall;
import game.protocol.Query;
import game.utils.ColorUtils;

public class KryoServer {

	private static Server server = new Server();
	private static List<LightCycle> lightCycles = new ArrayList<>();

	public static void start(int tcpPort, int udpPort) throws IOException {
		server.start();
		server.bind(tcpPort, udpPort);
		server.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				if (object instanceof String) {
					processData((String) object, connection);
				}
			}

			public void disconnected(Connection connection) {
				LightCycle lightCycle = getByName(connection.toString());
				lightCycles.remove(lightCycle);
			}
		});

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(300); // TODO change to 100
						for (LightCycle lightCycle : lightCycles) {
							lightCycle.move();
						}
						sendEntities();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public static List<LightCycle> getLightCycles() {
		return lightCycles;
	}

	public static void sendEntities() {
		if (lightCycles.isEmpty()) {
			return;
		}
		String result = Query.sendPlayers(lightCycles);
		server.sendToAllUDP(result);
	}

	private static void processData(String object, Connection connection) {
		String[] data = object.split(" ");
		if (data[0].equals("ADD")) {
			processAddQuery(data, connection);
		}
		if (object.contains("TURN")) {
			processTurnQuery(data, connection);
		}
	}

	private static void processTurnQuery(String[] data, Connection connection) {
		String nickname = data[1];
		if (!isExist(nickname)) {
			return;
		}
		String side = data[3];
		LightCycle lightCycle = getByName(nickname);
		if (side.equals("left")) {
			lightCycle.turnLeft();
		} else {
			lightCycle.turnRight();
		}
	}

	private static void processAddQuery(String[] data, Connection connection) {
		String nickname = data[2];
		if (isExist(nickname)) {
			server.sendToTCP(connection.getID(), "REPLY FAILED Error. Such nickname is already registered");
			return;
		}
		connection.setName(nickname);
		Color cycleColor = ColorUtils.stringToColor(data[3]);
		Color jetColor = ColorUtils.stringToColor(data[4]);
		int randX = 15; // TODO remove
		int randY = 15; // TODO remove
		LightCycle newLightCycle = new LightCycle(randX, randY, cycleColor, jetColor, nickname);
		lightCycles.add(newLightCycle);
		server.sendToTCP(connection.getID(), "REPLY OKAY");
		String query = Query.sendNewPlayer(nickname, data[3], data[4]);
		server.sendToAllExceptTCP(connection.getID(), query);

		if (lightCycles.size() > 1) {
			for (LightCycle lightCycle : lightCycles) {
				String thisNickname = lightCycle.getPlayer().getNickname();
				if (!thisNickname.equals(nickname)) {
					query = Query.sendNewPlayer(thisNickname, ColorUtils.colorToString(lightCycle.getCycleColor()),
							ColorUtils.colorToString(lightCycle.getJetColor()));
					server.sendToTCP(connection.getID(), query);
				}
			}
		}
	}

	private static boolean isExist(String nickname) {
		for (LightCycle lightCycle : lightCycles) {
			if (lightCycle.getPlayer().getNickname().equals(nickname)) {
				return true;
			}
		}
		return false;
	}

	private static LightCycle getByName(String nickname) {
		for (LightCycle lightCycle : lightCycles) {
			if (lightCycle.getPlayer().getNickname().equals(nickname)) {
				return lightCycle;
			}
		}
		return null;
	}

}
