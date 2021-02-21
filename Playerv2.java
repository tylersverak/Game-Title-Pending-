import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

public class Playerv2 extends GameObject implements Comparable<GameObject>{
	
	//clean this up cause protected
	protected int iTimer, jumpTimer, lastTimePressed, lastTimeUp, frameTimer, xv, yv, pyv;
	private char lastKeyDown;
	private final static int speed = 7, gravity = 7, timer = 27, keyBuffer = 10, defaultJumpHeight = 25; //timer is actually jump height, defaultjumpheight is for walljumps
	protected Handler handler;
	protected STATUS pStatus;
	protected STATUS lastStatus;
	protected static Map<STATUS, Attack> attackData;
	private Clone buddy;
	protected boolean groundMoving = false, actionable = true, faceRight = true;
	protected final int cloneValue = 10; //how much juice it takes to make a clone
	protected MovingTile anchor;
	
	public Playerv2 (int x, int y, Handler handler) throws FileNotFoundException{
		//last two are width then height but im gonna edit it
		//not certain but assuming x,y is upper left corner
		super(x, y, ID.Player, 15, 30);
		this.handler = handler;
		xv = 0;
		yv = 2;
		pStatus = STATUS.air;
		attackData = setupAttacks2();
		anchor = null;
	}
	
	public void tick() {
		collisionTile3();
		updateStati();
		updateTimers();
		updateGrav();
		checkEdges();
		collisionEnemy();
		updateStati();
	}
	
	private void updateTimers() {
		if (frameTimer > 0) {
			frameTimer--;
		}else if (pStatus != STATUS.air){
			frameTimer = attackData.get(pStatus).getTimer() - 1;
		}
		if (iTimer > 0) {
			iTimer--;
		}else {
			setActionable(!isAttacking());
		}
	}
	
	public void setStatus(STATUS s) {
		if (this instanceof Clone && s == STATUS.clone){
			return;
		}
		if (pStatus != s || pStatus == STATUS.none) {
			if (s != STATUS.none) {
				frameTimer = attackData.get(pStatus).getTimer();
			}
			pStatus = s;
		}
	}
	
	protected void setStatusOverride(STATUS s){
		if (pStatus != STATUS.clone){
			pStatus = s;
		}
	}
	
	private void updateGrav() {
		if (jumpTimer > 0) {
				yv = (jumpTimer * -jumpTimer + 40)/40;
			jumpTimer--;
		}else{
			int fallSpeed = 10;
			if (pStatus == STATUS.wallLeft || pStatus == STATUS.wallRight) {
				fallSpeed = fallSpeed / 4;
			}
			if (yv < fallSpeed) { //this is max fall speed
				yv++;
			}
		}
	}
	
	private void updateStati() {
		if (lastStatus != pStatus) {
			frameTimer = attackData.get(pStatus).getTimer();
		}
		lastStatus = pStatus;
	}
	
	//collision methods**********************************************************************************************************************************************************************
	private void checkEdges() {
		//edges
		if (!(this instanceof Clone)) {
			if (x > Game.WIDTH - width) {
				if (!Game.masterMap.hasRight()) {
					x = Game.WIDTH - width;
				}else if(x > Game.WIDTH + width * 2){
					x = -width;
					if(Game.masterMap.goRight(this)) {
						System.out.println("sucessR" + xv + " " + x);
						handler.recallClones();
					}else {
						System.out.println("fucking crap damn it");
					}
				}
			}else if (x < 0) {
				if (!Game.masterMap.hasLeft()) {
					x = 0;
				}else if (x < (-width * 2)){
					x = Game.WIDTH + width;
					//need to prune this later, dont need the return
					if(Game.masterMap.goLeft(this)) {
						System.out.println("sucessL" + xv + " " + x);
						handler.recallClones();
					}else {
						System.out.println("fucking crap damn it");
					}
				}
			}
			if (y > Game.HEIGHT - height) {
				if (!Game.masterMap.hasDown()) {
					y = Game.HEIGHT - height;
				}else if(y > Game.HEIGHT + height * 2){
					y = -height;
					if(Game.masterMap.goDown(this)) {
						System.out.println("sucessD");
						handler.recallClones();
					}else {
						System.out.println("fucking crap damn it");
					}
				}
			}else if (y < 0) {
				if (!Game.masterMap.hasUp()) {
					//y = 0; //makes the ceiling stop you dead
				}else if (y < (-height * 2)){
					y = Game.HEIGHT + height;
					if(Game.masterMap.goUp(this)) {
						System.out.println("sucessU");
						handler.recallClones();
					}else {
						System.out.println("fucking crap damn it");
					}
				}
			}
		}else {
			if (x > Game.WIDTH - width || x < 0) {
				handler.removeObject(this);
				handler.getPlayer().setBuddy(null);
			}
			if (y > Game.HEIGHT - height || y < -20) {
				handler.removeObject(this);
				handler.getPlayer().setBuddy(null);
			}
		}
	}
	
