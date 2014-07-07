package com.zybnet.autocomplete.server;

import com.zybnet.autocomplete.shared.AutocompleteFieldSuggestion;

public interface AutocompleteSuggestionPickedListener {

  void onSuggestionPicked(AutocompleteFieldSuggestion suggestion);

}
