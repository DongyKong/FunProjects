package environment;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

//	Parent class for anything on the map
public abstract class MapObject extends JPanel {
	protected BufferedImage myImage;
	
	public MapObject() {
		this.setLayout(new GridLayout(1, 1));
	}
	
	public MapObject(BufferedImage img) {
		myImage = img;
	}
}
