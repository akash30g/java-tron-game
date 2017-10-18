package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Color;
import java.io.IOException;

import org.junit.Test;

import game.KryoClient;
import game.KryoServer;
import game.entities.Direction;
import game.entities.LightCycle;
import game.protocol.Query;

public class GameTest {

	@Test
	public void movementTest() throws IOException {
		LightCycle lightCycle = new LightCycle(Color.BLACK, Color.BLACK, "test");
		lightCycle.setX(15);
		lightCycle.setY(15);
		lightCycle.setDirection(Direction.DOWN);
		lightCycle.move();
		assertEquals(15, lightCycle.getX());
		assertEquals(30, lightCycle.getY());
		lightCycle.setDirection(Direction.RIGHT);
		lightCycle.move();
		assertEquals(30, lightCycle.getX());
		assertEquals(30, lightCycle.getY());
		lightCycle.setDirection(Direction.UP);
		lightCycle.move();
		assertEquals(30, lightCycle.getX());
		assertEquals(15, lightCycle.getY());
		lightCycle.setDirection(Direction.LEFT);
		lightCycle.move();
		assertEquals(15, lightCycle.getX());
		assertEquals(15, lightCycle.getY());
	}

	@Test
	public void movementTestTwo() throws IOException {
		LightCycle lightCycle = new LightCycle(Color.BLACK, Color.BLACK, "test");
		lightCycle.setX(15);
		lightCycle.setY(15);
		lightCycle.setDirection(Direction.DOWN);
		lightCycle.move();
		assertEquals(15, lightCycle.getX());
		assertEquals(30, lightCycle.getY());
		lightCycle.turnLeft();
		lightCycle.move();
		assertEquals(30, lightCycle.getX());
		assertEquals(30, lightCycle.getY());
		lightCycle.turnLeft();
		lightCycle.move();
		assertEquals(30, lightCycle.getX());
		assertEquals(15, lightCycle.getY());
		lightCycle.turnRight();
		lightCycle.move();
		assertEquals(45, lightCycle.getX());
		assertEquals(15, lightCycle.getY());
		lightCycle.turnRight();
		lightCycle.move();
		assertEquals(45, lightCycle.getX());
		assertEquals(30, lightCycle.getY());
	}

	@Test
	public void connectionTest() throws IOException, InterruptedException {
		KryoServer.start(12345, 12345);
		KryoClient.connect("localhost", 12345, 12345);
		String data = Query.add("test", "blue", "blue");
		KryoClient.send(data);
		Thread.sleep(50);
		LightCycle lightCycle = KryoServer.getLightCycles().get(0);
		assertNotNull(lightCycle);
		assertEquals("test", lightCycle.getPlayer().getNickname());
		assertEquals("REPLY OKAY", KryoClient.getLastReply());
	}

}
