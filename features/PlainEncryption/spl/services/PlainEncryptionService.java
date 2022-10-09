package spl.services; 

public  class  PlainEncryptionService implements EncryptionService{
	
    public String encrypt(String text) {
        return text;
    }

    public String decrypt(String text) {
        return text;
    }
}
