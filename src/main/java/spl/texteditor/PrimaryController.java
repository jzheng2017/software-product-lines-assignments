package spl.texteditor; 

import java.io.File; 
import java.util.Map; 
import org.slf4j.Logger; 
import org.slf4j.LoggerFactory; 

import javafx.fxml.FXML; 

import javafx.scene.control.TextArea; 
import javafx.scene.input.KeyCode; 
import javafx.scene.input.KeyCodeCombination; 
import javafx.scene.input.KeyCombination; 
import javafx.scene.input.KeyEvent; 
import javafx.scene.input.DragEvent; 
import javafx.scene.input.Dragboard; 
import javafx.scene.input.TransferMode; 
import javafx.stage.Stage; 
import spl.texteditor.dialogs.*; 
import spl.texteditor.dialogs.Dialog; 
import spl.texteditor.dialogs.SaveFileDialog; 
import spl.texteditor.dialogs.OpenFileDialog; 
import spl.texteditor.storage.LocalFileSystemReadWriteService; 
import spl.texteditor.storage.ReadWriteService; 
import spl.texteditor.tasks.ScheduledTaskExecutorService; 
import spl.texteditor.tasks.TaskExecutorService; 

import java.util.Objects; 
import spl.texteditor.tasks.*; 

public   class  PrimaryController {
	
    private static final Logger LOGGER  = LoggerFactory.getLogger(PrimaryController.class);

	
    @FXML
    private TextArea textArea;

	
    @FXML
    private Stage stage;

	
    private ReadWriteService readWriteService  = new LocalFileSystemReadWriteService();

	

    @FXML
    public void onOpenFileAction() {
        Dialog<File> fileDialog = new OpenFileDialog(stage);
        File file = fileDialog.openAndWait(Map.of());
        
        if(file != null) {
        	textArea.setText(readWriteService.read(file.getPath()));
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
        KeyCombination ctrlAndF = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
        if (ctrlAndF.match(event)) {
            Dialog<FindAndReplaceResult> findAndReplaceDialog = new FindAndReplaceDialog();
            final FindAndReplaceResult result = findAndReplaceDialog.openAndWait(Map.of());

            if (result.isValid()) {
                textArea.setText(textArea.getText().replace(result.getTextToFind(), result.getReplacementText()));
            }
        }
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
        	textArea.setText(readWriteService.read(file.getPath()));
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
    }

	
    private TaskExecutorService taskExecutorService = new ScheduledTaskExecutorService();

	
 
    @FXML
    void initialize() {
        taskExecutorService.executeTask(new ScheduledTask(
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


}
