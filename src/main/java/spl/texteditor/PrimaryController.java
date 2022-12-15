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

import java.awt.Color; 
import java.util.Collections; 
import java.util.List; 
import java.util.Arrays; 
import javafx.scene.control.ListView; 

import javafx.scene.control.TextArea; 
import spl.texteditor.dialogs.*; 
import org.fxmisc.richtext.Selection; 
import org.fxmisc.richtext.SelectionImpl; 

import java.util.Objects; 
import java.util.concurrent.Future; 
import java.util.concurrent.ScheduledFuture; 
import java.util.concurrent.TimeUnit; 
import spl.texteditor.tasks.*; 
import javafx.scene.input.Dragboard; 
import javafx.scene.input.TransferMode; 

import javafx.scene.control.Tab; 
import javafx.scene.control.TabPane; 
import javafx.scene.control.Button; 

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

	

    @FXML
    public void onKeyPressed(KeyEvent event) {
    	
        KeyCombination ctrlAndF = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
        if (ctrlAndF.match(event)) {
            Dialog<FindResult> findDialog = new FindDialog();
            final FindResult result = findDialog.openAndWait(Map.of());
            if (result.isValid()) {
            	if(result.getCaseSensitive()) {
            		textArea.replaceText(textArea.getText().replace(result.getTextToFind(), result.getReplacementText()));
            	}
            	else {
            		textArea.replaceText(textArea.getText().replaceAll("(?i)"+result.getTextToFind(), result.getReplacementText()));
            	}
            }
        }
    }

	
    
    @FXML
    public void onQuit() {
    	Platform.exit();
    }

	
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
        	textArea.replaceText(readWriteService.read(file.getPath()));
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
    }

	
    private PluginManager pluginManager = Pf4JPluginManager.getInstance();

	

    @FXML
     private void  initialize__wrappee__PluginSystem() {
        LOGGER.info("Initialization started");
        Pf4JPluginManager pf4JPluginManager = (Pf4JPluginManager)pluginManager;
        pluginManager.addObserver(new TextAreaExtensionPointProcessor(pf4JPluginManager.getInternalPluginManager(), textArea));
    }

	
	@FXML
    protected void initialize() {
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

	
	
	public int highlightIncrementer = 0;

	
    private TaskExecutorService taskExecutorService = new ScheduledTaskExecutorService();

	

    @FXML
    private javafx.scene.layout.AnchorPane anchor;

	
	
	@FXML
	private TabPane tabpane = new TabPane();

	

	private MultiTabTextAreaManager multiTabTextAreaManager;

	
	
	// Handle new tabs
	EventHandler<ActionEvent> addEvent = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e)
        {
            multiTabTextAreaManager.addTab("");
        }
    };


}
