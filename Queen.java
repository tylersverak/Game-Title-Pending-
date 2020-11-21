import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
//x, y is upper left of the middle piece
//upper left of whole thing is -20, -40 in relation to x, y

public class Queen extends Enemy{

	private Set<Bee> hive;
	private final static int hiveSize = 10, startx = 1600, starty = 600;
	//WILL NEED EDITING LATER
	//dimensions should go from top to bottom, with 4 ints to make a rect
	//remember first two are distance from x and y variables
	//might need new dimension for color, in which case a txt file might be more convenient
	private int[][] dimensions =  {{30, -20, 20, 20}, {0, 0, 50, 50}, {40, 10, 30, 15}, {-40, 45, 50, 20}};;
	private int health, xv, yv, movetimer, iTimer, reflectValue, xangle, yangle;
	private boolean faceRight;
	private Playerv2 target, main;
	private MOVEMENT phase;
	private Playerv2 deadClone;
	private MOVEMENT[] patterns = {MOVEMENT.swoop, MOVEMENT.circle, MOVEMENT.bomb,
			MOVEMENT.sting, MOVEMENT.missile, MOVEMENT.die};
	
	public Queen(Handler handler) {
		super(startx, starty, 0, 0, handler);
		hive = new HashSet<>();
		health = 1000;
		iTimer = 0;
		faceRight = false;
		this.target = null;
		phase = MOVEMENT.intro;
		movetimer = 0;
		reflectValue = dimensions[1][2];
	}

	@Override
	public void tick() {
		movement2();
		manageHive();
		if (iTimer > 0) {
			iTimer--;
		}
	}
	
	public void beeLoss(Bee b) {
		if (hive.contains(b)) {
			hive.remove(b);
		}
	}
	
	private void manageHive() {
		PriorityThing BeeList = new PriorityThing();
		//PriorityThing TargetList = new PriorityThing();

		LinkedList<GameObject> master = handler.getMasterList();
		
		for (GameObject item: master) {
			if (item.getID() == ID.Bee) {
				BeeList.add(item, getDistance(item));
			}else if(item.getID() == ID.Player) {
				//TargetList.add(item, getDistance(item));
			}
		}
			
		while (!BeeList.isEmpty()) {
			Bee tempB = (Bee) BeeList.chunk();
			if (deadClone != null) {
				if(tempB.target == deadClone) {
					tempB.target = null;
				}
			}
			if (hiveSize > hive.size()) {
				hive.add(tempB);
			}
			if (!hive.contains(tempB)) {
				tempB.QueenUpdate(this);
			}else {
				tempB.QueenUpdate(this.x -50 + Game.rand.nextInt(150), this.y - 50 + Game.rand.nextInt(150), true); //50 cause width/height of main body
			}
			
		}
	}
	
	private double getDistance(GameObject other) {
		double XTemp = Math.pow(other.getX() + other.getWidth() / 2 + x + width / 2, 2);
		double YTemp = Math.pow(other.getY() + other.getHeight() / 2 + y + height / 2, 2);
		return Math.sqrt(XTemp + YTemp);
	}

	public void movement() {
		//change this
		xv = (int) (5 *Math.sin(Game.gameTime * 360));
		yv = (int) (5 *Math.cos(Game.gameTime * 10));
		x += xv;
		y += yv;
	}
	
