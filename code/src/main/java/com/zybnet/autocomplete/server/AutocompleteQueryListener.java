package com.zybnet.autocomplete.server;

public interface AutocompleteQueryListener<E> {
  void handleUserQuery(AutocompleteField<E> autocompleteField, String query);
}
