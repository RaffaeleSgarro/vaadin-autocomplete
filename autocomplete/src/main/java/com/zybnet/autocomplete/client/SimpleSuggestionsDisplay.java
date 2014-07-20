package com.zybnet.autocomplete.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
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
  
  @Override
  public void showSuggestions(final SuggestBox suggestBox, final java.util.Collection<? extends SuggestOracle.Suggestion> suggestions, final boolean isDisplayStringHTML, final boolean isAutoSelectEnabled, final SuggestBox.SuggestionCallback callback) {
    // This horrible hack is copied from VFilterSelect
    // Apparently, Vaadin layouts mess with paddings and margins, so we need to
    // wait until the layout code has executed before computing the left/top offsets
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      @Override
      public void execute() {
        SimpleSuggestionsDisplay.super.showSuggestions(suggestBox, suggestions, isDisplayStringHTML, isAutoSelectEnabled, callback);
      }
    });
  }


}
