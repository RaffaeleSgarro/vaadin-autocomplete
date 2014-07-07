package com.zybnet.autocomplete.shared;

import java.util.Collections;
import java.util.List;

import com.vaadin.shared.AbstractFieldState;

@SuppressWarnings("serial")
public class AutocompleteState extends AbstractFieldState {
  
  public List<AutocompleteFieldSuggestion> suggestions = Collections.emptyList();
  public int delayMillis = 300;
  
}
