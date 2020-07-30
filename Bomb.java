import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

//we decided bombs are circles

public class Bomb extends GameObject implements Comparable<GameObject>{
	
	private Handler handler;

	public Bomb(int x, int y, ID id, int width, Handler handler) {
		super(x, y, id, width, width);
		this.handler = handler;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		if (collideTile()) {
			explode();
		}
		y += 3;
	}
	
	public void explode() {
		handler.addObject(new Particle(x + Game.rand.nextInt(20), y + Game.rand.nextInt(20) - 20, 20, handler)); //might need to remove for lag
		handler.removeObject(this);
	}
	
	private boolean collideTile() {
		LinkedList<GameObject> temp = handler.getMasterList();
		for (GameObject item : temp) {
			if (item.getID() == ID.Tile || item.getID() == ID.MovingTile) {
				if (this.getBounds().intersects(((Tile)item).getBounds())) {
					System.out.println(item);
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean collides(Bomb b1, Rectangle r1) {
		if (b1.getX() - b1.getWidth() <= r1.x + r1.width && b1.getX() + b1.getWidth() >= r1.x) {
			if (b1.getY() - b1.getWidth() <= r1.y + r1.width && b1.getWidth() + b1.getY() >= r1.y) {
				return true;
			}
		}
	    double closestX = Game.clamp(b1.getX(), (r1.x), r1.x + r1.width);
	    double closestY = Game.clamp(b1.getY(), r1.y , r1.y + r1.height);
	 
	    double distanceX = b1.getX() - closestX;
	    double distanceY = b1.getY() - closestY;
	    
	    return (Math.pow(distanceX, 2) + Math.pow(distanceY, 2) < Math.pow(b1.getWidth(), 2));
	}

	@Override
	public void render(Graphics g) {
		// x y is center of bomb
		g.setColor(Color.black);
		g.fillOval(x, y, width, width);
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return new Rectangle(x - width / 2, y - width / 2, width, width); //width is radius
	}
	
}
