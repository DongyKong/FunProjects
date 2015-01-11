package environment;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import main.MapDatabase;
import monsters.MonsterFactory;

public class Entrance extends Gate {
	private MonsterFactory monsterFactory;
	
	public Entrance() {
		try {
			myImage = ImageIO.read(new File(MapDatabase.bluePortalImg));

			JLabel label = new JLabel(new ImageIcon(myImage));
			this.add(label);
		}
		catch (IOException e) { e.printStackTrace(); }
	}
}
