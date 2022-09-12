package spl.services;

public enum EncryptionType {
    ROT13,
    REVERSE;

    public static EncryptionType toEnum(String encryptionType) {
        return switch (encryptionType.toLowerCase()) {
            case "reverse" -> REVERSE;
            case "rot13" -> ROT13;
            default -> throw new IllegalStateException(String.format("Encryption type %s can not be converted to enum value", encryptionType));
        };
    }
}
