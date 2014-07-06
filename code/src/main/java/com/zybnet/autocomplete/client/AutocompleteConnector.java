package com.zybnet.autocomplete.client;

import com.zybnet.autocomplete.AutocompleteField;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;
import com.google.gwt.core.client.GWT;

@Connect(AutocompleteField.class)
@SuppressWarnings("serial")
public class AutocompleteConnector extends AbstractComponentConnector {
  
  @Override
  protected VAutocompleteField createWidget() {
    return GWT.create(VAutocompleteField.class);
  }
  
  @Override
  public VAutocompleteField getWidget() {
    return (VAutocompleteField) super.getWidget();
  }
  
}