import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

//IMPORTANT!!!

/* each file given must be a txt file (or some other text file idk how that works
 * though) and the names and shit must be correct obviously but more importantly
 * the beginning needs to follow the following format-
 * 
 * directions    p for positive and anything else for negative with values (p 0 p 0 is middle)
 * name		     (i.e. ty's house)
 * then the rest of the junk or that shit will NOT COMPILE
 */

public class GameMapTree {
	
	private ArrayList<ArrayList<ArrayList<Color>>> importedTextures;
	private GameMap[][] grandMap;
	private final int mapSize = 5;
	//private int roomX = mapSize / 2, roomY = mapSize / 2; //dictates where the player starts
	private int roomX = Game.roomStartX, roomY = Game.roomStartY;
	private Handler handler;
	private Game g;
	
	public GameMapTree(Game g, String[] levelNames, String[] textures, Handler handler) throws IOException{
		grandMap = new GameMap[mapSize][mapSize];
		if (levelNames.length == 0 || textures.length == 0|| handler == null) {
			throw new IllegalArgumentException();
		}
		importedTextures = new ArrayList<ArrayList<ArrayList<Color>>>();
		for (int i = 0; i < textures.length; i++) {
    		importedTextures.add(createList(Game.readFile(textures[i])));
    	}
		for (int i = 0; i < levelNames.length; i++) {
			Scanner currScan = Game.readFile(levelNames[i]);
			if (!currScan.hasNextLine()) {
				throw new IllegalArgumentException();
			}
			int xoffset = mapSize / 2 + currScan.nextInt();
			int yoffset = mapSize / 2 + currScan.nextInt();
			if (grandMap[xoffset][yoffset] != null) {
				System.out.println("oh shit I think a level got overwritten");
			}
			grandMap[xoffset][yoffset] = readLevel(currScan, handler);
		}
		PrintAll();
		grandMap[roomX][roomY].setup(new Playerv2(300, 400, 40, 40, handler));
		this.handler = handler;
		this.g = g;
	}
	
	private static ArrayList<ArrayList<Color>> createList(Scanner s){
       ArrayList<ArrayList<Color>> tem = new ArrayList<ArrayList<Color>>();
       while (s.hasNextLine()){
                  String temp = s.nextLine();
                  Scanner lineCheck = new Scanner(temp);
                  ArrayList<Color> tempList = new ArrayList<Color>();
                  while (lineCheck.hasNextInt()) {
                	  tempList.add(new Color(lineCheck.nextInt(), 
	                		  lineCheck.nextInt(), lineCheck.nextInt()));
                  }
                  tem.add(tempList);
                  lineCheck.close();
       } 
       s.close();
       return tem;
    }
	
