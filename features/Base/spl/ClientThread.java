package spl; 

//import spl.services.ConsoleLogService; 
import spl.services.LogService; 

import java.io.BufferedReader; 
import java.io.InputStream; 
import java.io.InputStreamReader; 
import java.net.Socket; 

public  class  ClientThread  implements Runnable {
	
    private final Socket skt;

	
    private final LogService logger = new ConsoleLogService();

	
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

                logger.write("Server message: " + message);
            } while (skt.isConnected());

        } catch (Exception e) {
            logger.write(e.getMessage());
        }
    }


}
