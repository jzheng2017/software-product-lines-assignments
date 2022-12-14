package spl.texteditor.dialogs;

public class FindResult {
    private String textToFind;
    private boolean caseSensitive;

    public FindResult(String textToFind, boolean caseSensitive) {
        this.textToFind = textToFind;
        this.caseSensitive = caseSensitive;
    }

    public String getTextToFind() {
        return textToFind;
    }

    public boolean getCaseSensitive() {
        return caseSensitive;
    }
    
    public boolean isValid() {
        return !textToFind.isEmpty();
    }
}