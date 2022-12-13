package spl.texteditor.plugin.core; 

/**
 * An observable interface that allows the implementing class to be observed by {@link PluginObserver}s. It can notified the registered observers if a plugin is added to or removed from the system.
 */
public  interface  PluginObservable {
	
    void addObserver(PluginObserver observer);

	
    void removeObserver(PluginObserver observer);

	
    /**
     * Notify all observers that a plugin added to the system
     */
    void notifyPluginAdded(Plugin plugin);

	

    /**
     * Notify all observers that a plugin removed from the system
     *
     * @param plugin
     */
    void notifyPluginRemoved(Plugin plugin);


}
