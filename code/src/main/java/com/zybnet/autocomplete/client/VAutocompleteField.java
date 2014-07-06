package com.zybnet.autocomplete.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.zybnet.autocomplete.shared.SuggestionImpl;

public class VAutocompleteField extends Composite {

  public static final String CLASSNAME = "v-autocomplete";
  
  private final SuggestOracle oracle = new SuggestOracleImpl();
  private final SuggestBox suggestBox;
  
  private QueryListener queryListener = null;
  private List<SuggestionImpl> suggestions = Collections.emptyList();
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
        response.setSuggestions(buildSuggestions(suggestions));
        callback.onSuggestionsReady(request, response);
      } else {
        // send event to the server side
        if (queryListener != null) queryListener.handleQuery(request.getQuery());
      }
    }
  }
  
  private List<SuggestOracle.Suggestion> buildSuggestions(List<SuggestionImpl> in) {
    List<SuggestOracle.Suggestion> out = new ArrayList<SuggestOracle.Suggestion>();
    for (final SuggestionImpl src : in) {
      out.add(new SuggestOracle.Suggestion(){
        @Override
        public String getDisplayString() {
          return src.getDisplayString();
        }

        @Override
        public String getReplacementString() {
          return src.getDisplayString();
        }
      });
    }
    return out;
    
  }

  public void setSuggestions(List<SuggestionImpl> suggestions) {
    isInitiatedFromServer = true;
    this.suggestions = Collections.unmodifiableList(suggestions);
    suggestBox.refreshSuggestionList();
    suggestBox.showSuggestionList();
    isInitiatedFromServer = false;
  }
  
  public void setQueryListener(QueryListener listener) {
    this.queryListener = listener;
  }
  
  public interface QueryListener {
    void handleQuery(String query);
  }
  
}