	//need to eventually combine with other collision and see if it works
	private void collisionEnemy() {
		Attack currAttack = attackData.get(pStatus);
		Rectangle[] collisions = currAttack.getHitBoxes(frameTimer, this);
		HitBox[] collection = new HitBox[0];
		if (isAttacking()) {
			collection = currAttack.getCurrHit(frameTimer);
		}
		boolean hitYet = false;
		if (handler.bossMode()) { //all for the queen
				Rectangle[]  body = (handler.getBoss()).getHurtboxes();
				for (int k = 0; k < body.length; k++) {
					for (int j = 0; j < collisions.length; j++) {
						if (collisions[j].intersects(body[k])){
							if (handler.getBoss() != null) {
								(handler.getBoss()).damagedBy(collection[j], this);
							}
							break;
						}
					}
					if (getBounds().intersects(body[k]) && iTimer == 0 && handler.bossMode()) {
						if ((handler.getBoss()).hittable()) {
							if ((this instanceof Clone)) {
								((Clone)this).die();
							}else {
								//reward enemy for hitting player?
								(handler.getBoss()).reward();
								HUD.HEALTH -= 20;
							}
							//  v amount of hitstun. map with different times?
							iTimer = 20;
							setActionable(false);
							//frameTimer = 0;
							int knockback = 6;
							Queen qtemp = handler.getBoss();
							if (x + width / 2 < qtemp.getX() + qtemp.getWidth() / 2) {
								xv = -knockback;
							}else {
								xv = knockback;
							}
							if (y + height / 2 < qtemp.getY() + qtemp.getHeight() / 2) {
								yv = knockback + 1;
							}else {
								yv= -knockback + 2;
							}
							setStatus(STATUS.air);
						}
					}
				}
				
			
			
		}
		// need to fix error where enemy freezes if the end of the move is lagless
		for (int i = 0; i < handler.getMasterList().size(); i++) {
			GameObject tempObject = handler.getMasterList().get(i);
			if (tempObject.getID() == ID.Enemy || tempObject.getID() == ID.Bee || tempObject.getID() == ID.Queen) {
				if (isAttacking()) {
					for (int j = 0; j < collisions.length; j++) {
						if (collisions[j].intersects(tempObject.getBounds())){
							((Enemy) tempObject).damagedBy(collection[j], this);
							hitYet = true;
							break;
						}
					}
					if (!hitYet) {
							((Enemy) tempObject).neutral();
					}
				}
				if (getBounds().intersects(tempObject.getBounds()) && iTimer == 0) {
					if (((Enemy)tempObject).hittable()) {
						if ((this instanceof Clone)) {
							((Clone)this).die();
						}else {
							//reward enemy for hitting player?
							((Enemy)tempObject).reward();
							if (buddy == null){
								int knockback = 6;
								int kx = knockback;
								int ky = knockback + 1;
								isHit(tempObject, 20, kx, ky);
							}else{
								swapOneWay(this, buddy);
								break;
							}
						}
					}
				}
			}else if (tempObject.getID() == ID.Player && tempObject instanceof Clone  && !(this instanceof Clone) && tempObject.getBounds().intersects(this.getBounds())){
				if (lastStatus != STATUS.clone){
					((Clone)tempObject).die(this);
				}else if( ((Clone)tempObject).getJumpTimer() >= defaultJumpHeight - 2){
					((Clone)tempObject).setJumpTimer(defaultJumpHeight + 4);
				}
			}
		}
	}
	