	private void movement2() {
		if (phase == null || phase == MOVEMENT.idle) {
			phase = patterns[Game.rand.nextInt(patterns.length - 2) + 1];
		}else {
			if (phase == MOVEMENT.bomb) {
				int bombtimer = 700;
				if (movetimer <= bombtimer) {
					if (movetimer == 1) {
						xv = -3;
					}else if (movetimer == bombtimer / 2) {
						xv = 3;
						faceRight = true;
					}
					if (movetimer % 20 == 0) {
						handler.addObject(new Bomb(x, y + 65, ID.Bomb, 15, handler));
					}
				}else {
					reset();
				}
			}else if(phase == MOVEMENT.swoop) {
				swoop();
			}else if(phase == MOVEMENT.intro) {
				if (movetimer < 100) {
					y--;
					if (movetimer % 10 == 0) {
						handler.addObject(new Bee(x, y, handler));
					}
				}else {
					reset();
				}
			}else if(phase == MOVEMENT.circle) {
				if (movetimer == 200) {
					faceRight = true;
				}
				if (movetimer < 400) {
					xv = (int)(-10 * Math.sin(movetimer * 2 * Math.PI / 400));
					yv = (int)(3 * Math.cos(-movetimer * 2 * Math.PI / 400));
				}else {
					faceRight = false;
					reset();
				}	
			}else if(phase == MOVEMENT.sting) {
				//no way to get player as target so should probably change something
				//also don't know if it works
				main = handler.getPlayer();
				if (main == null) {
					if (movetimer >= 100) {
						reset();
					}
				}else {
					if (movetimer == 100) {
						/*
						 //possible other targeting system using "math"
						double angle = Math.tan((main.getY() - y) * 1.0 / (double)(main.getX() - x));
						System.out.println(angle);
						double hypo = Math.sqrt(Math.pow(main.getY() - y, 2) + Math.pow(main.getX() - x, 2));
						xv = (int)(-15 * Math.acos(angle));
						yv = (int)(-15 * Math.asin(angle));
						*/
						xangle = (int)((main.getX() - x) / (double)(70));
						yangle = (int)((main.getY() - y) / (double)(70));
						xv = xangle;
						yv = yangle;
						xv = Game.clamp(xv, -15, 1);
						yv = Game.clamp(yv, -2, 2);
					}else if (movetimer == 170){
						xv = 0;
						yv = 0;
					}else if(movetimer == 200){
						xv = -xangle;
						yv=  -yangle;
						xv = Game.clamp(xv, -1, 15);
						yv = Game.clamp(yv, -2, 2);
					}else if(movetimer == 270) {
						xv = 0;
						yv = 0;
					}else if(movetimer >= 280) {
						reset();
					}
				}
			}else if(phase == MOVEMENT.missile) {
				if (movetimer == 1) {
					xv = -3;
				}else if(movetimer == 110) {
					yv = 1;
				}else if(movetimer == 210) {
					xv = 0;
					yv = 0;
				}else if(movetimer == 300) {
					if (hive.size() < 30) { //didn't test
						handler.addObject(new Bee(x, y, handler));
						handler.addObject(new Bee(x, y, handler));
					}
					yv = -1;
					xv = 3;
				}else if(movetimer == 410) {
					yv = 0;
				}else if(movetimer == 520) {
					reset();
				}
			}
		}
		x += xv;
		y += yv;
		//System.out.println("queen speed x: " + xv + " y: " + yv + " - " + movetimer);
		movetimer++;
	}
	
	private void swoop() {
		int time = 600;
		if(movetimer > time) {
			reset();
		}else if(movetimer > time * 3 / 4) {
			xv = 6;
		}else if(movetimer > time / 2) {
			xv = 0;
			yv = 0;
		}else if(movetimer > time / 4) {
			yv = -1;
		}else {
			xv = -3;
			yv = 1;
		}
	}
	
	private void reset() {
		faceRight = false;
		movetimer = -1;
		x = 1600;
		y = 500;
		xv = 0;
		yv = 0;
		phase = MOVEMENT.idle;
	}
	
