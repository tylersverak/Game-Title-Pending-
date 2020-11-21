import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class JumpTile extends Tile{
	
	public JumpTile(int x, int y, int width, int height, Handler handler, Color map[][]) {
		super(x, y, width, height, handler, map);
	}
	
	public void render(Graphics g) {
		if (Game.debugMode) {
			super.render(g);
		}else {
			g.setColor(Color.pink);
			g.fillRect(x, y, width, height);
			int[] xPoints = {x + width / 2, x + width * 3 / 4, x + width * 5 / 8, x + width * 5 / 8,
							 x + width * 3 / 8, x + width * 3 / 8, x + width / 4};
			int[] yPoints = {y + height / 4, y + height / 2, y + height / 2, y + height * 3 / 4,
							 y + height * 3 / 4, y + height / 2, y + height / 2};
			g.setColor(Color.white);
			g.fillPolygon(xPoints, yPoints, xPoints.length);
		}
	}
	
	public void touched(Playerv2 p) {
		if (p.getY() + p.getHeight() <= this.y) {
			p.setY(p.getY() - 1);
			p.setYV(-20);
			System.out.println(p.getY() + p.getHeight() + " " + this.y);
			System.out.println(p.getY() + p.getHeight() <= this.y);
		}
		
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
}
