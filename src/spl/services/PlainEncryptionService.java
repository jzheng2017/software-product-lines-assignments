package spl.services;  

public   class   PlainEncryptionService   implements EncryptionService {
	
	
    @Override
    public String encrypt(String text) {
        return text;
    }

	

	

    @Override
    public String decrypt(String text) {
        return text;
    }


}
