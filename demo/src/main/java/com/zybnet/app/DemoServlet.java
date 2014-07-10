package com.zybnet.app;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@WebServlet(value = "/*", asyncSupported = true)
@VaadinServletConfiguration(productionMode = false, ui = DevUI.class, widgetset = "com.zybnet.app.AppWidgetSet")
@SuppressWarnings("serial")
public class DemoServlet extends VaadinServlet {
  
  private final UIProvider uiProvider = new UiProviderImpl();
  
  @Override
  protected void servletInitialized() throws ServletException {
    super.servletInitialized();
    getService().addSessionInitListener(new SessionInitListener() {
      @Override
      public void sessionInit(SessionInitEvent event) throws ServiceException {
        event.getSession().addUIProvider(uiProvider);
      }
    });
  }
  
  public static class UiProviderImpl extends UIProvider {
    @Override
    public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
      String path = event.getRequest().getPathInfo();
      if (path.equals("/app")) {
        return DemoUI.class;
      } else {
        return DevUI.class;
      }
    }
  }
}
