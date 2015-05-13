package com.thomashaertel.billing;

import android.content.Context;

import com.securepreferences.SecurePreferences;
import com.thomashaertel.device.identification.SharedPreferencesStore;

/**
 * Created by Haertel on 11.01.2015.
 */
public class LicenseStore extends SharedPreferencesStore {

    private static final String LICENSE_STORE = "license";

    public LicenseStore(Context context, String deviceId) {
        super(context, LICENSE_STORE, true);
    }

}
