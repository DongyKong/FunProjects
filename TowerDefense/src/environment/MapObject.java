package environment;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

//	Parent class for anything on the map
public abstract class MapObject extends JPanel {
	private BufferedImage myImage;
	
	public MapObject() {}
	
	public MapObject(BufferedImage img) {
		myImage = img;
	}
}
