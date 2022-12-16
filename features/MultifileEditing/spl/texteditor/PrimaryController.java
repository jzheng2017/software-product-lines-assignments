package spl.texteditor;

import java.io.File;
import java.util.Map;

import javafx.scene.control.Tab;
import org.fxmisc.richtext.CodeArea;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spl.texteditor.dialogs.Dialog;
import spl.texteditor.dialogs.SaveFileDialog;
import spl.texteditor.dialogs.OpenFileDialog;
import spl.texteditor.storage.ReadWriteService;

public class PrimaryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrimaryController.class);

    @FXML
    private javafx.scene.layout.AnchorPane anchor;
	
	@FXML
	private TabPane tabpane = new TabPane();

	private MultiTabTextAreaManager multiTabTextAreaManager;
	@FXML
    void initialize() {
        multiTabTextAreaManager = new MultiTabTextAreaManager(tabpane);
        anchor.getChildren().remove(0);
        anchor.getChildren().add(tabpane);
        tabpane.setPrefSize(2000, 2000);
        multiTabTextAreaManager.addTab("");
        // Add buttons
        Button addButton = new Button("+");
        anchor.getChildren().add(addButton);
        addButton.relocate(600, 0);
        addButton.setOnAction(addEvent);
    }
	
	// Handle new tabs
	EventHandler<ActionEvent> addEvent = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e)
        {
            multiTabTextAreaManager.addTab("");
        }
    };
    
    @FXML
    public void onOpenFileAction() {
        Dialog<File> fileDialog = new OpenFileDialog(stage);
        File file = fileDialog.openAndWait(Map.of());
        
        if(file != null) {
            final CodeAreaWithTabAndReadWriteService currentActiveCodeAreaWithTabAndReadWriteService = multiTabTextAreaManager.getCurrentActiveCodeArea();
            CodeArea codeArea = currentActiveCodeAreaWithTabAndReadWriteService.codeArea();
            ReadWriteService readWriteService = currentActiveCodeAreaWithTabAndReadWriteService.readWriteService();
            Tab tab = currentActiveCodeAreaWithTabAndReadWriteService.tab();
            tab.setText(file.getName());
        	codeArea.replaceText(readWriteService.read(file.getPath()));
        } else {
        	LOGGER.warn("No file was selected.");
        }
    }

    @FXML
    public void onFileSave() {
        final CodeAreaWithTabAndReadWriteService currentActiveCodeAreaWithTabAndReadWriteService = multiTabTextAreaManager.getCurrentActiveCodeArea();
        CodeArea codeArea = currentActiveCodeAreaWithTabAndReadWriteService.codeArea();
        ReadWriteService readWriteService = currentActiveCodeAreaWithTabAndReadWriteService.readWriteService();

        String lastFileRead = readWriteService.lastFileRead();
        
        boolean isNewFile = lastFileRead == null || lastFileRead.isEmpty() || lastFileRead.isBlank();
        final String contents = codeArea.getText();
        if (isNewFile) {
            Dialog<File> fileDialog = new SaveFileDialog(stage);
            File file = fileDialog.openAndWait(Map.of());
            if(file != null) {
            	readWriteService.write(file.getPath(), contents);
            } else {
            	LOGGER.warn("Saving was cancelled.");
            }
        } else {
        	readWriteService.write(lastFileRead, contents);
        }
    }
    
}