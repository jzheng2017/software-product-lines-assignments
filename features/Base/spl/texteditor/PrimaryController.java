package spl.texteditor; 

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import spl.texteditor.storage.FileReadWriteService;
import spl.texteditor.storage.ReadWriteService;

public  class  PrimaryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrimaryController.class);

    @FXML
    private TextArea textArea;
    @FXML
    private Stage stage;
    private ReadWriteService readWriteService = new FileReadWriteService();
    @FXML
    public void onOpenFileAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a file you want to edit");
        File file = fileChooser.showOpenDialog(stage);
        textArea.setText(readWriteService.read(file.getPath()));
    }

    @FXML
    public void onFileSave() {
        String lastFileRead = readWriteService.lastFileRead();
        String contents = textArea.getText();
        if (lastFileRead == null || lastFileRead.isEmpty() || lastFileRead.isBlank()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save");
            File file = fileChooser.showSaveDialog(stage);
            readWriteService.write(file.getPath(), contents);
        } else {
            readWriteService.write(lastFileRead, contents);
        }
    }
}
