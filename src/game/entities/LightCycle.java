package game.entities;

import java.awt.Color;

/*
 * Class that holds main logic for light cycle
 */

public class LightCycle extends Entity {

	private double velocity;
	private boolean isJetWallOn;
	private Direction direction;
	private Color cycleColor;
	private Color jetColor;
	private Player player;
	private boolean isDerezzed;

	public LightCycle(int x, int y, Color cycleColor, Color jetColor, String nickname) {
		super(x, y);
		this.velocity = 10;
		this.direction = Direction.DOWN;
		this.player = new Player(nickname);
		this.isJetWallOn = true;
		this.cycleColor = cycleColor;
		this.jetColor = jetColor;
	}

	public LightCycle(Color cycleColor, Color jetColor, String nickname) {
		super(0, 0);
		this.velocity = 10;
		this.direction = Direction.DOWN;
		this.player = new Player(nickname);
		this.isJetWallOn = true;
		this.cycleColor = cycleColor;
		this.jetColor = jetColor;
	}

	/*
	 * Move for 15px in specified direction
	 */

	public void move() {
		switch (direction) {
		case UP:
			setY(getY() - 15);
			break;
		case DOWN:
			setY(getY() + 15);
			break;
		case LEFT:
			setX(getX() - 15);
			break;
		case RIGHT:
			setX(getX() + 15);
			break;
		}
	}

	/*
	 * Change direction according to the current
	 */

	public void turnLeft() {
		switch (direction) {
		case DOWN:
			direction = Direction.RIGHT;
			break;
		case LEFT:
			direction = Direction.DOWN;
			break;
		case RIGHT:
			direction = Direction.UP;
			break;
		case UP:
			direction = Direction.LEFT;
			break;
		}
	}

	public void turnRight() {
		switch (direction) {
		case DOWN:
			direction = Direction.LEFT;
			break;
		case LEFT:
			direction = Direction.UP;
			break;
		case RIGHT:
			direction = Direction.DOWN;
			break;
		case UP:
			direction = Direction.RIGHT;
			break;
		}
	}

	/*
	 * Increment velocity
	 */

	public void accelerate() {
		if (velocity != 9) { // Make sure it can't go higher than 9
			velocity++;
		}
	}

	/*
	 * Decrement velocity
	 */

	public void slowDown() {
		if (velocity != 1) {	// Make sure it can't go lower than 1
			velocity--;
		}
	}

	/*
	 * Getters & Setters and toString()
	 */
	
	public boolean isDerezzed() {
		return isDerezzed;
	}

	public void setDerezzed(boolean isDerezzed) {
		this.isDerezzed = isDerezzed;
	}

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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(";").append(getX()).append(";").append(getY()).append(";")
				.append(getPlayer().getNickname());
		return sb.toString();
	}
}
