package spl.texteditor.dialogs;

public class FindResult {
    private String textToFind;
    private String replacementText;
    private boolean caseSensitive;

    public FindResult(String textToFind, String replacementText, boolean caseSensitive) {
        this.textToFind = textToFind;
        this.replacementText = replacementText;
        this.caseSensitive = caseSensitive;
        
    }

    public String getTextToFind() {
        return textToFind;
    }
    
    public boolean getCaseSensitive() {
        return caseSensitive;
    }

    public String getReplacementText() {
        return replacementText;
    }

    public boolean isValid() {
        return !textToFind.isEmpty();
    }
}