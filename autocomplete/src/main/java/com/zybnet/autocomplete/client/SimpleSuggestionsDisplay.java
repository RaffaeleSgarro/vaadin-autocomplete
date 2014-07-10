package com.zybnet.autocomplete.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ui.VOverlay;

public class SimpleSuggestionsDisplay extends SuggestBox.DefaultSuggestionDisplay {

  private final Widget widget;
  
  public SimpleSuggestionsDisplay(Widget widget) {
    this.widget = widget;
  }
  
  @Override
  public PopupPanel createPopup() {
    VOverlay popup = GWT.create(VOverlay.class);
    popup.setOwner(widget);
    popup.setStyleName("gwt-SuggestBoxPopup");
    popup.setAutoHideEnabled(true);
    return popup;
  }

}
