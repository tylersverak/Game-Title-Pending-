import java.awt.Graphics;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Handler {

	private Queen boss = null;
	private LinkedList<GameObject> objectList = new LinkedList<GameObject>();
	private boolean bossmode = false;
	
	//tick
	public void tick() {
		PriorityQueue<GameObject> pQ = new PriorityQueue<>();
		
		if (boss == null) {
			bossmode = false;
		}else {
			bossmode = true;
			boss.tick();
		}
		
		for(int i = 0; i < objectList.size(); i++) {
			GameObject tempObject = objectList.get(i);
			pQ.add(tempObject);
		}
		while(!pQ.isEmpty()) {
			pQ.poll().tick();
		}
	}
	
	//render
	public void render(Graphics g) {
		PriorityQueue<GameObject> pQ = new PriorityQueue<>();
		for(int i = 0; i < objectList.size(); i++) {
			GameObject tempObject = objectList.get(i);
			pQ.add(tempObject);
		}
		if (boss != null) {
			pQ.add(boss);
		}
		while(!pQ.isEmpty()) {
			pQ.poll().render(g);
		}
	}
	
	//adds the GameObject to the Handler
	public void addObject(GameObject object) {
		//bossmode part here might be unnessecary cause you have to iterate through everything anyway
		if (object instanceof Queen) {
			boss = (Queen)object;
		}else {
			this.objectList.add(object);
		}
	}
	
	//removes the object from the Handler
	public void removeObject(GameObject object) {
		this.objectList.remove(object);
	}
	
	//empties the handler
	public void clear() {
		while (!objectList.isEmpty()) {
			objectList.remove(0);
		}
	}
	
	public LinkedList<GameObject> getMasterList(){
		return objectList;
	}
	
	public boolean bossMode() {
		return boss != null;
	}
	
	public Queen getBoss() {
		return boss;
	}
	
	public void killBoss() {
		boss = null;
		bossmode = false;
	}
	
	public Playerv2 getPlayer() {
		for (int i = 0; i < objectList.size(); i++) {
			if (objectList.get(i).getID() == ID.Player) {
				return (Playerv2)objectList.get(i);
			}
		}
		return null;
	}
	
	public void recallClones() {
		for (int i = 0; i < objectList.size(); i++) {
			GameObject temp = objectList.get(i);
			if (temp instanceof Clone) {
				((Clone)objectList.get(i)).die();
			}else if(temp.getID() == ID.Player){
				((Playerv2)temp).setBuddy(null);
			}
		}
	}
}
