import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class NoCloneZone extends GameObject implements Comparable<GameObject>{

	public NoCloneZone(int x, int y, int width) {
		super(x, y, ID.NoCloneZone, width, width);
	}

	@Override
	public void tick() {
		//default to nothing
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		if (!Game.debugMode) {
			int r = getColor(255);
			int gc = Game.clamp(getColor(110) - 10, 0, 255);
			int b = 0;
			g.setColor(new Color(r, gc, b));
		}
		int[] xcords = {x, x + width, x + width, x, x, x + width / 8, x + width * 7 / 8, x + width * 7 / 8, x + width / 8, x + width / 8, x, x};
		int[] ycords = {y, y, y + height, y + height, y + height / 8, y + height / 8, y + height / 8,
					    y + height * 7 / 8, y + width * 7 / 8, y + width / 8, y + width / 8, y};
		g.fillPolygon(xcords, ycords, xcords.length);
	}
	
	private int getColor(int max) {
		return (int)(Game.clamp(Math.abs(Math.sin(Game.gameTime / Math.PI / 60) * max), 100.0, 255.0));
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	
	
}
