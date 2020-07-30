import java.awt.Rectangle;

public class Attack {
	
	private GameObject user;
	private HitBox[][] boxes;
	private int duration;

	public Attack(HitBox[][] boxes, GameObject user) {
		if (boxes.length == 0) {
			throw new IllegalArgumentException();
		}
		this.duration = boxes.length;
		this.user = user;
		this.boxes = boxes;
	}
	
	public Attack(int duration, GameObject user) {
		this.duration = duration;
		this.user = user;
	}
	
	public HitBox[] getCurrHit(int time) {
		if (time < 0 || time >= duration) {
			System.out.println(time + " is the time and duration is " + duration);
			throw new IllegalArgumentException();
		}
		return boxes[time];
	}
	
	//rewrite it in player class to take the arguments from the rectangle
	public Rectangle[] getHitBoxes(int time, Playerv2 dude) {
		time = time % duration;
		Rectangle[] currBoxes = new Rectangle[boxes[time].length];
		if (dude.isFacingRight()) { //might need to restructure
			for (int i = 0; i < currBoxes.length; i++) {
				Rectangle temp = boxes[time][i].getRect();
				currBoxes[i] = new Rectangle((int)(dude.getX() + dude.getWidth() / 2 + temp.getX()), (int)(dude.getY() + dude.getHeight() / 2 + temp.getY()),
											 (int)temp.getWidth(), (int)temp.getHeight());
			}
		}else {
			for (int i = 0; i < currBoxes.length; i++) {
				Rectangle temp = boxes[time][i].getRect();
				currBoxes[i] = new Rectangle((int)(dude.getX() + dude.getWidth() / 2 - temp.getX() - temp.getWidth()),
						(int)(dude.getY() + dude.getHeight() / 2 + temp.getY()), (int)temp.getWidth(), (int)temp.getHeight());
			}
		}
		return currBoxes;
	}
	
	public int getTimer() {
		return duration;
	}
	
	public GameObject getUser() {
		return user;
	}
	
	public String toString() {
		return "This move lasts " + duration + " frames";
	}
	
}
