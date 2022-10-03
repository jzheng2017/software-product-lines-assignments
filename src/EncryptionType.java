package spl.services; 

public enum  EncryptionType {
    ROT13 , 
    REVERSE , 
    PLAIN; 

    public static EncryptionType toEnum(String encryptionType) {
        switch (encryptionType.toLowerCase()) {
            case "reverse": return REVERSE;
            case "rot13": return ROT13;
            default: return PLAIN;
        }
    }}
