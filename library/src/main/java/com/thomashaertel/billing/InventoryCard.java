package com.thomashaertel.billing;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Haertel on 15.01.2015.
 */
class InventoryCard extends BaseActionCard {

    private boolean mPurchased;
    private String mPrice;
    private String mDescription;

    // -------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------
    public InventoryCard(Context context) {
        super(context, R.layout.inventory_card);
    }

    // -------------------------------------------------------------
    // Build
    // -------------------------------------------------------------

    /**
     * Implements this method to build the custom card
     */
    @Override
    public void build() {

    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        TextView textTitle = (TextView) view.findViewById(R.id.text_title);
        textTitle.setText(mTitle);
        TextView textDescription = (TextView) view.findViewById(R.id.text_description);
        textDescription.setText(mDescription);
        TextView textPrice = (TextView) view.findViewById(R.id.text_price);
        textPrice.setText(mPrice);
        TextView textPurchased = (TextView) view.findViewById(R.id.text_purchased);
        textPurchased.setVisibility(mPurchased ? View.VISIBLE : View.INVISIBLE);
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setPrice(String price) {
        this.mPrice = price;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPurchased(boolean purchased) {
        this.mPurchased = purchased;
    }

    public boolean isPurchased() {
        return mPurchased;
    }
}
