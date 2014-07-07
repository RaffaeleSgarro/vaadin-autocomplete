package com.zybnet.autocomplete.client;

import com.zybnet.autocomplete.server.AutocompleteField;
import com.zybnet.autocomplete.shared.AutocompleteClientRpc;
import com.zybnet.autocomplete.shared.AutocompleteServerRpc;
import com.zybnet.autocomplete.shared.AutocompleteState;
import com.zybnet.autocomplete.shared.AutocompleteFieldSuggestion;
import com.vaadin.client.annotations.OnStateChange;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

@Connect(AutocompleteField.class)
@SuppressWarnings("serial")
public class AutocompleteConnector extends AbstractComponentConnector implements VAutocompleteField.QueryListener, VAutocompleteField.TextChangeListener, SelectionHandler<Suggestion>, AutocompleteClientRpc {
  
  private final AutocompleteServerRpc serverComponent;
  
  public AutocompleteConnector() {
    serverComponent = RpcProxy.create(AutocompleteServerRpc.class, this);
    getWidget().setQueryListener(this);
    getWidget().addSelectionHandler(this);
    getWidget().addTextChangeHandler(this);
    registerRpc(AutocompleteClientRpc.class, this);
  }
  
  @Override
  protected VAutocompleteField createWidget() {
    return GWT.create(VAutocompleteField.class);
  }
  
  @Override
  public VAutocompleteField getWidget() {
    return (VAutocompleteField) super.getWidget();
  }
  
  @Override
  public AutocompleteState getState() {
    return (AutocompleteState) super.getState();
  }
  
  @OnStateChange("suggestions")
  private void updateSuggestions() {
    getWidget().setSuggestions(getState().suggestions);
  }
  
  @OnStateChange("delayMillis")
  private void updateDelayMillis() {
    getWidget().setDelayMillis(getState().delayMillis);
  }

  @Override
  public void handleQuery(String query) {
    RpcProxy.create(AutocompleteServerRpc.class, this).onQuery(query);
  }

  @Override
  public void onSelection(SelectionEvent<Suggestion> event) {
    AutocompleteFieldSuggestion suggestion = ((OracleSuggestionImpl) event.getSelectedItem()).getWrappedSuggestion();
    serverComponent.onSuggestionPicked(suggestion);
  }

  @Override
  public void onTextChange(String text) {
    serverComponent.onTextValueChanged(text);
  }

  @Override
  public void setText(String text) {
    getWidget().setDisplayedText(text);
  }
}