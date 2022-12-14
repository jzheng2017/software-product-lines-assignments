package spl.texteditor;

import java.awt.Color;
import java.io.File;
import java.util.Map;
import java.util.Collections;

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
import spl.texteditor.dialogs.Dialog;
import spl.texteditor.dialogs.SaveFileDialog;
import spl.texteditor.dialogs.OpenFileDialog;
import spl.texteditor.storage.LocalFileSystemReadWriteService;
import spl.texteditor.storage.ReadWriteService;

import org.fxmisc.richtext.CodeArea;

public class PrimaryController {
	

    @FXML
    public void onKeyPressed(KeyEvent event) {
    	
        KeyCombination ctrlAndF = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
        if (ctrlAndF.match(event)) {
            Dialog<FindResult> findDialog = new FindDialog();
            final FindResult result = findDialog.openAndWait(Map.of());
            if (result.isValid()) {
            	String allText = textArea.getText();
            	String ttf = result.getTextToFind();
            	if(!result.getCaseSensitive()) {
            		allText = allText.toLowerCase();
            		ttf = ttf.toLowerCase();
            	}
            	int indexOfWord = allText.indexOf(ttf);
            	while (indexOfWord >= 0) {
            		LOGGER.info("Found word and highlighting it");
            		textArea.setStyleClass(indexOfWord, indexOfWord+ttf.length(), "-fx-font-color: red;");
            		indexOfWord = allText.indexOf(ttf, indexOfWord+ttf.length());
        		}
            }
        }
    }
}


