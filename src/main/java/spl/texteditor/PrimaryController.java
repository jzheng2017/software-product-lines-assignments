package spl.texteditor; 


import java.io.File; 
import java.util.Map; 

import javafx.scene.control.MenuBar; 
import org.fxmisc.richtext.CodeArea; 

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory; 

import javafx.application.Platform; 
import javafx.event.ActionEvent; 
import javafx.event.EventHandler; 

import javafx.fxml.FXML; 
import javafx.scene.input.KeyEvent; 
import javafx.scene.input.DragEvent; 
import javafx.scene.input.KeyCode; 
import javafx.scene.input.KeyCodeCombination; 
import javafx.scene.input.KeyCombination; 
import javafx.scene.input.KeyEvent; 
import javafx.stage.Stage; 
import spl.texteditor.dialogs.Dialog; 

import spl.texteditor.dialogs.SaveFileDialog; 
import spl.texteditor.dialogs.OpenFileDialog; 
import spl.texteditor.storage.LocalFileSystemReadWriteService; 
import spl.texteditor.storage.ReadWriteService; 
import spl.texteditor.plugin.core.PluginManager; 
import spl.texteditor.plugin.core.pf4j.Pf4JPluginManager; 
import spl.texteditor.plugin.core.pf4j.TextAreaExtensionPointProcessor; 
import javafx.scene.control.TextArea; 

import java.util.Objects; 
import java.util.concurrent.Future; 
import java.util.concurrent.ScheduledFuture; 
import java.util.concurrent.TimeUnit; 
import spl.texteditor.tasks.*; 

public   class  PrimaryController {
	
    private static final Logger LOGGER  = LoggerFactory.getLogger(PrimaryController.class);

	
    @FXML
    private CodeArea textArea;

	
    
    @FXML
    private MenuBar menuBar;

	
    @FXML
    private Stage stage;

	
    private ReadWriteService readWriteService  = new LocalFileSystemReadWriteService();

	
    
    @FXML
    public void onOpenFileAction() {
        System.out.println(menuBar);
        Dialog<File> fileDialog = new OpenFileDialog(stage);
        File file = fileDialog.openAndWait(Map.of());
        
        if(file != null) {
        	textArea.replaceText(readWriteService.read(file.getPath()));
        } else {
        	LOGGER.warn("No file was selected.");
        }
    }

	

    @FXML
    public void onFileSave() {
        String lastFileRead = readWriteService.lastFileRead();
        String contents = textArea.getText();
        boolean isNewFile = lastFileRead == null || lastFileRead.isEmpty() || lastFileRead.isBlank();

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

	
    
    @FXML
    public void onKeyPressed(KeyEvent event) {
        KeyCombination ctrlAndO = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);
        if (ctrlAndO.match(event)) {
            this.onOpenFileAction();
        }
        
        KeyCombination ctrlAndS = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        if (ctrlAndS.match(event)) {
        	this.onFileSave();
        }
    }

	
    
    @FXML
    public void onQuit() {
    	Platform.exit();
    }

	
    
    @FXML
    public void onDragOver(DragEvent event) {

    }

	
    
    @FXML
    public void onDragDropped(DragEvent event) {

    }

	
    private PluginManager pluginManager = Pf4JPluginManager.getInstance();

	

    @FXML
     private void  initialize__wrappee__PluginSystem() {
        LOGGER.info("Initialization started");
        Pf4JPluginManager pf4JPluginManager = (Pf4JPluginManager)pluginManager;
        pluginManager.addObserver(new TextAreaExtensionPointProcessor(pf4JPluginManager.getInternalPluginManager(), textArea));
    }

	

    @FXML
    void initialize() {
        initialize__wrappee__PluginSystem();
        taskExecutorService.executeRecurringTask(new ScheduledTask(
                new AutosaveTask(readWriteService, new ContentProvider() {
                	private String lastRequestedText;
                    @Override
                    public String getText() {
                    	lastRequestedText = textArea.getText();
                    	
                        return textArea.getText();
                    }
                    
                    @Override
                    public boolean isDirty() {
                    	return !Objects.equals(lastRequestedText, textArea.getText());
                    }
                }),
                5,
                true
        ));
    }

	
    private TaskExecutorService taskExecutorService = new ScheduledTaskExecutorService();


}
