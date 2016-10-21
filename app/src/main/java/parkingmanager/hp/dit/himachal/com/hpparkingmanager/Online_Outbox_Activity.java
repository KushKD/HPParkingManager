package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

import Adapters.Outbox_Adapter;
import HelperFunctions.AppStatus;
import JsonManager.Outbox_JSON;
import Model.Outbox_Pojo;
import Utils.EConstants;

public class Online_Outbox_Activity extends Activity {
    public String ID = null;
    ProgressBar pb;
    URL url_;
    HttpURLConnection conn_;
    StringBuilder sb = new StringBuilder();
    ListView listv;
    Context context;
    List<Fetch_Outbox> tasks;  //Changet he Object and task
    List<Outbox_Pojo> Outbox_Server;   // change the list
    Outbox_Adapter adapter;  // change the adapter
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outbox);

        Bundle bundle = getIntent().getExtras();

        ID = bundle.getString("ID");
        Log.e("id",ID);


        listv = (ListView) findViewById(R.id.list);
        context = this;
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setVisibility(View.INVISIBLE);
        tasks = new ArrayList<>();   //Write the Tasks

        if(AppStatus.getInstance(this).isOnline()){

            //Create Async Class
            Fetch_Outbox get_Outbox = new Fetch_Outbox();
            get_Outbox.execute(ID);


        }else{
            Toast.makeText(Online_Outbox_Activity.this, "No Network", Toast.LENGTH_SHORT).show();
        }

        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Outbox_Pojo Outbox_Details = (Outbox_Pojo) parent.getItemAtPosition(position);   //change object
                Intent userSearch = new Intent();
                userSearch.putExtra("OUTBOX", Outbox_Details);
                userSearch.setClass(Online_Outbox_Activity.this, Online_Outbox_Details_Activity.class);
                startActivity(userSearch);
                Online_Outbox_Activity.this.finish();


            }
        });
    }



    protected void updateDisplay() {

        // LGone.setVisibility(View.VISIBLE);   Adapter needs to be changed
        adapter = new Outbox_Adapter(this, R.layout.item_outbox, Outbox_Server);
        listv.setAdapter(adapter);
        //  adapter.notifyDataSetChanged();
        // listv.setTextFilterEnabled(true);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(AppStatus.getInstance(this).isOnline()){

            //Create Async Class
            Fetch_Outbox get_Outbox = new Fetch_Outbox();
            get_Outbox.execute(ID);


        }else{
            Toast.makeText(Online_Outbox_Activity.this, "No Network", Toast.LENGTH_SHORT).show();
        }
    }

    class Fetch_Outbox extends AsyncTask<String,String,String> {
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
                url_ =new URL(EConstants.Production_URL+"getAllParkOutReqest_JSON/"+params[0]);
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
            Log.e("Error",sb.toString());
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Outbox_Server = Outbox_JSON.parseFeed(result);
            if(Outbox_Server.isEmpty()){
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
