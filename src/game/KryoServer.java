package game;

import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class KryoServer {

	private Server server;

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

	public void sendUDP() {
		server.sendToAllUDP("");
	}
}
