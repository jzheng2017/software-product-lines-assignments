package spl.texteditor;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spl.texteditor.storage.LocalFileSystemReadWriteService;
import spl.texteditor.storage.ReadWriteService;
import spl.texteditor.tasks.*;

public class PrimaryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrimaryController.class);
    @FXML
    private TextArea textArea;
    @FXML
    private Stage stage;
    private ReadWriteService readWriteService = new LocalFileSystemReadWriteService();

    private TaskExecutorService taskExecutorService = new ScheduledExecutorTaskService();

    @FXML
    void initialize() {
        taskExecutorService.executeTask(new ScheduledTask(
                new AutosavingTask(readWriteService, new ContentProvider() {
                    @Override
                    public String getText() {
                        return textArea.getText();
                    }
                }),
                5,
                true
        ));
    }
}
