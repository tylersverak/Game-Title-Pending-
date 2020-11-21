import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Switch extends Tile implements Comparable<GameObject> {

	private int selfTimer;
	Tile control;
	
	public Switch(int x, int y, int width, int height, Handler handler, Tile control) {
		super(x, y, width, height, handler, null);
		if (control == null) {
			System.out.println("Switch at " + x + ", " + y + " has no tile its pointed to");
		}
		selfTimer = 0;
		activated = false;
		this.control = control;
		control.setActivated(false);
	}

	@Override
	public void tick() {
		if (selfTimer > 0) {
			selfTimer--;
			if (selfTimer == 0) {
				deactivateAll();
			}
		}
	}

	@Override
	public void render(Graphics g) {
		if (Game.debugMode) {
			g.setColor(Color.green);
			g.fillRect(x, y, width, height);
		}else {
			g.setColor(Color.white);
			g.fillRect(x, y, width, height);
			g.setColor(new Color(255, 100, 210));
			if (activated) {
				g.setColor(new Color(90, 5, 100));
			}
			g.fillRect(x + width / 4, y + height / 4, width / 2, height / 2);
		}
	}
	
	public void touched(Playerv2 player) {
		activateAll();
	}
	
	public void resetPos() {
		deactivateAll();
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	private void activateAll() {
		this.activated = true;
		control.activated = true;
		selfTimer = 30;
	}
	
	private void deactivateAll() {
		this.activated = false;
		control.deactivate();
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, getWidth(), getHeight());
	}
	
	public int compareTo(GameObject other) {
		if (other.getID() == ID.MovingTile || other.getID() == ID.Tile) {
			return 1;
		}
		return super.compareTo(other);
	}
}
