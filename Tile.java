import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Tile extends GameObject implements Comparable<GameObject>{
	
	Handler handler;
	private ArrayList<ArrayList<Color>> map;
	protected boolean touched;
	protected boolean activated;
	
	public Tile(int x, int y, int width, int height, Handler handler, ArrayList<ArrayList<Color>> map) {
		super(x, y, ID.Tile, width, height);
		this.handler = handler;
		this.map = map;
		activated = true;
	}

	public void tick() {
		
	}
	
	public void render(Graphics g) {
		if (Game.debugMode) {
			g.setColor(Color.BLUE);
			g.fillRect(x, y, getWidth(), getHeight());
			g.setColor(Color.WHITE);
			g.drawString("x: " + x + "y: " + y, x + 5, y + 20);
		}else {
			for(int i = 0; i < map.size(); i++) {
				for(int j = 0; j < map.get(i).size(); j++) {
					g.setColor(map.get(i).get(j));
					g.fillRect(x + width*i, y + height*j, width, height);
				}
			}
		}
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, getWidth(), getHeight());
	}
	
	public int getHeight() {
		return map.size() * height;
	}
	
	public int getWidth() {
		return map.get(0).size() * width;
	}
	
	public void touched(Playerv2 player) {
		//default is do nothing when touched
	}
	
	public boolean getActivated() {
		return activated;
	}
	
	public void setActivated(boolean activated) {
		this.activated = activated;
	}
	
	public void deactivate() {
		//default to nothing
	}
	
	public int compareTo(GameObject other) {
		if (other instanceof Switch) {
			return -1;
		}
		if (other.getID() == ID.Tile) {
			if (other.getX() == x) {
				return other.getY() - y;
			}
			return other.getX() - x;
		}
		return super.compareTo(other);
	}
}
