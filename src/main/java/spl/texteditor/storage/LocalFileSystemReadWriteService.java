package spl.texteditor.storage; 

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory; 

import java.io.FileInputStream; 
import java.io.IOException; 

public  class  LocalFileSystemReadWriteService  implements ReadWriteService {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalFileSystemReadWriteService.class);

	
    private TextStorageService storageService = new FileTextStorageService();

	
    private String lastFileRead;

	
    private String lastFileWritten;

	
    private String lastFileTouched;

	
    
    @Override
    public String read(String identifier) {
        this.lastFileRead = identifier;
        this.lastFileTouched = identifier;
        FileInputStream fileInputStream = null;
        
        try {
            LOGGER.info("Reading file '{}'", identifier);
            fileInputStream = (FileInputStream) storageService.retrieve(identifier);
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

	

    @Override
    public void write(String identifier, String content) {
        this.lastFileWritten = identifier;
        this.lastFileTouched = identifier;

        LOGGER.info("Start writing to file '{}'", identifier);
        storageService.store(identifier, content, true);
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


}
