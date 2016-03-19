package com.example.mohitgarg.fintech;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;


public class cart extends ActionBarActivity {

    String username = "Mohit";
    String userId = "1";
    String mallName = "PVR";
    String mallId = "1";

    String TAG = "Cart Activity";
    String totalPrice;

    ArrayList<Card> cards;
    CardArrayRecyclerViewAdapter bidViewAdapter;

    CardRecyclerView cardView ;

//    ListView lv;
//    myAdapter adapter;
//    TextView welcome;

    public static Button checkoutButtonBottom = null;

    JSONArray cart=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
        userId = extras.getString("userId");
        mallName = extras.getString("mallName");
        mallId = extras.getString("mallId");
        totalPrice = extras.getString("totalPrice");

        Log.d(TAG,username);

        cardView = (CardRecyclerView) this.findViewById(R.id.carddemo1);
        cardView.setLayoutManager(new LinearLayoutManager(  this));

        bidViewAdapter = new CardArrayRecyclerViewAdapter(  this, setBids());

        if (cardView!= null) {
            cardView.setAdapter(bidViewAdapter);
        }

        checkoutButtonBottom = (Button) findViewById(R.id.checkoutTextView);
        checkoutButtonBottom.setText("Pay Money                        ₹ "+totalPrice);
        checkoutButtonBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Checkout","Ready");
                checkOut();
            }
        });


//        welcome = (TextView)findViewById(R.id.welcomeMessage);

//        welcome.setVisibility(View.GONE);

  //      lv = (ListView) findViewById(R.id.items_list_view);
    //    lv.setVisibility(View.VISIBLE);

      //  adapter = new myAdapter(this,R.id.items_list_view, InsideMall.purchasedItems);
       // lv.setAdapter(adapter);


    }

//    @Override
  //  public void onBackPressed() {
    //}

    public ArrayList<Card> setBids(){
        cards=  new ArrayList<>();
//        ItemCard card = new ItemCard(this);

        for (int i=0;i<InsideMall.itemsList.size();i++)
        {
            if (InsideMall.itemsList.get(i).quantityBought!=0)
            {
                ItemCart a=new ItemCart(this);
                Item b=InsideMall.itemsList.get(i);
                a.setData(b.itemId,b.itemName,b.itemDes,b.itemUnit,b.itemUrl,b.itemBarCode,b.itemPrice,b.quantityBought,b.index);
                cards.add(a);
            }
        }
//        cards.add(card);
        //      cards.add(new ItemCard(this));
        //    cards.add(new ItemCard(this));
        //  cards.add(new ItemCard(this));
        //cards.add(new ItemCard(this));

        return cards;
    }

/*
    public class myAdapter extends ArrayAdapter<Item> {

        public myAdapter(Context context, int resource, List<Item> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            final Item  item = getItem(position);
            final int currentIndex= position;
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.purchased_item_item, parent, false);
            }
            TextView itemName = (TextView) convertView.findViewById(R.id.item_name);
//            TextView itemDes = (TextView) convertView.findViewById(R.id.item_des);
            TextView itemPrice = (TextView) convertView.findViewById(R.id.price);
            TextView itemUnit = (TextView) convertView.findViewById(R.id.unit);
            TextView itemQuantity = (TextView) convertView.findViewById(R.id.buyItemNo);

            Button addItem = (Button) convertView.findViewById(R.id.addItem);

            addItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Item Add Clicked!!!");
                    addItem(item.index,currentIndex);
                }
            });

            final Button decreaseItem = (Button) convertView.findViewById(R.id.decreaseButton);

            decreaseItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"Decrease Item Clicked!!!");
                    decreaseItem(item.index,currentIndex);
                }
            });


            itemName.setText(item.itemName);
//            itemDes.setText(item.itemDes);
            itemPrice.setText("₹ "+item.itemPrice);
            itemUnit.setText(item.itemUnit);
            itemQuantity.setText(item.quantityBought+"");


            return convertView;
        }
    }

    private void decreaseItem(int itemIndex,int currentIndex) {

        final int index=itemIndex;
        final int curr = currentIndex;

        final int quantityBought = InsideMall.itemsList.get(itemIndex).quantityBought;

        if (quantityBought==1)
        {
            final AlertDialog.Builder alertdialog=new AlertDialog.Builder(cart.this);
            alertdialog.setTitle("Remove this item from your Cart?");
            alertdialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertdialog.setPositiveButton("YES",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    InsideMall.itemsList.get(index).quantityBought=quantityBought-1;
                    InsideMall.purchasedItems.remove(curr);
                    adapter.notifyDataSetChanged();
                }
            });
            alertdialog.show();
        }
        else
        {
            InsideMall.itemsList.get(itemIndex).quantityBought=quantityBought-1;
            InsideMall.purchasedItems.get(currentIndex).quantityBought=quantityBought-1;
        }

        adapter.notifyDataSetChanged();

    }

    private void addItem(int itemIndex,int currentIndex) {

        int quantityBought = InsideMall.itemsList.get(itemIndex).quantityBought;

        InsideMall.itemsList.get(itemIndex).quantityBought=quantityBought+1;
        InsideMall.purchasedItems.get(currentIndex).quantityBought=quantityBought+1;
        adapter.notifyDataSetChanged();

    }
    */


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id==R.id.checkout)
        {
            checkOut();
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void checkOut() {
        Log.d("TAG","CHeck Out!!!");

        if (InsideMall.numberOfItemsBought==0)
        {
            Toast.makeText(getApplicationContext(), "Add Items before procedding to checkout!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONArray cart=new JSONArray();

        for (int i=0;i<InsideMall.itemsList.size();i++)
        {
            JSONObject temp=new JSONObject();
            try {
                temp.put("itemId",InsideMall.itemsList.get(i).itemId);
                temp.put("quantity",InsideMall.itemsList.get(i).quantityBought);
                cart.put(temp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG,cart.toString());

        Intent i=new Intent(cart.this,checkout.class);
        i.putExtra("mallId",mallId);
        i.putExtra("username",username);
        i.putExtra("userId",userId);
        i.putExtra("mallName",mallName);
        i.putExtra("cart",cart.toString());
        startActivity(i);
    }
}
