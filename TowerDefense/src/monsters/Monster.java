package monsters;

import javax.swing.JPanel;
import javax.swing.Timer;

public abstract class Monster extends JPanel implements Runnable {
	private int			xCoord, yCoord;
	private int			health;
	private int			speed;
	private int			reward;
	private Timer		timer;
}
