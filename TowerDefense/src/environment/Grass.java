package environment;

import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import main.MapDatabase;

public class Grass extends Land {
	public Grass() {
		super();
		try {
			myImage = ImageIO.read(new File(MapDatabase.grassImg));
			
			JLabel label = new JLabel(new ImageIcon(myImage));
			this.add(label);
		}
		catch (IOException e) { e.printStackTrace(); }
	}
}
