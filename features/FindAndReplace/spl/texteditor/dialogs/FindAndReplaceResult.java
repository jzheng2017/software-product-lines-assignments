package spl.texteditor.dialogs;

public class FindAndReplaceResult {
    private String textToFind;
    private String replacementText;

    public FindAndReplaceResult(String textToFind, String replacementText) {
        this.textToFind = textToFind;
        this.replacementText = replacementText;
    }

    public String getTextToFind() {
        return textToFind;
    }

    public String getReplacementText() {
        return replacementText;
    }

    public boolean isValid() {
        return !textToFind.isEmpty();
    }
}