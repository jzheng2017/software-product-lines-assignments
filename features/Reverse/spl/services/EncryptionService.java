package spl.services; 

public  class  EncryptionService {
	
    protected String encrypt(String text) {
        return new StringBuilder(text).reverse().toString();
    }
    
    protected String decrypt(String text) {
        return new StringBuilder(text).reverse().toString();
    }
}
