package spl;

import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.logging.*;

public class Main {

	private static String InstanceType;
    public static void main(String[] args) 
	{
		
	    // Get the instance type from the arguments
	    if(args.length == 1)
		{
			String tempArg = args[0].toLowerCase();
			if(tempArg.equals("client") || tempArg.equals("server"))
			{
				InstanceType = tempArg;	
			}
			else
			{
				InstanceType = "client";
			}
		}
	    else // Assume its a client
	    {
			InstanceType = "client";
	    }
        System.out.println("Instance Type : " + InstanceType);
		// Set up the logger
		Logger logger = Logger.getLogger("Logger");
    	FileHandler fh;  
    	try {  
	        // This block configure the logger with handler and formatter  
			fh = new FileHandler(InstanceType + ".log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();  
			fh.setFormatter(formatter);  
			// the following statement is used to log any messages : logger.info("My first log");
		} catch (SecurityException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}
		// Start server/client
		if(InstanceType.equals("server"))
		{
			while(true)
			{
				try {
					ServerSocket srvr = new ServerSocket(1234);
					Socket skt = srvr.accept();
					System.out.print("Server has connected!\n");
					// Wait for client messages
					BufferedReader in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
					while (!in.ready()) {}
					String readText = in.readLine();
					// Send message to clients
					PrintWriter out = new PrintWriter(skt.getOutputStream(), true);
					System.out.print("Sending string: '" + readText + "'\n");
					out.print(readText);
					// Log message
					out.close();
					skt.close();
					srvr.close();
				}
				catch(Exception e) {
					System.out.print("Error occurred in server\n");
				}
			}
		}
		else if(InstanceType.equals("client"))
		{
			while(true)
			{
				try 
				{
					Socket skt = new Socket("localhost", 1234);
					BufferedReader in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
					// Send message
					PrintWriter out = new PrintWriter(skt.getOutputStream(), true);
					BufferedReader reader = new BufferedReader( new InputStreamReader(System.in)); // Console reader
					System.out.println("Reading user input");
					String readText = reader.readLine();
					// Apply "Encryption"
					readText = encrypt(readText);
					out.print(readText);
					System.out.print("Sent string: " + readText);
					// Recieve messages
					while (!in.ready()) {}
					System.out.println(in.readLine()); // Read one line and output it
					System.out.print("'\n");
					in.close();
				}
				catch(Exception e) {
					System.out.print("Error occurred in client\n");
				}
			}
		}
    }
	
	private static String encrypt(String aString)
	{
		aString = reverse(aString);
		aString = rot13(aString);
		return aString;
	}

	private static String decrypt(String aString)
	{
		aString = rot13(aString);
		aString = reverse(aString);
		return aString;
	}

	public static String reverse(String aString)
	{
		return new StringBuffer(aString).reverse().toString();
	}

	public static String rot13(String input) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if       (c >= 'a' && c <= 'm') c += 13;
			else if  (c >= 'A' && c <= 'M') c += 13;
			else if  (c >= 'n' && c <= 'z') c -= 13;
			else if  (c >= 'N' && c <= 'Z') c -= 13;
			sb.append(c);
		}
		return sb.toString();
	}

	public static void log(String aString)
	{
		logger.info(aString);
	}
}
