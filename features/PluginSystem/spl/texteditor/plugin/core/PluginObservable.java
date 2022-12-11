package spl.texteditor.plugin.core;

public interface PluginObservable {
    void addObserver(PluginObserver observer);
    void removeObserver(PluginObserver observer);

    void notifyPluginAdded(Plugin plugin);

    void notifyPluginRemoved(Plugin plugin);
}
