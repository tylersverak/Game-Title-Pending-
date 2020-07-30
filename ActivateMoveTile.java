import java.awt.Color;
import java.util.ArrayList;

public class ActivateMoveTile extends MovingTile implements Comparable<GameObject>{

	private int timer;
	
	public ActivateMoveTile(int movementStyle, int x, int y, int width, int height, Handler handler,
			ArrayList<ArrayList<Color>> map) {
		super(movementStyle, x, y, width, height, handler, map);
		timer = 0;
	}
	
	public void tick() {
		updatePos(movementStyle);
		movePlayer();
	}
	
	private void updatePos(int choice) {
		if (activated) {
				int[] move = updateVelocity(choice);
				xv = move[0];
				yv = move[1];
				timer++;
		}else {
			xv = 0;
			yv = 0;
		}
		x += xv;
		y += yv;
	}
	
	private int[] updateVelocity(int choice) {
		int[] cords = new int[2];
		switch(choice) {
		case 1:
			cords[0] = 0;
			if (timer % 800 < 200 || timer % 800 >= 600) {
				cords[1] = 2;
			}else {
				cords[1] = -2;
			}
			break;
		case 2:
			int swingclock = 100;
			if (timer % swingclock < swingclock / 4) {
				cords[1] = -3;
				cords[0] = 2;
			}else if (timer % swingclock < swingclock / 2) {
				cords[1] = 3;
				cords[0] = -2;
			}else if (timer % swingclock < swingclock * 3 / 4) {
				cords[1] = -3;
				cords[0] = -2;
			}else {
				cords[1] = 3;
				cords[0] = 2;
			}
			break;
		default:
			
		}
		return cords;
	}
}
