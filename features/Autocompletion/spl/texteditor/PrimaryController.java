package spl.texteditor;


import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Popup;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.NavigationActions.SelectionPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrimaryController {
	private static final Logger LOGGER = LoggerFactory.getLogger(PrimaryController.class);

	@FXML
	public CodeArea textArea;
    private int curLength;
    
    public void setCurLength(String cur) {
    	curLength = cur.length();
    }
    
    public int getCurLength() {
    	return curLength;
    }
    
	@FXML
    public void initialize() {
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
        
        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observableValue, String s, String s2) {
            	popup.getContent().clear();
            	popup.hide();
                
                String content = textArea.getText();
                if (!content.isBlank()) {
                    content = content.substring(0, textArea.getAnchor()+1);
                    String curr = content.substring(content.lastIndexOf(" ")+1);
                    if(!curr.isBlank()) {
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
                                		ke.consume();
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
