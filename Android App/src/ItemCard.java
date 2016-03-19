package com.example.mohitgarg.fintech;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by mohit on 14/3/16.
 */
public class ItemCard extends Card {

        public String title1;

        public ItemCard(Context context){
            super(context, R.layout.item_card);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {
            TextView tx= (TextView)view.findViewById(R.id.card_main_inner_simple_title);
            tx.setText(title1);



        }



}