	public void isHit(GameObject other, int damage, int knockbackX, int knockbackY) {
		HUD.HEALTH -= damage;
		if (knockbackX != 0 && knockbackY !=0) {
			int[] midOther = other.getMiddle();
			int[] midThis = this.getMiddle();
			xv = knockbackX * Game.clamp(midThis[0] - midOther[0], -1, 1);
			yv = knockbackY * Game.clamp(midThis[1] - midOther[1], -1, 1);
			iTimer = 20;
			setActionable(false);
		}
	}
	
	public void isHit(GameObject other, int damage, int knockback) {
		isHit(other, damage, knockback, knockback);
	}
	
	
	private void collisionTile3() {
		boolean onGround = false;
		boolean rightWall = false;
		boolean leftWall = false;
		
		if (anchor != null) {
			y = anchor.getY() - height;
		}
		anchor = null;
		
		LinkedList<GameObject> objectList = handler.getMasterList();
		int noclingspeed = 3; //velocity at which the player will not stick to a wall if moving that much away
		for (int i = 0; i < objectList.size(); i++) {
			GameObject tempObject = objectList.get(i);
			if ((tempObject.getID() == ID.Tile || tempObject.getID() == ID.MovingTile) && getOffset().intersects(tempObject.getBounds())) {
				((Tile)tempObject).touched(this);
				//side of block the player is on
				int left = tempObject.getX() - x;
				int right = x + width - (tempObject.getX() + tempObject.getWidth());
				int top = y + height - tempObject.getY();
				int bottom = y - (tempObject.getY() + tempObject.getHeight());
				int output = collisionCompare2(top, bottom, left, right);
				
				if(output == Math.abs(top)) {
					//GROUNDED
					y = tempObject.getY() - height;
					onGround = true;
					//key stuff
					if (lastKeyDown == 's' && Game.keyPressed) {
						xv = 0;
					}
					if(pStatus == STATUS.grounded && !Game.keyPressed && Game.gameTime % 5 == 0) {
						xv = xv * 3 / 4;
					}else if(pStatus == STATUS.clone && Game.gameTime % 5 == 0) {
						xv = xv * 3 / 4;
					}
					if (tempObject instanceof MovingTile) {
						anchor = (MovingTile)tempObject;
					}
					yv = 0;
				}else if(output == Math.abs(bottom)) {
					//BUMP
					y = tempObject.getY() + tempObject.getHeight();
					yv = 0;
					jumpTimer = 0;
				}else if (Math.abs(left) == output) {
					// WALL ON RIGHT SIDE
					if (xv > -noclingspeed){
						x = tempObject.getX() - width;
						rightWall = true;
						if (iTimer > 0){
							xv = -xv;
						}else{
							xv = 1;
						}
					}
				}else if(output == Math.abs(right)) {
					//WALL ON LEFT SIDE
					if (xv < noclingspeed){
						x = tempObject.getX() + tempObject.getWidth();
						leftWall = true;
						if (iTimer > 0){
							xv = -xv;
						}else{
							xv = -1;
						}
					}
				}
			}else if(tempObject.getID() == ID.Chakra) {
				if (collides(((Chakra)tempObject), getBounds())){
					isHit(tempObject, -tempObject.getWidth(), 0);
					handler.removeObject(tempObject);
				}
			}else if(tempObject.getID() == ID.Bomb) {
				if (collides(((Bomb)tempObject), getBounds())){
					if ((this instanceof Clone)) {
						((Clone)this).die();
					}else {
						isHit(tempObject, 10, 6);
					}
					((Bomb)tempObject).explode();
				}
			}else if(tempObject.getID() == ID.NoCloneZone) {
				if (this instanceof Clone) {
					if (getOffsetBoundsX().intersects(((NoCloneZone)tempObject).getBounds())) {
						((Clone)this).die();
					}else if(getOffsetBoundsY().intersects(((NoCloneZone)tempObject).getBounds())){
						((Clone)this).die();
					}
				}
			}
		}
		if (walled(pStatus) && !leftWall && !rightWall) {
			if (pyv > 0){
				xv = 0;
				if (pStatus == STATUS.wallLeft){
					x++;
				}else if(pStatus == STATUS.wallRight){
					x--;
				}
			}
		}
		if (!isAttacking() || frameTimer == 0 && pStatus != STATUS.clone  || pStatus == STATUS.airNeutral && (onGround || rightWall || leftWall)
				|| pStatus == STATUS.dashAttack && (!onGround || rightWall || leftWall)) {
			if (onGround) {
				setStatus(STATUS.grounded);
			}else if (rightWall) {
				setStatus(STATUS.wallRight);
			}else if (leftWall) {
				setStatus(STATUS.wallLeft);
			}else if(!onGround) {
				setStatus(STATUS.air);
			}
		}
		
		if (pStatus == STATUS.grounded) {
			if (lastKeyDown == 's' && Game.keyPressed) {
				xv = 0;
			}
			if (!Game.keyPressed && Game.gameTime % 5 == 0) {
				xv = xv * 3 / 4;
			}
		}
		if(pStatus == STATUS.clone && Game.gameTime % 5 == 0) {
			xv = xv * 3 / 4;
		}
		
		if (pStatus != STATUS.wallRight && pStatus != STATUS.wallLeft && (faceRight && xv < 0 || !faceRight && xv > 0)) {
			faceRight = !faceRight;
		}
		y += yv;
		//x += xv;
		if (leftWall || rightWall){
			faceRight = leftWall; //makes them look the right way
		}else{
			x += xv;
		}
		pyv = yv;
	}
	
