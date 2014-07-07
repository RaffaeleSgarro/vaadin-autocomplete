package com.zybnet.autocomplete.server;

import java.util.Collections;
import java.util.List;

import com.vaadin.ui.AbstractField;
import com.zybnet.autocomplete.shared.AutocompleteClientRpc;
import com.zybnet.autocomplete.shared.AutocompleteServerRpc;
import com.zybnet.autocomplete.shared.AutocompleteState;
import com.zybnet.autocomplete.shared.AutocompleteFieldSuggestion;

@SuppressWarnings("serial")
public class AutocompleteField extends AbstractField<String> implements AutocompleteServerRpc{
  
  private String text;
  private AutocompleteQueryListener queryListener;
  private AutocompleteSuggestionPickedListener suggestionPickedListener;
  
  public AutocompleteField() {
    registerRpc(this, AutocompleteServerRpc.class);
  }
  
  @Override
  public Class<String> getType() {
    return String.class;
  }
  
  public void clearChoices() {
    getState().suggestions = Collections.emptyList();
  }

  public void setChoices(List<AutocompleteFieldSuggestion> suggestions) {
    getState().suggestions = suggestions;
  }
  
  @Override
  public AutocompleteState getState() {
    return (AutocompleteState) super.getState();
  }
  
  public void onQuery(String query) {
    if (queryListener != null) {
      queryListener.handleUserQuery(this, query);
    }
  }
  
  public void setQueryListener(AutocompleteQueryListener listener) {
    this.queryListener = listener;
  }

  @Override
  public void onSuggestionPicked(AutocompleteFieldSuggestion suggestion) {
    setText(suggestion.getDisplayString());
    if (suggestionPickedListener != null) suggestionPickedListener.onSuggestionPicked(suggestion);
  }
  
  public void setSuggestionPickedListener(AutocompleteSuggestionPickedListener listener) {
    this.suggestionPickedListener = listener;
  }

  public void setDelay(int delayMillis) {
    getState().delayMillis = delayMillis;
  }
  
  public void setText(String text) {
    this.text = text;
    getRpcProxy(AutocompleteClientRpc.class).setText(text);
  }
  
  public String getText() {
    return text;
  }

  @Override
  public void onTextValueChanged(String text) {
    this.text = text;
  }
}