package com.thomashaertel.billing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.thomashaertel.billing.validation.PurchaseValidator;
import com.thomashaertel.billing.validation.SimplePurchaseValidator;

import org.onepf.oms.OpenIabHelper;
import org.onepf.oms.appstore.googleUtils.IabHelper;
import org.onepf.oms.appstore.googleUtils.IabResult;
import org.onepf.oms.appstore.googleUtils.Inventory;
import org.onepf.oms.appstore.googleUtils.Purchase;
import org.onepf.oms.appstore.googleUtils.SkuDetails;
import org.onepf.oms.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Haertel on 12.01.2015.
 */
public abstract class BaseBillingFragment extends Fragment {

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;

    // Debug tag for logging
    static final String TAG = "OpenIAB";

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
            if (result.isFailure()) {
                // TODO
                return;
            }
            if (!mPurchaseValidator.validatePurchase(purchase)) {
                // TODO
                return;
            }

            Log.d(TAG, "Purchase successful.");

            // TODO Update Secure Preferences
        }
    };
    // Listener that's called when we finish querying the items and subscriptions we own
    private IabHelper.QueryInventoryFinishedListener mGotInventoryListener =
            new IabHelper.QueryInventoryFinishedListener() {
                @Override
                public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                    Log.d(TAG, "Query inventory finished.");
                    if (result.isFailure()) {
                        updateUi(inventory);
                        Log.d(TAG, "Failed to query inventory: " + result);
                        return;
                    }
                    Log.d(TAG, "Query inventory was successful.");

                    updateInventory(inventory);

                    updateUi(inventory);
                    Log.d(TAG, "Initial inventory query finished; enabling main UI.");
                }
            };
    // Does the user have the premium upgrade?
    boolean mIsPremium = false;
    // Does the user have an active subscription to the infinite gas plan?
    boolean mSubscribedToProfessional = false;
    private PurchaseValidator mPurchaseValidator = new SimplePurchaseValidator();
    private LicenseStore mLicenseStore;
    private Boolean setupDone;
    private OpenIabHelper mHelper;
    private List<String> skus = new ArrayList<String>();

    public PurchaseValidator getPurchaseValidator() {
        return mPurchaseValidator;
    }

    public void setPurchaseValidator(PurchaseValidator validator) {
        this.mPurchaseValidator = validator;
    }

    private void updateInventory(Inventory inventory) {
        /*
        * Check for items we own. Notice that for each purchase, we check
        * the developer payload to see if it's correct! See
        * verifyDeveloperPayload().
        */
        // Do we have the premium upgrade?
        Purchase premiumPurchase = inventory.getPurchase(InAppConfig.SKU_PREMIUM);
        mIsPremium = premiumPurchase != null && mPurchaseValidator.validatePurchase(premiumPurchase);
        Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
        mLicenseStore.put("premium", String.valueOf(mIsPremium));

        // Do we have the professional subscription?
        Purchase professionalSubscriptionPurchase = inventory.getPurchase(InAppConfig.SKU_PROFESSIONAL);
        mSubscribedToProfessional = (professionalSubscriptionPurchase != null &&
                mPurchaseValidator.validatePurchase(professionalSubscriptionPurchase));
        Log.d(TAG, "User " + (mSubscribedToProfessional ? "HAS" : "DOES NOT HAVE")
                + " infinite gas subscription.");
        mLicenseStore.put("professional", String.valueOf(mSubscribedToProfessional));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();

        skus.add(InAppConfig.SKU_PREMIUM);
        skus.add(InAppConfig.SKU_PROFESSIONAL);

        Logger.setLoggable(true);

        // Try to use more data here. ANDROID_ID is a single point of attack.
        String deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        // Create license store for storing purchases
        mLicenseStore = new LicenseStore(getActivity(), deviceId);

        // Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        //Only map SKUs for stores that using purchase with SKUs different from described in store console.
        OpenIabHelper.Options.Builder builder = new OpenIabHelper.Options.Builder()
                .setStoreSearchStrategy(OpenIabHelper.Options.SEARCH_STRATEGY_INSTALLER_THEN_BEST_FIT)
                .setVerifyMode(OpenIabHelper.Options.VERIFY_EVERYTHING)
                .addStoreKeys(InAppConfig.STORE_KEYS_MAP)
                .addAvailableStoreNames(OpenIabHelper.NAME_GOOGLE);
        mHelper = new OpenIabHelper(getActivity(), builder.build());
        // enable debug logging (for a production application, you should set this to false).
        //mHelper.enableDebugLogging(true);
        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d("", "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    setupDone = false;
                    updateUi(null);
                    Log.d(TAG, "Problem setting up in-app billing: " + result);
                    return;
                }
                // Hooray, IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                setupDone = true;
                mHelper.queryInventoryAsync(true, skus, skus, mGotInventoryListener);
            }
        });
    }

    public abstract void updateUi(Inventory inventory);

    public void purchaseItem(SkuDetails skuDetails) {
        String payload = "";
        mHelper.launchPurchaseFlow(getActivity(), skuDetails.getSku(), RC_REQUEST, mPurchaseFinishedListener, payload);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult() requestCode: " + requestCode + " resultCode: " + resultCode + " data: " + data);
        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

}