	private int collisionCompare2(int number, int c1, int c2, int c3) {
		if (c1 < 0 && c2 < 0 && c3 < 0 && number < 0) {
			return Math.max(number, Math.max(c1, Math.max(c2, c3)));
		}
		c1 = Math.abs(c1);
		c2 = Math.abs(c2);
		c3 = Math.abs(c3);
		number = Math.abs(number);
		return Math.min(number, Math.min(c1, Math.min(c2, c3)));
	}
	
	//lowkey stole this and modified it but it's fine
	//modified it a lot actually this wasn't very effective
	private boolean collides(Chakra c1, Rectangle r1) {
		if (c1.getX() <= x + width && c1.getX() >= x) {
			if (c1.getY() <= y + height && c1.getY() >= y) {
				return true;
			}
		}
	    double closestX = Game.clamp(c1.getX(), (r1.x), r1.x + r1.width);
	    double closestY = Game.clamp(c1.getY(), r1.y , r1.y + r1.height);
	 
	    double distanceX = c1.getX() - closestX;
	    double distanceY = c1.getY() - closestY;
	    
	    return (Math.pow(distanceX, 2) + Math.pow(distanceY, 2) + 50 < Math.pow(c1.getWidth(), 2));
	}
	
	private boolean collides(Bomb b1, Rectangle r1) {
		if (b1.getX() <= x + width && b1.getX() >= x) {
			if (b1.getY() <= y + height && b1.getY() >= y) {
				return true;
			}
		}
	    double closestX = Game.clamp(b1.getX(), (r1.x), r1.x + r1.width);
	    double closestY = Game.clamp(b1.getY(), r1.y , r1.y + r1.height);
	 
	    double distanceX = b1.getX() - closestX;
	    double distanceY = b1.getY() - closestY;
	    
	    return (Math.pow(distanceX, 2) + Math.pow(distanceY, 2) + 50 < Math.pow(b1.getWidth(), 2));
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		if (Game.debugMode) {
			if (isAttacking()) {
				g.setColor(Color.BLACK);
				g.fillRect(x, y, getWidth(), getHeight());
				g.setColor(new Color(80, 80, 130));
				Attack currAttack = attackData.get(pStatus);
				Rectangle[] display = currAttack.getHitBoxes(frameTimer, this);
				g.setColor(new Color(90, 90, 180));
				for (int i = 0; i < display.length; i++) {
					g.fillRect((int)display[i].getX(), (int)display[i].getY(), 
							(int)display[i].getWidth(), (int)display[i].getHeight());
				}
				g.setColor(Color.red);
			}else{
				//this defintely needs to be a map or some shit
				if (iTimer > 0) {
					g.setColor(Color.yellow);
				}else if (pStatus == STATUS.air) {
					g.setColor(Color.blue);
				}else if(pStatus == STATUS.wallLeft) {
					g.setColor(Color.green);
				}else if(pStatus == STATUS.wallRight) {
					g.setColor(Color.red);
				}else if(pStatus == STATUS.grounded) {
					g.setColor(Color.cyan);
				}else if(pStatus == STATUS.crouch) {
					g.setColor(Color.green);
				}else {
					g.setColor(Color.white);
				}
				g.fillRect(x, y, getWidth(), getHeight());
				if (faceRight) {
					g.fillRect(x + width, y, 5, 5);
				}else {
					g.fillRect(x - 5, y, 5, 5);
				}
			}
			//g2d.draw(getBounds()); //?
			g.setFont(new Font("Dialog", Font.ITALIC, 25));
			g.drawString(frameTimer + "" + pStatus, 400, 30);
			g.drawString(Game.masterMap.getLevel(), 400, 60);
			g.drawString("Clone active: " + String.valueOf(buddy != null), 400, 90);
		}else {
			g.setColor(new Color(Game.gameTime % 255, Game.gameTime % 255, Game.gameTime % 255));
			g.fillRect(x + width / 4, y, width / 2, height / 4);
			g.fillRect(x + width/2 - 1, y + height/2 - 1, 2, height/3 + 1);
			g.fillRect(x, y + width * 2 / 3, width, height / 3);
		}
		g.setColor(Color.red);
		g.drawLine(x, y, x + 10 * xv, y + 10 * yv);
	}
	
