package spl.services;  

public   class   EncryptionServiceFactory {
	
	
    public static EncryptionService createEncryptionService(EncryptionType encryptionType) {
        switch (encryptionType) {
            case REVERSE: return new ReverseStringEncryptionService();
            default: throw new IllegalStateException(String.format("Encryption type %s is not supported!", encryptionType));
        }
    }


}
