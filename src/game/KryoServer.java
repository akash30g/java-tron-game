package game;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import game.entities.Direction;
import game.entities.LightCycle;
import game.protocol.Query;
import game.utils.ColorUtils;

public class KryoServer {

	private static Server server = new Server();
	private static List<LightCycle> lightCycles = new ArrayList<>();

	private static HashMap<String, Integer> scoreMap = new HashMap<>();

	private static int playerCounter = 0;

	public static void start(int tcpPort, int udpPort) throws IOException {
		server.start();
		server.bind(tcpPort, udpPort);
		server.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				if (object instanceof String) {
					processData((String) object, connection);
				}
			}

			public void connected(Connection connection) {
				playerCounter++;
				if (playerCounter > 1) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								System.out.println("Game is about to start.");
								Thread.sleep(10000);// Waiting for 10 seconds
								server.sendToAllUDP("GAME IS READY");
								System.out.println("Game has started.");
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}).start();
				}
			}

			public void disconnected(Connection connection) {
				LightCycle lightCycle = getByName(connection.toString());
				lightCycle.setDerezzed(true);
				playerCounter--;
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
							if (isCrashed(lightCycle)) {
								lightCycle.setDerezzed(true);
							}
						}

						if (isGameOver(lightCycles)) {
							server.sendToAllUDP("GAME OVER");
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
		if (object.contains("JETWALL")) {
			processJetQuery(data, connection);
		}
		if (object.contains("REMOVE")) {
			processRemoveQuery(data, connection);
		}
		if (object.contains("SCORE")) {
			processScoreQuery(data, connection);
		}
	}

	private static void processScoreQuery(String[] data, Connection connection) {
		String nickname = data[1];
		Integer score = Integer.valueOf(data[3]);
		scoreMap.put(nickname, score);
	}

	private static void processRemoveQuery(String[] data, Connection connection) {
		String nickname = data[2];
		if (!isExist(nickname)) {
			return;
		}
		LightCycle lightCycle = getByName(nickname);
		lightCycle.setDerezzed(true);
	}

	private static void processJetQuery(String[] data, Connection connection) {
		String nickname = data[1];
		if (!isExist(nickname)) {
			return;
		}
		String dir = data[3];
		LightCycle lightCycle = getByName(nickname);
		if (dir.equals("true")) {
			lightCycle.setJetWallOn(true);
		} else {
			lightCycle.setJetWallOn(false);
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
		LightCycle newLightCycle = new LightCycle(5, 5, cycleColor, jetColor, nickname);
		putRandomlyOnGrid(newLightCycle);
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

	private static void putRandomlyOnGrid(LightCycle lightCycle) {
		int randomDirection = new Random().nextInt(Direction.values().length);

		Direction direction = Direction.DOWN;
		int x = 30;
		int y = 30;

		switch (Direction.values()[randomDirection]) {
		case DOWN:
			direction = Direction.DOWN;
			x = new Random().nextInt(650) + 50;
			y = new Random().nextInt(50) + 50;
			break;
		case LEFT:
			direction = Direction.LEFT;
			x = new Random().nextInt(50) + 650;
			y = new Random().nextInt(650) + 50;
			break;
		case RIGHT:
			direction = Direction.RIGHT;
			x = new Random().nextInt(50) + 50;
			y = new Random().nextInt(650) + 50;
			break;
		case UP:
			direction = Direction.UP;
			x = new Random().nextInt(650) + 50;
			y = new Random().nextInt(50) + 650;
			break;
		}
		x -= x % 15;
		y -= y % 15;
		lightCycle.setDirection(direction);
		lightCycle.setX(x);
		lightCycle.setY(y);
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

	private static boolean isGameOver(List<LightCycle> lightCycles) {
		if (lightCycles.size() > 1) {
			int activePlayers = 0;
			for (LightCycle lightCycle : lightCycles) {
				if (!lightCycle.isDerezzed()) {
					activePlayers++;
				}
			}
			if (activePlayers == 1) {
				return true;
			}
		}
		return false;
	}

	private static boolean isCrashed(LightCycle lightCycle) {
		if (lightCycle.getX() < -5 || lightCycle.getY() < -5) {
			return true;
		}
		if (lightCycle.getX() > 805 || lightCycle.getY() > 805) {
			return true;
		}
		return false;
	}

}