	public void keyInput(char key) throws FileNotFoundException{
		if (actionable) {
			action(key);
		}else {
			stopClone(key);
		}
	}
	
	public void action(char key) throws FileNotFoundException {
		//move this to in the top with other vairables
		// same with other values in these methods, declare it once
		int airSpeedMax = 3;
		int airSpeed = 3;
		if (key == 'd' || key == 'D') {			//for the D button
			Game.keyPressed = true;
			if (!isAttacking()) {
				//faceRight = true;
			}
			if(pStatus == STATUS.grounded) {
				if (Game.gameTime - lastTimePressed < keyBuffer && lastKeyDown == key) {
					xv = speed;
				}else{
					xv = Game.clamp(xv + 2, -speed, speed);
				}
			}else if(pStatus == STATUS.air) {
				if (xv <= airSpeedMax) {
					xv += airSpeed;
				}
			}
			if (pStatus == STATUS.wallRight) {
				yv = 0;
			}else if (pStatus == STATUS.wallLeft) {
					jump(defaultJumpHeight);
					xv = speed + 1;
			}
		}else if (key == 's' || key == 'S') { //for the S button
			if (!isAttacking()) {
				yv = speed + gravity;
				jumpTimer = 0;
				if (this.pStatus != STATUS.air) {
					xv = 0;
				}
				if (this.pStatus == STATUS.grounded) {
					//pStatus = STATUS.crouch;
					//frameTimer = attackData.get(pStatus).getTimer();
				}
				Game.keyPressed = true;
			}
		}else if (key == 'a' || key == 'A') {
			Game.keyPressed = true;
			if (!isAttacking()) {
				//faceRight = false;
			}
			if (pStatus == STATUS.grounded) {
				if (Game.gameTime - lastTimePressed < keyBuffer && lastKeyDown == key) {
					xv = -speed;
				}else{
					xv = Game.clamp(xv - 2, -speed, speed);
				}
			}else if(pStatus == STATUS.air) {
				if (xv >= -airSpeedMax) {
					xv = xv -= airSpeed;
				}
			}
			if (pStatus == STATUS.wallLeft) {
				yv = 0;
			}else if (pStatus == STATUS.wallRight) {
					jump(defaultJumpHeight);
					xv = -speed - 1;
			}
		}else if (key == 'w' || key == 'W') {
			if (pStatus == STATUS.grounded) {
				jump();
			}
		}else if (key == 'j' || key =='J') {
			if (!isAttacking() && pStatus != STATUS.wallLeft && pStatus != STATUS.wallRight) {
				if (pStatus == STATUS.grounded) {
					if (xv > 3 || xv < -3){
						setStatus(STATUS.dashAttack);
					}else if (lastKeyDown == 's') {
						setStatus(STATUS.downAttack);
						if (!groundMoving) {
							xv = 0;
						}
					}else{
						setStatus(STATUS.forwardAttack);
						if (!groundMoving) {
							xv = 0;
						}
					}
					
				}else {
					setStatus(STATUS.airNeutral);
				}
				frameTimer = attackData.get(pStatus).getTimer();
			}
		}else if (key == 'k' || key == 'K' ) {
			startClone();
		}else if(key == ' '){
			if (buddy != null){
				swap(this, buddy);
			}
		}
		//last default updates
		lastTimePressed = Game.gameTime;
		lastKeyDown = key;
	}
	
