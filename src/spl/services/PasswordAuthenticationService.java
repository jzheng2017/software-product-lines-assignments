package spl.services;  

public   class   PasswordAuthenticationService   implements AuthenticationService {
	
    @Override
    public boolean authenticate(String authenticationDetails) {
        return authenticationDetails.equals("password");
    }


}
