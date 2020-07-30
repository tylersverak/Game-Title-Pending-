import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.IOException;

public class TitleScreen extends Source{
	
	private static final long serialVersionUID = 1L;
	private int color = 0;
	
	public TitleScreen(String title, Run masterRun) throws IOException{
		super(title);
		this.requestFocusInWindow();
		this.addKeyListener(new KeyInput(handler, this, masterRun));
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		color++;
	}

	@Override
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(new Color(color % 255, 0, 0));
		g.setFont(new Font("Dialog", Font.PLAIN, 100));
		g.drawString("Oh wow cool a game", 500, 600);
		g.dispose();
		bs.show();
	}
}
