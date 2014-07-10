package com.zybnet.autocomplete.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SuggestBox.DefaultSuggestionDisplay;
import com.google.gwt.user.client.ui.SuggestBox.SuggestionDisplay;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.vaadin.client.ui.VTextField;
import com.zybnet.autocomplete.shared.AutocompleteFieldSuggestion;

public class VAutocompleteField extends Composite implements KeyUpHandler {

  public static final String CLASSNAME = "v-autocomplete";
  
  private final SuggestOracle oracle;
  private final SimpleSuggestionsDisplay suggestionsDisplay;
  private final VTextField textField;
  private final SuggestBox suggestBox;
  
  private int delayMillis = 300;
  private Timer sendQueryToServer = null;
  private QueryListener queryListener = null;
  private List<AutocompleteFieldSuggestion> suggestions = Collections.emptyList();
  private boolean isInitiatedFromServer = false;
  private TextChangeListener textChangeHandler;
  private boolean trimQuery = true;
  private int minimumQueryCharacters = 3;
  
  public VAutocompleteField() {
    oracle = new SuggestOracleImpl();
    suggestionsDisplay = new SimpleSuggestionsDisplay(this);
    textField = GWT.create(VTextField.class);
    suggestBox = new SuggestBox(oracle, textField, suggestionsDisplay);
    initWidget(suggestBox);
    suggestBox.getValueBox().addKeyUpHandler(this);
    setStyleName(CLASSNAME);
  }
  
  private class SuggestOracleImpl extends SuggestOracle {

    @Override
    public void requestSuggestions(Request request, Callback callback) {
      if (isInitiatedFromServer) {
        // invoke the callback
        Response response = new Response();
        response.setSuggestions(wrapSuggestions(suggestions));
        callback.onSuggestionsReady(request, response);
      } else {
        // send event to the server side
        String query = request.getQuery();
        
        if (isTrimQuery()) {
          query = query.trim();
        }
        
        if (query.length() >= getMinimumQueryCharacters()) {
          scheduleQuery(request.getQuery());
        }
      }
    }
  }
  
  private void scheduleQuery(final String query) {
    
    if (sendQueryToServer != null) {
      sendQueryToServer.cancel();
    }
    
    sendQueryToServer = new Timer() {
      @Override
      public void run() {
        sendQueryToServer = null;
        if (queryListener != null && query != null && query.equals(suggestBox.getText())) {
          queryListener.handleQuery(query);
        }
      }
    };
    
    sendQueryToServer.schedule(delayMillis);
  }
  
  private List<SuggestOracle.Suggestion> wrapSuggestions(List<AutocompleteFieldSuggestion> in) {
    List<SuggestOracle.Suggestion> out = new ArrayList<SuggestOracle.Suggestion>();
    for (final AutocompleteFieldSuggestion wrappedSuggestion : in) {
      out.add(new OracleSuggestionImpl(wrappedSuggestion));
    }
    return out;
    
  }

  public void setSuggestions(List<AutocompleteFieldSuggestion> suggestions) {
    isInitiatedFromServer = true;
    this.suggestions = Collections.unmodifiableList(suggestions);
    suggestBox.refreshSuggestionList();
    suggestBox.showSuggestionList();
    isInitiatedFromServer = false;
  }
  
  public void addSelectionHandler(SelectionHandler<Suggestion> handler) {
    suggestBox.addSelectionHandler(handler);
  }
  
  public void setQueryListener(QueryListener listener) {
    this.queryListener = listener;
  }
  
  public interface QueryListener {
    void handleQuery(String query);
  }
  
  public interface TextChangeListener {
    void onTextChange(String text);
  }

  public void setDelayMillis(int delayMillis) {
    this.delayMillis = delayMillis;
  }

  public void setDisplayedText(String text) {
    suggestBox.getValueBox().setText(text);
  }
  
  public void addTextChangeHandler(TextChangeListener handler) {
    this.textChangeHandler = handler;
  }

  public String getDisplayedText() {
    return suggestBox.getValueBox().getText();
  }

  @Override
  public void onKeyUp(KeyUpEvent event) {
    
    if (textChangeHandler != null) textChangeHandler.onTextChange(suggestBox.getText());
    
    switch (event.getNativeKeyCode()) {
    case KeyCodes.KEY_ESCAPE:
    case KeyCodes.KEY_TAB:
      SuggestionDisplay display = suggestBox.getSuggestionDisplay();
      if (display instanceof DefaultSuggestionDisplay) {
        ((DefaultSuggestionDisplay) display).hideSuggestions();
      }
      event.stopPropagation();
      break;
    }
  }

  public TextChangeListener getTextChangeHandler() {
    return textChangeHandler;
  }

  public void setTextChangeHandler(TextChangeListener textChangeHandler) {
    this.textChangeHandler = textChangeHandler;
  }

  public boolean isTrimQuery() {
    return trimQuery;
  }

  public void setTrimQuery(boolean trimQuery) {
    this.trimQuery = trimQuery;
  }

  public int getMinimumQueryCharacters() {
    return minimumQueryCharacters;
  }

  public void setMinimumQueryCharacters(int minimumQueryCharacters) {
    this.minimumQueryCharacters = minimumQueryCharacters;
  }
}