package spl.texteditor.dialogs; 

import javafx.stage.FileChooser; 
import javafx.stage.Stage; 
import org.slf4j.Logger; 
import org.slf4j.LoggerFactory; 

import java.io.File; 
import java.util.Map; 

public  class  SaveFileDialog  implements Dialog<File> {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveFileDialog.class);

	
    private final Stage stage;

	

    public SaveFileDialog(Stage stage) {
        this.stage = stage;
    }

	

    @Override
    public File openAndWait(Map<String, String> args) {
        LOGGER.info("Save file dialog opened");
        FileChooser fileChooser = new FileChooser();
        String title = args.get("title");
        fileChooser.setTitle(title != null ? title : "Save");

        return fileChooser.showSaveDialog(stage);
    }


}
