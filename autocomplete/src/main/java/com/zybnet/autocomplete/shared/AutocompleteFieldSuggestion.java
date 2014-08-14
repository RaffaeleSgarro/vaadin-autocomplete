package com.zybnet.autocomplete.shared;

import java.io.Serializable;


@SuppressWarnings("serial")
public class AutocompleteFieldSuggestion implements Serializable {

	private Integer id;
	private String tooltip;
	private String replacementString;


	public String getDisplayString() {
		if (tooltip == null) {
			return replacementString;
		}
		return "<div title='" + tooltip + "'>" + replacementString + "</div>";
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getTooltip() {
		return tooltip;
	}


	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}


	public String getReplacementString() {
		return replacementString;
	}


	public void setReplacementString(String replacementString) {
		this.replacementString = replacementString;
	}

}
