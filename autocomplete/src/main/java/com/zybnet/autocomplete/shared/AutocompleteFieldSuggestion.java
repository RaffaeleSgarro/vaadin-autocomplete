package com.zybnet.autocomplete.shared;

import java.io.Serializable;


@SuppressWarnings("serial")
public class AutocompleteFieldSuggestion implements Serializable {

	private Integer id;
	private String tooltip;
	private String replacementString;


	public AutocompleteFieldSuggestion() {
		/* needed for widgetset compilation */
	}


	public AutocompleteFieldSuggestion(Integer id, String replacementString, String tooltip) {
		this.id = id;
		this.replacementString = replacementString;
		this.tooltip = tooltip;
	}


	public String getDisplayString() {
		if (tooltip == null) {
			return replacementString;
		}
		return "<div title='" + tooltip + "'>" + replacementString + "</div>";
	}


	public Integer getId() {
		return id;
	}


	public String getTooltip() {
		return tooltip;
	}


	public String getReplacementString() {
		return replacementString;
	}

}