	//MASTER LEVEL READER MAJOR KEY
	 private GameMap readLevel(Scanner s, Handler handler) throws IOException{
	    	/* example of readable lines
	    	 * e 100 400 20 20
	    	 * t 800 350 40 20 (later put name of texture file here)
	    	 * p 50 400 40 80 
	    	 * tb all the words in the bubble can be put here 600 700 400 200
	    	 */
		 	Tile lastTile = null;
	    	Set<GameObject> gameq = new HashSet<>();
	    	if (!s.hasNextLine()) {
	    		throw new IllegalArgumentException();
	    	}
	    	String name = s.next();
	    	System.out.println(name);
	    	while(s.hasNextLine()) {
	    		String type = s.next();
	    		if(type.equals("e")) {
	    			gameq.add(new BasicEnemy(s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), handler));
	    		}else if(type.equals("t")) {
	    			lastTile = new Tile(s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), handler, importedTextures.get(0));
	    			gameq.add(lastTile);
	    		}else if(type.equals("mt")) {
	    			lastTile = new MovingTile(s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), handler, importedTextures.get(0));
	    			gameq.add(lastTile);
	 			}else if(type.equals("jt")) {
	 				gameq.add(new JumpTile(s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), handler, importedTextures.get(0)));
	 			}else if(type.equals("st")) {
	 				gameq.add(new switchTile(s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), handler, importedTextures.get(0)));
	 			}else if(type.equals("s")) {
	 				gameq.add(new Switch(s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), handler, lastTile));
	    		}else if (type.contentEquals("B")) {
	 				gameq.add(new Bee(s.nextInt(), s.nextInt(), handler));
	 			}else if(type.equals("p")) {
	 				//so think about removing these if they are constant
	    			gameq.add(new Playerv2(s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), handler));   			
	    		}else if(type.contentEquals("ncz")) {
	    			gameq.add(new NoCloneZone(s.nextInt(), s.nextInt(), s.nextInt()));
	    		}else if(type.contentEquals("amt")) {
	    			lastTile = new ActivateMoveTile(s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), handler, importedTextures.get(0));
	    			gameq.add(lastTile);
	    		}else if(type.equals("tb")) {
	    			//assumes theres at least one word
	    			String content = s.next();
	    			while (!s.hasNextInt()) {
	    				content = content + " " + s.next();
	    			}
	    			gameq.add(new TextBubble(content, s.nextInt(), s.nextInt(), ID.CutObject, s.nextInt(), s.nextInt(), 120));
	    		} else if (type.contentEquals("QB")) {
	    			gameq.add(new Queen(handler));
	    		}
	    			   	
	    	}
	    	s.close();
	    	return new GameMap(name, gameq, handler);
	    }
	 
	 public boolean goRight(Playerv2 curr) {
		 if (grandMap[roomX + 1][roomY] == null) {
			 return false;
		 }else{
			 grandMap[roomX][roomY].updateObjects(g.getHandler().getMasterList());
			 roomX++;
			 grandMap[roomX][roomY].setup(curr);
			 return true;
		 }
	 }
	 
	 public boolean goLeft(Playerv2 curr) {
		 if (grandMap[roomX - 1][roomY] == null) {
			 return false;
		 }else{
			 grandMap[roomX][roomY].updateObjects(g.getHandler().getMasterList());
			 roomX--;
			 grandMap[roomX][roomY].setup(curr);
			 return true;
		 }
	 }
	 
	 public boolean goUp(Playerv2 curr) {
		 if (grandMap[roomX][roomY + 1] == null) {
			 return false;
		 }else{
			 grandMap[roomX][roomY].updateObjects(g.getHandler().getMasterList());
			 roomY++;
			 grandMap[roomX][roomY].setup(curr);
			 return true;
		 }
	 }
	 
	 public boolean goDown(Playerv2 curr) {
		 if (grandMap[roomX][roomY - 1] == null) {
			 return false;
		 }else{
			 grandMap[roomX][roomY].updateObjects(g.getHandler().getMasterList());
			 roomY--;
			 grandMap[roomX][roomY].setup(curr);
			 return true;
		 }
	 }
	 
	 public boolean hasRight() {
		 if (roomX == mapSize) {
			 return false;
		 }
		 return grandMap[roomX + 1][roomY] != null;
	 }
	 
	 public boolean hasLeft() {
		 if(roomX == 0) {
			 return false;
		 }
		 return grandMap[roomX - 1][roomY] != null;
	 }
	 
	 public boolean hasUp() {
		 if(roomY == mapSize) {
			 return false;
		 }
		 return grandMap[roomX][roomY + 1] != null;
	 }
	 
	 public boolean hasDown() {
		 if (roomY == 0) {
			 return false;
		 }
		 return grandMap[roomX][roomY - 1] != null;
	 }
	 
	 //toString method
	 public String toString() {
		 return toStringHelp(grandMap[roomX][roomY]);
	 }
	 
	 public void PrintAll() {
		 for (int i = 0; i < mapSize; i++) {
			 for (int j = 0; j < mapSize; j++) {
				 if (grandMap[i][j] != null) {
					 System.out.println("room at " + i + ", " + j);
				 }else {
					 System.out.println("no room at " + i + ", " + j);
				 }
			 }
		 }
	 }
	 
	 private String toStringHelp(GameMap root) {
		 if (root == null) {
			 return "";
		 }
		 return root.toString();
				 //+ toStringHelp(root.up) + toStringHelp(root.right) +
				 //toStringHelp(root.down) + toStringHelp(root.left);
	 }
	 
	 public String getLevel() {
		 return grandMap[roomX][roomY].toString();
	 }
	 
	 public String levelName() {
		 return grandMap[roomX][roomY].levelName();
	 }
	 
	 public boolean enterSecret(Playerv2 currPlayer) {
		 return false; //currently not being used
	 }
	 
	 //each GameMap
	 private class GameMap {

			GameMap left, right, up, down, secret;
			private Set<GameObject> currObjects;
			private Handler handler;
			private String name;
			private int position[] = {0, 0};
			
			public GameMap(String name, Set<GameObject> q, Handler handler) {
				this.secret = null;
				currObjects = q;
				this.handler = handler;
				this.name = name;
			}
			
			public void setup(Playerv2 currPlayer) {
				handler.clear();
				handler.addObject(currPlayer);
				for (GameObject g: currObjects) {
					handler.addObject(g);
				}
			}
			
			public String levelName() {
				return name;
			}
			
			public String toString() {
				return this.name + " has " + currObjects.size() + " GameObjects.\n";
			}
			
			public void updateObjects(LinkedList<GameObject> list) {
				Set<GameObject> temp = new HashSet<>();
				for (GameObject item: list) {
					//next line is super important! it's what will stay when you leave
					if (item.getID() != ID.Chakra && item.getID() != ID.Player) {
						temp.add(item);
					}
				}
				currObjects = temp;
			}
			
			public int[] getPosition() {
				return position;
			}
			
		}

}
