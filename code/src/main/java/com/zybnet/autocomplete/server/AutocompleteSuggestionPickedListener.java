package com.zybnet.autocomplete.server;

public interface AutocompleteSuggestionPickedListener<E> {

  void onSuggestionPicked(E suggestion);

}
