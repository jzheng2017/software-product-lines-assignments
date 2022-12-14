package spl.texteditor;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import spl.texteditor.dialogs.*;
import spl.texteditor.storage.LocalFileSystemReadWriteService;
import spl.texteditor.storage.ReadWriteService;

public class PrimaryController {

    @FXML
    public void onKeyPressed(KeyEvent event) {
    	
        KeyCombination ctrlAndF = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
        if (ctrlAndF.match(event)) {
            Dialog<FindResult> findDialog = new FindDialog();
            final FindResult result = findDialog.openAndWait(Map.of());
            if (result.isValid()) {
            	if(result.getCaseSensitive()) {
            		textArea.replaceText(textArea.getText().replace(result.getTextToFind(), result.getReplacementText()));
            	}
            	else {
            		textArea.replaceText(textArea.getText().replaceAll("(?i)"+result.getTextToFind(), result.getReplacementText()));
            	}
            }
        }
    }
}
