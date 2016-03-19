package com.example.mohitgarg.fintech;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;

public class TestCards extends ActionBarActivity {

    ArrayList<Card> cards;
    CardArrayRecyclerViewAdapter bidViewAdapter;

    CardRecyclerView cardView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_cards);

        cardView = (CardRecyclerView) this.findViewById(R.id.carddemo1);
        bidViewAdapter = new CardArrayRecyclerViewAdapter(  this, setBids());
          cardView.setLayoutManager(new LinearLayoutManager(  this));

        if (cardView!= null) {
            cardView.setAdapter(bidViewAdapter);
        }





        //Set card in the cardView



    }

    public ArrayList<Card> setBids(){
        cards=  new ArrayList<>();
        ItemCard card = new ItemCard(this);
//        card.title1 = "....";
        cards.add(card);
        cards.add(new ItemCard(this));
        cards.add(new ItemCard(this));
        cards.add(new ItemCard(this));
        cards.add(new ItemCard(this));

        return cards;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_cards, menu);

        MenuItem item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.actionbar_badge_layout);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);

        TextView tv = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
        tv.setText("12");

        notifCount.findViewById(R.id.imagebuttonid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clicekc", "Finally!!!");
            }
        });

        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
