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

	private Server server;
	private static List<Entity> entities;

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

	public KryoServer() {
		server = new Server();

		// server.getKryo().register();
	}

	public void start(int tcpPort, int udpPort) throws IOException {
		server.start();
		server.bind(54555, 54777);
		server.addListener(new Listener() {
			public void received(Connection connection, Object object) {

			}
		});
	}

	public static List<Entity> getEntities() {
		return entities;
	}

	public void sendUDP() {
		server.sendToAllUDP("");
	}
}
