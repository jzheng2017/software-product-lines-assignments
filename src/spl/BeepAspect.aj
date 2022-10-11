package spl;

import spl.services.ToolkitMessageSound;
import java.io.PrintWriter; 

public aspect BeepAspect {
	ToolkitMessageSound toolkitMessageSound = new ToolkitMessageSound();
	
    pointcut sendMessage() : call(* PrintWriter.println(String));
    //Maybe add " && (withincode(* Client.sendMessage(..)) || withincode(* ServerThread.run()))" ???
    
    after() : sendMessage(){
		toolkitMessageSound.beep();
    }
}