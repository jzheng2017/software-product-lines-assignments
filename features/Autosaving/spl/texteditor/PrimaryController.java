package spl.texteditor;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spl.texteditor.storage.LocalFileSystemReadWriteService;
import spl.texteditor.storage.ReadWriteService;
import spl.texteditor.tasks.*;

public class PrimaryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrimaryController.class);
    @FXML
    private Stage stage;
    private ReadWriteService readWriteService = new LocalFileSystemReadWriteService();
    private TaskExecutorService taskExecutorService = new ScheduledTaskExecutorService();

    @FXML
    public void initialize() {
        original();
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
}
