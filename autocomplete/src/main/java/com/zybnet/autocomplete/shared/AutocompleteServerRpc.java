package com.zybnet.autocomplete.shared;

import com.vaadin.shared.annotations.Delayed;
import com.vaadin.shared.communication.ServerRpc;

public interface AutocompleteServerRpc extends ServerRpc {
  public void onQuery(String query);
  public void onSuggestionPicked(AutocompleteFieldSuggestion suggestion);
  @Delayed public void onTextValueChanged(String text);
}
