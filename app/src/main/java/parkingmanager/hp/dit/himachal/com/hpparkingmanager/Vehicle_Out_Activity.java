package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import Adapters.OUT_Adapter;
import HelperFunctions.AppStatus;
import JsonManager.Out_Json;
import Model.OUT_POJO;
import Utils.EConstants;

public class Vehicle_Out_Activity extends Activity {

    String ID = null;
    private String Date_Service = null;


    ProgressBar pb;
    URL url_;
    HttpURLConnection conn_;
    StringBuilder sb = new StringBuilder();
    ListView listv;
    Context context;
    List<GET_CARS_FOR_OUT> tasks;
    List<OUT_POJO> ads_Server;
    OUT_Adapter adapter;
    LinearLayout LGone;
    EditText Search_EditText;
    Button refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_);

        Bundle bundle = getIntent().getExtras();

        ID = bundle.getString("ID");
        Log.e("id",ID);
        Search_EditText = (EditText)findViewById(R.id.edit_text_search);
        refresh = (Button)findViewById(R.id.refresh);
        LGone = (LinearLayout)findViewById(R.id.lgone);

        listv = (ListView) findViewById(R.id.list_ads);
        context = this;
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setVisibility(View.INVISIBLE);
        tasks = new ArrayList<>();


        //getParkedVehiclelist_JSON/{ParkingId}
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(Vehicle_Out_Activity.this).isOnline()) {
                    Search_EditText.setText("");
                    GET_CARS_FOR_OUT asy_Get_Ads = new GET_CARS_FOR_OUT();
                    asy_Get_Ads.execute(ID);
                } else {
                    Toast.makeText(getApplicationContext(),"Please Connect to Internet", Toast.LENGTH_LONG).show();
                }
            }
        });

        if(AppStatus.getInstance(this).isOnline()){

            GET_CARS_FOR_OUT asy_Get_Ads = new GET_CARS_FOR_OUT();
            asy_Get_Ads.execute(ID);


        }else if(!AppStatus.getInstance(this).isOnline()){

          //  setContentView(R.layout.activity_splash_screen);

            Intent i = new Intent(Vehicle_Out_Activity.this, Vehicle_Out_Offline.class);
            i.putExtra("ID",ID);
            startActivity(i);
            Vehicle_Out_Activity.this.finish();
        }else{
            Toast.makeText(Vehicle_Out_Activity.this, "Please connect to Internet.", Toast.LENGTH_SHORT).show();

        }



        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OUT_POJO Ads_Details = (OUT_POJO) parent.getItemAtPosition(position);
                Intent userSearch = new Intent();
                userSearch.putExtra("ADS_Details", Ads_Details);
                userSearch.setClass(Vehicle_Out_Activity.this, Vehicle_Out_Details_Activity.class);
                startActivity(userSearch);
                Vehicle_Out_Activity.this.finish();


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
                Vehicle_Out_Activity.this.adapter.getFilter().filter(s);


            }
        });


    }











    class GET_CARS_FOR_OUT extends AsyncTask<String,String,String> {
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

                url_ =new URL(EConstants.Production_URL+"getParkedVehiclelist_JSON/"+params[0]);
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

            if(AppStatus.getInstance(Vehicle_Out_Activity.this).isOnline()){
                ads_Server = Out_Json.parseFeed(result);
                if(ads_Server.isEmpty()){
                    Toast.makeText(getApplicationContext(),"List Empty",Toast.LENGTH_LONG).show();
                }else
                {
                    LGone.setVisibility(View.VISIBLE);
                    adapter = new OUT_Adapter(Vehicle_Out_Activity.this, R.layout.item_out_list, ads_Server);
                    listv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.setNotifyOnChange (true);
                }
                tasks.remove(this);
                if (tasks.size() == 0) {
                    pb.setVisibility(View.INVISIBLE);
                }

            }else if(!AppStatus.getInstance(Vehicle_Out_Activity.this).isOnline()){
                Toast.makeText(getApplicationContext(),"You are Offline",Toast.LENGTH_LONG).show();
            }
        }
    }
}
