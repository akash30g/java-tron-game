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

	/*
	 * Test
	 */

	static {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (true) {
						Thread.sleep(100);
						// if (!entities.isEmpty()) {
						// int random = new Random().nextInt(entities.size());
						// entities.get(random).setX(entities.get(random).getX() + (random + 1) * 2);
						// entities.get(random).setY(entities.get(random).getY() + (random + 1) * 3);
						// }
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

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

			}
		});

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(100);
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
			processConnectData(data, connection);
		}
	}

	private static void processConnectData(String[] data, Connection connection) {
		String nickname = data[2];
		connection.setName(nickname);
		Color cycleColor = ColorUtils.stringToColor(data[3]);
		Color jetColor = ColorUtils.stringToColor(data[4]);
		int randX = 15; // TODO remove
		int randY = 15; // TODO remove
		lightCycles.add(new LightCycle(randX, randY, cycleColor, jetColor, nickname));
	}

}
