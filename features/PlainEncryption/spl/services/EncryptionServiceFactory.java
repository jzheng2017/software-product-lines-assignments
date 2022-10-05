package spl.services; 

public  class  EncryptionServiceFactory {
	
    public static EncryptionService createEncryptionService(EncryptionType encryptionType) {
        switch (encryptionType) {
            case PLAIN: return new PlainEncryptionService();
            default: throw new IllegalStateException(String.format("Encryption type %s is not supported!", encryptionType));
        }
    }


}
