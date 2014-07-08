package com.zybnet.app;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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

@Theme("mytheme")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI {

  private final VerticalLayout layout = new VerticalLayout();
  private final AutocompleteField<WikipediaPage> search = new AutocompleteField<WikipediaPage>();
  private final Button createPageButton = new Button("No result found. Create Page!");
  
  @WebServlet(value = "/*", asyncSupported = true)
  @VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class, widgetset = "com.zybnet.app.AppWidgetSet")
  public static class Servlet extends VaadinServlet {
  }

  @Override
  protected void init(VaadinRequest request) {

    layout.setMargin(true);
    layout.setSpacing(true);
    setContent(layout);

    search.setDelay(200);
    search.setWidth("400px");
    search.setCaption("Search Wikipedia for:");
    
    search.setQueryListener(new AutocompleteQueryListener<WikipediaPage>() {
      @Override
      public void handleUserQuery(AutocompleteField<WikipediaPage> field, String query) {
        handleSearchQuery(field, query);
      }
    });
    
    search.setSuggestionPickedListener(new AutocompleteSuggestionPickedListener<WikipediaPage>() {
      
      @Override
      public void onSuggestionPicked(WikipediaPage page) {
        handleSuggestionSelection(page);
      }
    });
    
    createPageButton.setVisible(false);
    
    createPageButton.addClickListener(new Button.ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        Notification.show("Creating page for \"" + search.getText() + "\"");
      }
    });

    layout.addComponents(search, createPageButton);
  }

  protected void handleSuggestionSelection(WikipediaPage suggestion) {
    Notification.show("Selected " + suggestion.getTitle() + ", URL: " + suggestion.getUrl());
  }
  
  private void handleSearchQuery(AutocompleteField<WikipediaPage> field, String query) {
    try {
      field.clearChoices();
      List<WikipediaPage> result = wikiSearch(query);
      createPageButton.setVisible(result.isEmpty());
      for (WikipediaPage page : result) {
        field.addSuggestion(page, page.getTitle());
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    
  }
  
  private List<WikipediaPage> wikiSearch(String query) throws IOException, SAXException, ParserConfigurationException {
    Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse("http://en.wikipedia.org/w/api.php?action=opensearch&namespace=0&format=xml&search=" + URLEncoder.encode(query, "UTF-8"));
    NodeList pages = dom.getElementsByTagName("Item");
    List<WikipediaPage> result = new ArrayList<WikipediaPage>();
    for (int i = 0; i < pages.getLength(); i++) {
      WikipediaPage page = new WikipediaPage();
      result.add(page);
      Element src = (Element) pages.item(i);
      page.setTitle(src.getElementsByTagName("Text").item(0).getTextContent());
      page.setUrl(src.getElementsByTagName("Url").item(0).getTextContent());
      page.setDescription(src.getElementsByTagName("Description").item(0).getTextContent());
    }
    return result;
  }

}
