package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Adapters.Reports_Collection_Adapter;
import JsonManager.Reports_Collection_Json;
import Model.Collection_Reports_Pojo;
import Utils.EConstants;

public class List_Reports extends AppCompatActivity {

    private String Date_Service_From = null;
    private  String Date_Service_To = null;
    private String ID_Server = null;
    private String Reformated_From_Date,Reformated_To_Date = null;

    private String Date_Service = null;
    URL url_;
    HttpURLConnection conn_;
    StringBuilder sb = new StringBuilder();

    ListView listv;
    Context context;

    List<GET_REPORT> tasks;
    List<Collection_Reports_Pojo> Collection_Reports_pojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__reports);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ID_Server = bundle.getString("ID");
        Date_Service_From = bundle.getString("FROM");
        Date_Service_To = bundle.getString("TO");





        listv = (ListView) findViewById(R.id.list);
        context = this;


        tasks = new ArrayList<>();

        if (isOnline()) {
            GET_REPORT asy_Get_PD = new GET_REPORT();
            asy_Get_PD.execute(ID_Server,Date_Service_From,Date_Service_To);
        } else {
            Toast.makeText(List_Reports.this, "", Toast.LENGTH_SHORT).show();
        }
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    protected void updateDisplay() {

        Reports_Collection_Adapter adapter = new Reports_Collection_Adapter(this, R.layout.item_reports_collection, Collection_Reports_pojo);
        listv.setAdapter(adapter);
    }

    class GET_REPORT extends AsyncTask<String,String,String> {

        private ProgressDialog dialog;
        String url = null;
        String P_ID_Server = null;
        String FromDate_Server = null;
        String ToDate_Server = null;
        JSONStringer userJson = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (tasks.size() == 0) {
                dialog = new ProgressDialog(List_Reports.this);
                this.dialog.setMessage("Please wait ..");
                this.dialog.show();
                this.dialog.setCancelable(false);
                this.dialog.setCancelable(false);
            }
            tasks.add(this);
        }

        @Override
        protected String doInBackground(String... params) {

            P_ID_Server = params[0];
            FromDate_Server = params[1];
            ToDate_Server = params[2];


            try {
                url_ =new URL(EConstants.Production_URL+"getDailyReport_JSON");
                conn_ = (HttpURLConnection)url_.openConnection();
                conn_.setDoOutput(true);
                conn_.setRequestMethod("POST");
                conn_.setUseCaches(false);
                conn_.setConnectTimeout(10000);
                conn_.setReadTimeout(10000);
                conn_.setRequestProperty("Content-Type", "application/json");
                conn_.connect();

                userJson = new JSONStringer()
                        .object().key("fDate")
                        .object()
                        .key("ParkingId").value(P_ID_Server)
                        .key("FromDate").value(FromDate_Server)
                        .key("LastDate").value(ToDate_Server)
                        .endObject()
                        .endObject();


                System.out.println(userJson.toString());
                Log.e("Object",userJson.toString());
                OutputStreamWriter out = new OutputStreamWriter(conn_.getOutputStream());
                out.write(userJson.toString());
                out.close();

                try{
                    int HttpResult =conn_.getResponseCode();
                    if(HttpResult ==HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        System.out.println(sb.toString());

                    }else{
                        System.out.println("Server Connection failed.");
                    }

                } catch(Exception e){
                    return "Server Connection failed.";
                }

            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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
            Collection_Reports_pojo = Reports_Collection_Json.parseFeed(result);
            if(Collection_Reports_pojo.isEmpty()){
                Toast.makeText(getApplicationContext(),"No record found.",Toast.LENGTH_LONG).show();
            }else
            {
                updateDisplay();
            }
            tasks.remove(this);
            if (tasks.size() == 0) {
              //  pb.setVisibility(View.INVISIBLE);
                this.dialog.dismiss();
            }
        }
    }
}
