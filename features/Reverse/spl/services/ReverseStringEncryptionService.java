package spl.services; 

public  class  ReverseStringEncryptionService implements EncryptionService {
	
    public String encrypt(String text) {
        return new StringBuilder(text).reverse().toString();
    }
    
    public String decrypt(String text) {
        return new StringBuilder(text).reverse().toString();
    }
}
