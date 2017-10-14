package game.entities;

public class Wall extends Entity {

	public Wall(int x, int y) {
		super(x, y);
	}

	public Wall(int x, int y, LightCycle owner) {
		super(x, y);
		this.owner = owner;
	}

	private LightCycle owner;

	public LightCycle getOwner() {
		return owner;
	}

	public void setOwner(LightCycle owner) {
		this.owner = owner;
	}

	public boolean isJetWall() {
		return owner != null;
	}
}
