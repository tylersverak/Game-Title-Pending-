import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.FileNotFoundException;

public class Clone extends Playerv2{

	public Clone(Playerv2 curr, Handler handler, int xv, int xy, boolean faceRight) throws FileNotFoundException {
		super(curr.getX(), curr.getY(), handler);
		this.faceRight = faceRight;
		System.out.println("new one at " + Game.gameTime);
	}
	
	@Override
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
			}else{
				g.setColor(new Color(10, 95, 32));
				g.fillRect(x, y, getWidth(), getHeight());
			}
		}else {
			g.setColor(new Color(10, 95, 32));
			g.fillRect(x + width / 4, y, width / 2, height / 4);
			g.fillRect(x + width/2 - 1, y + height/2 - 1, 2, height/3 + 1);
			g.fillRect(x, y + width * 2 / 3, width, height / 3);
		}
	}
	
	public void die(Playerv2 dude) {
		HUD.HEALTH += this.cloneValue;
		handler.removeObject(this);
		dude.setClone(null);
	}
	
	public void die() {
		Playerv2 dude = handler.getPlayer();
		handler.addObject(new Chakra(x + width / 2,
		y + height / 2, ID.Chakra, 5 + Game.rand.nextInt(5),
		handler, dude));
		dude.setClone(null);
		handler.removeObject(this);
	}
	
	protected void setStatusOverride(STATUS s){
		if (s != STATUS.clone){
			pStatus = s;
		}
	}
	
	public void swapWith(Playerv2 dude){
		this.x = dude.getX();
		this.y = dude.getY();
		this.xv = dude.getXV();
		this.yv = dude.getYV();
		this.setStatusOverride(dude.getStatus());
		this.iTimer = dude.getiTimer();
		this.jumpTimer = dude.getJumpTimer();
		this.lastTimePressed = dude.lastTimePressed;
		this.lastTimeUp = dude.lastTimeUp;
		this.frameTimer = dude.frameTimer;
		this.groundMoving = dude.groundMoving;
		this.actionable = dude.actionable;
		this.faceRight = dude.isFacingRight();
	}

}
