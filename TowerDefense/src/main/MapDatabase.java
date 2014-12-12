package main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class MapDatabase extends Thread {
	/*******************	CONSTANTS AND STATICS	*******************/
	
	public final static int totalNumMaps = 100;
	
	public static int nextMapID = 0;
	
	/*******************	CLASS VARIABLES		*******************/
	
	public ArrayList<Map> allMaps;
	
	/*******************	CLASS METHODS		*******************/

	public MapDatabase() {
		allMaps = new ArrayList<Map>();
	}
	
	//	Load the maps from file
	@SuppressWarnings("unchecked")
	public void loadMaps() {
		try (InputStream file = new FileInputStream(Main.mapDataFile);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer); )
		{
			allMaps = (ArrayList<Map>)input.readObject();
		}
		catch(ClassNotFoundException e) { e.getMessage(); }
		catch(ClassCastException e) { e.getMessage(); }
		catch(IOException e) { e.getMessage(); }
	}
	
	//	Writes all the maps to file
	public void writeMaps() {
		try (OutputStream file = new FileOutputStream(Main.mapDataFile);
				OutputStream buffer = new BufferedOutputStream(file);
				ObjectOutput output = new ObjectOutputStream(buffer); )
			{
				output.writeObject(allMaps);
			}
			catch(IOException e) { e.getMessage(); }
	}
	
	//	Return a random map for use then remove from database
	public Map getMap() {
		if(allMaps.size() == 0)
			return null;
		
		int roll = getRandomNumber(allMaps.size() - 1);
		Map map = allMaps.get(roll);
		allMaps.remove(roll);
		return map;
	}
		
	//	Generate a random map
	public char[][] generateMap() {
		/**	KEY:
		 * 		"-" - GROUND
		 * 		P - PATH
		 * 		O - OBSTACLE
		 * 		S - START
		 * 		F - FINISH
		 */
		
		int[] start = new int[2];
		
		boolean startOver = false;	//	In case generated dead end
		
		char[][] grid = new char[Main.gameGridDim][Main.gameGridDim];
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[i].length;j++)
				grid[i][j] = '-';
		
		/**	Create the path first from screen left to right:
		 * 		Build straight: 0-60
		 * 		Build left:		61-80
		 * 		Build right:	81-100 
		 */
		int x = (int)Math.floor(Math.random()*Main.gameGridDim);
		int y = 0;
		start[0] = x; start[1] = y;
		int[] curr = { x, 0 };
		int[] prev = { x, -1 };
		grid[curr[0]][curr[1]] = 'P';	//	Start point
		
		//	Build path until reached right side
		boolean justTurned = false;	//	Go at least 2 spaces before turning again
		while(curr[1] < Main.gameGridDim-1) {
			
			if(isDeadEnd(grid, curr)) {
				startOver = true;
				break;
			}
			
			//	Roll to decide which way to build
			int roll = getRandomNumber(100);
			
			//	Figure out orientation
			int[] straight = new int[2];
			int[] left = new int[2];
			int[] right = new int[2];
			
			if(prev[1] < curr[1]) {
				//	Straight is to the right
				straight[0] = 0; straight[1] = 1;
				left[0] = -1; left[1] = 0;
				right[0] = 1; right[1] = 0;
			}
			else if(prev[1] == curr[1]) {
				if(prev[0] > curr[0]) {
					//	Straight is up
					straight[0] = -1; straight[1] = 0;
					left[0] = 0; left[1] = -1;
					right[0] = 0; right[1] = 1;
				}
				else {
					//	Straight is down
					straight[0] = 1; straight[1] = 0;
					left[0] = 0; left[1] = 1;
					right[0] = 0; right[1] = -1;
				}
			}
			else {
				//	Straight is to the left
				straight[0] = 0; straight[1] = -1;
				left[0] = 1; left[1] = 0;
				right[0] = -1; right[1] = 0;
			}
			
			x = curr[0];
			y = curr[1];
			
			//	Set so it's more likely to turn towards the right of the screen
			int goLeft = 80;
			if(left[0] == 0 && left[1] == -1) {
				//	Turning right takes you to the right
				goLeft -= 10;
			}
			else if(left[0] == 0 && left[1] == 1)
				goLeft += 10;
			
			//	Build straight
			if(roll <= 60) {
				justTurned = false;
				curr[0] += straight[0];
				curr[1] += straight[1];
				
				if(checkOutOfBounds(curr) || grid[curr[0]][curr[1]] == 'P') {
					curr[0] -= straight[0];
					curr[1] -= straight[1];
					continue;
				}
			}
			//	Build left
			else if(roll <= goLeft) {
				if(justTurned)
					continue;
				
				curr[0] += left[0];
				curr[1] += left[1];
				
				if(checkOutOfBounds(curr) || grid[curr[0]][curr[1]] == 'P') {
					curr[0] -= left[0];
					curr[1] -= left[1];
					continue;
				}
				
				justTurned = true;
			}
			//	Build right
			else {
				if(justTurned)
					continue;
				
				curr[0] += right[0];
				curr[1] += right[1];
				
				if(checkOutOfBounds(curr) || grid[curr[0]][curr[1]] == 'P') {
					curr[0] -= right[0];
					curr[1] -= right[1];
					continue;
				}
				
				justTurned = true;
			}
			
			//	We good
			prev[0] = x;
			prev[1] = y;
			grid[curr[0]][curr[1]] = 'P';
		}
		
		if(startOver) {
			return generateMap();	//	Do it over again
		}
		else {
			grid[start[0]][start[1]] = 'S';
			grid[curr[0]][curr[1]] = 'F';
			
			//	Add random obstacles
			for(int i = 0; i < grid.length; i++) {
				for(int j = 0; j < grid[i].length; j++) {
					if(grid[i][j] == '-') {
						int roll = getRandomNumber(100);
						if(roll < 5)
							grid[i][j] = 'O';
					}
				}
			}
			
			return grid;
		}
	}
	
	//	Keep track of the number of maps and keep them at totalNumMaps
	@Override
	public void run() {
		loadMaps();
		
		while(true) {
			if(allMaps.size() < totalNumMaps) {
				//	Generate a new map
				char[][] map = generateMap();
				allMaps.add(new Map(map, nextMapID));
				nextMapID++;
			}
		}
	}
	
	/*******************	HELPER METHODS	*******************/
	
	//	Gets a rando number from 0 to max
	private int getRandomNumber(int max) {
		return (int)Math.ceil(Math.random()*max); 
	}
	
	//	Check if a coordinate is out of game grid bounds
	private boolean checkOutOfBounds(int[] coord) {
		if(coord[0] < 0 || coord[1] < 0)
			return true;
		if(coord[0] >= Main.gameGridDim || coord[1] >= Main.gameGridDim)
			return true;
	
		return false;
	}
	
	//	Check if map generated a dead end
	private boolean isDeadEnd(char[][] grid, int[] coord) {
		int bad = 0;
		
		//	Check for boundaries and paths/obstacles
		coord[0] -= 1;
		if(checkOutOfBounds(coord))
			bad++;
		else if(grid[coord[0]][coord[1]] != 'G' && grid[coord[0]][coord[1]] != '-')
			bad++;
		
		coord[0] += 1;
		coord[1] -= 1;
		if(checkOutOfBounds(coord))
			bad++;
		else if(grid[coord[0]][coord[1]] != 'G' && grid[coord[0]][coord[1]] != '-')
			bad++;
		
		coord[1] += 2;
		if(checkOutOfBounds(coord))
			bad++;
		else if(grid[coord[0]][coord[1]] != 'G' && grid[coord[0]][coord[1]] != '-')
			bad++;
		
		coord[0] += 1;
		coord[1] -= 1;
		if(checkOutOfBounds(coord))
			bad++;
		else if(grid[coord[0]][coord[1]] != 'G' && grid[coord[0]][coord[1]] != '-')
			bad++;
		
		coord[0] -= 1;
		
		return (bad < 4 ? false : true);
	}
	
	/*******************	INNER CLASSES		*******************/
	
	//	Wrapper class for the map char grid
	public class Map implements Serializable {
		private static final long serialVersionUID = -5909161986357882140L;
		
		private int ID;
		private char[][] map;
		
		public Map(char[][] map, int ID) {
			this.map = map; 
			this.ID = ID;
		}
		
		//	Print the map to screen
		public void printMap() {
			for(int i = 0; i < map.length; i++) {
				for(int j = 0; j < map[i].length; j++)
					System.out.print(map[i][j] + " ");
				System.out.println();
			}
			System.out.println();System.out.println();
		}
		
		public char[][] getMap() { return map; }
		
		public int getID() { return ID; }
	}
}
