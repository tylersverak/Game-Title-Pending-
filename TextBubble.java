import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class TextBubble extends GameObject implements Comparable<GameObject>{
	
	private String toPrint;
	private static final int lineSpeed = 20;
	private int index, lineLength, timer;
	private Queue<String> wordQueue;
	private String[] displayedWords;
	
	//constructs a TextBubble, a rectangle at (xText, yText) with width and height
	//as its dimensions, and a text it will produce
	public TextBubble(String text, int xText, int yText, ID id, int width, int height, int timer) throws IllegalArgumentException{
		super(xText, yText, id, width, height);
		if (text.length() == 0) { //has to have some text duh
			throw new IllegalArgumentException();
		}
		this.timer = timer;
		this.toPrint = text;
		this.lineLength = (width/13 - 1);
		this.index = 0;
		this.displayedWords = new String[(height-10)/30];
		for(int j = 0; j < displayedWords.length; j++) {
			displayedWords[j] = "";
		}
		wordQueue = new LinkedList<String>(); //creates an arraylist for the parts of the String
		initializeWordQueue(new Scanner(toPrint));
	}
	
	//renders each part of the text box
	public void render(Graphics g) {
		if (timer >= 0) { //only renders while the timer is active
			
			g.setColor(Color.WHITE); //renders the box and details to make it look pretty
			g.fillRect(x, y, width, height);
			g.setColor(Color.PINK);
			g.drawRect(x, y, width, height);
			int radius = 10;
			g.fillOval(x-radius, y-radius, radius*2, radius*2); //patch this up later
			g.fillOval(x+width-radius, y-radius, radius*2, radius*2);
			g.fillOval(x-radius, y+height-radius, radius*2, radius*2);
			g.fillOval(x+width-radius, y+height-radius, radius*2, radius*2);
			
			g.setColor(new Color(60, 6, 180)); //renders the text
		    g.setFont(new Font("Dialog", Font.ITALIC, 25));
		    for(int i = 0; i < displayedWords.length; i++) {
		    	String toDisplay = displayedWords[i].trim();
		    	if (toDisplay.length() == lineLength && Character.isLetter(toDisplay.charAt(toDisplay.length() - 1))) {
		    		toDisplay += "-";
		    	}
			    g.drawString(toDisplay , x+10, y+(30*(i+1)));
		    }
		}
	}
	
	//updates the TextBubble
	public void tick() {
		//increase index every few frames to include the next letter from String text
		//if (index < trueLength(wordQueue) && timer % lineSpeed == 0) {
		if (index < toPrint.length()) {
			if (timer % lineSpeed == 0) {
				updateWordDisplay(toPrint, index, displayedWords);
				index++;
			}
		}else{
			timer--;
		}
	}
	
	//intializes the wordQueue object, used only internally
	private void initializeWordQueue(Scanner s) {
		while (s.hasNext()) {
			wordQueue.add(s.next());
		}
	}
	
	//another attempt
	private void updateWordDisplay(String text, int index, String[] displayed) {
		insertLetter(text.charAt(index), displayed);
	}
	
	//inserts the character at the end of the displayedWords
	private void insertLetter(Character c, String[] array) {
		//inserts the next character
		for(int i = 0; i < array.length; i++) {
				if(array[i].length() < lineLength) {
					array[i] += c;
					return;
				}
		}
		
		for(int i = 0; i < array.length - 1; i++) {
			array[i] = array[i + 1];
		}
		array[array.length - 1] = "";
		insertLetter(c, array);
	}
	
	public Rectangle getBounds() {
		return new Rectangle(0, 0, 0, 0);
	}
}
