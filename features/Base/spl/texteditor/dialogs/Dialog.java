package spl.texteditor.dialogs;

import java.util.Map;

public interface Dialog<T> {
    /**
     * A blocking call that opens a dialog
     * @param args the arguments you want to provide to the dialog (ex. title)
     * @return a value you want to return (ex. user input)
     */
    T openAndWait(Map<String, String> args);
}
