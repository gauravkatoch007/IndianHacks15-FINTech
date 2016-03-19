package com.example.mohitgarg.fintech;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by mohit on 18/3/16.
 */
public class SimpleCard extends Card {

    String title;

    public SimpleCard(Context context){
        super(context, R.layout.simple_card);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        TextView titleView = (TextView) view.findViewById(R.id.simple_item_name);
        titleView.setText(title);
    }
}
