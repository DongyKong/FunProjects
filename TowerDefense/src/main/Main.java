package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.MapDatabase.Map;
import monsters.MonsterFactory;
import environment.Bank;
import environment.Entrance;
import environment.Exit;
import environment.Land;
import towers.Tower;

/**
 * TODO: 	START TIME = 5:10
 * 			END TIME =
 * @author Eric Dong
 *
 */

public class Main extends JFrame implements Runnable {
	/*******************	CONSTANTS AND STATICS	*******************/
	private static final long serialVersionUID = 1L;
	
	public final static int gameWindowHeight = 700;
	public final static int gameWindowWidth = 1000;
	public final static int gameGridDim = 25;
	
	public final static String mapDataFile = "data/maps.txt";
	
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
	private MapDatabase				mapDatabase;
	
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
		
		mapDatabase = new MapDatabase();
		mapDatabase.start();
		
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
		
		this.add(sidePanel, BorderLayout.EAST);
		this.add(gamePanel, BorderLayout.CENTER);
		
		this.setSize(gameWindowWidth, gameWindowHeight);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		for(Map map; (map = mapDatabase.getMap()) != null;)
			map.printMap();
	}

	/*******************	HELPER FUNCTIONS	*******************/
	
	
	
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
