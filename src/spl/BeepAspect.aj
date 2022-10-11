package spl;

import java.io.PrintWriter;
import java.awt.Toolkit;

public aspect BeepAspect {

	
    pointcut sendMessage() : call(* PrintWriter.println(String));
    //Maybe add " && (withincode(* Client.sendMessage(..)) || withincode(* ServerThread.run()))" ???
    
    after() : sendMessage(){
    	Toolkit.getDefaultToolkit().beep();
    }
}