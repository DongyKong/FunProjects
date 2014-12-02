package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import monsters.MonsterFactory;
import environment.Bank;
import environment.Entrance;
import environment.Exit;
import environment.Land;
import towers.Tower;

public class Main extends JFrame implements Runnable {
	/*******************	CONSTANTS AND STATICS	*******************/
	public static int gameWindowHeight = 700;
	public static int gameWindowWidth = 1000;
	public static int gameGridDim = 50;
	
	private static ArrayList<Event> allEvents;
	
	/*******************	CLASS VARIABLES		*******************/
	
	private ArrayList<Tower> 		allTowers;
	private ArrayList<Land>			allLand;
	
	private JPanel					gamePanel;
	private JPanel					sidePanel;
	private JPanel					towerPanel;
	private JPanel					infoPanel;
	private JFrame 					startMenu;
	
	private MonsterFactory			monsterFactory;
	private Bank					bank;
	private Entrance				entrance;
	private Exit					exit;
	
	/*******************	CLASS METHODS		*******************/
	
	public Main() {
		super("Defense of the Donger");
		
		allTowers 	= new ArrayList<Tower>();
		allEvents 	= new ArrayList<Event>();
		allLand 	= new ArrayList<Land>();
		gamePanel	= new JPanel(new GridLayout(gameGridDim, gameGridDim));
		sidePanel	= new JPanel(new GridLayout(2, 1));
		towerPanel	= new JPanel(new GridLayout(6, 1));
		infoPanel	= new JPanel(new GridLayout(2, 1));
		
		//	Initialize the game
		initGame();
	}
	
	//	Start menu
	public void initGame() {
		startMenu = new JFrame("Defense of the Donger");
		startMenu.setLayout(new GridLayout(3, 1));

		JButton button = new JButton("New Game");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				startMenu.dispose();
				runGame();
			}
		});
		startMenu.add(button);
		
		button = new JButton("About");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				JOptionPane.showMessageDialog(null, "Created by Eric Dong\nUniversity of Southern California\nBalls");
			}
		});
		startMenu.add(button);
		
		button = new JButton("Quit");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		});
		startMenu.add(button);
		
		startMenu.setSize(250, 400);
		startMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startMenu.setLocationRelativeTo(null);
		startMenu.setVisible(true);
	}
	
	//	Run the game
	public void runGame() {
		sidePanel.setPreferredSize(new Dimension(100, gameWindowHeight));
		sidePanel.add(towerPanel);
		sidePanel.add(infoPanel);
		
		//	Create the map on gamePanel
		for(int x = 0; x < 10; x++) {
			char[][] grid = generateMap();
			System.out.println("\nI'M DONE!!!");
			for(int i = 0; i < grid.length; i++) {
				for(int j = 0; j < grid[i].length; j++)
					System.out.print(grid[i][j] + " ");
				System.out.println();
			}
		}
		
		this.add(sidePanel, BorderLayout.EAST);
		this.add(gamePanel, BorderLayout.CENTER);
		
		this.setSize(gameWindowWidth, gameWindowHeight);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	/*******************	HELPER FUNCTIONS	*******************/
	
	//	Generate a random map
	private char[][] generateMap() {
		/**	KEY:
		 * 		G - GROUND
		 * 		P - PATH
		 * 		O - OBSTACLE
		 * 		S - START
		 * 		F - FINISH
		 */
		
		int[] start = new int[2];
		
		boolean startOver = false;	//	In case generated dead end
		
		char[][] grid = new char[gameGridDim][gameGridDim];
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[i].length;j++)
				grid[i][j] = '-';
		
		/**	Create the path first from screen left to right:
		 * 		Build straight: 0-60
		 * 		Build left:		61-80
		 * 		Build right:	81-100 
		 */
		int x = (int)Math.floor(Math.random()*gameGridDim);
		int y = 0;
		start[0] = x; start[1] = y;
		int[] curr = { x, 0 };
		int[] prev = { x, -1 };
		grid[curr[0]][curr[1]] = 'P';	//	Start point
		
		//	Build path until reached right side
		boolean justTurned = false;	//	Go at least 2 spaces before turning again
		while(curr[1] < gameGridDim-1) {
			
			try {
				Thread.sleep(200);
			}
			catch(InterruptedException e) { e.printStackTrace(); }
			
			if(isDeadEnd(grid, curr)) {
				startOver = true;
				break;
			}
			
			//	Roll to decide which way to build
			int roll = (int)Math.ceil(Math.random()*100);
			
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
			else if(roll <= 80) {
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
			
			//	PRINT FOR DEBUGGING
//			for(int i = 0; i < grid.length; i++) {
//				for(int j = 0; j < grid[i].length; j++)
//					System.out.print(grid[i][j] + " ");
//				System.out.println();
//			}
//			System.out.println();System.out.println();
		}
		
		if(startOver) {
			System.out.println("I FUCKED UP");
			return generateMap();	//	Do it over again
		}
		else {
			grid[start[0]][start[1]] = 'S';
			grid[curr[0]][curr[1]] = 'F';
			return grid;
		}
	}
	
	//	Check if a coordinate is out of game grid bounds
	private boolean checkOutOfBounds(int[] coord) {
		if(coord[0] < 0 || coord[1] < 0)
			return true;
		if(coord[0] >= gameGridDim || coord[1] >= gameGridDim)
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
	
	/*******************	STATIC METHODS		*******************/
	
	public static void main(String[] args) {
		new Main();
	}
	
	//	Add event to event queue
	public static void addEvent(Event e) {
		allEvents.add(e);
	}
	
	//	Deals with all game events
	@Override
	public void run() {
		
	}
}
