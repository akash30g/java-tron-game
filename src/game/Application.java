package game;

import java.awt.EventQueue;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Application extends JFrame {

	public Application() {
		initUI();
	}

	public static void main(String[] args) {

		initMenu();

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Application ex = new Application();
				ex.setVisible(true);
			}
		});
	}

	private void initUI() {
		Map map = new Map();
		add(map);
		setSize(800, 800);
		setTitle("Tron game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	private static void initMenu() {
		StringBuilder sb = new StringBuilder();
		sb.append("Main menu:").append("\n");
		sb.append("-----------------").append("\n");
		sb.append("[1] - Host").append("\n");
		sb.append("[2] - Connect").append("\n");
		sb.append("[3] - Exit.");
		String input = JOptionPane.showInputDialog(sb.toString());
		switch (input) {
		case "1":
			try {
				KryoServer.start(12345, 12345);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Server start failed. Application shutdown.");
			}
			break;
		case "2":
			try {
				KryoClient.connect("localhost", 12345, 12345);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Connection failed. Application shutdown.");
			}
			break;
		case "3":
			System.exit(0);
			break;
		default:
			break;
		}
	}
}
