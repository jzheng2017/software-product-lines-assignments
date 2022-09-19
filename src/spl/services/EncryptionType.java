package spl.services;

public enum EncryptionType {
    ROT13,
    REVERSE;

    public static EncryptionType toEnum(String encryptionType) {
        switch (encryptionType.toLowerCase()) {
            case "reverse": return REVERSE;
            case "rot13": return ROT13;
            default: throw new IllegalStateException(String.format("Encryption type %s can not be converted to enum value", encryptionType));
        }
    }
}
