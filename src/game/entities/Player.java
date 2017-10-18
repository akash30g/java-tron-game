package game.entities;

public class Player {
	private String nickname;
	private int score = 0;

	public Player(String nickname) {
		this.nickname = nickname;
	}

	public int getScore() {
		return score;
	}
	
	public void incrementScore() {
		score++;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}
