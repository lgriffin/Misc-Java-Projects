package main;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import imp.im.ImpSession;
import imp.im.ImpSession.PresenceType;
import imp.xmpp.XmppSession;

public class Start {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String server = "10.37.84.218";
		int port = 5222;
		String username = "a";
		String password = "a";
		
		ImpSession session = new XmppSession();
		
		try {
			session.connect(server, port);
			System.out.println("Connected!");
			
			session.authenticate(username, password);
			
			System.out.println("Logged in successfully");
			
			String presenceMessage = "initial presence (5s)";
			
			session.setPresence(PresenceType.AVAILABLE, presenceMessage);
			
			Thread.sleep(5000);
			System.out.println("Sleep for 5 seconds so it shows");
			
			while(!presenceMessage.equals("end"))
			{
				presenceMessage = fileRead();
				session.setPresence(PresenceType.AVAILABLE, presenceMessage);
				
				if(!presenceMessage.equals("end"))
			{
					Thread.sleep(10000);
			}
				
			}
			session.setPresence(PresenceType.AVAILABLE, "End was read in...exiting!!!");
			
			session.disconnect();
			
			System.out.println("Logging out!");
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		
		
		

	}
	
	public static String fileRead() throws IOException
	{
		
	//	File file = new File("test.txt");
		File file = new File("../simpleBundle/test.txt");
	    FileInputStream fis = new FileInputStream(file);
	    BufferedInputStream bis = new BufferedInputStream(fis);
	    DataInputStream dis = new DataInputStream(bis);
	    String line = "";
	   
	      while (dis.available() != 0) {

	      // this statement reads the line from the file and print it to
	        // the console.
	       line = dis.readLine(); 
	       
	      }

	      // dispose all the resources after using them.
	      fis.close();
	      bis.close();
	      dis.close();
	      
	      return line;
	}

}
