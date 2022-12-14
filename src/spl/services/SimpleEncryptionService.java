package spl.services;


public class SimpleEncryptionService implements EncryptionService {
    @Override
    public String encrypt(String text) {
        return rot13(new StringBuilder(text).reverse().toString());
    }

    @Override
    public String decrypt(String text) {
        return new StringBuilder(rot13(text)).reverse().toString();
    }

    private String rot13(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c >= 'a' && c <= 'm') c += 13;
            else if (c >= 'A' && c <= 'M') c += 13;
            else if (c >= 'n' && c <= 'z') c -= 13;
            else if (c >= 'N' && c <= 'Z') c -= 13;
            sb.append(c);
        }
        return sb.toString();
    }
}
