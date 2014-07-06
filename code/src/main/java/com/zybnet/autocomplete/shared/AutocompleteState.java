package com.zybnet.autocomplete.shared;

import java.util.Collections;
import java.util.List;

import com.vaadin.shared.AbstractFieldState;

@SuppressWarnings("serial")
public class AutocompleteState extends AbstractFieldState {
  
  public List<SuggestionImpl> suggestions = Collections.emptyList();
  
}
