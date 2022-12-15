package spl.texteditor;


import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;

import javafx.scene.control.MenuBar;
import org.fxmisc.richtext.CodeArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.control.ListView;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Popup;
import spl.texteditor.dialogs.Dialog;
import spl.texteditor.dialogs.FindDialog;
import spl.texteditor.dialogs.FindResult;
import spl.texteditor.dialogs.SaveFileDialog;
import spl.texteditor.dialogs.OpenFileDialog;
import spl.texteditor.storage.LocalFileSystemReadWriteService;
import spl.texteditor.storage.ReadWriteService;

public class PrimaryController {
    private int curLength;
    
    public void setCurLength(String cur) {
    	curLength = cur.length();
    }
    
    public int getCurLength() {
    	return curLength;
    }
    
	@FXML
    protected void initialize() {
		original();
		String[] wordsInFile = new String[]{};
		try {
			wordsInFile = getAutocompletionWords("src/main/java/spl/texteditor/AutocompletionWords.txt");
			}
			catch(Exception e) {
				LOGGER.warn("Could not read file for autocompletion words");
				System.out.println(e.toString());
				return;
			}
		String[] autowords = wordsInFile;
        Popup popup = new Popup();
        popup.setAutoHide(true);
        ObservableList<String> fil = FXCollections.observableArrayList();        
        
		((CodeArea)tabpane.getSelectionModel().getSelectedItem().getContent()).textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
            	popup.getContent().clear();
            	popup.hide();
                CodeArea sto = ((CodeArea)tabpane.getSelectionModel().getSelectedItem().getContent());
                String curr = "";
                for (int i = sto.getAnchor(); i > 0; i--) {
                    if (sto.getText().charAt(i) == '\n' || sto.getText().charAt(i) == ' ') {
                        break;
                    }else {
                        curr = sto.getText().charAt(i) + curr;
                    }
                }
                if(curr != "" && curr != " ") {
                	setCurLength(curr);
                    fil.clear();
                    for (int i = 0; i < autowords.length; i++) {
                    	if(autowords[i].startsWith(curr)) {
                    		fil.add(autowords[i]);
                    	}
                    }
                    if(!fil.isEmpty()) {
                        ListView<String> list = new ListView<String>();
                        list.setOnKeyPressed(new EventHandler<KeyEvent>() {
                            public void handle(KeyEvent ke) {
                            	if(ke.getCode().equals(KeyCode.ENTER)) {
                                    String autocompleteString = list.getSelectionModel().getSelectedItem().substring(getCurLength()) + " ";
                            		textArea.insertText(textArea.getCaretPosition(), autocompleteString);
                            		popup.hide();
                            	}
                            }
                        });
                        list.getItems().addAll(fil);
                        popup.getContent().addAll(list);
                        popup.show(textArea, textArea.getCaretBounds().get().getMaxX(), textArea.getCaretBounds().get().getMaxY());
                    }
                }
            } 
        });
	}
	
	public String[] getAutocompletionWords(String filename) throws IOException {
		FileReader fileReader = new FileReader(filename);
		
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		List<String> lines = new ArrayList<String>();
		String line = null;
		while((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		bufferedReader.close();
		return lines.toArray(new String[lines.size()]);
	}
}
