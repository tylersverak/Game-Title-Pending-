import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Tile2 extends GameObject implements Comparable<GameObject>{
	
	Handler handler;
	private ArrayList<ArrayList<Color>> skin;
	protected boolean touched;
	protected boolean activated;
	
	public Tile2(int x, int y, int width, int height, Handler handler, ArrayList<ArrayList<Color>> skin) {
		super(x, y, ID.Tile, width, height);
		this.handler = handler;
		this.skin = skin;
		activated = true;
		scaleSkin();
	}

	public void tick() {
		
	}
	
	public void render(Graphics g) {
		if (Game.debugMode) {
			g.setColor(Color.BLUE);
			if (!activated){
				g.setColor(new Color(200, 50, 50));
			}
			g.fillRect(x, y, getWidth(), getHeight());
			g.setColor(Color.WHITE);
			g.drawString("x: " + x + "y: " + y, x + 5, y + 20);
		}else if (activated){
			for(int i = 0; i < skin.size(); i++) {
				for(int j = 0; j < skin.get(i).size(); j++) {
					g.setColor(skin.get(i).get(j));
					g.fillRect(x + width*i, y + height*j, width, height);
				}
			}
		}
	}
	
	public Rectangle getBounds() {
		if (!activated){
			return new Rectangle(-90, 0, 0, 0);
		}
		return new Rectangle(x, y, getWidth(), getHeight());
	}
	
	private void scaleSkin(){
		width = width / skin.size() * width;
		height = height / skin.get(0).size() * height;
	}
	
	public int getHeight() {
		return skin.size() * height;
	}
	
	public int getWidth() {
		return skin.get(0).size() * width;
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
		activated = false;
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
