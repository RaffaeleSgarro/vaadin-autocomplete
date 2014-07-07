package com.zybnet.app;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.zybnet.autocomplete.server.AutocompleteField;
import com.zybnet.autocomplete.server.AutocompleteQueryListener;
import com.zybnet.autocomplete.server.AutocompleteSuggestionPickedListener;
import com.zybnet.autocomplete.shared.AutocompleteFieldSuggestion;

@Theme("mytheme")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI {

  private final VerticalLayout layout = new VerticalLayout();

  @WebServlet(value = "/*", asyncSupported = true)
  @VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class, widgetset = "com.zybnet.app.AppWidgetSet")
  public static class Servlet extends VaadinServlet {
  }

  @Override
  protected void init(VaadinRequest request) {

    layout.setMargin(true);
    layout.setSpacing(true);
    setContent(layout);

    AutocompleteField search = new AutocompleteField();
    search.setDelay(200);
    search.setWidth("400px");
    search.setCaption("Search 'java'");
    
    search.setQueryListener(new AutocompleteQueryListener() {
      @Override
      public void handleUserQuery(AutocompleteField field, String query) {
        handleSearchQuery(field, query);
      }
    });
    
    search.setSuggestionPickedListener(new AutocompleteSuggestionPickedListener() {
      
      @Override
      public void onSuggestionPicked(AutocompleteFieldSuggestion suggestion) {
        handleSuggestionSelection(suggestion);
      }
    });
    
    Button setShortDelayBtn = new Button("Set short search delay");
    setShortDelayBtn.addClickListener(new UpdateDelay(search, 200));
    
    Button setLongDelayBtn = new Button("Set long search delay");
    setLongDelayBtn.addClickListener(new UpdateDelay(search, 1000));

    layout.addComponents(search, setShortDelayBtn, setLongDelayBtn);
  }
  
  private static class UpdateDelay implements Button.ClickListener {

    private final AutocompleteField search;
    private final int delay;
    
    public UpdateDelay(AutocompleteField search, int delay) {
      this.search = search;
      this.delay = delay;
    }

    @Override
    public void buttonClick(ClickEvent event) {
      search.setDelay(delay);
    }
  }

  protected void handleSuggestionSelection(AutocompleteFieldSuggestion suggestion) {
    Notification.show("Selected " + suggestion.getDisplayString() + ", ID: " + suggestion.getId());
  }
  
  private void handleSearchQuery(AutocompleteField field, String query) {
    try {
      // Simulate a slow query
      Thread.sleep(1000);
      List<AutocompleteFieldSuggestion> suggestions = new ArrayList<AutocompleteFieldSuggestion>();
      for (int i = 0; i < 10; i++) {
        AutocompleteFieldSuggestion suggestion = new AutocompleteFieldSuggestion();
        suggestion.setId(i + 1);
        suggestion.setDisplayString((i + 1) + ": " + query);
        suggestions.add(suggestion);
      }
      field.setChoices(suggestions);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    
  }

}
