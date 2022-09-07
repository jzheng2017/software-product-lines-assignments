package spl.services;

public interface EncryptionService {
    String encrypt(String text);
    String decrypt(String text);
}
