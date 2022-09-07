package spl.services;

import static spl.Main.rot13;

public class SimpleEncryptionService implements EncryptionService{
    @Override
    public String encrypt(String text) {
        return rot13(new StringBuilder(text).reverse().toString());
    }

    @Override
    public String decrypt(String text) {
        return new StringBuilder(rot13(text)).reverse().toString();
    }
}
