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

import spl.texteditor.dialogs.FindAndReplaceDialog; 
import spl.texteditor.dialogs.FindAndReplaceResult; 

import javafx.scene.control.TextArea; 
import spl.texteditor.dialogs.*; 

import java.util.Objects; 
import spl.texteditor.tasks.*; 
import javafx.scene.input.Dragboard; 
import javafx.scene.input.TransferMode; 
import java.util.List; 
import javafx.scene.control.TabPane; 
import javafx.scene.control.Tab; 
import javafx.scene.control.Button; 
import javafx.scene.layout.AnchorPane; 
import javafx.beans.value.ChangeListener; 
import java.util.ArrayList; 
import javafx.beans.value.ChangeListener; 
import javafx.beans.value.ObservableValue; 

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
        int index = tabpane.getSelectionModel().getSelectedIndex();
        // Remove the current service if it exists
        if(services.size() >= index + 1)
        {
        	services.remove(index);
        }
        ReadWriteService service = new LocalFileSystemReadWriteService();
        services.add(tabpane.getSelectionModel().getSelectedIndex(),service);
        
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

	
    
    @FXML
     private void  onKeyPressed__wrappee__Base(KeyEvent event) {
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
    public void onKeyPressed(KeyEvent event) {
    	onKeyPressed__wrappee__Base(event);
        KeyCombination ctrlAndF = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
        if (ctrlAndF.match(event)) {
            Dialog<FindAndReplaceResult> findAndReplaceDialog = new FindAndReplaceDialog();
            final FindAndReplaceResult result = findAndReplaceDialog.openAndWait(Map.of());

            if (result.isValid()) {
                textArea.replaceText(textArea.getText().replace(result.getTextToFind(), result.getReplacementText()));
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

	
    private TaskExecutorService taskExecutorService = new ScheduledTaskExecutorService();

	
	@FXML
    private javafx.scene.layout.AnchorPane anchor;

	
	
	@FXML
	private TabPane tabpane = new TabPane();

	
	
	@FXML
	private List<ReadWriteService> services = new ArrayList<ReadWriteService>();

	
	
	// Handle new tabs
	EventHandler<ActionEvent> addEvent = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e)
        {
        	// Get filename
        	Dialog<File> fileDialog = new OpenFileDialog(stage);
            File file = fileDialog.openAndWait(Map.of());
            CodeArea textArea = new CodeArea();
            
            ReadWriteService service = new LocalFileSystemReadWriteService();
            services.add(service);
            
            if(file != null) {
            	textArea.replaceText(service.read(file.getPath()));
            } else {
            	LOGGER.warn("No file was selected.");
            }
            Tab tab = new Tab(file.getName(), textArea);
            tabpane.getTabs().add(tab);
            
        }
    };


}
