package spl.texteditor;

import java.io.File;
import java.util.Map;

import javafx.scene.control.TextArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import spl.texteditor.storage.LocalFileSystemReadWriteService;
import spl.texteditor.storage.ReadWriteService;

public class PrimaryController {
    private ReadWriteService readWriteService = new LocalFileSystemReadWriteService();
    @FXML
    private TextArea textArea;
    @FXML
    public void onDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
        event.consume();
    }
    
    @FXML
    public void onDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
        	File file = db.getFiles().get(0);
        	textArea.setText(readWriteService.read(file.getPath()));
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
    }
}
