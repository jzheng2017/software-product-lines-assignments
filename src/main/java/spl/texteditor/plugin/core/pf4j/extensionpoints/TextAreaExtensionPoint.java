package spl.texteditor.plugin.core.pf4j.extensionpoints; 

import javafx.scene.control.TextArea; 
import org.fxmisc.richtext.CodeArea; 
import org.pf4j.ExtensionPoint; 

/**
 * An extension point for the main text area of the editor
 */
public  interface  TextAreaExtensionPoint  extends ExtensionPoint {
	

    void extendTextArea(CodeArea textArea);


}
