package com.example.mohitgarg.fintech;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class Register extends ActionBarActivity {

    Button loginButton;
    Button registerButton;
    EditText mobileEditText;
    EditText usernameEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;

    String mobile;
    String username;
    String password;
    String confirmPassword;


    String TAG="Register Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        TextView check=(TextView) findViewById(R.id.appname);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/GOODDP__.TTF");
        check.setTypeface(type);

        TextView check1=(TextView) findViewById(R.id.tagline);
        check1.setTypeface(type);


        loginButton = (Button) findViewById(R.id.login);

        registerButton = (Button) findViewById(R.id.register);

        mobileEditText = (EditText) findViewById(R.id.mobile);

        usernameEditText = (EditText) findViewById(R.id.username);

        passwordEditText = (EditText) findViewById(R.id.password);

        confirmPasswordEditText = (EditText) findViewById(R.id.confirmPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        Log.d(TAG,"Register Button Clicked");

        mobile = mobileEditText.getText().toString();
        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        confirmPassword = confirmPasswordEditText.getText().toString();

        Log.d(TAG,"mobile = "+mobile + "username = "+username + "password = " + password + "confirm password = "+confirmPassword);

        if (mobile.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Enter Mobile Number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mobile.length()!=10)
        {
            Toast.makeText(getApplicationContext(),"Invalid Mobile Number",Toast.LENGTH_SHORT).show();
            return;
        }
        if (username.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Enter Username", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }
        if (confirmPassword.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Enter Confirm Password",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword))
        {
            Toast.makeText(getApplicationContext(),"Passwords Do not Match",Toast.LENGTH_SHORT).show();
            return;
        }

        //register here
        JSONObject data=new JSONObject();
        try {
            data.put("mobileNumber",mobile);
            data.put("username",username);
            data.put("password",password);
            data.put("confirmPassword",confirmPassword);
            ServerConnect myServer=new ServerConnect();
            myServer.execute(getString(R.string.globalUrl)+getString(R.string.register),data);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class ServerConnect extends AsyncTask<Object,Void,Void>
    {
        HttpPost httppost;
        HttpClient httpclient;
        ProgressDialog asyncDialog = new ProgressDialog(Register.this);


        @Override
        protected void onPreExecute() {
            asyncDialog.setMessage(getString(R.string.registerMessage));
            asyncDialog.show();
            asyncDialog.setCanceledOnTouchOutside(false);
            super.onPreExecute();
        }



        @Override
        protected Void doInBackground(Object... params) {
            httpclient=new DefaultHttpClient();
            Log.d(TAG,params[0]+" ");
            httppost= new HttpPost((String)params[0]);

            try {
                StringEntity se = new StringEntity( params[1].toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httppost.setEntity(se);
                HttpResponse response = httpclient.execute(httppost);
                Log.d(TAG,response.toString());

                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String json = reader.readLine();
                JSONTokener tokener = new JSONTokener(json);
                final JSONObject j = new JSONObject(tokener);
                Log.d(TAG,j.toString());

                if (j.getString("status").equals("false"))
                {
                    Register.this.runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                Toast.makeText(getApplicationContext(),j.getString("message"),Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else
                {
                    Log.d(TAG,j.getString("status"));
                    Log.d(TAG,j.getString("message"));
                    String userId = j.getString("userId");
                    String username = j.getString("username");
                    Intent i=new Intent(Register.this,MallsList.class);
                    i.putExtra("userId",userId);
                    i.putExtra("username",username);
                    startActivity(i);
                    //Redirect Here
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

            super.onPostExecute(result);
        }
    };


    private void login() {
        Log.d(TAG, "Login Button Clicked");


        Intent i=new Intent(Register.this,Login.class);
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
