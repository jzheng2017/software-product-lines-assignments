package spl.texteditor.plugin.core;


public interface PluginObserver {

    String getName();


    void onPluginAdded(Plugin plugin);


    void onPluginRemoved(Plugin plugin);


}
