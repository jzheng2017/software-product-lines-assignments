package spl.services;

public class EncryptionServiceFactory {
    public static EncryptionService createEncryptionService(EncryptionType encryptionType) {
        return switch (encryptionType) {
            case ROT13 -> new Rot13EncryptionService();
            case REVERSE -> new ReverseStringEncryptionService();
            default -> throw new IllegalStateException(String.format("Encryption type %s is not supported!", encryptionType));
        };
    }
}
