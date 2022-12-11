package spl.texteditor; 

import javafx.application.Application; 
import javafx.fxml.FXMLLoader; 
import javafx.scene.Parent; 
import javafx.scene.Scene; 
import javafx.stage.Stage; 
import org.slf4j.Logger; 

import java.io.IOException; 
import spl.texteditor.plugin.core.PluginSystemBootstrapper; 

public   class  App  extends Application {
	

    private static Scene scene;

	

    
     private void  start__wrappee__Base(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

	

    @Override
    public void start(Stage primaryStage) throws Exception {
        start__wrappee__Base(primaryStage);
        pluginSystemBootstrapper.start();
    }

	

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

	

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

	

    public static void main(String[] args) {
        System.setProperty(org.slf4j.simple.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");
        launch();
    }

	
    private PluginSystemBootstrapper pluginSystemBootstrapper = new PluginSystemBootstrapper();


}
