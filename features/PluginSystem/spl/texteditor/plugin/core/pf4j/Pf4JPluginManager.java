package spl.texteditor.plugin.core.pf4j;

import org.pf4j.JarPluginManager;
import org.pf4j.ExtensionPoint;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spl.texteditor.plugin.core.Plugin;
import spl.texteditor.plugin.core.PluginManager;
import spl.texteditor.plugin.core.PluginObservable;
import spl.texteditor.plugin.core.PluginObserver;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Pf4JPluginManager implements PluginManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(Pf4JPluginManager.class);
    private static Pf4JPluginManager instance;
    private List<PluginObserver> observers = new ArrayList();
    private final org.pf4j.PluginManager pluginManager = new JarPluginManager(Path.of("plugins"));

    private Pf4JPluginManager() {
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
    }

    public static Pf4JPluginManager getInstance() {
        if (instance == null) {
            instance = new Pf4JPluginManager();
        }

        return instance;
    }

    @Override
    public void loadPlugin(Path path) {
        final String pluginId = pluginManager.loadPlugin(path);
        notifyPluginAdded(new Plugin(pluginId));
    }

    @Override
    public void unloadPlugin(String pluginId) {
        pluginManager.unloadPlugin(pluginId);
        notifyPluginRemoved(new Plugin(pluginId));
    }

    @Override
    public void startPlugin(String pluginId) {
        pluginManager.startPlugin(pluginId);
    }

    @Override
    public void stopPlugin(String pluginId) {
        pluginManager.stopPlugin(pluginId);
    }

    @Override
    public List<String> listPlugins() {
        return pluginManager
                .getPlugins()
                .stream()
                .map(new Function<PluginWrapper, String>() {
                    @Override
                    public String apply(PluginWrapper pluginWrapper) {
                        return pluginWrapper.getPluginId();
                    }
                })
                .toList();
    }

    @Override
    public void addObserver(PluginObserver observer) {
        observers.add(observer);
        LOGGER.info("New observer added {}", observer.getName());
        pluginManager.getPlugins().forEach(new Consumer<PluginWrapper>() {
            @Override
            public void accept(PluginWrapper pluginWrapper) {
                observer.onPluginAdded(new Plugin(pluginWrapper.getPluginId()));
            }
        });
    }

    @Override
    public void removeObserver(PluginObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyPluginAdded(Plugin plugin) {
        observers.forEach(new Consumer<PluginObserver>() {
            @Override
            public void accept(PluginObserver observer) {
                observer.onPluginAdded(plugin);
            }
        });
    }

    @Override
    public void notifyPluginRemoved(Plugin plugin) {
        observers.forEach(new Consumer<PluginObserver>() {
            @Override
            public void accept(PluginObserver observer) {
                observer.onPluginRemoved(plugin);
            }
        });
    }

    public <T extends ExtensionPoint> List<T> getExtensions(Class<T> extensionPoint) {
        return pluginManager.getExtensions(extensionPoint);
    }

    public org.pf4j.PluginManager getInternalPluginManager() {
        return pluginManager;
    }
}
