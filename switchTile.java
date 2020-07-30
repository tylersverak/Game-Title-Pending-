import java.awt.Color;
import java.util.ArrayList;

public class switchTile extends Tile implements Comparable<GameObject>{

	public switchTile(int x, int y, int width, int height, Handler handler, ArrayList<ArrayList<Color>> map) {
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
