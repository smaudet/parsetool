package net.rptools.parsetool;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rptools.parser.Parser;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Copyright smaudet on 3/5/17.
 */
public class ParseTool extends Application {

  class ParseToolModule extends AbstractModule {

    @Override
    protected void configure() {
      SmartVariableResolver vr = new SmartVariableResolver();
      Parser parser = new Parser(vr, true);
      bind(Parser.class).toInstance(parser);
      bind(SmartVariableResolver.class).toInstance(vr);
    }
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    URL location = getClass().getResource("/main.fxml");
    if(location == null) {
      System.err.println("Err, could not find fxml");
      System.exit(1);
    }
    Parent root;
    ParseToolModule module;
    module = new ParseToolModule();
    final Injector injector = Guice.createInjector(module);
    ResourceBundle resource;
    resource = ResourceBundle.getBundle("strings");
    FXMLLoader loader = new FXMLLoader(location, resource);

    loader.setControllerFactory(injector::getInstance);

    root = loader.load();
    Scene scene;
    scene = new Scene(root, 600, 400);

    primaryStage.setTitle("Parse Tool");
    primaryStage.setScene(scene);
    primaryStage.show();

  }

  public static void main(String[] args) {
    launch(args);
  }

}
