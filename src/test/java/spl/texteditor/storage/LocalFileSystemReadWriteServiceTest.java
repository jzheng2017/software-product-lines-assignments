package spl.texteditor.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LocalFileSystemReadWriteServiceTest {
    @InjectMocks
    private LocalFileSystemReadWriteService sut;
    @Mock
    private TextStorageService textStorageService;
    @Mock
    private FileInputStream mockedFileInputStream;
    private final String filePath = "boop";
    private final String fileContents = "Friet > Patat";

    @BeforeEach
    void setup() throws IOException {
        MockitoAnnotations.openMocks(this);
        when(textStorageService.retrieve(any())).thenReturn(mockedFileInputStream);
        when(mockedFileInputStream.readAllBytes()).thenReturn(fileContents.getBytes());
    }

    @Test
    void testThatReadingFromFileTheCorrectPathIsPassedIn() {
        sut.read(filePath);

        Mockito.verify(textStorageService).retrieve(filePath);
    }

    @Test
    void testThatReadingFromFileGetsCorrectContent() {
        String actualContent = sut.read(filePath);
        String expectedContent = fileContents;

        assertEquals(expectedContent, actualContent);
    }

    @Test
    void testThatReadingFileUpdatesLastFileReadInformation() {
        assertNull(sut.lastFileRead());
        sut.read(filePath);
        String expectedFilePath = filePath;

        assertEquals(expectedFilePath, sut.lastFileRead());
    }

    @Test
    void testThatWritingToFilePassesInCorrectArguments() throws ExecutionException, TimeoutException, InterruptedException {
        Future<?> future = sut.write(filePath, fileContents);
        future.get(1, TimeUnit.SECONDS);
        verify(textStorageService).store(filePath, fileContents, true);
    }

    @Test
    void testThatWritingToFileUpdatesLastFileWrittenInformation() {
        assertNull(sut.lastFileWritten());
        sut.write(filePath, fileContents);

        String expectedFilePath = filePath;

        assertEquals(expectedFilePath, sut.lastFileWritten());
    }

    @Test
    void testThatReadingOrWritingUpdatesLastFileTouchedInformation() {
        assertNull(sut.lastFileTouched());
        sut.write(filePath, fileContents);

        String expectedFilePath = filePath;

        assertEquals(expectedFilePath, sut.lastFileTouched());
        expectedFilePath += ".";

        sut.read(expectedFilePath);

        assertEquals(expectedFilePath, sut.lastFileTouched());

    }
}
