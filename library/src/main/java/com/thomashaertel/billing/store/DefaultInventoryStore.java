package com.thomashaertel.billing.store;

import android.content.Context;

import com.securepreferences.SecurePreferences;

public class DefaultInventoryStore implements InventoryStore {
    private SecurePreferences preferences;

    public DefaultInventoryStore(Context context) {
        this.preferences = new SecurePreferences(context, true);
    }
}
