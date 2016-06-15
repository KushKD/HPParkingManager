package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Adapters.Inbox_Adapter;
import HelperFunctions.AppStatus;
import JsonManager.Inbox_Json;
import Model.Inbox_Pojo;
import Utils.EConstants;

public class Inbox_Activity extends Activity {

    public String ID = null;
    ProgressBar pb;
    URL url_;
    HttpURLConnection conn_;
    StringBuilder sb = new StringBuilder();
    ListView listv;
    Context context;
    List<Fetch_Inbox> tasks;  //Changet he Object and task
    List<Inbox_Pojo> Inbox_Server;   // change the list
    Inbox_Adapter adapter;  // change the adapter

    LinearLayout LGone;
    EditText Search_EditText;
    Button refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        Bundle bundle = getIntent().getExtras();

        ID = bundle.getString("ID");
        Log.e("id",ID);

        Search_EditText = (EditText)findViewById(R.id.edit_text_search);
        refresh = (Button)findViewById(R.id.refresh);
        LGone = (LinearLayout)findViewById(R.id.lgone);

        listv = (ListView) findViewById(R.id.list);
        context = this;
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setVisibility(View.INVISIBLE);
        tasks = new ArrayList<>();   //Write the Tasks

        //getParkedVehiclelist_JSON/{ParkingId}
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(Inbox_Activity.this).isOnline()) {
                    Search_EditText.setText("");
                    Fetch_Inbox get_Inbox = new Fetch_Inbox();
                    get_Inbox.execute(ID);
                } else {
                    Toast.makeText(getApplicationContext(),"Please Connect to Internet", Toast.LENGTH_LONG).show();
                }
            }
        });

        if(AppStatus.getInstance(this).isOnline()){

            //Create Async Class
            Fetch_Inbox get_Inbox = new Fetch_Inbox();
            get_Inbox.execute(ID);


        }else{
            Toast.makeText(Inbox_Activity.this, "No Network", Toast.LENGTH_SHORT).show();
        }

        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Inbox_Pojo Inbox_Details = (Inbox_Pojo) parent.getItemAtPosition(position);   //change object
                Intent userSearch = new Intent();
                userSearch.putExtra("INBOX", Inbox_Details);
                userSearch.setClass(Inbox_Activity.this, Inbox_Details_Activity.class);
                startActivity(userSearch);
                Inbox_Activity.this.finish();


            }
        });

        Search_EditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                //Main_Activity.this.adapt.getFilter().filter(s);
                //  String searchString=Search_EditText.getText().toString();
                //  adapter.getFilter().filter(searchString);
                // System.out.println("Text ["+s+"] - Start ["+start+"] - Before ["+before+"] - Count ["+count+"]");
               /* if (count < before) {
                    // We're deleting char so we need to reset the adapter data
                    adapter.resetData();
                }*/

                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Inbox_Activity.this.adapter.getFilter().filter(s);


            }
        });
    }



    protected void updateDisplay() {

         LGone.setVisibility(View.VISIBLE);   //Adapter needs to be changed
        adapter = new Inbox_Adapter(this, R.layout.item_inbox, Inbox_Server);
        listv.setAdapter(adapter);
        //  adapter.notifyDataSetChanged();
        // listv.setTextFilterEnabled(true);

    }



    class Fetch_Inbox extends AsyncTask<String,String,String> {
        String url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (tasks.size() == 0) {
                pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                //Change URL Function
                url_ =new URL(EConstants.Production_URL+"getAllParkReqest_JSON/"+params[0]);
                conn_ = (HttpURLConnection)url_.openConnection();
                conn_.setRequestMethod("GET");
                conn_.setUseCaches(false);
                conn_.setConnectTimeout(20000);
                conn_.setReadTimeout(20000);
                conn_.connect();

                int HttpResult =conn_.getResponseCode();
                if(HttpResult ==HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    System.out.print(sb.toString());

                }else{
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                if(conn_!=null)
                    conn_.disconnect();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Inbox_Server = Inbox_Json.parseFeed(result);
            if(Inbox_Server.isEmpty()){
                Toast.makeText(getApplicationContext(),"List Empty",Toast.LENGTH_LONG).show();
            }else
            {
                updateDisplay();
            }
            tasks.remove(this);
            if (tasks.size() == 0) {
                pb.setVisibility(View.INVISIBLE);
            }
        }
    }

}
