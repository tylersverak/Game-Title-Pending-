import java.awt.Canvas;
import java.awt.event.WindowEvent;
import java.io.IOException;

public abstract class Source extends Canvas{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final static int WIDTH = 1800, HEIGHT = 1000;
	public Window masterWindow;
	public Handler handler;
	
	public Source(String title) throws IOException{
		masterWindow = new Window(WIDTH, HEIGHT, title, this);
		handler = new Handler();
	}
	
	public abstract void tick();
	public abstract void render();
	public void close() {
		masterWindow.getFrame().dispatchEvent(new WindowEvent(masterWindow.getFrame(), WindowEvent.WINDOW_CLOSING));
	}
}
