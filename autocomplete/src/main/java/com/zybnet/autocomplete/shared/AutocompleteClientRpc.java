package com.zybnet.autocomplete.shared;

import com.vaadin.shared.communication.ClientRpc;

import java.util.List;

public interface AutocompleteClientRpc extends ClientRpc {
  void showSuggestions(List<AutocompleteFieldSuggestion> suggestions);
}
