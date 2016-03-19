package com.example.mohitgarg.fintech;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.Random;


public class payment extends ActionBarActivity {

    String username = "Mohit";
    String userId;
    String mallName = "PVR";
    String mallId = "1";
    String transactionId;
    String paymentMethod;
    String counter;

    TextView message;
    Button b;

    String TAG="payment Activity";

    TextView timerTextView;
    long startTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = startTime - System.currentTimeMillis();
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d : %02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
        userId = extras.getString("userId");
        mallName = extras.getString("mallName");
        mallId = extras.getString("mallId");
        transactionId=extras.getString("transactionId");
        paymentMethod=extras.getString("paymentMethod");

        Log.d(TAG,username);

        message = (TextView) findViewById(R.id.amount2);
        b = (Button) findViewById(R.id.button_home);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Home Delivery!!!");

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(payment.this);
                alertDialog.setTitle("Address");
                alertDialog.setMessage("Enter Address");

                final EditText input = new EditText(payment.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.logo1);

                alertDialog.setPositiveButton("Deliver",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String address = input.getText().toString();
                                if (address.compareTo("") == 0) {
                                    Toast.makeText(getApplicationContext(),"Please Enter Address",Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Log.d(TAG, address);
                                    Toast.makeText(getApplicationContext(),"Thanks for shopping.",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();


            }
        });

        String url = getString(R.string.globalUrl)+getString(R.string.payment);
        JSONObject data=new JSONObject();
        try {

            data.put("transactionId",transactionId);
            data.put("paymentMethod",paymentMethod);
            Log.d(TAG,data.toString());
            ServerConnect myServer=new ServerConnect();
            myServer.execute(url, data);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        timerTextView = (TextView) findViewById(R.id.textView2);

    }

//    @Override
  //  public void onBackPressed() {
    //}

    private class ServerConnect extends AsyncTask<Object,Void,Void> {
        HttpPost httppost;
        HttpClient httpclient;
        ProgressDialog asyncDialog = new ProgressDialog(payment.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setMessage(getString(R.string.paymentMessage));
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
                    payment.this.runOnUiThread(new Runnable() {
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
                    counter=j.getString("counter");
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
        message.setText("Your cart will be available on Counter Number : "+counter);

        Random r=new Random();
        long time=1000*60*(r.nextInt()%10+10);
        Log.d(TAG,time+" ");
        startTime = System.currentTimeMillis() + time;
        timerHandler.postDelayed(timerRunnable, 0);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payment, menu);
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
