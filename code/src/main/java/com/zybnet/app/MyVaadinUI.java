package com.zybnet.app;

import java.text.DateFormat;
import java.util.Date;

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
import com.zybnet.autocomplete.AutocompleteField;

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
    setContent(layout);

    AutocompleteField search = new AutocompleteField();
    search.setCaption("Search 'java'");

    Button btn = new Button("Click me!");
    btn.addClickListener(new Button.ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        onButtonClicked(event);
      }
    });

    layout.addComponents(search, btn);
  }

  private void onButtonClicked(ClickEvent event) {
    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG);
    Notification.show("Server time " + df.format(new Date()));
  }

}
