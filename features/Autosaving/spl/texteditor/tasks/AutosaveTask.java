package spl.texteditor.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spl.texteditor.ContentProvider;
import spl.texteditor.storage.ReadWriteService;

import java.util.concurrent.Callable;

public class AutosaveTask implements Callable<Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutosaveTask.class);
    private ReadWriteService readWriteService;
    private ContentProvider contentProvider;

    public AutosaveTask(ReadWriteService readWriteService, ContentProvider contentProvider) {
        this.readWriteService = readWriteService;
        this.contentProvider = contentProvider;
    }

    @Override
    public Void call() throws Exception {
        final String identifier = readWriteService.lastFileTouched();
        if (identifier != null && contentProvider.isDirty()) {
            readWriteService.write(identifier, contentProvider.getText());
            LOGGER.info("Autosaved..");
        }

        return null;
    }
}
