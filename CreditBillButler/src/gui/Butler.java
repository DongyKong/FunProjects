package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

//	TODO: add proper filtering for column headers

/**
 * Utility to facilitate transaction verification for Redstone credit card statements
 * @author Eric Dong
 *
 */

@SuppressWarnings("serial")
public class Butler extends JFrame {
	public static final String		DATA_FILE = "transactions.txt";
	
	private JTextField				searchBar;
	private JTable					table;
	private Object[][]				transactions;
	private ButlerWindowListener	listener;
	
	public Butler(Object[][] transactions) {
		//	Error check purchases
		boolean check = true;
		for(int i = 0; i < transactions.length; i++) {
			if(transactions[i].length == 0)
				check = false;
		}
		if(transactions.length == 0 || !check) {
			System.err.println("Error: Unrecognized format in text file.");
			System.exit(1);
		}
		
		//	Create text field
		searchBar = new JTextField();
		searchBar.setColumns(30);
		
		JPanel searchPanel = new JPanel();
		searchPanel.add(searchBar);
		searchPanel.setOpaque(true);
		searchPanel.setBackground(Color.DARK_GRAY);
		
		//	Create table
		Object[] headers = {"Verified", "Transaction Date", "Post Date", "Type", "Amount", "Description"};
		this.transactions = new Object[transactions.length][transactions[0].length];
		
		double totalDebt = 0;
		for(int i = 0; i < this.transactions.length; i++) {
			for(int j = 0; j < this.transactions[i].length; j++) {
				if(transactions[i][j].toString().equals("**false"))			//	Verification column
					this.transactions[i][j] = false;
				else if(transactions[i][j].toString().equals("**true"))		//	Verification column
					this.transactions[i][j] = true;
				else
					this.transactions[i][j] = transactions[i][j];
				
				//	Add up the amounts
				if(j == 4) {
					String amount = this.transactions[i][j].toString();
					
					if(amount.charAt(0) == '$')
						totalDebt += Double.parseDouble(amount.substring(1));
					else
						totalDebt += Double.parseDouble(amount.substring(0));
				}
			}
		}
		
		DefaultTableModel model = new DefaultTableModel(this.transactions, headers);
		table = new JTable(model) {
			@Override
			public Class<?> getColumnClass(int column) {
				switch(column) {
				case 0:
					return Boolean.class;	//	Tells table to display a check box
				default:
					return String.class;
				}
			}
		};
		
		//	Customize initial column widths
		for(int i = 0; i < 6; i++) {
			TableColumn col = table.getColumnModel().getColumn(i);
			
			int pref = 0;
			switch(i) {
			case 0:
				pref = 50;
				break;
			case 1:
				pref = 100;
				break;
			case 2:
				pref = 100;
				break;
			case 3:
				pref = 70;
				break;
			case 4:
				pref = 70;
				break;
			case 5:
				pref = 400;
				break;
			}
			
			col.setPreferredWidth(pref);
		}
		
		//	Finalize the table
		table.setAutoCreateRowSorter(true);
		table.setBackground(Color.DARK_GRAY);
		table.setForeground(Color.WHITE);
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.add(table.getTableHeader(), BorderLayout.PAGE_START);
		tablePanel.add(table, BorderLayout.CENTER);
		
		JScrollPane jsp = new JScrollPane(tablePanel);
		
		this.add(searchPanel, BorderLayout.NORTH);
		this.add(jsp, BorderLayout.CENTER);
		
		//	Display the total debt
		searchBar.setText("Total amount owed: $" + totalDebt);
		
		//	Set up the window
		this.pack();
		this.setLocationRelativeTo(null);
		this.getContentPane().setBackground(Color.DARK_GRAY);
		this.setVisible(true);
		
		//	Attach window to the window listener
		listener = new ButlerWindowListener(this);
		this.addWindowListener(listener);
	}
	
	public JTable getJTable() { return table; }
	
	public static void main(String[] args) {
		
		File f = new File(DATA_FILE);
		if(!f.exists()) {
			System.err.println("Error: " + DATA_FILE + " not found.");
			System.exit(1);
		}
		
		BufferedReader br = null;
		try {
			//	Read from file and store transactions
			br = new BufferedReader(new FileReader(f));
			List<String[]> transactions = new ArrayList<String[]>();
			
			for(String line; (line = br.readLine()) != null;) {
				String[] split = line.split("\\t");
				List<String> conv = Arrays.asList(split);
				ArrayList<String> list = new ArrayList<String>(conv);
				
				//	Check if verification has not been provided
				if(!split[0].substring(0, 2).equals("**"))
					list.add(0, "**false");
				
				//	In case type and amount are stuck together
				String type = "", amount = "";
				int typeI = 3, amountI = 4;
				if(list.get(typeI).contains("Purchase") && list.get(typeI).length() > 8) {
					type = list.get(typeI).substring(0, 8);
					amount = list.get(typeI).substring(8);
					
					list.remove(typeI);
					list.add(typeI,  type);
					list.add(amountI, amount);
				}
				if(list.get(typeI).contains("PaymentCredit") && list.get(typeI).length() > 13) {
					type = list.get(typeI).substring(0, 13);
					amount = list.get(typeI).substring(13);
					
					list.remove(typeI);
					list.add(typeI,  type);
					list.add(amountI, amount);
				}
				
				transactions.add(list.toArray(new String[list.size()]));
			}
			
			//	Convert to 2D array
			Object[][] array = new Object[transactions.size()][];
			for(int i = 0; i < array.length; i++)
				array[i] = transactions.get(i);
			
			new Butler(array);
		}
		catch(IOException e) { e.printStackTrace(); }
	}
}
