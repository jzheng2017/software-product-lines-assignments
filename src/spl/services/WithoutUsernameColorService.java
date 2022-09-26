package spl.services;

public class WithoutUsernameColorService implements ColorService{

    @Override
    public String sendMessage(String color, String msg) {
        return msg;
    }
    
}
