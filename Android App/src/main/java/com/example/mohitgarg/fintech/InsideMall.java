package com.example.mohitgarg.fintech;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;


public class InsideMall extends ActionBarActivity {

    String username = "Mohit";
    String userId = "1";
    String mallName = "PVR";
    String mallId = "1";
    public static ArrayList<Item> itemsList = new ArrayList<Item>();
    public static ArrayList<Item> purchasedItems = new ArrayList<>();

    public static TextView cartIconMenuBar = null;

    public static int numberOfItemsBought = 0;
    public static int totalPriceOfItemsBought = 0;

    public static Button checkoutButtonBottom = null;

    String TAG = "InsideMAll Activity";

    ArrayList<Card> cards;
    CardArrayRecyclerViewAdapter bidViewAdapter;

    CardRecyclerView cardView ;

//    ListView lv;
//    myAdapter adapter;
    EditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_inside_mall);

        itemsList = new ArrayList<Item>();
        purchasedItems = new ArrayList<>();


        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
        userId = extras.getString("userId");
        mallName = extras.getString("mallName");
        mallId = extras.getString("mallId");


        cardView = (CardRecyclerView) this.findViewById(R.id.carddemo);
        cardView.setLayoutManager(new LinearLayoutManager(  this));

        checkoutButtonBottom = (Button) findViewById(R.id.checkoutTextView);
        checkoutButtonBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Checkout","Ready");
                makeCart();
            }
        });

        Button b=(Button) findViewById(R.id.scanBarCodeButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBarCode();
            }
        });

        String url = getString(R.string.globalUrl)+getString(R.string.item);
        JSONObject data=new JSONObject();
        try {
            data.put("mallId",mallId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerConnect myServer=new ServerConnect();
        myServer.execute(url,data);



    }



    private class ServerConnect extends AsyncTask<Object,Void,JSONArray> {
        HttpPost httppost;
        HttpClient httpclient;
        ProgressDialog asyncDialog = new ProgressDialog(InsideMall.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setMessage(getString(R.string.itemMessage));
            asyncDialog.show();
            asyncDialog.setCanceledOnTouchOutside(false);
            super.onPreExecute();
        }


        @Override
        protected JSONArray doInBackground(Object... params) {
            httpclient = new DefaultHttpClient();
            Log.d(TAG, params[0] + " ");
            httppost = new HttpPost((String) params[0]);

            try {
                StringEntity se = new StringEntity(params[1].toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httppost.setEntity(se);

                HttpResponse response = httpclient.execute(httppost);
                Log.d(TAG, response.toString());

                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String json = reader.readLine();
                JSONTokener tokener = new JSONTokener(json);
                JSONObject j = new JSONObject(tokener);

                Log.d(TAG, j.getString("itemList"));

                tokener = new JSONTokener(j.getString("itemList"));
                JSONArray malls = new JSONArray(tokener);

                return malls;

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(JSONArray result) {

            Log.d(TAG,result.toString());
            JSONTokener tokener;
            JSONObject item;

            for (int i=0;i<result.length();i++)
            {
                try {
                    Log.d(TAG,result.get(i).toString());
                    tokener = new JSONTokener(result.get(i).toString());
                    item = new JSONObject(tokener);
                    Log.d(TAG,item.toString());

                    String id=item.get("itemId").toString();
                    String name=item.get("itemName").toString();
                    String des=item.get("description").toString();
                    String unit=item.get("unit").toString();
                    String barCode=item.get("barcode").toString();


                    //Edit Here
                    String url=item.get("url").toString();

                    int price=Integer.parseInt(item.get("price").toString());

                    Item a=new Item(InsideMall.this);
                    a.setData(id,name,des,unit,url,barCode,price,i);
                    itemsList.add(a);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            displayList();


            asyncDialog.dismiss();

            super.onPostExecute(result);
        }

    }

    public class myAdapter extends ArrayAdapter<Item> {

        public myAdapter(Context context, int resource, List<Item> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            final Item  item = getItem(position);
            final int itemIndex = position;
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_item, parent, false);
            }
            TextView itemName = (TextView) convertView.findViewById(R.id.item_name);
            TextView itemDes = (TextView) convertView.findViewById(R.id.item_des);
            TextView itemPrice = (TextView) convertView.findViewById(R.id.price);
            TextView itemUnit = (TextView) convertView.findViewById(R.id.unit);
            Button addToCart = (Button) convertView.findViewById(R.id.add_to_cart);

            ImageView itemImage = (ImageView) convertView.findViewById(R.id.list_image);
            itemImage.setTag("http://pngsammlung.com/thumbs/fruits/apple/apple-48.png");

            new DownloadImagesTask().execute(itemImage);

            itemName.setText(item.itemName);
            itemDes.setText(item.itemDes);
            itemPrice.setText("₹ "+item.itemPrice);
            itemUnit.setText(item.itemUnit);

            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"Add To Cart Clicked!!!");
                    addToCart(itemIndex);
                }
            });

            return convertView;
        }
    }

    private void addToCart(final int itemIndex) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InsideMall.this);
        alertDialog.setTitle("Enter Quantity");
        final EditText input = new EditText(InsideMall.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setText("1");
        alertDialog.setView(input);
        alertDialog.setPositiveButton("Buy",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String quantity=input.getText().toString();
                if (quantity.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter Quantity",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    int quant = Integer.parseInt(quantity);
                    Log.d(TAG,"Quantity = "+quant);
                    //Redirect Here
//                    Intent i=new Intent();
  //                  i.putExtra("itemIndex",itemIndex);
    //                i.putExtra("quantity",quant);
      //              setResult(Activity.RESULT_OK,i);
        //            finish();

                    ItemPurchased(itemIndex,quant);

                }
            }
        });
        alertDialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void displayList() {

        bidViewAdapter = new CardArrayRecyclerViewAdapter(  this, setBids());

        if (cardView!= null) {
            cardView.setAdapter(bidViewAdapter);
        }

//        lv = (ListView) findViewById(R.id.items_list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
//        adapter = new myAdapter(this,R.id.items_list_view, InsideMall.itemsList);
//        lv.setAdapter(adapter);
      /*  lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Item item=itemsList.get(position);

                String selectedMallName = item.mallName;
                String selectedMallId = mall.mallId;

                Log.d(TAG,selectedMallId+" "+selectedMallName);


            }
        });*/


 /*       inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text

                int textlength = cs.length();
                ArrayList<Item> tempArrayList = new ArrayList<Item>();
                for(Item c: InsideMall.itemsList){
                    if (textlength <= c.itemName.length()) {
                        if (c.itemName.toLowerCase().contains(cs.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }
                myAdapter mAdapter = new myAdapter(InsideMall.this,R.id.items_list_view, tempArrayList);
                lv.setAdapter(mAdapter);

                InsideMall.this.adapter.getFilter().filter(cs);
            }



            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
*/

    }

    public ArrayList<Card> setBids(){
        cards=  new ArrayList<>();
//        ItemCard card = new ItemCard(this);

        for (int i=0;i<itemsList.size();i++)
        {
            cards.add(itemsList.get(i));
        }
//        cards.add(card);
  //      cards.add(new ItemCard(this));
    //    cards.add(new ItemCard(this));
      //  cards.add(new ItemCard(this));
        //cards.add(new ItemCard(this));

        return cards;
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inside_mall, menu);

        MenuItem item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.actionbar_badge_layout);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);

        cartIconMenuBar = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
        //cartIconMenuBar.setText("12");

        notifCount.findViewById(R.id.imagebuttonid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clicekc", "Finally!!!");
                makeCart();

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
//        if (id==R.id.action_search)
  //      {
//            searchItem();
      //      return true;
       // }
//        if (id==R.id.cart)
  //      {
    //        makeCart();

      //      return true;
        //}
        if (id==R.id.barcodescan)
        {
            scanBarCode();
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void makeCart() {

        Log.d("TAG","Cart Out!!!");

        if (numberOfItemsBought==0)
        {
            Toast.makeText(getApplicationContext(), "Add Items before procedding to cart!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i=new Intent(InsideMall.this,cart.class);
        i.putExtra("mallId",mallId);
        i.putExtra("username",username);
        i.putExtra("userId",userId);
        i.putExtra("mallName",mallName);
        i.putExtra("totalPrice",String.valueOf(totalPriceOfItemsBought));
        startActivity(i);
    }

    private void scanBarCode() {
        boolean isZxingInstalled;
        try
        {
            ApplicationInfo info = getPackageManager().getApplicationInfo("com.google.zxing.client.android", 0 );
            Log.d(TAG,info.toString());
            isZxingInstalled = true;
        }
        catch(PackageManager.NameNotFoundException e){
            isZxingInstalled=false;
        }

        Log.d(TAG,isZxingInstalled+" ");
        if (isZxingInstalled)
        {
            Intent i=new Intent("com.google.zxing.client.android.SCAN");
            startActivityForResult(i,0);
        }

        else
        {
            Intent DownloadZxing = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.zxing.client.android"));
            startActivityForResult(DownloadZxing,2);
        }
    }



//    private void searchItem() {
  //      Intent i=new Intent(InsideMall.this,ItemLists.class);
    //    startActivityForResult(i,1);

//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

/*        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                int itemIndex=data.getIntExtra("itemIndex", -1);
                int quantity = data.getIntExtra("quantity",-1);
                if (quantity==-1 || itemIndex==-1)
                {
                    Toast.makeText(getApplicationContext(),"Error Occurred!! Please Try Again",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.d(TAG, "Item Index = "+itemIndex);
                    Log.d(TAG,"Quantity = "+quantity);
                    ItemPurchased(itemIndex,quantity);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(TAG,"Cancelled!!!");
            }
        }*/
        if (requestCode==2)
        {
            boolean isZxingInstalled;
            try
            {
                ApplicationInfo info = getPackageManager().getApplicationInfo("com.google.zxing.client.android", 0 );
                Log.d(TAG,info.toString());
                isZxingInstalled = true;
            }
            catch(PackageManager.NameNotFoundException e){
                isZxingInstalled=false;
            }

            Log.d(TAG,isZxingInstalled+" ");
            if (isZxingInstalled)
            {
                scanBarCode();
            }

            else
            {
                Toast.makeText(getApplicationContext(),"Please Install Barcode Reader!!!",Toast.LENGTH_SHORT).show();
            }

        }
        if (requestCode==0)
        {
            if (resultCode==RESULT_OK)
            {
                String scannedBarCode = data.getStringExtra("SCAN_RESULT");
                Log.d(TAG,scannedBarCode);
                final int itemIndex=findItem(scannedBarCode);
                if (itemIndex==-1)
                {
                    Toast.makeText(getApplicationContext(),"Item Not Found.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(InsideMall.this);
                    alertDialog.setTitle("Enter Quantity");
                    final EditText input = new EditText(InsideMall.this);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    input.setText("1");
                    alertDialog.setView(input);
                    alertDialog.setPositiveButton("Buy",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String quantity=input.getText().toString();
                            if (quantity.equals(""))
                            {
                                Toast.makeText(getApplicationContext(),"Enter Quantity",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                int quant = Integer.parseInt(quantity);
                                Log.d(TAG,"Quantity = "+quant);
                                ItemPurchased(itemIndex,quant);

                            }
                        }
                    });
                    alertDialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                }

            }

            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(TAG,"Cancelled!!!");
            }

        }
    }


    private int findItem(String scannedBarCode) {
        for (int i=0;i<itemsList.size();i++)
        {
            if (itemsList.get(i).itemBarCode.equals(scannedBarCode))
            {
                return i;
            }
        }
        return -1;
    }

    private void ItemPurchased(int itemIndex, int quantity) {

//        if (purchasedItems.size()==0)
  //      {
    //        displayEmptyList();
      //  }

        String itemName = itemsList.get(itemIndex).itemName;
        String itemDes = itemsList.get(itemIndex).itemDes;
        String itemId = itemsList.get(itemIndex).itemId;
        String itemUnit = itemsList.get(itemIndex).itemUnit;
        String itemUrl = itemsList.get(itemIndex).itemUrl;
        String itemBarCode = itemsList.get(itemIndex).itemBarCode;
        int itemPrice = itemsList.get(itemIndex).itemPrice;
        int quantityBought = itemsList.get(itemIndex).quantityBought;
        int index=itemsList.get(itemIndex).index;


        if (quantityBought!=0)
        {
            Toast.makeText(getApplicationContext(),"Already Bought!!!",Toast.LENGTH_SHORT).show();
        }
        else
        {
            itemsList.get(itemIndex).butItemBarCodeScan(quantity);
//            itemsList.get(itemIndex).quantityBought=quantity;

            //Update Here
//            purchasedItems.add(new Item(itemId,itemName,itemDes,itemUnit,itemUrl,itemBarCode,itemPrice,quantity,index));
//            adapter.notifyAll();
//            adapter.notifyDataSetChanged();
        }

    }

    public class DownloadImagesTask extends AsyncTask<ImageView, Void, Bitmap> {

        ImageView imageView = null;

        @Override
        protected Bitmap doInBackground(ImageView... imageViews) {
            this.imageView = imageViews[0];
            Log.d(TAG,"Started!!!");
            return download_Image((String)imageView.getTag());
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }

        private Bitmap download_Image(String url) {

            Bitmap bmp =null;
            try{
                URL ulrn = new URL(url);
                HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
                InputStream is = con.getInputStream();
                bmp = BitmapFactory.decodeStream(is);
                if (null != bmp)
                    return bmp;

            }catch(Exception e){}
            return bmp;
        }
    }


}
