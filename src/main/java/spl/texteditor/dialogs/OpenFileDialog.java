package spl.texteditor.dialogs; 

import javafx.stage.FileChooser; 
import javafx.stage.Stage; 
import org.slf4j.Logger; 
import org.slf4j.LoggerFactory; 

import java.io.File; 
import java.util.Map; 

public  class  OpenFileDialog  implements Dialog<File> {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenFileDialog.class);

	
    private Stage stage;

	

    public OpenFileDialog(Stage stage) {
        this.stage = stage;
    }

	

    @Override
    public File openAndWait(Map<String, String> args) {
        LOGGER.info("Open file dialog opened");
        FileChooser fileChooser = new FileChooser();
        String title = args.get("title");
        fileChooser.setTitle(title != null ? title : "Choose a file");

        return fileChooser.showOpenDialog(stage);
    }


}
