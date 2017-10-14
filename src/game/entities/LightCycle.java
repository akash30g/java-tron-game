package game.entities;

import java.awt.Color;

public class LightCycle extends Entity {

	public LightCycle(int x, int y) {
		super(x, y);
	}

	public LightCycle(int x, int y, Color cycleColor, Color jetColor) {
		super(x, y);
		this.cycleColor = cycleColor;
		this.jetColor = jetColor;
	}

	private double velocity;
	private boolean isJetWallOn;
	private Direction direction;
	private Color cycleColor;
	private Color jetColor;
	private Player player;

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public boolean isJetWallOn() {
		return isJetWallOn;
	}

	public void setJetWallOn(boolean isJetWallOn) {
		this.isJetWallOn = isJetWallOn;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Color getCycleColor() {
		return cycleColor;
	}

	public void setCycleColor(Color cycleColor) {
		this.cycleColor = cycleColor;
	}

	public Color getJetColor() {
		return jetColor;
	}

	public void setJetColor(Color jetColor) {
		this.jetColor = jetColor;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
