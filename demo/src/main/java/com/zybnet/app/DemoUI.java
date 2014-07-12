package com.zybnet.app;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.vaadin.annotations.Theme;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.zybnet.autocomplete.server.AutocompleteField;
import com.zybnet.autocomplete.server.AutocompleteQueryListener;
import com.zybnet.autocomplete.server.AutocompleteSuggestionPickedListener;

@Theme("mytheme")
@SuppressWarnings("serial")
public class DemoUI extends UI {

  private final AutocompleteField<WikipediaPage> search = new AutocompleteField<WikipediaPage>();
  private final Button createPageButton = new Button("No result found. Create Page!");
  private final BrowserFrame wikipediaPage = new BrowserFrame();

  @Override
  protected void init(VaadinRequest request) {
    setWidth(null);
    addStyleName("wrapper");
    
    CssLayout layout = new CssLayout();
    layout.addStyleName("main-content");
    setContent(layout);

    search.setDelay(200);
    search.setWidth("100%");
    
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

    Label searchLabel = new Label("Search Wikipedia for");
    searchLabel.setWidth("100%");
    searchLabel.addStyleName("search-label");
    search.addStyleName("search");
    createPageButton.addStyleName("create-page-button");
    createPageButton.setWidth("100%");
    wikipediaPage.addStyleName("wikipedia-page");
    
    CssLayout header = new CssLayout(searchLabel, search, createPageButton);
    header.addStyleName("header");
    
    CssLayout body = new CssLayout(wikipediaPage);
    body.addStyleName("body");
    
    layout.addComponents(header, body);
  }

  protected void handleSuggestionSelection(WikipediaPage suggestion) {
    wikipediaPage.setWidth("100%");
    wikipediaPage.setHeight("400px");
    String mobileVersion = suggestion.getUrl().replaceFirst("en\\.wikipedia\\.org", "en\\.m\\.wikipedia\\.org");
    wikipediaPage.setSource(new ExternalResource(mobileVersion));
  }
  
  private void handleSearchQuery(AutocompleteField<WikipediaPage> field, String query) {
    try {
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
