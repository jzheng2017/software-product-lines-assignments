package spl.texteditor;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.fxmisc.richtext.CodeArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import spl.texteditor.dialogs.*;
import spl.texteditor.dialogs.Dialog;
import spl.texteditor.dialogs.SaveFileDialog;
import spl.texteditor.dialogs.OpenFileDialog;
import spl.texteditor.storage.LocalFileSystemReadWriteService;
import spl.texteditor.storage.ReadWriteService;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class PrimaryController {
	@FXML
    private javafx.scene.layout.AnchorPane anchor;
	
	@FXML
	private TabPane tabpane = new TabPane();
	
	@FXML
	private List<ReadWriteService> services = new ArrayList<ReadWriteService>();
	
	@FXML
    protected void initialize() {
		LOGGER.warn("Initialising.");
		// Remove the current content
        anchor.getChildren().remove(0);
        // Add the tab pane
        anchor.getChildren().add(tabpane);
        tabpane.setPrefSize(2000, 2000);
        // Add first tab
        textArea = new CodeArea("");
        Tab firstTab = new Tab("", textArea);
        ReadWriteService service = new LocalFileSystemReadWriteService();
        services.add(service);
        firstTab.setClosable(false);
        tabpane.getTabs().add(firstTab);
        tabpane.getSelectionModel().selectedItemProperty().addListener(
        	    new ChangeListener<Tab>() {
        	        @Override
        	        public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
        	            System.out.println("Tab Selection changed");
        	            // Set textArea to current tab area
        	            textArea = (CodeArea) tabpane.getSelectionModel().getSelectedItem().getContent();
        	        }
        	    }
        	);

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
        	// Get filename
        	Dialog<File> fileDialog = new OpenFileDialog(stage);
            File file = fileDialog.openAndWait(Map.of());
            CodeArea textArea = new CodeArea();
            
            if(file != null) {
            	textArea.replaceText(readWriteService.read(file.getPath()));
            } else {
            	LOGGER.warn("No file was selected.");
            }
            Tab tab = new Tab(file.getName(), textArea);
            tabpane.getTabs().add(tab);
        }
    };
    
    @FXML
    public void onOpenFileAction() {
        System.out.println(menuBar);
        Dialog<File> fileDialog = new OpenFileDialog(stage);
        File file = fileDialog.openAndWait(Map.of());
        ReadWriteService service = new LocalFileSystemReadWriteService();
        services.add(service);
        
        if(file != null) {
        	tabpane.getSelectionModel().getSelectedItem().setText(file.getName());
        	CodeArea text = (CodeArea) tabpane.getSelectionModel().getSelectedItem().getContent();
        	text.replaceText(service.read(file.getPath()));
        } else {
        	LOGGER.warn("No file was selected.");
        }
    }

    @FXML
    public void onFileSave() {
    	ReadWriteService service = services.get(tabpane.getSelectionModel().getSelectedIndex());
    	String lastFileRead = service.lastFileRead();
        
    	CodeArea area = (CodeArea) tabpane.getSelectionModel().getSelectedItem().getContent();
        String contents = area.getText(); 
        boolean isNewFile = lastFileRead == null || lastFileRead.isEmpty() || lastFileRead.isBlank();

        if (isNewFile) {
            Dialog<File> fileDialog = new SaveFileDialog(stage);
            File file = fileDialog.openAndWait(Map.of());
            if(file != null) {
            	service.write(file.getPath(), contents);
            } else {
            	LOGGER.warn("Saving was cancelled.");
            }
        } else {
        	service.write(lastFileRead, contents);
        }
    }
    
}