package spl;

import java.io.*;
import java.net.*;
 

public class ServerThread extends Thread {
    private Socket skt;
 
    public ServerThread(Socket socket) {
        skt = socket;
    }
 
    public void run() {
        try {
            InputStream input = skt.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
            OutputStream output = skt.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
 
            String text;
 
            do {
                text = reader.readLine();
                writer.println("Je bericht is ontvangen");
                System.out.println("Bericht ontvangen: " + text);
 
            } while (skt.isConnected());
 
            
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}