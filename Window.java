import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Window extends Canvas{

	private static final long serialVersionUID = -8255319694373975038L;
	private JFrame frame;
	
	public Window(int WIDTH, int HEIGHT, String title, Source source) {
		frame = new JFrame(title);
		Dimension standard = new Dimension(WIDTH,HEIGHT);
		frame.setPreferredSize(standard);
		frame.setMaximumSize(standard);
		frame.setMinimumSize(standard);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.add(source);
		frame.setVisible(true);
		//source.run() was here
	}
	
	public JFrame getFrame() {
		return frame;
	}
}
