package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import game.entities.Entity;

public class KryoClient {

	private static Client client;
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
		List<Entity> entities = new ArrayList<>();
		for (int i = 1; i < data.length; i++) {
			String entity = data[i];
			String[] entityData = entity.split(";");
		}
	}

}
