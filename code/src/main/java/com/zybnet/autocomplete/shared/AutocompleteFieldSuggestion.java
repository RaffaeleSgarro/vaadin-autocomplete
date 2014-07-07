package com.zybnet.autocomplete.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AutocompleteFieldSuggestion implements Serializable {
  
  private Integer id;
  private String displayString;

  public String getDisplayString() {
    return displayString;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setDisplayString(String displayString) {
    this.displayString = displayString;
  }
  
}