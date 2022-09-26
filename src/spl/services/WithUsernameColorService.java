package spl.services;

public class WithUsernameColorService implements ColorService{

    @Override
    public String sendMessage(String color, String msg) {
        return "[" + color + "]: " + msg;
    }
    
}
