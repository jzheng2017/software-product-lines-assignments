package spl;

import spl.services.AuthenticationService;
import spl.services.ChatService;
import spl.services.FileLogService;
import spl.services.PasswordAuthenticationService;
import spl.services.SimpleEncryptionService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;


public class ServerThread extends Thread {
    private final Socket skt;
    private final ChatService chatService;
    private final AuthenticationService authenticationService;

    public ServerThread(Socket socket) {
        skt = socket;
        chatService = new ChatService(new FileLogService(), new SimpleEncryptionService());
        authenticationService = new PasswordAuthenticationService();
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

                if (message == null) {
                    skt.close();
                    break;
                }

                if (message.contains("/auth")) {
                    message = message.substring(message.indexOf("/auth") + "/auth".length()).trim();
                    writer.println(authenticationService.authenticate(message));
                    continue;
                }

                writer.println("Message received");
                System.out.println("Received message: " + message);
                chatService.sendMessage(message + "\n");
            } while (!skt.isClosed());

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}