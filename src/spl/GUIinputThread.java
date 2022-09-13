package spl;
import spl.services.FeatureConfigurationService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class GUIinputThread implements Runnable {

    List<String> cmds = Arrays.asList("usernameColors", "encryptMessages");
    FeatureConfigurationService fcs;
    
    public GUIinputThread(FeatureConfigurationService fcs) {
    	this.fcs = fcs;
    }

    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] input;
        while(true) {
    		try {
    			input = reader.readLine().split("\\s+");
    			if(input.length > 1) {
        			String command = input[0];
        			if(cmds.contains(command)) {
    					boolean arg = Boolean.parseBoolean(input[1]);
    					fcs.changeFeatureVal(command, arg);
    					GUI.toggleColorSelection(arg);
        			}
        			else {
        				System.out.println("Command not recognized");
        			}
    			}
    			else {
    				System.out.println("Command not recognized/no arguments given");
    			}
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        }
    }
}
