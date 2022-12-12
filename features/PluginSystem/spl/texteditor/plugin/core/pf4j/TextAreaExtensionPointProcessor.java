package spl.texteditor.plugin.core.pf4j;

import org.fxmisc.richtext.CodeArea;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spl.texteditor.plugin.core.Plugin;
import spl.texteditor.plugin.core.PluginObserver;
import spl.texteditor.plugin.core.pf4j.extensionpoints.TextAreaExtensionPoint;

/**
 * A class that processes all {@link TextAreaExtensionPoint} which are discovered in the system
 */
public class TextAreaExtensionPointProcessor implements PluginObserver {
    private static final Logger LOGGER = LoggerFactory.getLogger(TextAreaExtensionPointProcessor.class);
    private PluginManager pluginManager;
    private CodeArea textArea;

    public TextAreaExtensionPointProcessor(PluginManager pluginManager, CodeArea textArea) {
        this.pluginManager = pluginManager;
        this.textArea = textArea;
    }

    @Override
    public void onPluginAdded(Plugin plugin) {
        LOGGER.info("New plugin added '{}'", plugin.getName());
        for (TextAreaExtensionPoint textAreaExtensionPoint : pluginManager.getExtensions(TextAreaExtensionPoint.class)) {
            LOGGER.debug("Extension found '{}'", textAreaExtensionPoint.getClass().getSimpleName());
            textAreaExtensionPoint.extendTextArea(textArea);
        }
    }

    @Override
    public void onPluginRemoved(Plugin plugin) {
        //TODO
    }

    @Override
    public String getName() {
        return "text area extension point processor";
    }
}
