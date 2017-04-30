package com.zybnet.autocomplete.server;

import com.zybnet.autocomplete.shared.AutocompleteServerRpc;

public interface AutocompleteTextFieldResetListener<E> {
    void onReset(AutocompleteField<E> autoCompleteField);
}