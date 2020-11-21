import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bee extends Enemy{

	
	//x and y is upper left corner of bee
	private static final int bwidth = 10, bheight = 10, attackRadi = 20;
	private int xv, yv, trackx, tracky, speed;
	private boolean faceRight;
	private MODE status;
	public Playerv2 target;
	private double targetDistance;
	private final int maxDistance = 300;
	private BufferedImage sprite, rsprite;
	
	public Bee(int x, int y, Handler handler) {
		super(x, y, bwidth, bheight, handler);
		faceRight = true;
		trackx = x;
		tracky = y;
		xv = 0;
		yv = 0;
		status = MODE.none;
		target = null;
		speed = 5;
		imageSetup();
		health = 20;
	}
	
	private void imageSetup() {
		try {
		    sprite = ImageIO.read(new File("tinyonlinebee.png"));
		    rsprite = Game.flippedImage("tinyonlinebee.png");
		} catch (IOException e) {
			System.out.println("Bee sprite setup failed!");
		}
		
	}

	@Override
	public void tick() {
		if (handler.getBoss() == null) {
			die(null);
		}
		if (target != null) {
			targetDistance = playerNear(target);
		}
		movement2();
		collisionTile();
		orientation();
	}
	
	private void orientation() {
		if (status == MODE.defense) {
			Queen tempq = handler.getBoss();
			if (tempq != null) {
				faceRight = tempq.facingRight();
			}
		}else {
			faceRight = trackx > x;
		}
	}
	
	private void movement() {
		int snapDistance = 2;
		
		
		/*
		if (status == MODE.offense) {
			int[] temp = target.getMiddle();
			trackx = temp[0];
			tracky = temp[1] - height * 2;
		}else if (status == MODE.defense) {
			int[] temp = handler.getBoss().needDefense(this);
			trackx = temp[0];
			tracky = temp[1];
		}else {
			trackx = x;
			tracky = y;
		}
		*/
		
		if (x > trackx + snapDistance * 4) {
			xv = -speed;
		}else if (x < trackx - snapDistance * 4) {
			xv = speed;
		}else {
			xv = trackx - x;
		}
		
		if (y > tracky + snapDistance * 4) {
			yv = -speed;
		}else if (y < tracky - snapDistance * 4) {
			yv = speed;
		}else {
			yv = tracky - y;
		}
		
	}
	
	private void movement2() {
		double xdistance = trackx - x;
		double ydistance = tracky - y;
		double maxdistance = Math.sqrt(xdistance * xdistance + ydistance * ydistance);
		xv = (int)(xdistance / maxdistance * speed);
		yv = (int)(ydistance / maxdistance * speed);
	}
	
	private void collisionTile() {
		int tempX = 1;
		int tempY = 1;
		
		for (int i = 0; i < handler.getMasterList().size(); i++) {
			GameObject tempObject = handler.getMasterList().get(i);
			if(tempObject.getID() == ID.Tile || tempObject.getID() == ID.MovingTile) {
				Rectangle playerRectX = getOffsetBoundsX(); //maybe move these
				Rectangle playerRectY = getOffsetBoundsY();
				Rectangle otherRect =  new Rectangle(tempObject.getX(), tempObject.getY(),
						tempObject.getWidth(), tempObject.getHeight());
				if (playerRectY.intersects(otherRect)) {
					tempY = 0;
					int yTarget = tempObject.getY();
					if (y < yTarget - 1) {
						y = yTarget - height - 1;
					}else {
						y = yTarget + tempObject.getHeight();
					}
					if (Math.abs(xv) < 1) {
						if (target.getX() < x) {
							xv = - 1;
						}else {
							xv = 1;
						}
					}
				}
				
				if (playerRectX.intersects(otherRect)) {
					tempX = 0;
					int xTarget = tempObject.getX();
					if (x < xTarget - 1) {
						x = xTarget - width - 1;
					}else {
						x = xTarget + tempObject.getWidth();
					}
					if (Math.abs(yv) < 1) {
						if (target.getY() < y) {
							yv = -1;
						}else {
							yv = 1;
						}
					}
				}
				
			}else if (tempObject.getID() == ID.Player) {
				if (target == null || playerNear((Playerv2)(tempObject)) < maxDistance) { 
					target = (Playerv2)tempObject;
					targetDistance = playerNear(target);
				}
			}
		}
		
		x += xv * tempX;
		y += yv * tempY;
	}
	
	private Rectangle getOffsetBoundsY() {
		return new Rectangle (x, y + yv, width, height);
	}

	private Rectangle getOffsetBoundsX() {
		return new Rectangle (x + xv, y, width, height);
	}

	private double playerNear(Playerv2 tar) {
		double distance = Math.pow(x + width - tar.getX() + tar.getWidth() / 2, 2) + 
						  Math.pow(y + height - tar.getY() + tar.getHeight() / 2, 2);
		return Math.sqrt(distance);
	}

	@Override
	public void render(Graphics g) {
		if (Game.debugMode) {
			if (status == MODE.offense) {
				g.setColor(Color.red);
			}else {
				g.setColor(Color.yellow);
			}
			g.drawRect(x, y, width, height);
			if (faceRight) {
				g.drawRect(x + width, y + height / 4, width, height / 2);
			}else {
				g.drawRect(x - width, y + height / 4, width, height / 2);
			}
		}else {
			if (!faceRight) {
				g.drawImage(sprite, x, y, null);
			}else {
				g.drawImage(rsprite, x, y, null);
			}
		}
	}

	@Override
	public void damagedBy(HitBox hit, Playerv2 dude) {
		die(dude);
	}
	
	private enum MODE {
		offense, //for when player is close
		defense,
		none;
	}

	
	public void die(Playerv2 dude) {
		
		Queen temp = handler.getBoss();
		if (temp != null) {
			temp.beeLoss(this);
		}
		handler.addObject(new Particle(x,y,10,handler));
		handler.removeObject(this);
	}
	
	public void reward() {
		this.die(target);
	}
	
	public void killedClone(Playerv2 clone) {
		if (handler.getBoss() != null) {
			handler.getBoss().reportKill(clone);
		}
	}
	
	public void QueenUpdate(int x, int y, boolean isDefensive) {
		//System.out.println("Queen sent to " + x + ", " + y);
		if (isDefensive) {
			status = MODE.defense;
			this.trackx = x;
			this.tracky = y;
			this.speed = (int)(Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2))) / 10;
		}else {
			status = MODE.offense;
			speed = 3;
		}
	}
	
	public void QueenUpdate(Queen q) {
		if (target != null && targetDistance < maxDistance) {
			int[] temp = target.getMiddle();
			trackx = temp[0];
			tracky = temp[1] - height * 2;
			QueenUpdate(target.getX(), target.getY(), false);
		}else {
			trackx = q.getX() + Game.rand.nextInt(600) - 300;
			tracky = q.getY() + Game.rand.nextInt(600) - 300;
			status = MODE.defense;
			speed = 6;
		}
	}
	
	public ID getID() {
		return ID.Bee;
	}

}
