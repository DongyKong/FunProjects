package gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

/**
 * Keeps an ear on the close button and saves any changes to file before shutting down
 * @author Eric Dong
 *
 */
public class ButlerWindowListener implements WindowListener {
	
	private Butler frame;
	
	public ButlerWindowListener(Butler frame) {
		this.frame = frame;
	}
	
	@Override
	public void windowOpened(WindowEvent e) {}
	
	//	Save the state before closing
	@Override
	public void windowClosing(WindowEvent e) {
		
		TableModel model = frame.getJTable().getModel();
		String[][] data = new String[model.getRowCount()][model.getColumnCount()];
		
		//	Store in 2D array
		for(int i = 0; i < model.getRowCount(); i++) {
			for(int j = 0; j < model.getColumnCount(); j++) {
				if(j == 0) {
					//	Insert the ** in front of verification
					String line = model.getValueAt(i, j).toString();
					line = "**" + line;
					data[i][j] = line;
				}
				else
					data[i][j] = model.getValueAt(i, j).toString();
			}
		}
		
		//	Overwrite text file
		File f = new File(Butler.DATA_FILE);
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(f), true);
			
			for(int i = 0; i < data.length; i++) {
				for(int j = 0; j < data[i].length; j++) {
					pw.print(data[i][j] + "\t");
				}
				pw.println();
			}
		}
		catch(IOException ioE) { ioE.printStackTrace(); }
		
		JOptionPane.showMessageDialog(null, "Your changes have been saved!");
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}
}
