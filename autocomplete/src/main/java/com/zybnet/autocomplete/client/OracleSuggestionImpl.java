package com.zybnet.autocomplete.client;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.zybnet.autocomplete.shared.AutocompleteFieldSuggestion;

public class OracleSuggestionImpl implements Suggestion {
  
  private final String displayString;
  private final AutocompleteFieldSuggestion wrappedSuggestion;
  
  public OracleSuggestionImpl(AutocompleteFieldSuggestion wrappedSuggestion) {
    this.wrappedSuggestion = wrappedSuggestion;
    this.displayString = wrappedSuggestion.getDisplayString();
  }

  @Override
  public String getDisplayString() {
    return displayString;
  }

  @Override
  public String getReplacementString() {
    return displayString;
  }
  
  public AutocompleteFieldSuggestion getWrappedSuggestion() {
    return wrappedSuggestion;
  }

}
