package net.rptools.parsetool.ui.components;

import java.util.ArrayList;
import java.util.Collection;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TitledPanes extends Application {

  public static void main(String [] args){ launch(args); }

  @Override
  public void start(Stage primaryStage) throws Exception {

    HBox root = new HBox();

    VBox noaccordion = new VBox();
    noaccordion.getChildren().addAll(this.createPanes());

    VBox yesaccordion = new VBox();
    Accordion acc = new Accordion();
    acc.getPanes().addAll(this.createPanes());
    yesaccordion.getChildren().add(acc);

    root.getChildren().addAll(noaccordion, yesaccordion);
    primaryStage.setScene(new Scene(root,800,400));
    primaryStage.show();
  }

  private Collection<TitledPane> createPanes(){

    Collection<TitledPane> result = new ArrayList<>();
    TitledPane tp = new TitledPane();
    tp.setText("Pane 1");
    tp.setContent(new TextArea("Random text..."));
    result.add(tp);
    tp = new TitledPane();
    tp.setText("Pane 2");
    tp.setContent(new TextArea("Random text..."));
    result.add(tp);
    tp = new TitledPane();
    tp.setText("Pane 3");
    tp.setContent(new TextArea("Random text..."));
    result.add(tp);
    return result;

  }
}


