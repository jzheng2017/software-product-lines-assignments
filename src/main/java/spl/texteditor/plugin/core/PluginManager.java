package spl.texteditor.plugin.core;

import java.nio.file.Path;
import java.util.List;


public interface PluginManager extends PluginObservable {


    void loadPlugin(Path path);


    void unloadPlugin(String pluginId);


    void startPlugin(String pluginId);


    void stopPlugin(String pluginId);


    List<String> listPlugins();


}
