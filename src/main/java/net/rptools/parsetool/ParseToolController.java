package net.rptools.parsetool;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.scene.text.TextLine;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import net.rptools.parser.Expression;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;
import net.rptools.parser.VariableResolver;

import java.util.*;

import static java.util.Arrays.asList;
import static javafx.scene.input.KeyCode.UP;

/**
 * Created by smaudet on 3/5/17.
 */
public class ParseToolController
//  implements Initializable
{


  private Parser parser;


  private SmartVariableResolver variableResolver;
  private List<String> history = new ArrayList<>();
  int lastIdx = 0;

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
  @FXML private ScrollPane variablesScrollPane;
  @FXML private ListView<String> variablesListView;

  Set<String> lastSet;
  ObservableList<String> observable;

  public void evalExpression(ActionEvent actionEvent) {
    String text = evalEntry.getText();
    evalEntry.setText("");
    Expression expression;
    Object evaluate;
    try {
      expression = parser.parseExpression(text);
      evaluate = expression.evaluate();
      history.add(text);
      lastIdx++;
    } catch (ParserException e) {
      resultArea.appendText(e.getLocalizedMessage()+"\n");
      return;
    }
    Set<String> variableNameSet;
    if(null == observable) {
      variableNameSet = variableResolver.getVariableNames();
      ArrayList<String> variableNames = Lists.newArrayList(variableNameSet);
      observable = new ObservableListWrapper<>(variableNames);
      variablesListView.setItems(observable);
    } else {
      variableNameSet = variableResolver.getVariableNames();
      Set<String> diff = new HashSet<>();
      diff.addAll(variableNameSet);
      diff.removeAll(lastSet);
      observable.addAll(diff);
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
        default:
          //noop
          break;
      }
    }
  }

//  @Override
//  public void initialize(URL location, ResourceBundle resources) {
//    evalArea = (TextArea) mainScroll.lookup("#resultArea");
//  }

}
