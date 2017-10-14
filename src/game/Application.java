package game;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Application extends JFrame {

	public Application() {
		initUI();
	}

	public static void main(String[] args) {

		initMainMenu();

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

	private static void initMainMenu() {
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
				KryoClient.connect("localhost", 12345, 12345);
				initPlayerMenu();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Server start failed. Application shutdown.");
				e.printStackTrace();
			}
			break;
		case "2":
			try {
				KryoClient.connect("localhost", 12345, 12345);
				initPlayerMenu();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Connection failed. Application shutdown.");
			}
			break;
		case "3":
			System.exit(0);
			break;
		default:
			System.exit(0);
		}
	}

	private static void initPlayerMenu() {
		JTextField nickname = new JTextField();
		JPanel holder = new JPanel();
		holder.setLayout(new BoxLayout(holder, 1));
		holder.add(new JLabel("Input your nickname:"));
		holder.add(nickname);
		holder.add(new JLabel("Choose your cycle color:"));
		JComboBox<Color> cycleColorComboBox = new JComboBox<>(new Color[] { Color.BLUE, Color.RED, Color.GREEN });
		holder.add(cycleColorComboBox);
		holder.add(new JLabel("Choose your jetwall color:"));
		JComboBox<Color> jetWallColorComboBox = new JComboBox<>(new Color[] { Color.BLUE, Color.RED, Color.GREEN });
		holder.add(jetWallColorComboBox);
		JOptionPane.showMessageDialog(null, holder);
	}

}
