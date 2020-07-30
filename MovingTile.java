import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

public class MovingTile extends Tile{

	private int timer, stall;
	protected int xv, yv, movementStyle;
	protected Playerv2 target;
	//use this to pick the way the tile moves
	
	public MovingTile(int movementStyle, int x, int y, int width, int height, Handler handler, ArrayList<ArrayList<Color>> map) {
		super(x, y, width, height, handler, map);
		this.movementStyle = movementStyle;
		xv = 0;
		yv = 0;
		timer = 0;
		stall = 0;
		updatePos(movementStyle, timer);
		target = null;
	}
	
	@ Override
	public void tick() {
		updatePos(movementStyle, timer);
		timer = Game.gameTime;
		movePlayer();
	}
	
	protected void movePlayer() {
		
		if (target != null) {
			target.setX(target.getX() + xv);
			target.setY(target.getY() + yv);
		}
		target = null;
	}
	
	private void updatePos(int choice, int time) {
		if (activated) {
			timer = timer - stall;
			while (timer <= Game.gameTime - stall) {
				int[] move = updateVelocity(choice, timer);
				xv = move[0];
				yv = move[1];
				x += xv;
				y += yv;
				timer++;
			}
			stall = 0;
		}else {
			stall++;
		}
	}
	
	private int[] updateVelocity(int choice, int time) {
		time -= stall;
		int[] cords = new int[2];
		switch(choice) {
		case 1:
			
			break;
		case 2:
			break;
		default:
			
		}
		return cords;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, getWidth(), getHeight()); //this used to have nxv and nyv
	}
	
	public Rectangle getNextBounds() {
		int speeds[] = updateVelocity(movementStyle, Game.gameTime + 1);
		return new Rectangle(x + speeds[0], y + speeds[1], getWidth(), getHeight()); //this used to have nxv and nyv
	}
	
	public int[] getNext() {
		
		int speeds[] = {0, 0};
		if (activated) {
			speeds = updateVelocity(movementStyle, Game.gameTime + 1);
		}
		return speeds;
	}
	
	public int getXV() {
		return xv;
	}
	
	public int getYV() {
		return yv;
	}
	
	public void setXV(int xv) {
		this.xv = xv;
	}
	
	public void setYV(int yv) {
		this.yv = yv;
	}
	
	//be aware that the handler might not call everything the right way because of this ID
	public ID getID() {
		return ID.MovingTile;
	}
	
	public void deactivate() {
		activated = false;
		xv = 0;
		yv = 0;
	}
	
	public void activate() {
		activated = true;
	}
	
	public void touched(Playerv2 t) {
		System.out.println(t);
		this.target = t;
	}

}
