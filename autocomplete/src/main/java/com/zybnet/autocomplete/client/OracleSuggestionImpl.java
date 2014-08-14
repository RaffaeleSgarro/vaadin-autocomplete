package com.zybnet.autocomplete.client;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.zybnet.autocomplete.shared.AutocompleteFieldSuggestion;

public class OracleSuggestionImpl implements Suggestion {
  
  private final AutocompleteFieldSuggestion wrappedSuggestion;
  private final String displayString;
  private final String replacementString;
  
  public OracleSuggestionImpl(AutocompleteFieldSuggestion wrappedSuggestion) {
    this.wrappedSuggestion = wrappedSuggestion;
    replacementString = wrappedSuggestion.getReplacementString();
    displayString = wrappedSuggestion.getDisplayString();
  }

  @Override
  public String getDisplayString() {
    return displayString;
  }

  @Override
  public String getReplacementString() {
    return replacementString;
  }
  
  public AutocompleteFieldSuggestion getWrappedSuggestion() {
    return wrappedSuggestion;
  }

}
