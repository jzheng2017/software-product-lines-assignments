package spl.texteditor.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spl.texteditor.ContentProvider;
import spl.texteditor.storage.ReadWriteService;

public class AutosavingTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutosavingTask.class);
    private ReadWriteService readWriteService;
    private ContentProvider contentProvider;

    public AutosavingTask(ReadWriteService readWriteService, ContentProvider contentProvider) {
        this.readWriteService = readWriteService;
        this.contentProvider = contentProvider;
    }

    @Override
    public void run() {
        final String identifier = readWriteService.lastFileTouched();
        if (identifier != null && contentProvider.isDirty()) {
            readWriteService.write(identifier, contentProvider.getText());
            LOGGER.info("Autosaved..");
        }
    }
}
