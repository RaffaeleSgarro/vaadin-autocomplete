package com.zybnet.autocomplete.shared;

import com.vaadin.shared.communication.ServerRpc;

public interface AutocompleteServerRpc extends ServerRpc {
  public void onQuery(String query);
}
