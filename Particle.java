import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Particle extends GameObject implements Comparable<GameObject>{
	
	private Handler handler;
	private int life, value;
	private int colors[];
	
	public Particle(int x, int y, int value, Handler handler) {
		super(x, y, ID.Particle, 0, 0);
		this.life = value;
		this.value = value;
		this.handler = handler;
		colors = new int[3];
		colors[0] = Game.rand.nextInt(255 - 140) + 140;
		colors[1] = Game.rand.nextInt(150);
		colors[2] = Game.rand.nextInt(80);
	}

	@Override
	public int compareTo(GameObject other) {
		// TODO Auto-generated method stub
		ID type = other.getID();
		if (type == ID.CutObject) {
			return -1;
		}else if(type == ID.Particle){
			return 0;
		}else {
			return 1;
		}
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		if (Game.gameTime % 3 == 0) {
			y -= 1;
		}
		life--;
		if (life <= 0) {
			handler.removeObject(this);
		}
	}

	@Override
	public void render(Graphics g) {
		// x y is center of particle
		g.setColor(new Color(colors[0], colors[1], colors[2]));
		int size = 2 * life;
		g.fillOval(x - size / 2, y - size / 2 + 20, size, size);
	}

	@Override
	public Rectangle getBounds() {
		return null;
	}
}
