package spl.texteditor.dialogs; 

import javafx.stage.FileChooser; 
import javafx.stage.Stage; 

import java.io.File; 
import java.util.Map; 

public  class  OpenFileDialog  implements Dialog<File> {
	
    private Stage stage;

	

    public OpenFileDialog(Stage stage) {
        this.stage = stage;
    }

	

    @Override
    public File openAndWait(Map<String, String> args) {
        FileChooser fileChooser = new FileChooser();
        String title = args.get("title");
        fileChooser.setTitle(title != null ? title : "Choose a file");

        return fileChooser.showOpenDialog(stage);
    }


}
