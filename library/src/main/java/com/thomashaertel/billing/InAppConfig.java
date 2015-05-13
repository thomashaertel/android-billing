package com.thomashaertel.billing;

import org.onepf.oms.OpenIabHelper;
import org.onepf.oms.SkuManager;

import java.util.HashMap;
import java.util.Map;

public final class InAppConfig {
    // initialize config as singleton
    static {
        init();
    }

    //premium upgrade (non-consumable)
    public static final String SKU_PREMIUM = " de.dsm.premium";
    //premium upgrade (subscription)
    public static final String SKU_PROFESSIONAL = "de.dsm.professional";

    //Google Play
    public static final String GOOGLE_PLAY_KEY
            = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoGoZEdubL26XURSAF8R/D8J2zYcFfkces7aCoCUg821aIgZSE0ZQGePxvGVuNYaMPkruky+QEb24TY5IuV49asJmzd8g1hFCEpC+4XVouNg4TXA3MOrc8uPkZ1XpgqqwcyJfzWoSRdrLP4iqSYoBIYHkhCuwzdBFJLInaFerofYOmR08NGb9Rrb9SoEnyNATfDTiplK0Qam2Flgh4jnvihleONiyrkRNakZI5S1bjBPyYBtjfruYwXpzMCt2pVFBZXlJpzcqb0OBEbOFi1ZOW0DqSBOIPPqwyUF9PG6AFjr520146lye9pmnP8Q9YpxvbbEd1qPqQbwjaXuap6cc+wIDAQAB";


    public static Map<String, String> STORE_KEYS_MAP;

    public static void init() {
        STORE_KEYS_MAP = new HashMap<>();
        STORE_KEYS_MAP.put(OpenIabHelper.NAME_GOOGLE, InAppConfig.GOOGLE_PLAY_KEY);
//        STORE_KEYS_MAP.put(OpenIabHelper.NAME_AMAZON,
//                "Unavailable. Amazon doesn't support RSA verification. So this mapping is not needed");
//        STORE_KEYS_MAP.put(OpenIabHelper.NAME_SAMSUNG,
//                "Unavailable. SamsungApps doesn't support RSA verification. So this mapping is not needed");

        // TODO: Keys are not correct for app stores
        SkuManager.getInstance()
                //Google
                .mapSku(SKU_PREMIUM, OpenIabHelper.NAME_GOOGLE, "de.dsm.premium")
                .mapSku(SKU_PROFESSIONAL, OpenIabHelper.NAME_GOOGLE, "de.dsm.professional")
                //Amazon
                .mapSku(SKU_PREMIUM, OpenIabHelper.NAME_AMAZON, "de.dsm.premium")
                .mapSku(SKU_PROFESSIONAL, OpenIabHelper.NAME_AMAZON, "de.dsm.professional")
                //Samsung
                .mapSku(SKU_PREMIUM, OpenIabHelper.NAME_SAMSUNG, "000000000000/de.dsm.premium")
                .mapSku(SKU_PROFESSIONAL, OpenIabHelper.NAME_SAMSUNG, "000000000000/de.dsm.professional");
    }

    private InAppConfig() {
    }
}
