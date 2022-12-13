package spl.texteditor.plugin.core.pf4j.extensionpoints; 

import javafx.scene.control.TextArea; 
import org.fxmisc.richtext.CodeArea; 
import org.pf4j.ExtensionPoint; 

public  interface  TextAreaExtensionPoint  extends ExtensionPoint {
	

    void extendTextArea(CodeArea textArea);


}
