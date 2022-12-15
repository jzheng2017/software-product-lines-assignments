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
import java.io.IOException; 
import java.io.BufferedReader; 
import java.io.FileReader; 
import javafx.scene.control.ListView; 
import javafx.collections.ObservableList; 
import javafx.collections.FXCollections; 
import javafx.stage.Popup; 
import spl.texteditor.dialogs.FindDialog; 
import spl.texteditor.dialogs.FindResult; 

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
     private void  initialize__wrappee__MultifileEditing() {
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

	
    
	@FXML
    protected void initialize() {
		initialize__wrappee__MultifileEditing();
		String[] wordsInFile = new String[]{};
		try {
			wordsInFile = getAutocompletionWords("src/main/java/spl/texteditor/AutocompletionWords.txt");
			}
			catch(Exception e) {
				LOGGER.warn("Could not read file for autocompletion words");
				System.out.println(e.toString());
				return;
			}
		String[] autowords = wordsInFile;
        Popup popup = new Popup();
        popup.setAutoHide(true);
        ObservableList<String> fil = FXCollections.observableArrayList();        
        
		((CodeArea)tabpane.getSelectionModel().getSelectedItem().getContent()).textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
            	popup.getContent().clear();
            	popup.hide();
                CodeArea sto = ((CodeArea)tabpane.getSelectionModel().getSelectedItem().getContent());
                String curr = "";
                for (int i = sto.getAnchor(); i > 0; i--) {
                    if (sto.getText().charAt(i) == '\n' || sto.getText().charAt(i) == ' ') {
                        break;
                    }else {
                        curr = sto.getText().charAt(i) + curr;
                    }
                }
                if(curr != "" && curr != " ") {
                	setCurLength(curr);
                    fil.clear();
                    for (int i = 0; i < autowords.length; i++) {
                    	if(autowords[i].startsWith(curr)) {
                    		fil.add(autowords[i]);
                    	}
                    }
                    if(!fil.isEmpty()) {
                        ListView<String> list = new ListView<String>();
                        list.setOnKeyPressed(new EventHandler<KeyEvent>() {
                            public void handle(KeyEvent ke) {
                            	if(ke.getCode().equals(KeyCode.ENTER)) {
                                    String autocompleteString = list.getSelectionModel().getSelectedItem().substring(getCurLength()) + " ";
                            		textArea.insertText(textArea.getCaretPosition(), autocompleteString);
                            		popup.hide();
                            	}
                            }
                        });
                        list.getItems().addAll(fil);
                        popup.getContent().addAll(list);
                        popup.show(textArea, textArea.getCaretBounds().get().getMaxX(), textArea.getCaretBounds().get().getMaxY());
                    }
                }
            } 
        });
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

	
    private int curLength;

	
    
    public void setCurLength(String cur) {
    	curLength = cur.length();
    }

	
    
    public int getCurLength() {
    	return curLength;
    }

	
	
	public String[] getAutocompletionWords(String filename) throws IOException {
		FileReader fileReader = new FileReader(filename);
		
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		List<String> lines = new ArrayList<String>();
		String line = null;
		while((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		bufferedReader.close();
		return lines.toArray(new String[lines.size()]);
	}


}
