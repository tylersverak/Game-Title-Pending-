import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

public class MovingTile2 extends Tile{

	private Playerv2 p;
	private int movementStyle, xv, yv, intialx, intialy, lastTime;
	//use this to pick the way the tile moves
	//maybe it can or cant go through other tiles?
	//think about it crunching the player or other stuff
	//maybe iteract with being damaged?
	
	public MovingTile2(int movementStyle, int x, int y, int width, int height, Handler handler, Color map[][]) {
		super(x, y, width, height, handler, map);
		this.movementStyle = movementStyle;
		xv = 0;
		yv = 0;
		intialx = x;
		intialy = y;
		p = null;
		lastTime = Game.gameTime;
	}
	
	@ Override
	public void tick() {
		if (activated) {
			move(Game.gameTime);
		}
		if (p != null && p.getStatus() != STATUS.clone) { //had something about itimr == 0 here
			p.setY(p.getY() + this.yv);
			p.setX(p.getX() + this.xv);
		}
		p = null;
	}
	
	private void move(int time) {
		int[] cords = updateVelocity(movementStyle, time);
		xv = cords[0];
		yv = cords[1];
		x = x + xv;
		y = y + yv;
	}
	
	private int[] updateVelocity(int choice, int time) {
		int[] cords = new int[2];
		switch(choice) {
		case 1:
			if (time % 800 < 400) {
				cords[1] = 2;
			}else {
				cords[1] = -2;
			}
			break;
		case 2:
			cords[0] = (int)(2 * Math.sin(Game.gameTime / 50.0));
			cords[1] = (int)(2 * Math.sin(Game.gameTime / 50.0));
			break;
		default:
			
		}
		return cords;
	}
	
	public void getNextXV() {
		
	}
	
	public void getNextYV() {
		
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x + xv, y + yv, getWidth(), getHeight()); //this used to have nxv and nyv
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
	
	public void resetPos() {
		while (lastTime < Game.gameTime) {
			move(lastTime);
			lastTime++;
		}
		//one of my biggest failures... jk
	}
	
	public void deactivate() {
		activated = false;
		xv = 0;
		yv = 0;
	}
	
	public void touched(Playerv2 p) {
		this.p = p;
	}
	
	public void collision(Playerv2 p) {
		int tempTX = this.getMiddle()[0];
		int tempTY = this.getMiddle()[1];
		int tempPX = p.getMiddle()[0];
		int tempPY = p.getMiddle()[1];
		double angle = 0;
		if (tempPX - tempTX != 0) {
			angle = Math.atan2(((double)(tempPY-tempTY)), ((double)(tempPX-tempTX)));
		}
		System.out.println((tempPY-tempTY) + " " + (tempPX-tempTX));
		System.out.println("a" + (angle / Math.PI));
	}
	
	public void collision2(Playerv2 p) {
		int tempTX = this.getMiddle()[0];
		int tempTY = this.getMiddle()[1];
		int tempPX = p.getMiddle()[0];
		int tempPY = p.getMiddle()[1];
		if (tempTX < tempPX) {
			//player is to the right
			if (tempTY < tempPY) {
				//player is above
				if (p.getX() > x + width) {
					p.setX(x + width);
				}
				if(p.getY() + p.getHeight() > y) {
					p.setY(y - p.getHeight());
				}
			}else {
				if (p.getX() > x + width) {
					p.setX(x + width);
				}
				if (p.getY() > y + height) {
					p.setY(y);
				}
			}
		}else if(tempTY >= tempPY) {
			//player is to the left
			if (tempTY < tempPY) {
				//player is on top
				if (p.getX() + p.getWidth() < x) {
					p.setX(x - p.getWidth());
				}
				if(p.getY() + p.getHeight() > y) {
					p.setY(y - p.getHeight());
				}
			}else {
				if (p.getX() + p.getWidth() < x) {
					p.setX(x - p.getWidth());
				}
				if (p.getY() > y + height) {
					p.setY(y);
				}
			}
		}
		touched(p);
	}
}
