package com.thomashaertel.billing.validation;

import org.onepf.oms.appstore.googleUtils.Purchase;

/**
 * Created by Haertel on 30.01.2015.
 */
public interface PurchaseValidator {

    public boolean validatePurchase(Purchase purchase);
}
