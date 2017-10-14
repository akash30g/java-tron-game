package game;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class KryoClient {

	private static Client client;

	public static void connect(String host, int tcpPort, int udpPort) throws IOException {
		client = new Client();
		client.start();
		client.connect(10000, host, tcpPort, udpPort);
		client.addListener(new Listener() {
			public void received(Connection connection, Object object) {

			}
		});
	}

	public static void send(String data) {
		client.sendTCP(data);
	}

}
