package spl.texteditor.storage; 

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory; 
import spl.texteditor.tasks.ScheduledTask; 
import spl.texteditor.tasks.ScheduledTaskExecutorService; 
import spl.texteditor.tasks.TaskExecutorService; 

import java.io.FileInputStream; 
import java.io.IOException;
import java.util.concurrent.*;

public  class  LocalFileSystemReadWriteService  implements ReadWriteService {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalFileSystemReadWriteService.class);

	
    private TextStorageService storageService = new FileTextStorageService();

	
    private String lastFileRead;

	
    private String lastFileWritten;

	
    private String lastFileTouched;

	
    private TaskExecutorService taskExecutorService = new ScheduledTaskExecutorService();

	

    public LocalFileSystemReadWriteService() {

    }

	

    //only for unit testing purposes
    LocalFileSystemReadWriteService(TextStorageService textStorageService) {
        this.storageService = textStorageService;
    }

	

    @Override
    public String read(String identifier) {
        this.lastFileRead = identifier;
        this.lastFileTouched = identifier;

        try {
            return (String)taskExecutorService.executeTask(new ScheduledTask(new ReadFileTask(identifier), 0, false)).get(5L, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOGGER.warn("Could not read contents of file '{}'", identifier);
            throw new RuntimeException(e);
        }
    }

	

    @Override
    public Future<?> write(String identifier, String content) {
        this.lastFileWritten = identifier;
        this.lastFileTouched = identifier;

        LOGGER.info("Start writing to file '{}'", identifier);
        return taskExecutorService.executeTask(new ScheduledTask(
                new WriteFileTask(identifier, content),
                0,
                false));
    }

	

    @Override
    public String lastFileRead() {
        return lastFileRead;
    }

	

    @Override
    public String lastFileWritten() {
        return lastFileWritten;
    }

	
    
    @Override
    public String lastFileTouched() {
    	return lastFileTouched;
    }

	

    private  class  WriteFileTask  implements Callable<Void> {
		
        private final String filePath;

		
        private final String content;

		
        public WriteFileTask(String filePath, String content) {
            this.filePath = filePath;
            this.content = content;
        }

		

        @Override
        public Void call() throws Exception {
            storageService.store(filePath, content, true);
            return null;
        }


	}

	

    private  class  ReadFileTask  implements Callable<String> {
		
        private final String filePath;

		

        private ReadFileTask(String filePath) {
            this.filePath = filePath;
        }

		

        @Override
        public String call() throws Exception {
            FileInputStream fileInputStream = null;

            try {
                LOGGER.info("Reading file '{}'", filePath);
                fileInputStream = (FileInputStream) storageService.retrieve(filePath);
                byte[] file = fileInputStream.readAllBytes();
                return new String(file);
            } catch (IOException ex) {
                LOGGER.warn("Something went wrong while reading file..", ex);
                return null;
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException ex) {
                        LOGGER.warn("Something went wrong while trying to close file..", ex);
                    }
                }
            }
        }


	}


}
