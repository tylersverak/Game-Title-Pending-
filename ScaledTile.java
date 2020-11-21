import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class ScaledTile extends Tile{

	//has a multiplier for the whole image, need to adjust other methods accordingly
	private int dilation;
	
	public ScaledTile(int x, int y, int dilation, Handler handler, Color skin[][]) {
		super(x, y, skin.length * dilation, skin[0].length * dilation, handler, skin);
		this.dilation = dilation;
	}

	public void extend(int horizontal, int vertical){ //this and rotate might not work with skins that aren't square
		Color newskin[][] = new Color[skin.length + horizontal][skin[0].length + vertical];
		int half = skin.length / 2;
		int verthalf = skin[0].length / 2;
		for (int i =0; i < skin.length + horizontal; i++){
			for (int j = 0; j < skin[0].length + vertical; j++){
				int skinx = i;
				int skiny = j;
				if (skinx > half && skinx < half + horizontal){
					skinx = half;
				}else if(skinx >= half + horizontal){
					skinx = skinx - horizontal;
				}
				if (skiny > verthalf && skiny < verthalf + vertical){
					skiny = verthalf;
				}else if(skiny >= verthalf + vertical){
					skiny = skiny - vertical;
				}
				newskin[i][j] = skin[skinx][skiny];
			}
		}
		skin = newskin;
	}
	
	public void rotate(){
		Color newskin[][] = new Color[skin[0].length][skin.length];
		for (int i = 0; i < skin.length; i++){
			for (int j = 0; j < skin[i].length; j++){
				newskin[j][i] = skin[i][j];
			}
		}
		skin = newskin;
	}
	
	public void render(Graphics g){
		if (Game.debugMode) {
			g.setColor(Color.BLUE);
			if (!activated){
				g.setColor(new Color(200, 50, 50));
			}
			g.fillRect(x, y, dilation * skin.length, dilation * skin[0].length);
			g.setColor(Color.white);
			g.drawString("x: " + x + "y: " + y, x + 5, y + 20);
			g.drawString("w: " + width + "h: " + height, x + 5, y + 40);
		}else if (activated){
			for(int i = 0; i < skin.length; i++) {
				for(int j = 0; j < skin[i].length; j++) {
					g.setColor(skin[i][j]);
					g.fillRect(x + dilation*i, y + dilation*j, dilation, dilation);
				}
			}
		}
	}
	
	public int getHeight() {
		return skin[0].length * dilation;
	}
	
	public int getWidth() {
		return skin.length * dilation;
	}

}
