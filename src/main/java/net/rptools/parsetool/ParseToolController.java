package net.rptools.parsetool;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.rptools.parser.Expression;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;

import java.net.URL;
import java.util.*;

import static javafx.beans.binding.Bindings.createStringBinding;

/**
 * Copyright by smaudet on 3/5/17.
 */
public class ParseToolController
  implements Initializable
{


  private Parser parser;


  private SmartVariableResolver variableResolver;
  private List<String> history = new ArrayList<>();
  int lastIdx = 0;
  private ObservableListWrapper historyObservable;

  @Inject
  public void setVariableResolver(SmartVariableResolver variableResolver) {
    this.variableResolver = variableResolver;
  }

  @Inject
  public void setParser(Parser parser) {
    this.parser = parser;
  }

  @FXML private TextArea resultArea;
  @FXML private TextField evalEntry;
  @FXML private ListView<String> variablesListView;
  @FXML private ListView<String> historyListView;

  @FXML private TitledPane variablesViewPane;
  @FXML private TitledPane historyViewPane;
  @FXML private VBox leftVBox;
  @FXML private HBox hboxHistoryView;

//  @FXML private ScrollPane leftScroll;
//
//  @FXML private TextField leftHD;
//  @FXML private TextField variablesHD;
//  @FXML private TextField historyHD;
//  @FXML private TextField leftMaxHD;

  Set<String> lastSet;
  ObservableList<String> variablesObservable;

  public void evalExpression(ActionEvent actionEvent) {
    String text = evalEntry.getText();
    evalEntry.setText("");
    Expression expression;
    Object evaluate;
    try {
      expression = parser.parseExpression(text);
      evaluate = expression.evaluate();
      history.add(text);
      historyObservable.add(text);
      lastIdx++;
    } catch (ParserException e) {
      resultArea.appendText(e.getLocalizedMessage()+"\n");
      return;
    }
    Set<String> variableNameSet;
    if(null == variablesObservable) {
      variableNameSet = variableResolver.getVariableNames();
      ArrayList<String> variableNames = Lists.newArrayList(variableNameSet);
      variablesObservable = new ObservableListWrapper<>(variableNames);
      variablesListView.setItems(variablesObservable);
    } else {
      variableNameSet = variableResolver.getVariableNames();
      Set<String> diff = new HashSet<>();
      diff.addAll(variableNameSet);
      diff.removeAll(lastSet);
      variablesObservable.addAll(diff);
    }
    lastSet = variableNameSet;
    resultArea.appendText(text+" -> "+evaluate.toString()+"\n");
  }

  public void handleKeyUp(KeyEvent keyEvent) {
    if(keyEvent.getCode().isArrowKey()) {
      switch (keyEvent.getCode()) {
        case UP:
          if (lastIdx >= 0 && history.size() > 0) {
            if(lastIdx > 0) {
              lastIdx--;
            }
            int idx = lastIdx;
            String text = history.get(idx);
            evalEntry.setText(text);
          }
          break;
        case DOWN:
          if(lastIdx <= history.size()) {
            if(lastIdx < history.size()-1){
              lastIdx++;
            }
            int idx = lastIdx;
            String text = history.get(idx);
            evalEntry.setText(text);
          }
          break;
        case RIGHT:
//          double maxViewHeight = (leftVBox.getHeight() - (variablesViewPane.getHeight()*2));
//          variablesViewPane.resize(variablesViewPane.getWidth(),maxViewHeight);
//          historyViewPane.resize(variablesViewPane.getWidth(),maxViewHeight);
          break;
        default:
          //noop
          break;
      }
    }
  }

  /**
   * Utility binding function
   * @param prop
   * @return
   */
  private static StringBinding bindFromDouble(ReadOnlyDoubleProperty prop){
    return createStringBinding(() -> prop.getValue().toString(), prop);
  }

  private DoubleBinding historyBoxBinding;

  private void changeListenerFunc(
    ObservableValue<? extends Boolean>  observable, Boolean oldValue, Boolean isExpanded) {
    if(isExpanded) {
      historyViewPane.prefHeightProperty().bind(leftVBox.heightProperty());

      historyListView.prefHeightProperty().bind(historyBoxBinding);
    } else {
      historyViewPane.prefHeightProperty().unbind();
      historyViewPane.prefHeightProperty().set(0);

      historyListView.prefHeightProperty().unbind();
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    variablesViewPane.maxHeightProperty().bind(leftVBox.heightProperty());
    historyViewPane.maxHeightProperty().bind(leftVBox.heightProperty());

    bindExpanded(variablesViewPane, leftVBox.heightProperty());
    //bind the internal historyListView to an appropriate size
    historyBoxBinding = leftVBox.heightProperty().subtract(hboxHistoryView.heightProperty());
    historyListView.maxHeightProperty().bind(historyBoxBinding);
    historyViewPane.expandedProperty().addListener(this::changeListenerFunc);
    this.changeListenerFunc(null,null,historyViewPane.isExpanded());

    historyObservable = new ObservableListWrapper<>(new ArrayList<>());
    historyListView.setItems(historyObservable);
    variablesListView.setItems(variablesObservable);

    //-----------------------
    //this was testing code

//    ArrayList<String> list = new ArrayList<>();
    for (int i = 0; i < 50; i++) {
      historyObservable.add("asdf");
    }
//    ObservableListWrapper<String> observableListWrapper;
//    observableListWrapper = new ObservableListWrapper<>(list);
//    variablesListView.setItems(observableListWrapper);
//    historyListView.setItems(observableListWrapper);

    //this may be usefull if you need to debug some property on the UI
//    StringBinding stringBinding;
//    stringBinding = bindFromDouble(variablesViewPane.heightProperty());
//    variablesHD.textProperty().bind(stringBinding);
//    stringBinding = bindFromDouble(historyViewPane.heightProperty());
//    historyHD.textProperty().bind(stringBinding);
//    stringBinding = bindFromDouble(leftVBox.heightProperty());
//    leftHD.textProperty().bind(stringBinding);
//    stringBinding = bindFromDouble(leftVBox.maxHeightProperty());
//    leftMaxHD.textProperty().bind(stringBinding);

  }

  private static void bindExpanded(TitledPane pane, ReadOnlyDoubleProperty prop) {
    pane.expandedProperty().addListener((observable, oldValue, newValue) -> {
      if(newValue) {
        pane.prefHeightProperty().bind(prop);
      } else {
        pane.prefHeightProperty().unbind();
        pane.prefHeightProperty().set(0);
      }
    });
  }

  public void clearHistory(ActionEvent actionEvent) {
    historyObservable.clear();
  }
}
