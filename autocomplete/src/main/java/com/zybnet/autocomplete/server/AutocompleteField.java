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
    getRpcProxy(AutocompleteClientRpc.class).showSuggestions(Collections.<AutocompleteFieldSuggestion>emptyList());
    items.clear();
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
    getState().text = text;
  }
  
  public String getText() {
    return getState().text;
  }
  
  public void setTabIndex(int tabIdx) {
    getState().tabIndex = tabIdx;
  }
  
  public void setEnabled(boolean enabled) {
    getState().enabled = enabled;
  }

  public void showSuggestions(final Map<E, String> suggestions) {
    items.clear();
    final List<AutocompleteFieldSuggestion> list = new ArrayList<AutocompleteFieldSuggestion>();
    int i = 0;
    for (Map.Entry<E, String> entry : suggestions.entrySet()) {
        items.put(i, entry.getKey());
        list.add(new AutocompleteFieldSuggestion(i, entry.getValue()));
        i++;
    }
    getRpcProxy(AutocompleteClientRpc.class).showSuggestions(list);
  }

  public void setMinimumQueryCharacters(int minimumQueryCharacters) {
    getState().minimumQueryCharacters = minimumQueryCharacters;
  }
  
  public void setTrimQuery(boolean trimQuery) {
    getState().trimQuery = trimQuery;
  }
}