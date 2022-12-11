package spl.texteditor;

import javafx.fxml.FXML;
import org.fxmisc.richtext.CodeArea;
import spl.texteditor.plugin.core.PluginManager;
import spl.texteditor.plugin.core.pf4j.Pf4JPluginManager;
import spl.texteditor.plugin.core.pf4j.TextAreaExtensionPointProcessor;

public class PrimaryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrimaryController.class);
    @FXML
    private CodeArea textArea;
    private PluginManager pluginManager = Pf4JPluginManager.getInstance();

    @FXML
    public void initialize() {
        LOGGER.info("Initialization started");
        Pf4JPluginManager pf4JPluginManager = (Pf4JPluginManager)pluginManager;
        pluginManager.addObserver(new TextAreaExtensionPointProcessor(pf4JPluginManager.getInternalPluginManager(), textArea));
    }
}
