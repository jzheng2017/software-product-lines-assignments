package spl.texteditor.plugin.core; 

/**
 * An observer interface that allows the implementing class to observe {@link PluginObservable} classes and get notified when plugins are added/removed from the system
 */
public  interface  PluginObserver {
	
    String getName();

	

    /**
     * Logic that should be executed when a plugin is added to the system
     */
    void onPluginAdded(Plugin plugin);

	
    /**
     * Logic that should be executed when a plugin is removed from the system
     */
    void onPluginRemoved(Plugin plugin);


}
