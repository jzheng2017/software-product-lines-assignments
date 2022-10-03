package spl.services; 

public  class  EncryptionServiceFactory {
	
    public static EncryptionService createEncryptionService(EncryptionType encryptionType) {
        switch (encryptionType) {
            case ROT13: return new Rot13EncryptionService();
            case REVERSE: return new ReverseStringEncryptionService();
            case PLAIN: return new PlainEncryptionService();
            default: throw new IllegalStateException(String.format("Encryption type %s is not supported!", encryptionType));
        }
    }


}
