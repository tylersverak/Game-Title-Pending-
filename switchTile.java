import java.awt.Color;
import java.util.ArrayList;

public class switchTile extends Tile implements Comparable<GameObject>{

	
	//NOT USED
	
	public switchTile(int x, int y, int width, int height, Handler handler, Color map[][]) {
		super(x, y, width, height, handler, map);
	}
	
	public void tick() {
		checkCondition();
	}

	public void checkCondition() {
		if (!handler.bossMode()) {
			handler.removeObject(this);
		}
	}
		
}
