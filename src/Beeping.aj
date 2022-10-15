package spl;

import java.io.PrintWriter;
import java.awt.Toolkit;

public aspect Beeping {

	
    pointcut sendMessage() : call(* PrintWriter.println(String));
    //Maybe add " && (withincode(* Client.sendMessage(..)) || withincode(* ServerThread.run()))" ???
    
    after() : sendMessage(){
    	Toolkit.getDefaultToolkit().beep();
    }
}