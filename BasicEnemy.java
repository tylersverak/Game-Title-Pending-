import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class BasicEnemy extends Enemy implements Comparable<GameObject>{
	
	private int timer, xv, yv;
	private final int maxTimer = 80;
	
	public BasicEnemy(int x, int y, int width, int height, Handler handler) {
		super(x, y, width, height, handler);
		this.handler = handler;
		health = 40;
		timer = Game.rand.nextInt(maxTimer);
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	
	public void tick() {
		calcVelo();
		collisionTile();
		x = Game.clamp(x, 0, Game.WIDTH);
		y = Game.clamp(y, 0, Game.HEIGHT);
	}
	
	
	private void calcVelo() {
		int distance = 4;
		if(timer <= 0) {
			timer = Game.rand.nextInt(maxTimer);
			xv = Game.rand.nextInt(distance + 1) * 2 - distance;
			yv = Game.rand.nextInt(distance + 1) * 2 - distance;
		}else {
			timer--;
		}
	}
	
	public void render(Graphics g) {
		if (Game.debugMode) {
			if (canHit) {
				int tempHealth = Game.clamp(health, 0, 255);
				g.setColor(new Color(tempHealth, tempHealth, tempHealth));
			}else {
				g.setColor(Color.yellow);
			}
			g.fillRect(x,y,width,height);
		} else {
			for (int i = 0; i < 5; i++) {
				g.setColor(new Color(i * 50, i * 50, 255 - 50 * i));
				g.fillRect(x + i * width / 5, y, width/5, height);
			}
		}
		g.setColor(Color.black);
		g.drawString(health + " " + x + "," + y, x, y);
	}
	
	public void damagedBy(HitBox hit, Playerv2 dude) {
		//can think about doing something with effect too\
		//need to find way to stop attack from hitting every frame
		if (canHit) {
			health -= hit.getDamage();
			canHit = false;
		}
		if (health <= 0) {
			die(dude);
		}
	}
	
	public void neutral() {
		canHit = true;
	}
	
	public boolean hittable() {
		return canHit;
	}
	
	//might think about making this private
	public void die(Playerv2 dude) {
		int count = Game.rand.nextInt(4) + 1;
		for (int i = 0; i < count; i++) {
			handler.addObject(new Chakra(x + width / 2,
					y + height / 2, ID.Chakra, 10 + Game.rand.nextInt(5),
					handler, dude));
		}
		handler.removeObject(this);
	}
	
	public void reward() {
		// no reward for now
	}
	
	public void killedClone(Playerv2 clone) {
		handler.addObject(new BasicEnemy(x,y,width,height,handler));
	}
	
	private Rectangle getOffsetBoundsY() {
		return new Rectangle (x, y + yv, width, height);
	}

	private Rectangle getOffsetBoundsX() {
		return new Rectangle (x + xv, y, width, height);
	}
	
	private void collisionTile() {
		int tempX = 1;
		int tempY = 1;
		
		for (int i = 0; i < handler.getMasterList().size(); i++) {
			GameObject tempObject = handler.getMasterList().get(i);
			if(tempObject.getID() == ID.Tile || tempObject.getID() == ID.MovingTile) {
				Rectangle basicenemyRectX = getOffsetBoundsX();
				Rectangle basicenemyRectY = getOffsetBoundsY();
				Rectangle otherRect =  new Rectangle(tempObject.getX(), tempObject.getY(),
						tempObject.getWidth(), tempObject.getHeight());
				if (basicenemyRectY.intersects(otherRect)) {
					tempY = 0;
					int yTarget = tempObject.getY();
					if (y < yTarget - 1) {
						y = yTarget - height - 1;
					}else {
						y = yTarget + tempObject.getHeight();
					}
				}
				if (basicenemyRectX.intersects(otherRect)) {
					tempX = 0;
					int xTarget = tempObject.getX();
					if (x < xTarget - 1) {
						x = xTarget - width - 1;
					}else {
						x = xTarget + tempObject.getWidth();
					}
				}
			}
		}
		
		if (Game.gameTime % 2 == 0) {
			x += xv * tempX;
			y += yv * tempY;
		}
	}
	
}
