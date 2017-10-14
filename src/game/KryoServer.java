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

public class KryoServer {

	private static Server server = new Server();
	private static List<Entity> entities;

	/*
	 * Test
	 */

	static {
		entities = new ArrayList<>();
		entities.add(new Wall(35, 45));
		entities.add(new LightCycle(35, 45, Color.BLUE, Color.GREEN));
		entities.add(new Wall(25, 45, (LightCycle) entities.get(1)));

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (true) {
						Thread.sleep(200);
						int random = new Random().nextInt(entities.size());
						entities.get(random).setX(entities.get(random).getX() + (random + 1) * 5);
						entities.get(random).setY(entities.get(random).getY() + (random + 1) * 5);
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

	public static List<Entity> getEntities() {
		return entities;
	}

	public static void sendEntities() {
		StringBuilder sb = new StringBuilder();
		for (Entity entity : entities) {
			if (entity instanceof LightCycle) {
				LightCycle lightCycle = (LightCycle) entity;
				sb.append(lightCycle).append(">");
			}
			if (entity instanceof Wall) {
				Wall wall = (Wall) entity;
				sb.append(wall).append(">");
			}
		}
		server.sendToAllUDP(sb.toString());
	}

	private static void processData(String object, Connection connection) {
		String[] data = object.split(";");
		if (data[0].equals("CONNECT")) {
			processConnectData(data);
		}
	}

	private static void processConnectData(String[] data) {
		String nickname = data[1];
		Color cycleColor = ColorUtils.stringToColor(data[2]);
		Color jetColor = ColorUtils.stringToColor(data[3]);
		entities.add(new LightCycle(15, 15, cycleColor, jetColor, nickname));
	}

}
