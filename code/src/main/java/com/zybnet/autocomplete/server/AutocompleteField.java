package com.zybnet.autocomplete.server;

import java.util.Collections;
import java.util.List;

import com.vaadin.ui.AbstractField;
import com.zybnet.autocomplete.shared.AutocompleteServerRpc;
import com.zybnet.autocomplete.shared.AutocompleteState;
import com.zybnet.autocomplete.shared.SuggestionImpl;

@SuppressWarnings("serial")
public class AutocompleteField extends AbstractField<String> implements AutocompleteServerRpc{
  
  private AutocompleteQueryListener queryListener;
  
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

  public void setChoices(List<SuggestionImpl> suggestions) {
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
}