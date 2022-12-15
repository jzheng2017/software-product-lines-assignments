package spl.texteditor;

import javafx.scene.control.Tab;
import org.fxmisc.richtext.CodeArea;
import spl.texteditor.storage.ReadWriteService;

public class CodeAreaWithTabAndReadWriteService {
    private CodeArea codeArea;
    private ReadWriteService readWriteService;
    private Tab tab;

    public CodeAreaWithTabAndReadWriteService(CodeArea codeArea, ReadWriteService readWriteService, Tab tab) {
        this.codeArea = codeArea;
        this.readWriteService = readWriteService;
        this.tab = tab;
    }

    public ReadWriteService readWriteService() {
        return readWriteService;
    }

    public CodeArea codeArea() {
        return codeArea;
    }

    public Tab tab() {
        return tab;
    }
}

