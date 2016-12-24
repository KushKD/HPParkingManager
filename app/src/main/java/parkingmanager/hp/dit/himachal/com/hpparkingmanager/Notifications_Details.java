package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import Adapters.notifications_Adapter;
import HelperFunctions.AppStatus;
import JsonManager.Inbox_JSON;
import Model.Inbox_Pojo;
import Model.Notifications;
import Presentation.Custom_Dialog;
import Utils.EConstants;

public class Notifications_Details extends AppCompatActivity {

    public String ID = null;
    ProgressBar pb;
    URL url_;
    HttpURLConnection conn_;
    StringBuilder sb = new StringBuilder();
    ListView listv;
    Context context;
    List<Notifications_Details.Fetch_Inbox> tasks;  //Changet he Object and task
    List<Notifications> notifications_Server;   // change the list
    notifications_Adapter adapter;  // change the adapter

    LinearLayout LGone;
    EditText Search_EditText;
    Button refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications__details);

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
            Fetch_Inbox get_Inbox = new Fetch_Inbox();
            Log.e("We are","Here");
            get_Inbox.execute(ID);


        }else{
            Toast.makeText(Notifications_Details.this, "No Network", Toast.LENGTH_SHORT).show();
        }

        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notifications Inbox_Details = (Notifications) parent.getItemAtPosition(position);   //change object
                Custom_Dialog CD = new Custom_Dialog();
                CD.showDialog(Notifications_Details.this,Inbox_Details.getNotification());


            }
        });
    }


    protected void updateDisplay() {

       // LGone.setVisibility(View.VISIBLE);   //Adapter needs to be changed
        adapter = new notifications_Adapter(this, R.layout.item_inbox, notifications_Server);
        listv.setAdapter(adapter);
        //  adapter.notifyDataSetChanged();
        // listv.setTextFilterEnabled(true);

    }


   public class Fetch_Inbox extends AsyncTask<String,String,String> {
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
                url_ =new URL(EConstants.Production_URL+"getNotificationList_JSON/"+params[0]+"/na/out");
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
                    Log.e("Error",sb.toString());

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
            Log.e("Error",result);
            notifications_Server = Inbox_JSON.parseFeedNotifications(result);
            if(notifications_Server.isEmpty()){
                Toast.makeText(getApplicationContext(),"Notifications Empty",Toast.LENGTH_LONG).show();
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
