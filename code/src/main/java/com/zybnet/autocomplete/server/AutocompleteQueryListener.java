package com.zybnet.autocomplete.server;

public interface AutocompleteQueryListener {
  void handleUserQuery(AutocompleteField autocompleteField, String query);
}
