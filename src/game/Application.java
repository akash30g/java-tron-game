package game;

import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import game.entities.LightCycle;
import game.protocol.Query;
import game.utils.ColorUtils;
import game.utils.ReplyUtils;

/*
 * This class contains starting point of the game
 */

public class Application extends JFrame {

	private static final long serialVersionUID = -3301717846800216801L;

	/*
	 * Constructor that initialise UI
	 */

	public Application() {
		initUI();
	}

	public static void main(String[] args) throws InterruptedException {

		/*
		 * Before the game starts, create and show menu
		 */

		initMainMenu();

		/*
		 * Launch game after this
		 */

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Application ex = new Application();
				ex.setVisible(true);
			}
		});
	}

	/*
	 * This method draws map that will contain light cycles and walls
	 */

	private void initUI() {
		Map map = new Map();
		add(map);
		setSize(800, 800);
		setTitle("Tron game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	/*
	 * This contains main menu logic
	 */

	private static void initMainMenu() throws InterruptedException {

		/*
		 * Create menu with those strings
		 */

		StringBuilder sb = new StringBuilder();
		sb.append("Main menu:").append("\n");
		sb.append("-----------------").append("\n");
		sb.append("[1] - Host").append("\n");
		sb.append("[2] - Connect").append("\n");
		sb.append("[3] - Exit.");

		/*
		 * Get user input
		 */

		String input = JOptionPane.showInputDialog(sb.toString());
		switch (input) {
		case "1": // If input is 1
			try {
				KryoServer.start(12345, 12345); // Start server
				KryoClient.connect("localhost", 12345, 12345); // Connect
				registerPlayer(); // Register new player
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Server start failed. Application shutdown.");
				System.exit(0);
			}
			break;
		case "2": // If input is 2
			try {
				KryoClient.connect("localhost", 12345, 12345); // Connect
				registerPlayer(); // Register new player
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Connection failed. Application shutdown.");
				System.exit(0);
			}
			break;
		case "3": // If input is 3
			System.exit(0); // Exit
			break;
		default:
			JOptionPane.showMessageDialog(null, "Unrecognized parameters. Application shutdown.");
			System.exit(0);
			break;
		}
	}

	private static void registerPlayer() throws InterruptedException {

		/*
		 * Create form for nickname and two colors
		 */

		JTextField nicknameField = new JTextField();
		JPanel holder = new JPanel();
		holder.setLayout(new BoxLayout(holder, 1));
		holder.add(new JLabel("Input your nickname:"));
		holder.add(nicknameField);
		holder.add(new JLabel("Choose your cycle color:"));
		String[] colors = { "Blue", "Red", "Green" };
		JComboBox<String> cycleColorComboBox = new JComboBox<>(colors);
		holder.add(cycleColorComboBox);
		holder.add(new JLabel("Choose your jetwall color:"));
		JComboBox<String> jetWallColorComboBox = new JComboBox<>(colors);
		holder.add(jetWallColorComboBox);
		JOptionPane.showMessageDialog(null, holder);

		/*
		 * Extract user input
		 */

		String nickname = nicknameField.getText();
		String cycleColor = (String) cycleColorComboBox.getSelectedItem();
		String jetColor = (String) jetWallColorComboBox.getSelectedItem();
		KryoClient.setNickname(nickname);

		/*
		 * Create and send request to the server
		 */

		String request = Query.add(nickname, cycleColor, jetColor);
		KryoClient.sendAndWait(request);

		/*
		 * Wait for reply
		 */

		System.out.println("[LOG] Waiting.");
		while (KryoClient.isWaitingForReply()) {
			Thread.sleep(500);
		}
			
		System.out.println("[LOG] Connected.");
		String reply = KryoClient.getLastReply();

		/*
		 * If failed, show error and close the game Otherwise add player to the field
		 * and start the game
		 */
		if (!ReplyUtils.isFailed(reply)) {
			KryoClient.getEntities().add(
					new LightCycle(ColorUtils.stringToColor(cycleColor), ColorUtils.stringToColor(jetColor), nickname));
		} else {
			JOptionPane.showMessageDialog(null, ReplyUtils.getFailedReason(reply));
			System.exit(0);
		}

	}

}
