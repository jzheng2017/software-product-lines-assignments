package spl.texteditor;

import java.awt.Color;
import java.io.File;
import java.util.Map;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
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
import org.fxmisc.richtext.Selection;
import org.fxmisc.richtext.SelectionImpl;

public class PrimaryController {
	
	public int highlightIncrementer = 0;
	
    @FXML
    public void onKeyPressed(KeyEvent event) {
    	// REMOVE AFTER
    	//CodeArea textArea;
    	List<Selection> highlightedWords = new ArrayList<Selection>();
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
            		highlightIncrementer += 1;
            		Selection highlightedWord = new SelectionImpl("highlight" + highlightIncrementer, textArea);
            		textArea.addSelection(highlightedWord);
            		highlightedWord.selectRange(indexOfWord, indexOfWord+ttf.length());
            		highlightedWords.add(highlightedWord);
            		indexOfWord = allText.indexOf(ttf, indexOfWord+ttf.length());
        		}
            }
        }
		((CodeArea)tabpane.getSelectionModel().getSelectedItem().getContent()).textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
            	for (Selection word : highlightedWords) {
            		  word.deselect();
            		  word.dispose();
            		}
            	highlightedWords.clear();
            } 
        });
    }
    
    
}


