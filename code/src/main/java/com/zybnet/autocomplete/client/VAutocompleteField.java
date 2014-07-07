package com.zybnet.autocomplete.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.zybnet.autocomplete.shared.AutocompleteFieldSuggestion;

public class VAutocompleteField extends Composite {

  public static final String CLASSNAME = "v-autocomplete";
  public static final int DELAY_MS = 300;
  
  private final SuggestOracle oracle = new SuggestOracleImpl();
  private final SuggestBox suggestBox;
  
  private Timer sendQueryToServer = null;
  private QueryListener queryListener = null;
  private List<AutocompleteFieldSuggestion> suggestions = Collections.emptyList();
  private boolean isInitiatedFromServer = false;
  
  public VAutocompleteField() {
    suggestBox = new SuggestBox(oracle);
    initWidget(suggestBox);
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
        maybeInvokeQueryListenerLater(request.getQuery());
      }
    }
  }
  
  private void maybeInvokeQueryListenerLater(final String query) {
    
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
    
    sendQueryToServer.schedule(DELAY_MS);
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
  
}