	public void keyUpInput(char key) {
		if (key == 's' || key == 'S') {
			yv = gravity;
			Game.keyPressed = false;
		}else if (key == 'a' || key == 'A' || key == 'd' || key == 'D') {
			Game.keyPressed = false;
		}else if (key == 'k' || key == 'K') {
			
		}
		lastKeyDown = key;
		lastTimeUp = Game.gameTime;
	}
	
	private void jump(int power) {
		setStatus(STATUS.air);
		yv = -speed;
		anchor = null;
		jumpTimer = power;
	}
	
	private void startClone() throws FileNotFoundException{
		if ( !(this instanceof Clone) && pStatus != STATUS.clone){
			if (HUD.HEALTH > 0 && buddy == null) {
				buddy = new Clone(this, handler, xv, yv, faceRight);
				handler.addObject(buddy);
				HUD.HEALTH -= 10;
			}
			if (pStatus == STATUS.grounded) {
				xv = 0;
			}
			//setActionable(false); //probably don't need this
			setStatus(STATUS.clone);
			//iTimer = 30; //don't reallt need this either
		}
	}
	
	public void stopClone(char key) {
		if (pStatus == STATUS.clone){
			if ((key == 'k' || key == 'K') && iTimer <= 0) {
				setStatus(STATUS.grounded);
			}else if(key == ' '){
				try {
					if (buddy != null){
						swap(this, buddy);
					}
				} catch (FileNotFoundException e) {
					System.out.println("Fucky wucky in stopClone");
					e.printStackTrace();
				}
			}
		}
	}
	
	private void swap(Playerv2 dude, Clone buddy) throws FileNotFoundException{
		int txv = buddy.getXV();
		int tyv = buddy.getYV();
		Clone temp = new Clone(buddy, handler, buddy.getXV(), buddy.getYV(), buddy.isFacingRight());
		buddy.swapWith(this);
		this.setX(temp.getX());
		this.setY(temp.getY());
		this.setXV(txv);
		this.setYV(tyv);
		this.setStatusOverride(temp.getStatus());
		this.faceRight = temp.isFacingRight();
	}
	
	private void swapOneWay(Playerv2 dude, Clone buddy){
		int tpx = dude.getX();
		int tpy = dude.getY();
		this.setX(buddy.getX());
		this.setY(buddy.getY());
		this.setXV(buddy.getXV());
		this.setYV(buddy.getYV());
		this.setStatusOverride(buddy.getStatus());
		this.faceRight = buddy.isFacingRight();
		buddy.setX(tpx);
		buddy.setY(tpy);
		buddy.die();
	}
	
	private void jump() {
		jump(timer);
	}
	
	//getters and setters*******************************************************************************************************************************************************************
	public boolean isFacingRight() {
		return this.faceRight;
	}
	public STATUS getStatus() {
		return this.pStatus;
	}
	public STATUS getLastStatus() {
		return this.lastStatus;
	}
	public int getJumpTimer() {
		return jumpTimer;
	}
	public void setJumpTimer(int t) {
		jumpTimer = t;
	}
	public int getXV() {
		return this.xv;
	}
	public int getYV() {
		return yv;
	}
	public void setYV(int yv) {
		this.yv = yv;
	}
	public void setXV(int xv) {
		this.xv = xv;
	}
	public int[] getMiddle() {
		int[] place = {x + width / 2, y + height / 2};
		return place;
	}
	public void setActionable(boolean state) {
		actionable = state;
	}
	public void setClone(Clone h){
		buddy = h;
	}
	public boolean hasClone(){
		return buddy != null;
	}
	public int getiTimer() {
		return iTimer;
	}
	private boolean walled(STATUS p) {
		return p == STATUS.wallLeft || p == STATUS.wallRight;
	}
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	
	public Rectangle getOffsetBoundsY() {
		return new Rectangle(x, y + yv , width, height);
	}
	
