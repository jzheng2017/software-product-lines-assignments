package spl.texteditor.plugin.core; 

import java.nio.file.Path; 
import java.util.List; 

/**
 * An interface that allows one to manage all plugins within the system
 */
public  interface  PluginManager  extends PluginObservable {
	

    void loadPlugin(Path path);

	
    void unloadPlugin(String pluginId);

	
    void startPlugin(String pluginId);

	
    void stopPlugin(String pluginId);

	
    /**
     * Retrieves a list all loaded plugins
     *
     * @return list of all loaded plugins
     */
    List<String> listPlugins();


}
