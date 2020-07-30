import java.awt.Rectangle;

public class HitBox{

	private int damage, x, y, width, height;
	private String effect;
	
	public HitBox(String effect, int damage, int x, int y, int width, int height) {
		this.damage = damage;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.effect = effect;
	}
	
	public HitBox() {
		this.damage = 0;
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
		this.effect = "none";
	}
	
	public Rectangle getRect() {
		int xoff = 0;
		int yoff = 0;
		if (effect.contains("BKWD")) {
			xoff = width;
		}
		if (effect.contains("BLO")) {
			yoff = height;
		}
		return new Rectangle(x - xoff, y - yoff, width, height);
	}
	
	public String getEffect() {
		return effect;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public boolean isHit() {
		if (damage == 0 || width == 0 || height == 0) {
			return false;
		}
		return true;
	}
	
	public int getHitX() {
		return x;
	}
	
	public int getHitY() {
		return y;
	}
	
	public int getHitWidth() {
		return width;
	}
	
	public int getHitHeight() {
		return height;
	}
}