	public Rectangle getOffsetBoundsX() {
		return new Rectangle(x + xv, y, width, height);
	}
	
	public Rectangle getOffset() {
		return new Rectangle(x + xv, y + yv, width, height);
	}
	//add other status later
	public boolean isAttacking() {
		//need a set of all status
		if (pStatus == STATUS.forwardAttack || pStatus ==STATUS.downAttack || pStatus == STATUS.airNeutral || pStatus == STATUS.dashAttack || pStatus == STATUS.clone) {
			return true;
		}
		return false;
	}
	public Clone getBuddy(){
		return buddy;
	}
	public void setBuddy(Clone c){
		buddy = c;
	}
	
	// one time use************************************************************************************************************************************************************************************
	private Map<STATUS, Attack> setupAttacks2() throws FileNotFoundException{
		Scanner scan = new Scanner(new File("trial1.txt"));
		//each attack is scanned as follows
		//name (if there), duration, effect, damage, x, y, width, height
		ArrayList<HitBox> frameBoxes = new ArrayList<>(); //all hit boxes in given frame
		ArrayList<HitBox[]> allBoxes = new ArrayList<>(); //all hit boxes in given move
		Map<STATUS, Attack> moves = new HashMap<>(); //all moves
		String currname = "";
		scan.nextLine();
		while (scan.hasNextLine()) {
			String key = scan.next();
			if (key.equals("*")) {
				if (!currname.equals("")) {
					HitBox[][] wholeMove = new HitBox[frameBoxes.size()][];
					Attack curratk = new Attack(allBoxes.toArray(wholeMove), this);
					moves.put(STATUS.valueOf(currname), curratk);
					//System.out.println(currname + " " + curratk);
					frameBoxes = new ArrayList<>();
					allBoxes = new ArrayList<>();
				}
				currname = scan.next();
			}else{
				for (int j = 0; j < key.length(); j++) {
					char grammar = key.charAt(j);
					int time = scan.nextInt();
					if (grammar == '-') {
						HitBox[] temp = new HitBox[0];
						for (int i = 0; i < time; i++) {
							allBoxes.add(temp);
						}
						//might be able to take out this next line later
					}else if(grammar == ':' || grammar == ';') {
						HitBox special = new HitBox(scan.next(), scan.nextInt(),
								scan.nextInt(), scan.nextInt(), scan.nextInt(), scan.nextInt());
						frameBoxes.add(special);
						if (grammar == ':') {
							HitBox[] listBoxes = new HitBox[frameBoxes.size()];
							listBoxes = frameBoxes.toArray(listBoxes);
							for (int k = 0; k < time; k++) {
								allBoxes.add(listBoxes);
							}
							frameBoxes = new ArrayList<>();
						}
					}
				}
			}
		}
		HitBox[][] wholeMove = new HitBox[frameBoxes.size()][];
		Attack curratk = new Attack(allBoxes.toArray(wholeMove), this);
		moves.put(STATUS.valueOf(currname), curratk);
		scan.close();
		return moves;
	}
}


/*
 * ~Scratch Notes~
 * While this likely won't be very neat or make much sense at a glance,
 * I figure I'll just leave these here for future reference. Would not
 * leave this here if I were to formally present this as a project
 * but makes it convenient for keeping things consistent and might
 * be fun to look back on one day.
 * 
 * As of 2/26/2020 player 15 by 30 frame will be permanent and hard coding around it
 * will be expected.
 * Probably should have a double jump or some other way to gain height
 * 
 * As of 11/21/2020 switched to having only one clone, last git push was prior to this change
 */
