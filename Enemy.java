import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public abstract class Enemy extends GameObject implements Comparable<GameObject>{
	
	protected int move, nextX, nextY, health;
	protected Handler handler; //MKE THIS A FIELD OF GAME OBJECT
	protected boolean canHit = true;
	
	public Enemy(int x, int y, int width, int height, Handler handler) {
		super(x, y, ID.Enemy, width, height);
		nextX = Game.rand.nextInt(40) + x -20;
		nextY = Game.rand.nextInt(40) + y - 20;
		this.handler = handler;
		move = 1;
		health = 40;
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
	public abstract void damagedBy(HitBox hit, Playerv2 dude);
	public abstract void reward();
	public abstract void killedClone(Playerv2 clone);
	//might think about making this private
	public abstract void die(Playerv2 dude);
	
	public Rectangle getBounds() {
		return new Rectangle (x, y, width, height);
	}
	
	public void neutral() {
		canHit = true;
	}
	
	public boolean hittable() {
		return canHit;
	}
	
}
