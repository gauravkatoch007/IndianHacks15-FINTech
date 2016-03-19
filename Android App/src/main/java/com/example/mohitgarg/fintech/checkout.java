package com.example.mohitgarg.fintech;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class checkout extends ActionBarActivity {

    String username = "Mohit";
    String userId;
    String mallName = "PVR";
    String mallId = "1";
    String transactionId;
    String amountToPay;
    String paymentMethod;

    TextView message;
    Button cashPayment;
//    Button onlinePayment;

    ListView lv;
    myAdapter adapter;

    JSONArray cart=null;

    String TAG="checkout Activity";


    ArrayList<Mall> mallsList = new ArrayList<Mall>();

    public class Mall {
        public String title;

        public Mall(String t)
        {
            title = t;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
              userId = extras.getString("userId");
            mallName = extras.getString("mallName");
          mallId = extras.getString("mallId");
        try {
            cart=new JSONArray(extras.getString("cart"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, cart.toString());
        Log.d(TAG,username);

        message = (TextView) findViewById(R.id.amount_to_pay);
        cashPayment = (Button) findViewById(R.id.cod_button);
//        onlinePayment = (Button) findViewById(R.id.online);

        cashPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Cash Payment");
                paymentMethod="COD";

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(checkout.this);
                alertDialog.setTitle("Pay Cash.");
                alertDialog.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Redirect Here
                        completePayment();
                    }
                });
                alertDialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();


//                completePayment();
            }
        });

 /*       onlinePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Online Payment");
                paymentMethod="OnlinePayment";

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(checkout.this);
                alertDialog.setTitle("Continue to Payment");
                alertDialog.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Redirect Here
                        completePayment();
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
        });
*/
        String url = getString(R.string.globalUrl)+getString(R.string.checkout);
        JSONObject data=new JSONObject();
        try {

            data.put("mallId",mallId);
            data.put("userId",userId);
            data.put("cart",cart);
            Log.d(TAG,data.toString());
            ServerConnect myServer=new ServerConnect();
            myServer.execute(url, data);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void completePayment() {

        Intent i=new Intent(checkout.this,payment.class);
        i.putExtra("mallId",mallId);
        i.putExtra("username",username);
        i.putExtra("userId",userId);
        i.putExtra("mallName",mallName);
        i.putExtra("transactionId",transactionId);
        i.putExtra("paymentMethod",paymentMethod);
        startActivity(i);

    }

//    @Override
  //  public void onBackPressed() {
    //}

    private class ServerConnect extends AsyncTask<Object,Void,Void> {
        HttpPost httppost;
        HttpClient httpclient;
        ProgressDialog asyncDialog = new ProgressDialog(checkout.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setMessage(getString(R.string.checkoutMessage));
            asyncDialog.show();
            asyncDialog.setCanceledOnTouchOutside(false);

            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Object... params) {
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
                Log.d(TAG,json);
                JSONTokener tokener = new JSONTokener(json);
                final JSONObject j = new JSONObject(tokener);

                if (j.getString("success").equals("false"))
                {
                    checkout.this.runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                Toast.makeText(getApplicationContext(), j.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else
                {
                    transactionId=j.getString("transactionId");
                    amountToPay=j.getString("amountToPay");
                }


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
        protected void onPostExecute(Void result) {

            asyncDialog.dismiss();
            completeTransaction();

            super.onPostExecute(result);
        }

    }

    private void completeTransaction() {

        message.setText("Amount To pay : ₹"+amountToPay);

   //     mallsList.add(new Mall("Amount To pay : ₹"+amountToPay));
//        mallsList.add(new Mall("Pay On Counter."));
  //      mallsList.add(new Mall("Online Payment."));
        mallsList.add(new Mall("State Bank of India."));
        mallsList.add(new Mall("Axis Bank."));
        mallsList.add(new Mall("HDFC Bank."));
        mallsList.add(new Mall("State Bank of Patiala."));
        mallsList.add(new Mall("Oriental Bank of Commerce."));
        mallsList.add(new Mall("Punjab & Sind Bank."));
        mallsList.add(new Mall("IDBI Bank."));
        mallsList.add(new Mall("Punjab National Bank."));


        displayList();
//        message.setText("Amount To pay : ₹"+amountToPay);
    }

    public class myAdapter extends ArrayAdapter<Mall> {

        public myAdapter(Context context, int resource, List<Mall> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Mall  mall = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.mall_item, parent, false);
            }
            TextView mallName = (TextView) convertView.findViewById(R.id.mall_name);

            mallName.setText(mall.title);

            return convertView;
        }
    }

    private void displayList() {

        lv = (ListView) findViewById(R.id.list_view);
        adapter = new myAdapter(this,R.id.list_view, mallsList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Log.d(TAG,position+"");

                    Log.d(TAG,"Bank");

                    Log.d(TAG,"Online Payment");
                    paymentMethod="OnlinePayment";

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(checkout.this);
                    alertDialog.setTitle("Continue to Payment via "+mallsList.get(position).title);
                    alertDialog.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Redirect Here
                            completePayment();
                        }
                    });
                    alertDialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();


                //Edit Here

/*                Mall mall=mallsList.get(position);

                String selectedMallName = mall.title;

                Intent i=new Intent(MallsList.this,InsideMall.class);
                i.putExtra("mallName",selectedMallName);
                i.putExtra("mallId",selectedMallId);
                i.putExtra("username",username);
                i.putExtra("userId",userId);
                startActivity(i);

                Log.d(TAG,selectedMallId+" "+selectedMallName);
*/

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_checkout, menu);
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
