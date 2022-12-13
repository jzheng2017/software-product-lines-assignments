package spl.texteditor.plugin.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * A task that discovers jars (-> plugins) which then are dispatched to be loaded in the plugin system.
 */
public class DiscoverAndLoadPluginTask implements Callable<List<String>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscoverAndLoadPluginTask.class);
    private PluginManager pluginManager;
    private final String directoryPath;

    public DiscoverAndLoadPluginTask(PluginManager pluginManager, String directoryPath) {
        this.pluginManager = pluginManager;
        this.directoryPath = directoryPath;
    }

    @Override
    public List<String> call() throws Exception {
        Set<String> plugins = new HashSet(pluginManager.listPlugins());
        LOGGER.debug("Trying to discover new plugins..");
        List<File> discoveredPlugins = getAllJars(directoryPath);
        List<String> loadedPlugins = new ArrayList();

        for (File plugin : discoveredPlugins) {
            final String pluginNameWithoutJarExtension = plugin.getName().substring(0, plugin.getName().lastIndexOf("."));
            if (!plugins.contains(pluginNameWithoutJarExtension)) {
                LOGGER.info("New plugin '{}' found in directory '{}'", pluginNameWithoutJarExtension, directoryPath);
                pluginManager.loadPlugin(plugin.toPath());
                loadedPlugins.add(pluginNameWithoutJarExtension);
                pluginManager.startPlugin(pluginNameWithoutJarExtension);
            }
        }

        if (loadedPlugins.isEmpty()) {
            LOGGER.debug("No new plugins found.");
        } else {
            LOGGER.info("Loaded {} new plugins.", loadedPlugins.size());
        }

        return loadedPlugins;
    }

    private List<File> getAllJars(String pathDir) {
        File files = new File(pathDir);

        return Arrays.stream(files.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".jar");
            }
        })).toList();
    }
}
