package spl.texteditor.dialogs;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.NoSuchElementException;

public class FindAndReplaceDialog implements Dialog<FindAndReplaceResult> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FindAndReplaceDialog.class);

    @Override
    public FindAndReplaceResult openAndWait(Map<String, String> args) {
        LOGGER.info("Find and replace dialog opened");
        javafx.scene.control.Dialog findAndReplaceDialog = new javafx.scene.control.Dialog();
        configureLabels(args, findAndReplaceDialog);
        configureControls(findAndReplaceDialog);

        try {
            return (FindAndReplaceResult)findAndReplaceDialog.showAndWait().get();
        } catch (NoSuchElementException ex) {
            return new FindAndReplaceResult("", "");
        }
    }

    private static void configureControls(javafx.scene.control.Dialog<FindAndReplaceResult> findAndReplaceDialog) {
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);

        findAndReplaceDialog
                .getDialogPane()
                .getButtonTypes()
                .addAll(okButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 10, 10, 10));

        TextField findTextField = new TextField();
        findTextField.setPromptText("Find");
        TextField replaceTextField = new TextField();
        replaceTextField.setPromptText("Replace");
        gridPane.add(findTextField, 0, 0);
        gridPane.add(replaceTextField, 0, 1);

        findAndReplaceDialog.getDialogPane().setContent(gridPane);

        findAndReplaceDialog.setResultConverter(new Callback<ButtonType, FindAndReplaceResult>() {
            @Override
            public FindAndReplaceResult call(ButtonType buttonType) {
                boolean okButtonClicked = okButtonType == buttonType;

                if (okButtonClicked) {
                    LOGGER.info("Find and replaceTextField dialog completed with: find {}, replace: {}", findTextField.getText(), replaceTextField.getText());
                    return new FindAndReplaceResult(findTextField.getText(), replaceTextField.getText());
                }

                LOGGER.info("Find and replaceTextField dialog cancelled");
                return new FindAndReplaceResult("", "");
            }
        });
    }

    private void configureLabels(Map<String, String> args, javafx.scene.control.Dialog<FindAndReplaceResult> findAndReplaceDialog) {
        String title = args.get("title");
        String content = args.get("content");
        findAndReplaceDialog.setTitle(title != null ? title : "Find and replace");
        findAndReplaceDialog.setContentText(content != null ? title: "Enter a text that you want to be replaced");
    }
}
