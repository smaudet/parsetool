package net.rptools.parsetool;

import net.rptools.CaseInsensitiveHashMap;
import net.rptools.parser.ParserException;
import net.rptools.parser.VariableModifiers;
import net.rptools.parser.VariableResolver;

import java.util.Map;
import java.util.Set;

/**
 * Created by smaudet on 3/11/17.
 */
public class SmartVariableResolver implements VariableResolver {
  private final Map<String, Object> variables = new CaseInsensitiveHashMap();

  public SmartVariableResolver() {
  }

  public Set<String> getVariableNames() {
    return variables.keySet();
  }

  public boolean containsVariable(String name) throws ParserException {
    return this.containsVariable(name, VariableModifiers.None);
  }

  public void setVariable(String name, Object value) throws ParserException {
    this.setVariable(name, VariableModifiers.None, value);
  }

  public Object getVariable(String variableName) throws ParserException {
    return this.getVariable(variableName, VariableModifiers.None);
  }

  public boolean containsVariable(String name, VariableModifiers vType) throws ParserException {
    return this.variables.containsKey(name);
  }

  public void setVariable(String name, VariableModifiers vType, Object value) throws ParserException {
    this.variables.put(name, value);
  }

  public Object getVariable(String variableName, VariableModifiers vType) throws ParserException {
    return this.variables.get(variableName);
  }
}
