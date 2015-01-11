package environment;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import main.MapDatabase;

public class Obstacle extends Land {
	public Obstacle() {
		try {
			myImage = ImageIO.read(new File(MapDatabase.rockImg));

			JLabel label = new JLabel(new ImageIcon(myImage));
			this.add(label);
		}
		catch (IOException e) { e.printStackTrace(); }
	}
}
