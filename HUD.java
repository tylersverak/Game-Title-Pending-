import java.awt.*;
import java.util.*;

public class HUD{
	
	public static final int FULL_HEALTH = 100;
	public static int HEALTH = 50;
	
	public void tick() {
		if (Game.gameTime % 200 == 0 && Game.gameTime != 0) {
			HEALTH++;
		}
		HUD.HEALTH = Game.clamp(HEALTH, 0, 100);
	}
	
	public void render(Graphics g) {
		//health
		g.setColor(Color.gray);
		g.drawString(""+ HEALTH, 90, 80);
		g.fillRect(90, 90, 100, 50);
		if (HEALTH <= 25) {
			g.setColor(Color.red);
		}else if(HEALTH <= 50) {
			g.setColor(Color.yellow);
		}else {
			g.setColor(Color.green);
		}
		g.fillRect(100, 100, HEALTH*4/5, 30);	
	}
	
	
}
