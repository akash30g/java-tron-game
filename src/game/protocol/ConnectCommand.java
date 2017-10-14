package game.protocol;

public class ConnectCommand {
	private String nickname;
	private String cycleColor;
	private String jetColor;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getCycleColor() {
		return cycleColor;
	}

	public void setCycleColor(String cycleColor) {
		this.cycleColor = cycleColor;
	}

	public String getJetColor() {
		return jetColor;
	}

	public void setJetColor(String jetColor) {
		this.jetColor = jetColor;
	}

}
