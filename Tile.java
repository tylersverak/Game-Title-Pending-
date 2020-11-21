import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Tile extends GameObject implements Comparable<GameObject>{
	
	Handler handler;
	protected Color skin[][];
	protected boolean touched;
	protected boolean activated;
	protected int order;
	
	public Tile(int x, int y, int width, int height, Handler handler, Color skin[][]) {
		super(x, y, ID.Tile, width, height);
		this.handler = handler;
		this.skin = skin;
		activated = true;
		order = Game.getNextOrder();
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
			g.drawString("w: " + width + "h: " + height, x + 5, y + 40);
		}else if (activated){
			for(int i = 0; i < skin.length; i++) {
				for(int j = 0; j < skin[i].length; j++) {
					g.setColor(skin[i][j]);
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
	
	public int getHeight() {
		return skin[0].length * height;
	}
	
	public int getWidth() {
		return skin.length * width;
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
	
	public int getOrder(){
		return order;
	}
	
	public void deactivate() {
		activated = false;
	}
	
	public int compareTo(GameObject other) {
		if (other instanceof Switch) {
			return -1;
		}
		if (other.getID() == ID.Tile) {
			return ((Tile)other).getOrder() - this.getOrder();
		}
		return super.compareTo(other);
	}
}
