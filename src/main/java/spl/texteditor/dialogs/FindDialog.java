package spl.texteditor.dialogs; 

import javafx.application.Platform; 
import javafx.geometry.Insets; 
import javafx.scene.control.ButtonBar; 
import javafx.scene.control.ButtonType; 
import javafx.scene.control.TextField; 
import javafx.scene.control.CheckBox; 
import javafx.scene.layout.GridPane; 
import javafx.util.Callback; 
import org.slf4j.Logger; 
import org.slf4j.LoggerFactory; 

import java.util.Map; 
import java.util.NoSuchElementException; 

public   class  FindDialog  implements Dialog<FindResult> {
	
    private static final Logger LOGGER  = LoggerFactory.getLogger(FindDialog.class);

	

    @Override
    public FindResult openAndWait(Map<String, String> args) {
        LOGGER.info("Find and replace dialog opened");
        javafx.scene.control.Dialog findDialog = new javafx.scene.control.Dialog();
        configureLabels(args, findDialog);
        configureControls(findDialog);

        return (FindResult) findDialog
                .showAndWait()
                .orElse(new FindResult("", "", false));
    }

	

    private void configureControls(javafx.scene.control.Dialog<FindResult> findDialog) {
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);

        findDialog
                .getDialogPane()
                .getButtonTypes()
                .addAll(okButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 10, 10, 10));

        TextField findTextField = new TextField();
        TextField replaceTextField = new TextField();
        CheckBox caseSensitive = new CheckBox("Case sensitive");
        findTextField.setPromptText("Find");
        replaceTextField.setPromptText("Replace");
        gridPane.add(findTextField, 0, 0);
        gridPane.add(caseSensitive, 0, 2);
        gridPane.add(replaceTextField, 0, 1);

        findDialog.getDialogPane().setContent(gridPane);

        findDialog.setResultConverter(new Callback<ButtonType, FindResult>() {
            @Override
            public FindResult call(ButtonType buttonType) {
                boolean okButtonClicked = okButtonType == buttonType;

                if (okButtonClicked) {
                    LOGGER.info("Find and replaceTextField dialog completed with: find {}, replace: {}", findTextField.getText(), replaceTextField.getText());
                    return new FindResult(findTextField.getText(), replaceTextField.getText(), caseSensitive.isSelected());
                }

                LOGGER.info("Find and replaceTextField dialog cancelled");
                return new FindResult("", "", false);
            }
        });
    }

	

    private void configureLabels  (Map<String, String> args, javafx.scene.control.Dialog<FindResult> findDialog) {
        String title = args.get("title");
        String content = args.get("content");
        findDialog.setTitle(title != null ? title : "Find and replace");
        findDialog.setContentText(content != null ? title : "Enter a text that you want to be replaced");
    }


}
