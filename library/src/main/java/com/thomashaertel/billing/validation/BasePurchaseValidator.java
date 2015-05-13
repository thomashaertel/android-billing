package com.thomashaertel.billing.validation;

import org.onepf.oms.appstore.googleUtils.Purchase;

/**
 * Created by Haertel on 30.01.2015.
 */
public class BasePurchaseValidator implements PurchaseValidator {
    @Override
    public boolean validatePurchase(Purchase purchase) {
        return validateDeveloperPayload(purchase.getDeveloperPayload());
    }

    public boolean validateDeveloperPayload(String developerPayload) {
        return true;
    }
    
}
