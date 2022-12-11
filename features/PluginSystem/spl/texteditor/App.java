package spl.texteditor;

import javafx.application.Application;
import javafx.stage.Stage;
import spl.texteditor.plugin.core.PluginSystemBootstrapper;

public class App extends Application {
    private PluginSystemBootstrapper pluginSystemBootstrapper = new PluginSystemBootstrapper();

    @Override
    public void start(Stage primaryStage) throws Exception {
        original(primaryStage);
        pluginSystemBootstrapper.start();
    }
}
