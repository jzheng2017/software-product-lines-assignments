package spl.services; 

public  class  ReverseStringEncryptionService  implements EncryptionService {
	
    @Override
    public String encrypt(String text) {
        return new StringBuilder(text).reverse().toString();
    }

	

    @Override
    public String decrypt(String text) {
        return new StringBuilder(text).reverse().toString();
    }


}
