import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Chakra extends GameObject implements Comparable<GameObject>{
	
	private int size, xv, yv, timer, distance;
	private Handler handler;
	private String level;
	private Playerv2 target;
	
	public Chakra(int x, int y, ID id, int size, Handler handler, Playerv2 dude) {
		super(x, y, id, size, size);
		this.size = size;
		this.distance = 1;
		this.xv = Game.rand.nextInt(distance * 2 + 1) - distance;
		this.yv = Game.rand.nextInt(distance * 2 + 1) - distance;
		this.timer = 60 * 4;
		this.handler = handler;
		this.level = Game.masterMap.levelName();
		target = dude;
	}

	@Override
	public void tick() {
		deteriorate();
		x += xv;
		y += yv;
		if (Game.gameTime % 3 == 0) {
			if (x != target.getMiddle()[0]) {
				xv = (target.getMiddle()[0]- x) / Math.abs((target.getMiddle()[0]- x));
				xv += Game.rand.nextInt(distance * 2 + 1) - distance;
			}
			if (y != target.getMiddle()[1]) {
				yv = (target.getMiddle()[1] - y) / Math.abs((target.getMiddle()[1]- y));
				yv += Game.rand.nextInt(distance * 2 + 1) - distance;
			}
			//xv = Game.rand.nextInt(distance * 2 + 1) - distance;
			//yv = Game.rand.nextInt(distance * 2 + 1) - distance;
		}
	}
	
	private void deteriorate() {
		if (size <= 0) {
			handler.removeObject(this);
		}else if(timer <= 0) {
			size--;
		}else {
			timer--;
		}
		if (Game.masterMap.levelName() != level) {
			handler.removeObject(this);
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(new Color(106, 90, 205));
		drawCircle(g, x, y, size);
		g.setColor(new Color(96, 80, 195));
		drawCircle(g, x, y, size * 4 / 5);
		g.setColor(new Color(238, 232, 170));
		drawCircle(g, x, y, size * 2 / 5);
	}
	
	private void drawCircle(Graphics cg, int xCenter, int yCenter, int r) {
		cg.fillOval(xCenter-r, yCenter-r, 2*r, 2*r);
	}//end drawCircle

	@Override
	public Rectangle getBounds() {
		// gotta do something about this
		return null;
	}

}
