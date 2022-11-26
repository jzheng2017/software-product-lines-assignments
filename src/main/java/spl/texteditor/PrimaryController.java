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
import spl.texteditor.dialogs.Dialog; 
import spl.texteditor.dialogs.SaveFileDialog; 
import spl.texteditor.dialogs.OpenFileDialog; 
import spl.texteditor.storage.LocalFileSystemReadWriteService; 
import spl.texteditor.storage.ReadWriteService; 

public  class  PrimaryController {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(PrimaryController.class);

	
    @FXML
    private TextArea textArea;

	
    @FXML
    private Stage stage;

	
    private ReadWriteService readWriteService = new LocalFileSystemReadWriteService();

	

    @FXML
    public void onOpenFileAction() {
        Dialog<File> fileDialog = new OpenFileDialog(stage);
        File file = fileDialog.openAndWait(Map.of());
        textArea.setText(readWriteService.read(file.getPath()));
    }

	

    @FXML
    public void onFileSave() {
        String lastFileRead = readWriteService.lastFileRead();
        String contents = textArea.getText();
        boolean isNewFile = lastFileRead == null || lastFileRead.isEmpty() || lastFileRead.isBlank();

        if (isNewFile) {
            Dialog<File> fileDialog = new SaveFileDialog(stage);
            File file = fileDialog.openAndWait(Map.of());
            readWriteService.write(file.getPath(), contents);
        } else {
            readWriteService.write(lastFileRead, contents);
        }
    }

	
    
    @FXML
    public void onKeyPressed(KeyEvent event) {
    	
    }


}
