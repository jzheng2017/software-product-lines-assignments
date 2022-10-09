package spl.services; 

public  class  EncryptionServiceFactory {
	
    public static EncryptionService createEncryptionService() {
    	return new PlainEncryptionService();
    }


}
