import java.io.IOException;

public class Run implements Runnable{

	protected boolean running;
	protected Thread thread;
	protected Handler handler;
	private Source runner;
	
	public Run() throws IOException {
		//runner = new TitleScreen("Title", this);
		runner = new Game(this);
		start();
	}
	
	public void startGame() throws IOException {
		stop();
		runner = new Game(this);
		start();
	}
	
	public void startTitle() throws IOException {
		stop();
		runner = new TitleScreen("title", this);
		start();
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				runner.tick();
				delta--;
			}
			
			if (running) {
				runner.render();
			}
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}
	

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop() {
		/*try {
			thread.join();
			running = false;
		}catch(Exception e){
			e.printStackTrace();
		}*/
		runner.close();
		running = false;
		thread = null;
	}
	
	public static void main(String[] args) throws IOException{
		new Run();
	}
}