	@Override
	public void render(Graphics g) {
		if (Game.debugMode) {
			int color = (int)(health / 1000.0 * 255);
			g.setColor(new Color(color, color, color));
			if (phase == MOVEMENT.idle) {
				//g.setColor(new Color(90, 90, 90)); //use to detect idleness, flashes weird when left on
			}
			for (int i = 0; i  < dimensions.length; i++) {
				for (int j = 0; j < dimensions[i].length; j++) {
					if (faceRight) {
						g.fillRect(x - dimensions[i][0] - dimensions[i][2] + reflectValue, y + dimensions[i][1], dimensions[i][2], dimensions[i][3]);
					}else {
						g.fillRect(x + dimensions[i][0], y + dimensions[i][1], dimensions[i][2], dimensions[i][3]);
					}
				}
			}
			g.drawString("Health: " + health, x, y - 30);
		}else {
			BufferedImage img = null;
			BufferedImage imgr = null;
			try {
			    img = ImageIO.read(new File("tybee3.png"));
			    imgr = ImageIO.read(new File("tybee4.png"));
			} catch (IOException e) {
				g.fillRect(x, y, 30, 30);
			}
			if (!faceRight) {
				g.drawImage(img, x - 80, y - 110, null);
			}else {
				g.drawImage(imgr, x - 80, y - 110, null);
			}
		}
	}
	
	public void reportKill(Playerv2 clone) {
		deadClone = clone;
	}

	@Override
	public void damagedBy(HitBox hit, Playerv2 dude) {
		target = dude;
		if (iTimer == 0) {
			health -= hit.getDamage();
			if (health <= 0) {
				die(dude);
			}
			iTimer = 20; //frames of invin
		}
	}

	@Override
	public void reward() {
		handler.addObject(new Bee(x, y, handler));
	}

	@Override
	public void die(Playerv2 dude) {
		for (int i = 0; i < 4; i++) {
			handler.addObject(new Particle(x + dimensions[i][2]/2, y + dimensions[i][2]/2, 50, handler));
		}
		health = 0;
		handler.killBoss();
	}
	
	/*
	public int[] needDefense(Bee grunt) {
		for (int i = 0; i < hive.length; i++) {
			if (hive[i] == null) {
				int[] temp = {x + (i % 2) * 50, y + (i / 2) * 10};
				hive[i] = grunt;
				return temp;
			}
		}
		return null;
	}*/
	
	public Rectangle[] getHurtboxes() {
		Rectangle[] toreturn = new Rectangle[dimensions.length];
		for (int i = 0; i < dimensions.length; i++) {
			int shift = 0;
			if (faceRight) {
				shift = reflectValue; //should alredy be updated somewhere, need to make this more global
			}
			toreturn[i] = new Rectangle(dimensions[i][0] + x + shift, dimensions[i][1] + y,
										dimensions[i][2], dimensions[i][3]);
		}
		return toreturn;
	}
	
	public boolean facingRight() {
		return faceRight;
	}
	
	public void killedClone(Playerv2 clone) {
		deadClone = clone;
	}
	
	private class PriorityThing {
		
		private PriorityNode start;
		
		private PriorityThing() {
			start = null;
		}
		
		public void add(GameObject thing, double distance) {
			PriorityNode addition = new PriorityNode(thing, distance);
			if (start == null) {
				start = addition;
			}else {
				PriorityNode temp = start;
				if (temp.distance > distance) {
					addition.next = temp;
					start = addition;
				}else {
					while (temp.next != null) {
						if (temp.next.distance > distance) {
							addition.next = temp.next;
							temp.next = addition;
							return;
						}
						temp = temp.next;
					}
					temp.next = addition;
				}
			}
		}
		
		public GameObject chunk() {
			if (start == null) {
				return null;
			}else {
				GameObject toReturn = start.target;
				start = start.next;
				return toReturn;
			}
		}
		
		public boolean isEmpty() {
			return start == null;
		}
		
		public void testPrint() {
			PriorityNode temp = start;
			while (temp != null) {
				//fix if you use it again
				System.out.println("testing " + temp.distance + temp.target.toString());
				temp = temp.next;
			}
		}
		
		private class PriorityNode {
			
			public PriorityNode next;
			public GameObject target;
			public double distance;
			
			private PriorityNode(GameObject target, double distance) {
				this.distance = distance;
				this.target = target;
				next = null;
			}
		}
	}
	
	private enum MOVEMENT{
		intro,
		swoop,
		circle,
		bomb,
		sting,
		missile,
		idle,
		die;
	}
}


