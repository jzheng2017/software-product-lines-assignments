package spl;

import spl.services.ChatService;
import spl.services.MessageTransformer;

import java.util.List;
import java.util.Scanner;


public class CLI extends Interface {

    public CLI(ChatService cs, MessageTransformer messageTransformer) {
        super(cs, messageTransformer);
        initCLI();
    }

    @Override
    public void init() {
        initCLI();
    }

    @Override
    public void update() {
        updateChat();
    }

    private void initCLI() {
        user.connect();
        Scanner input = new Scanner(System.in);
        while (!Client.IS_AUTHENTICATED) {
            String message = input.nextLine();
            user.sendMessage(message);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                logger.write("Could not update chat. Cause: " + e.getMessage());
            }
        }
        input.close();
        updateChat();
    }

    private void updateChat() {
        while (true) {
            if (Client.IS_AUTHENTICATED) {
                System.out.print("\n---- CHAT START ----\n");
                List<String> chatLines = chatService.readAll();
                System.out.print(String.join("\n", chatLines));
                System.out.print("\n---- CHAT END ----\n");
            } else {
                logger.write("You are not authenticated to read the chat logs!");
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                logger.write("Could not update chat. Cause: " + e.getMessage());
            }
        }
    }

}
