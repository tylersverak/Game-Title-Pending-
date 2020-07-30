import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

public class KeyInput extends KeyAdapter{
	
	private Handler handler;
	private Source s;
	private Run r;
	
	public KeyInput(Handler handler, Source s, Run r) {
		this.handler = handler;
		this.s = s;
		this.r = r;
	}

	public void keyPressed(KeyEvent e){
		char key = e.getKeyChar();
		if(key == KeyEvent.VK_ESCAPE) {
			System.exit(1);
		}
		
		if (s instanceof Game) {
			try {
				downGame(key);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else {
			if (Character.isLetter(key)) {
				try {
					r.startGame();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	private void downGame(char key) throws IOException {
		if (key == 'm' || key == 'M') {
			r.startTitle();
		}
		for (int i = 0; i < handler.getMasterList().size(); i++) {
			GameObject tempObject = handler.getMasterList().get(i);
			if (tempObject.getID() == ID.Player) {
				Playerv2 tempPlayer = (Playerv2)tempObject;
				try {
					tempPlayer.keyInput(key);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					//gonna be honest... don't know what this does
					e1.printStackTrace();
				}
				if (key == 'v' || key == 'V') {
					tempPlayer.setX(400);
					tempPlayer.setY(250);
					tempPlayer.setXV(0);
					tempPlayer.setYV(0);
				}
				if (key == 'p' || key == 'P') {
					System.out.println(Game.masterMap);
					System.out.println("Player is at " + tempPlayer.getX() + " " + tempPlayer.getY());
				}
			}
		}
	}
	
	public void keyReleased(KeyEvent e) {
		char key = e.getKeyChar();
		if (s instanceof Game) {
			for (int i = 0; i < handler.getMasterList().size(); i++) {
				GameObject tempObject = handler.getMasterList().get(i);
				if (tempObject.getID() == ID.Player) {
					Playerv2 tempPlayer = (Playerv2) tempObject;
					tempPlayer.keyUpInput(key);
				}
			}
		}
	}
	
}
