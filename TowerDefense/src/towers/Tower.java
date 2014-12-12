package towers;

import javax.swing.JPanel;

import environment.HealthBar;
import environment.MapObject;

public abstract class Tower extends MapObject implements Runnable {
	private int			xCoord, yCoord;
	private int			damagePerShot;
	private int			fireRate;
	private int			range;
	private int			cost;
	private boolean		special;
	
	private HealthBar	health;
	private Attack		shot;
}
