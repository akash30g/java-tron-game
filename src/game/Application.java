package game;

import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;

public class Application extends JFrame {
	private KryoServer kryoServer;
	private List<KryoClient> kryoClients;

	public Application() {
		initUI();
	}

	public static void main(String[] args) {
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
}
