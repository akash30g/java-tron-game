package game;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class KryoClient {

	private static Client client;

	public static void connect(String host, int tcpPort, int udpPort) throws IOException {
		client = new Client();
		// client.getKryo().register();
		client.start();
		client.connect(5000, "192.168.0.4", 54555, 54777);
		client.addListener(new Listener() {
			public void received(Connection connection, Object object) {

			}
		});
	}

	public void send() {
		client.sendTCP("");
	}
}
