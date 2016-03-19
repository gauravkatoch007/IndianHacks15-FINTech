package com.example.mohitgarg.fintech;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;

/**
 * Created by mohit on 5/1/16.
 */
public class Item extends Card {

    public String itemName;
    public String itemDes;
    public String itemId;
    public String itemUnit;
    public String itemUrl;
    public String itemBarCode;
    public int itemPrice;
    public int index;
    public int quantityBought;

    TextView itemNameTextView;
    TextView itemDesTextView;
    TextView itemUnitTextView;
    TextView itemPriceTextView;
    TextView quantityBoughtTextView;
    Button addItemButton;
    Button minusItemButton;
    ImageView itemImage;

    public Item(Context context){
        super(context, R.layout.item_card);
    }

    public void setData(String id,String name,String des,String unit,String url,String barCode,int price,int ind)
    {
        itemId = id;
        itemName = name;
        itemDes = des;
        itemUnit = unit;
        itemUrl = url;
        itemBarCode = barCode;
        itemPrice = price;
        quantityBought = 0;
        index=ind;
    }
    public void setData(String id,String name,String des,String unit,String url,String barCode,int price,int bought,int ind)
    {
        itemId = id;
        itemName = name;
        itemDes = des;
        itemUnit = unit;
        itemUrl = url;
        itemBarCode = barCode;
        itemPrice = price;
        quantityBought = bought;
        index=ind;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        itemNameTextView = (TextView) view.findViewById(R.id.item_name);
        itemDesTextView = (TextView) view.findViewById(R.id.item_des);
        itemPriceTextView = (TextView) view.findViewById(R.id.price);
        itemUnitTextView  = (TextView) view.findViewById(R.id.unit);
        quantityBoughtTextView = (TextView) view.findViewById(R.id.quantity);

        itemImage = (ImageView) view.findViewById(R.id.list_image);
        itemImage.setTag(itemUrl);

        new DownloadImagesTask().execute(itemImage);

        itemNameTextView.setText(itemName);
        itemDesTextView.setText(itemDes);
        itemPriceTextView.setText("₹ "+String.valueOf(itemPrice));
        itemUnitTextView.setText(" ( "+itemUnit+" ) ");
        quantityBoughtTextView.setText(String.valueOf(quantityBought));

        addItemButton=(Button)view.findViewById(R.id.plusButton);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clicked", "Clecji");
                quantityBought += 1;
                quantityBoughtTextView.setText(String.valueOf(quantityBought));

                InsideMall.numberOfItemsBought+=1;

                InsideMall.cartIconMenuBar.setText(String.valueOf(InsideMall.numberOfItemsBought));

                InsideMall.totalPriceOfItemsBought += itemPrice;
                InsideMall.checkoutButtonBottom.setText("CheckOut                        ₹ "+InsideMall.totalPriceOfItemsBought);

            }
        });

        minusItemButton=(Button)view.findViewById(R.id.minusButton);
        minusItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Cliced","OOOO");
                if (quantityBought>0)
                {
                    quantityBought -= 1;
                    quantityBoughtTextView.setText(String.valueOf(quantityBought));

                    InsideMall.numberOfItemsBought-=1;

                    InsideMall.cartIconMenuBar.setText(String.valueOf(InsideMall.numberOfItemsBought));

                    InsideMall.totalPriceOfItemsBought -= itemPrice;
                    InsideMall.checkoutButtonBottom.setText("CheckOut                        ₹ "+InsideMall.totalPriceOfItemsBought);
                }
            }
        });



    }

    public void butItemBarCodeScan(int no)
    {
        quantityBought += no;
        quantityBoughtTextView.setText(String.valueOf(quantityBought));

        InsideMall.numberOfItemsBought+=no;

        InsideMall.cartIconMenuBar.setText(String.valueOf(InsideMall.numberOfItemsBought));

        InsideMall.totalPriceOfItemsBought += no*itemPrice;
        InsideMall.checkoutButtonBottom.setText("CheckOut                        ₹ "+InsideMall.totalPriceOfItemsBought);

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
