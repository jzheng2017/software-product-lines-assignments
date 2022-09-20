package spl;

import spl.services.ChatService;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;



public class CLI {

        // Connect status constants
        final static int DISCONNECTED = 0;
        final static int BEGIN_CONNECT = 1;
        final static int CONNECTED = 2;
        private static final Logger logger = Logger.getLogger(CLI.class.getName());
        private static ChatService chatService;

        // Connection info
        public static String hostIP = "localhost";
        public static int port = 1234;
        public static int connectionStatus = DISCONNECTED;
        public static boolean isHost = true;
        public static Client user = new Client(hostIP, port, "Bob");
        public static String usernameColor = "Red";
    
        public CLI(ChatService cs){
            chatService = cs;
            initCLI();
        }

        public void initCLI(){
            user.connect();
            Scanner input = new Scanner(System.in);
            while(!Client.IS_AUTHENTICATED){
                String message = input.nextLine();
                user.sendMessage(message);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    logger.log(Level.WARNING, "Could not update chat", e);
                }
            }
            input.close();
            updateChat();
        }

        public static void updateChat() {
            while (true) {
                if (Client.IS_AUTHENTICATED) {
                    System.out.print("\n---- CHAT START ----\n");
                    List<String> chatLines = chatService.readAll();
                    System.out.print(String.join("\n", chatLines));
                    System.out.print("\n---- CHAT END ----\n");
                } else {
                    logger.log(Level.WARNING, "You are not authenticated to read the chat logs!");
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    logger.log(Level.WARNING, "Could not update chat", e);
                }
            }
        }

}
