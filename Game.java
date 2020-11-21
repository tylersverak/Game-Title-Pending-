//version 1.9.1

/*
 * to-do~
 * 1. sprites
 * 2. balance clone/mana/dying system
 * 3. rework all the jumping stuff/ add element using clones
 * 4. maybe have just one clone, cost chakra to have it out, but allow you to switch between the two
 */
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.imageio.ImageIO;

public class Game extends Source{

	//final variables
	private static final long serialVersionUID = 1902147405213257243L;
	private Run r;
	public static Random rand = new Random();
	private static final String[] textures = { "patsdino.txt", "brain_pat.txt", "goo.txt", "platform_grey.txt", "robot.txt", "block.txt", "ocean_spiral.txt"}, 
			levels = {"levelremake", "weird floor and wall.txt", "airship.txt", "skyfield2", "level3.txt","basement.txt","treasure.txt","junglegym.txt"};
	public final static boolean debugMode = true;
	public final static String gameTitle = "sample"; //lol
	public static final int roomStartX = 2, roomStartY = 2;
	
	//updated with the game
	public static int gameTime, gameSpeed;
	public static boolean keyPressed;
	//objects for the game
	private HUD hud;
	public static GameMapTree masterMap;
	public static Map<ID, Integer> priorityList;
	private static int order = 0;
	
	
	public Game(Run masterRun) throws IOException{
		super(gameTitle);
		this.requestFocusInWindow();
		r = masterRun;
		this.addKeyListener(new KeyInput(this.handler, this, r));
		//***make this pretty and useful
		hud = new HUD();
		masterMap =  new GameMapTree(this, levels, textures, handler);
		
		gameTime = 0;
		gameSpeed = 1;
		intializePriorityList();
	}
	
	private void intializePriorityList() {
		priorityList = new HashMap<ID, Integer>();
		ID[] hierachy = {ID.CutObject, ID.Particle, ID.Chakra, ID.Player, ID.Bee, ID.Queen,
						 ID.Queen, ID.Enemy, ID.Bomb, ID.NoCloneZone, ID.MovingTile, ID.Tile};
		for (int i = 0; i < hierachy.length; i++) {
			priorityList.put(hierachy[i], i);
		}
	}
	
	public void tick() {
		handler.tick();
		try {
			hud.tick();
		} catch(Exception e){
			System.out.println("Ok HUD didn't work for some reason");
		}
		gameTime += gameSpeed;
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.setColor(new Color(95,5,25));
		g.fillRect(0,0,WIDTH,HEIGHT);
		
		handler.render(g);
		try {
			hud.render(g);
		} catch(Exception e) {
			System.out.println("Finally caught ya... Game ~line 154");
		}
		
		g.dispose();
		bs.show();
	}
	
	//"clamps" an integer by returning it if it is between the min
	//and max parameters, otherwise passing the boundary it is outside of
	//throws an IllegalArgumentException if the min boundary is
	//greater than or equal to the max boundary
	public static int clamp(int var, int min, int max) {
		if (min >= max) {
			System.out.println("Woah there cowboy! Min was " + min + " and max " + max);
			throw new IllegalArgumentException();
		}
		if (var > max) {
			return max;
		}else if(var < min) {
			return min;
		}else {
			return var;
		}
	}
	
	public static int getNextOrder(){
		return ++order;
	}
	
	//same as above but with doubles
	public static double clamp(double var, double min, double max) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		if (var > max) {
			return max;
		}else if(var < min) {
			return min;
		}else {
			return var;
		}
	}
	
	public static BufferedImage flippedImage(String filename){
		BufferedImage flipped = null;
		try {
			BufferedImage img = ImageIO.read(new File(filename));
			int width = img.getWidth();
			int height = img.getHeight();
			flipped = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					flipped.setRGB((width - 1) - i, j, img.getRGB(i, j));
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Image with name " + filename + " was not found.");
		}
		return flipped;
	}
	
	//returns a scanner of a file with the given name
    public static Scanner readFile(String fileName) throws FileNotFoundException, IOException{
        File myFile = new File(fileName);
        //System.out.println(myFile.getCanonicalPath()); //use this line and two below for testing
       //File file = new File(".");         
        //for(String fileNames : file.list()) System.out.println(fileNames);
        Scanner scan = new Scanner(myFile);
        return scan;
    }
    
    //returns the handler
    public Handler getHandler() {
    	return handler;
    }

}
