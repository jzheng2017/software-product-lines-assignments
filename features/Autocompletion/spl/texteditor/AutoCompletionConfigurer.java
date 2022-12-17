package spl.texteditor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Popup;
import org.fxmisc.richtext.CodeArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AutoCompletionConfigurer {
    private static Logger LOGGER = LoggerFactory.getLogger(AutoCompletionConfigurer.class);

    private AutoCompletionConfigurer() {

    }
    public static void configure(CodeArea codeArea) {
        String[] wordsInFile;
        try {
            wordsInFile = getAutocompletionWords("src/main/java/spl/texteditor/AutocompletionWords.txt");
        }
        catch(Exception e) {
            LOGGER.warn("Could not read file for autocompletion words", e);
            return;
        }
        String[] autowords = wordsInFile;
        Popup popup = new Popup();
        popup.setAutoHide(true);
        ObservableList<String> fil = FXCollections.observableArrayList();

        codeArea.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                popup.getContent().clear();
                popup.hide();
                String curr = "";
                for (int i = codeArea.getAnchor(); i > 0; i--) {
                    if (codeArea.getText().charAt(i) == '\n' || codeArea.getText().charAt(i) == ' ') {
                        break;
                    }else {
                        curr = codeArea.getText().charAt(i) + curr;
                    }
                }
                if(!curr.equals("") && !curr.equals(" ")) {
                    fil.clear();
                    for (int i = 0; i < autowords.length; i++) {
                        if(autowords[i].startsWith(curr)) {
                            fil.add(autowords[i]);
                        }
                    }
                    if(!fil.isEmpty()) {
                        ListView<String> list = new ListView();
                        list.setOnKeyPressed(new EventHandler<KeyEvent>() {
                            public void handle(KeyEvent ke) {
                                if(ke.getCode().equals(KeyCode.ENTER)) {
                                    String autocompleteString = list.getSelectionModel().getSelectedItem().substring(codeArea.getText().length()) + " ";
                                    codeArea.insertText(codeArea.getCaretPosition(), autocompleteString);
                                    popup.hide();
                                }
                            }
                        });
                        list.getItems().addAll(fil);
                        popup.getContent().addAll(list);
                        popup.show(codeArea, codeArea.getCaretBounds().get().getMaxX(), codeArea.getCaretBounds().get().getMaxY());
                    }
                }
            }
        });
    }
    private static String[] getAutocompletionWords(String filename) throws IOException {
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

