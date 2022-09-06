package spl;

import spl.services.ChatService;
import spl.services.FileLogService;

import java.awt.*;
import java.io.*;
import java.net.*;
 

public class ServerThread extends Thread {
    private Socket skt;
    private ChatService chatService;

    public ServerThread(Socket socket) {
        skt = socket;
        chatService = new ChatService(new FileLogService());
    }
 
    public void run() {
        try {
            InputStream input = skt.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
            OutputStream output = skt.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
 
            String message;
 
            do {
            	message = reader.readLine();
                writer.println("Je bericht is ontvangen");
                System.out.println("Bericht ontvangen: " + message);
                chatService.sendMessage(message + "\n");
            } while (skt.isConnected());
            
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}