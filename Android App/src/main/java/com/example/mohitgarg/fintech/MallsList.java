package com.example.mohitgarg.fintech;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
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


public class MallsList extends ActionBarActivity {

    ListView lv;
    myAdapter adapter;
    EditText inputSearch;
    String TAG="MallsList Activity";
    ArrayList<Mall> mallsList = new ArrayList<Mall>();

    String username = "mohit";
    String userId = "1";

    public class Mall {
        public String mallName;
        public String mallId;

        public Mall(String name,String id)
        {
            mallName = name;
            mallId = id;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_malls_list);

        Bundle extras = getIntent().getExtras();

        username = extras.getString("username","mohit");
        userId = extras.getString("userId","1");

        Log.d(TAG,"Welcome "+username);

        ServerConnect myServer=new ServerConnect();
        myServer.execute(getString(R.string.globalUrl)+getString(R.string.malls));

    }

    private class ServerConnect extends AsyncTask<Object,Void,JSONArray>
    {
        HttpPost httppost;
        HttpClient httpclient;
        ProgressDialog asyncDialog = new ProgressDialog(MallsList.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setMessage(getString(R.string.mallMessage));
            asyncDialog.show();
            asyncDialog.setCanceledOnTouchOutside(false);
            super.onPreExecute();
        }


        @Override
        protected JSONArray doInBackground(Object... params) {
            httpclient=new DefaultHttpClient();
            Log.d(TAG, params[0] + " ");
            httppost= new HttpPost((String)params[0]);

            try {
                HttpResponse response = httpclient.execute(httppost);
                Log.d(TAG,response.toString());

                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String json = reader.readLine();
                JSONTokener tokener = new JSONTokener(json);
                JSONObject j = new JSONObject(tokener);

                Log.d(TAG,j.getString("mallList"));

                tokener = new JSONTokener(j.getString("mallList"));
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
            JSONObject mall;

            for (int i=0;i<result.length();i++)
            {
                try {
                    Log.d(TAG,result.get(i).toString());
                    tokener = new JSONTokener(result.get(i).toString());
                    mall = new JSONObject(tokener);
                    Log.d(TAG,mall.toString());
                    mallsList.add(new Mall(mall.get("mallName").toString(),mall.get("id").toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            asyncDialog.dismiss();

            displayList();

            super.onPostExecute(result);
        }
    }

    public class myAdapter extends ArrayAdapter<Mall>{

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

            mallName.setText(mall.mallName);

            return convertView;
        }
    }

    private void displayList() {

        lv = (ListView) findViewById(R.id.malls_list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        adapter = new myAdapter(this,R.id.malls_list_view, mallsList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Mall mall=mallsList.get(position);

                String selectedMallName = mall.mallName;
                String selectedMallId = mall.mallId;

                Intent i=new Intent(MallsList.this,InsideMall.class);
                i.putExtra("mallName",selectedMallName);
                i.putExtra("mallId",selectedMallId);
                i.putExtra("username",username);
                i.putExtra("userId",userId);
                startActivity(i);

                Log.d(TAG,selectedMallId+" "+selectedMallName);


            }
        });
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text

                int textlength = cs.length();
                ArrayList<Mall> tempArrayList = new ArrayList<Mall>();
                for(Mall c: mallsList){
                    if (textlength <= c.mallName.length()) {
                        if (c.mallName.toLowerCase().contains(cs.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }
                myAdapter mAdapter = new myAdapter(MallsList.this,R.id.malls_list_view, tempArrayList);
                lv.setAdapter(mAdapter);

                MallsList.this.adapter.getFilter().filter(cs);
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


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_malls_list, menu);
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
        if (id == R.id.action_profile)
        {
            return true;
        }
        if (id==R.id.action_track_order)
        {
            return true;
        }
        if (id==R.id.action_logout)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
