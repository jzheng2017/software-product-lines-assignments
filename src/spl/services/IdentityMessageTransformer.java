package spl.services;

public class IdentityMessageTransformer implements MessageTransformer {

    @Override
    public String transform(String msg) {
        return msg;
    }
    
}
