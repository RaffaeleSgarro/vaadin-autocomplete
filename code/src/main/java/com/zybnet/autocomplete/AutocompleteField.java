package com.zybnet.autocomplete;

import com.vaadin.ui.*;

@SuppressWarnings("serial")
public class AutocompleteField extends AbstractField<String> {

  @Override
  public Class<String> getType() {
    return String.class;
  }
  
}