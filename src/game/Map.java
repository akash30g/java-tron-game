package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import game.entities.Entity;
import game.entities.LightCycle;
import game.entities.Wall;
import javafx.scene.input.KeyCode;

public class Map extends JPanel {

	public Map() {
		setFocusable(true);
		setBackground(Color.BLACK);

		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				repaint();
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
	}

	private void drawEntities(Graphics g) {
		List<Entity> entities = KryoServer.getEntities();
		for (Entity entity : entities) {
			if (entity instanceof Wall) {
				Wall wall = (Wall) entity;
				Color color = (wall.isJetWall()) ? wall.getOwner().getJetColor() : Color.RED;
				g.setColor(color);
				g.fillRect(wall.getX(), wall.getY(), 10, 10);
			}
			if (entity instanceof LightCycle) {
				LightCycle lightCycle = (LightCycle) entity;
				Color color = lightCycle.getCycleColor();
				g.setColor(color);
				g.fillRect(lightCycle.getX(), lightCycle.getY(), 25, 25);
			}
		}
	}

}
