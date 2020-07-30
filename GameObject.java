import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject implements Comparable<GameObject>{
	
	//put protected variables here
	protected int x,y,height,width;
	protected ID id;

	public GameObject(int x, int y, ID id, int width, int height) {
		this.x = x;
		this.y = y;
		this.id = id;
		this.width = width;
		this.height = height;
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
	public abstract Rectangle getBounds();
	
	//setters
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setID(ID id) {
		this.id = id;
	}
	//getters
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	public ID getID() {
		return this.id;
	}
	public int getHeight() {
		return this.height;
	}
	public int getWidth() {
		return this.width;
	}
	public String toString() {
		return (x + " " + y + " " + id);
	}
	public int[] getMiddle() {
		int[] temp = {x + width / 2, y + height / 2};
		return temp;
	}
	
	public int compareTo(GameObject other) {
		return Game.priorityList.get(this.getID()) - Game.priorityList.get(other.getID());
	}
}
