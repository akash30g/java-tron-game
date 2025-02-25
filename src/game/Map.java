package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JPanel;

import game.entities.Entity;
import game.entities.LightCycle;
import game.entities.Wall;
import game.protocol.Query;

public class Map extends JPanel {

	private static final long serialVersionUID = 1338402938933071768L;

	public Map() {
		setFocusable(true);
		setBackground(Color.BLACK);

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(50);
						repaint();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 37) { // LEFT ARROW
					String query = Query.turnLeft(KryoClient.getNickname());
					KryoClient.send(query);
				} else if (e.getKeyCode() == 39) { // RIGHT ARROW
					String query = Query.turnRight(KryoClient.getNickname());
					KryoClient.send(query);
				} else if (e.getKeyCode() == 69) { // E
					String query = Query.jetOff(KryoClient.getNickname());
					KryoClient.send(query);
				} else if (e.getKeyCode() == 81) { // Q
					String query = Query.jetOn(KryoClient.getNickname());
					KryoClient.send(query);
				}
			}
		});
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawBackground(g);
		drawEntities(g);
	}

	private void drawBackground(Graphics g) {
		int step = 15;
		int max = 885;

		for (int i = 0; i < max; i += step) {
			g.drawLine(0, i, max, i);
		}
		for (int i = 0; i < max; i += step) {
			g.drawLine(i, 0, i, max);
		}
		
		g.setColor(Color.white);
		g.drawLine(1, 0, 1, max);
		g.drawLine(1, 0, 795, 1);
		g.drawLine(795, 1, 795, 795);
		g.drawLine(1, 750, max, 750);
	}

	private void drawEntities(Graphics g) {
		try {
			List<Entity> entities = KryoClient.getEntities();
			for (Entity entity : entities) {
				if (entity instanceof Wall) {
					Wall wall = (Wall) entity;
					Color color = wall.getOwner().getJetColor();
					g.setColor(color);
					g.fillRect(wall.getX(), wall.getY(), 7, 7);
				}
				if (entity instanceof LightCycle) {
					LightCycle lightCycle = (LightCycle) entity;
					if (lightCycle.getX() == 0 || lightCycle.getY() == 0) {
						return;
					}
					Color color = lightCycle.getCycleColor();
					g.setColor(color);
					g.fillRect(lightCycle.getX(), lightCycle.getY(), 15, 15);
					g.drawString(lightCycle.getPlayer().getNickname(), lightCycle.getX(), lightCycle.getY() - 25);
				}
			}
		} catch (Exception e) {

		}
	}

}
