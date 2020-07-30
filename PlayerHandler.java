import java.awt.Graphics;
import java.util.LinkedList;

public class PlayerHandler {
//probably can delete cause its all out of the handler now
	LinkedList<Playerv2> players = new LinkedList<>();
	
	public void tick() {
		for(int i = 0; i < players.size(); i++) {
			//Playerv2 tempObject = players.get(i);
			//tempObject.tick();
		}
	}
	
	public void render(Graphics g) {
		for(int i = 0; i < players.size(); i++) {
			Playerv2 tempObject = players.get(i);
			tempObject.render(g);
		}
	}
	
	public void addObject(Playerv2 object) {
		this.players.add(object);
	}
	
	public void removeObject(Playerv2 object) {
		this.players.remove(object);
	}
	
	public void clear() {
		while (!players.isEmpty()) {
			players.remove(0);
		}
	}
}
