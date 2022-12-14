package spl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread implements Runnable {
    private final Socket skt;

    public ClientThread(Socket socket) {
        skt = socket;
    }

    public void run() {
        try {
            InputStream input = skt.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String message;

            do {
                message = reader.readLine();
                if (message.equals("true")) {
                    Client.IS_AUTHENTICATED = true;
                }
                System.out.println("Server message: " + message);

            } while (skt.isConnected());

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
