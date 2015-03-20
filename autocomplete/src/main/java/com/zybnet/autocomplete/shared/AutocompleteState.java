package com.zybnet.autocomplete.shared;

import com.vaadin.shared.AbstractFieldState;
import com.vaadin.shared.annotations.DelegateToWidget;

@SuppressWarnings("serial")
public class AutocompleteState extends AbstractFieldState {
  public String text;
  public int delayMillis = 300;
  @DelegateToWidget public int minimumQueryCharacters = 3;
  @DelegateToWidget public boolean trimQuery = true;
}
