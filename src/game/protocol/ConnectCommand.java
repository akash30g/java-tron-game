package game.protocol;

import java.awt.Color;

public class ConnectCommand {
	private String nickname;
	private Color cycleColor;
	private Color jetColor;

	public ConnectCommand(String nickname, Color cycleColor, Color jetColor) {
		this.nickname = nickname;
		this.cycleColor = cycleColor;
		this.jetColor = jetColor;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
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

}
