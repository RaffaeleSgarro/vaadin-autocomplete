package app;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.zybnet.autocomplete.server.AutocompleteField;
import com.zybnet.autocomplete.server.AutocompleteQueryListener;
import com.zybnet.autocomplete.server.AutocompleteSuggestionPickedListener;

@SuppressWarnings("serial")
public class DevUI extends UI {

  @WebServlet(value = "/*", asyncSupported = true)
  @VaadinServletConfiguration(productionMode = false, ui = DevUI.class, widgetset = "com.zybnet.autocomplete.AutocompleteWidgetSet")
  public static class DevServlet extends VaadinServlet {}

  private final AutocompleteField<Integer> search1 = new AutocompleteField<Integer>();
  private final AutocompleteField<Integer> search2 = new AutocompleteField<Integer>();

  @Override
  protected void init(VaadinRequest request) {
    
    VerticalLayout layout = new VerticalLayout();

    TabSheet content = new TabSheet();
    content.addTab(layout, "1");
    content.addTab(new VerticalLayout(), "2");
    setContent(content);
    layout.setWidth("800px");
    layout.setHeight(null);


    HorizontalLayout row = new HorizontalLayout();
    
    layout.addComponents(search1, row);
    search1.setInputPrompt("write something here");

    row.setSizeFull();
    Label lbl = new Label("Hello World!");
    row.addComponents(search2, lbl);
    search2.setWidth("100%");
    lbl.setWidth(null);
    row.setExpandRatio(search2, 1);
    
    setUpAutocomplete(search1);
    setUpAutocomplete(search2);
    
    Button btn = new Button("Focus search 2");
    layout.addComponent(btn);
    btn.addClickListener(new Button.ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        search2.focus();
      }
    });
    
    Button btnToggleEnabled = new Button("Toggle enabled");
    layout.addComponent(btnToggleEnabled);
    btnToggleEnabled.addClickListener(new Button.ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        search2.setEnabled(!search2.isEnabled());
      }
    });
  }
  
  private void setUpAutocomplete(AutocompleteField<Integer> search) {
    search.setQueryListener(new AutocompleteQueryListener<Integer>() {
      @Override
      public void handleUserQuery(AutocompleteField<Integer> field, String query) {
        handleSearchQuery(field, query);
      }
    });
    
    search.setSuggestionPickedListener(new AutocompleteSuggestionPickedListener<Integer>() {
      @Override
      public void onSuggestionPicked(Integer page) {
        handleSuggestionSelection(page);
      }
    });
  }

  protected void handleSuggestionSelection(Integer suggestion) {
    Notification.show("Selected " + suggestion);
  }
  
  private void handleSearchQuery(AutocompleteField<Integer> field, String query) {
    for (int i = 0; i < 10; i++) {
        field.addSuggestion(i, i + ": " + query);
    }
  }

}
