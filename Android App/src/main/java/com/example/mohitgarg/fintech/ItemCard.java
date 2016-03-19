package com.example.mohitgarg.fintech;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by mohit on 14/3/16.
 */
public class ItemCard extends Card {

    public ItemCard(Context context){
        super(context, R.layout.item_card);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {



        Button b=(Button)view.findViewById(R.id.plusButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clicked", "Clecji");
            }
        });



    }
}
