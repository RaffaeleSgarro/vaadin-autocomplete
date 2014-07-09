package com.zybnet.autocomplete.shared;

import com.vaadin.shared.annotations.Delayed;
import com.vaadin.shared.communication.ClientRpc;

public interface AutocompleteClientRpc extends ClientRpc {
  @Delayed public void setText(String text);
}
