package com.zybnet.autocomplete.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.ui.AbstractField;
import com.zybnet.autocomplete.shared.AutocompleteClientRpc;
import com.zybnet.autocomplete.shared.AutocompleteServerRpc;
import com.zybnet.autocomplete.shared.AutocompleteState;
import com.zybnet.autocomplete.shared.AutocompleteFieldSuggestion;

@SuppressWarnings("serial")
public class AutocompleteField<E> extends AbstractField<String> implements AutocompleteServerRpc {
  
  private String text;
  private AutocompleteQueryListener<E> queryListener;
  private AutocompleteSuggestionPickedListener<E> suggestionPickedListener;
  private Map<Integer, E> items = new HashMap<Integer, E>();

  public AutocompleteField() {
    registerRpc(this, AutocompleteServerRpc.class);
  }
  
  @Override
  public Class<String> getType() {
    return String.class;
  }
  
  public void clearChoices() {
    getState().suggestions = Collections.emptyList();
    items = new HashMap<Integer, E>();
  }
  
  @Override
  public AutocompleteState getState() {
    return (AutocompleteState) super.getState();
  }
  
  public void onQuery(String query) {
    clearChoices();
    if (queryListener != null) {
      queryListener.handleUserQuery(this, query);
    }
  }
  
  public void setQueryListener(AutocompleteQueryListener<E> listener) {
    this.queryListener = listener;
  }

  @Override
  public void onSuggestionPicked(AutocompleteFieldSuggestion suggestion) {
    setText(suggestion.getDisplayString());
    if (suggestionPickedListener != null) suggestionPickedListener.onSuggestionPicked(items.get(suggestion.getId()));
  }
  
  public void setSuggestionPickedListener(AutocompleteSuggestionPickedListener<E> listener) {
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
    // TODO ugly. We must avoid to call setText() because that whould
    // send the value back to the client
    this.text = text;
  }
  
  public void addSuggestion(E id, String title) {
    int index = getState().suggestions.size();
    items.put(index, id);
    List<AutocompleteFieldSuggestion> newSuggestionList = new ArrayList<AutocompleteFieldSuggestion>(getState().suggestions);
    AutocompleteFieldSuggestion suggestion = new AutocompleteFieldSuggestion();
    suggestion.setId(index);
    suggestion.setDisplayString(title);
    newSuggestionList.add(suggestion);
    getState().suggestions = newSuggestionList;
  }
  
  public void setMinimumQueryCharacters(int minimumQueryCharacters) {
    getState().minimumQueryCharacters = minimumQueryCharacters;
  }
  
  public void setTrimQuery(boolean trimQuery) {
    getState().trimQuery = trimQuery;
  }
}