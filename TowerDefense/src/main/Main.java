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
		gamePanel	= new JPanel(new GridLayout(100, 100));
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
		sidePanel.setPreferredSize(new Dimension(100, 700));
		sidePanel.add(towerPanel);
		sidePanel.add(infoPanel);
		
		this.add(sidePanel, BorderLayout.EAST);
		this.add(gamePanel, BorderLayout.CENTER);
		
		this.setSize(1000, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
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
