package com.thomashaertel.billing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.onepf.oms.appstore.googleUtils.Inventory;
import org.onepf.oms.appstore.googleUtils.SkuDetails;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.gmariotti.cardslib.library.cards.actions.BaseSupplementalAction;
import it.gmariotti.cardslib.library.cards.actions.TextSupplementalAction;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;


public class PurchaseFragment extends BaseBillingFragment {

    private CardListView cardListView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase, container, false);

        cardListView = (CardListView) view.findViewById(R.id.cardviewlist);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private Card initCard(SkuDetails sku, boolean purchased) {
        InventoryCard card = new InventoryCard(getActivity());

        // set card from sku information
        card.setTitle(adjustSkuTitle(sku.getTitle()));
        card.setPrice(sku.getPrice());
        card.setDescription(sku.getDescription());
        
        // add click listener
        card.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Toast.makeText(getActivity(), "Card Clicked", Toast.LENGTH_SHORT).show();
                
            }
        });
        
        return card;
    }

    @Override
    public void updateUi(Inventory inventory) {
        if (inventory == null)
            return;

        Iterator<Map.Entry<String, SkuDetails>> iter = inventory.getSkuMap().entrySet().iterator();

        List<Card> cards = new ArrayList<Card>();

        while (iter.hasNext()) {
            SkuDetails skuDetails = iter.next().getValue();
            cards.add(initCard(skuDetails, false));
        }

        CardArrayAdapter adapter = new CardArrayAdapter(getActivity(), cards);

        cardListView.setAdapter(adapter);
    }

    private static String adjustSkuTitle(String title) {
        int idx = title.lastIndexOf('(');

        return title.substring(0, idx != -1 ? idx : title.length());
    }
}
