package spl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread implements Runnable  
{
    private Socket skt;
    
    public ClientThread(Socket socket) {
        skt = socket;
    }
    
    public void run()  
    {
        while(true)
        {
        	try {
                InputStream input = skt.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                String message = reader.readLine();

                System.out.println(message);
        	}
            catch (Exception e){
            	System.out.println(e);
            }
         }
      }
}
