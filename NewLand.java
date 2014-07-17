package NewLand;

import javax.swing.JFrame;

public class NewLand extends JFrame {
	private final int t_width = 100;
	private final int t_height = 100;
	private final int t_zoom = 5;

	public NewLand() {
		add(new Board(t_width, t_height, t_zoom));
        setTitle("New Land");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize((t_width * t_zoom) + 2, (t_height * t_zoom) + 124);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
	}

	public static void main(String[] args) {
		new NewLand();
	}
}