package towers;

import javax.swing.JPanel;

import environment.HealthBar;

public abstract class Tower extends JPanel implements Runnable {
	private int			xCoord, yCoord;
	private int			damagePerShot;
	private int			fireRate;
	private int			range;
	private int			cost;
	private boolean		special;
	
	private HealthBar	health;
	private Attack		shot;
}
