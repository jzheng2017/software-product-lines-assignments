package spl.services; 

import java.awt.Toolkit;


public class  ToolkitMessageSound implements MessageSound {
	
    public void beep() {
    	Toolkit.getDefaultToolkit().beep();
    }


}