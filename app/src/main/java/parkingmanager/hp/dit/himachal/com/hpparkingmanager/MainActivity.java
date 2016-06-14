package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Adapters.Parking_Adapter;
import JsonManager.ParkingJSON;
import Model.ParkingPOJO;
import Utils.EConstants;

public class MainActivity extends Activity {

   private String[] town;
   private Spinner spinner_town;
    private LinearLayout listServer;
    ProgressBar pb;
    URL url_;
    HttpURLConnection conn_;
    StringBuilder sb = new StringBuilder();
    ListView listv;
    Context context;
    List<GetParkingDetails> tasks;
    List<ParkingPOJO> Parking_Server;
    Parking_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner_town = (Spinner) findViewById(R.id.spinner_town);
        town = getResources().getStringArray(R.array.town);
        listv = (ListView) findViewById(R.id.list);
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setVisibility(View.INVISIBLE);
        context = this;
        tasks = new ArrayList<>();



        spinner_town.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                String Check = spinner_town.getSelectedItem().toString().trim();

                if (isOnline()) {

                    GetParkingDetails asy_Get_Vacancy = new GetParkingDetails();
                    asy_Get_Vacancy.execute("10297");
                } else {
                    Toast.makeText(getApplicationContext(),"Network not available.", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


               final ParkingPOJO vacancy_Details = (ParkingPOJO) parent.getItemAtPosition(position);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("You are about to select "+vacancy_Details.getParkingPlace()+". Are you sure you want to continue?");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {


                        try{
                            SharedPreferences settings = getSharedPreferences(EConstants.PREF_NAME, 0); // 0 - for private mode
                            SharedPreferences.Editor editor = settings.edit();
                            //Set "hasLoggedIn" to true
                            editor.putBoolean("hasParkingSelected", true);
                            editor.putString("ParkingID",vacancy_Details.getParkingID().toString());
                            editor.putString("Parking_Name",vacancy_Details.getParkingPlace());
                            editor.putString("Capacity",vacancy_Details.getCapacity());
                            editor.putString("thrashhold",vacancy_Details.getThrashholdValue());
                            editor.putString("Identifier",vacancy_Details.getIdentifier());
                            // Commit the edits!
                            editor.commit();
                            Intent Details_Intent = new Intent(MainActivity.this,ParkingDetails.class);
                            startActivity(Details_Intent);
                            MainActivity.this.finish();


                        }catch (Exception e){
                            Log.e("ERROR",e.getLocalizedMessage().toString());
                        }
                        // now get the lat/lon from the location and do something with it.
                        // nowDoSomethingWith(location.getLatitude(), location.getLongitude());
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                       // Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();


            }
        });


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

       // LGone.setVisibility(View.VISIBLE);
        adapter = new Parking_Adapter(this, R.layout.item_parking, Parking_Server);
        listv.setAdapter(adapter);

    }

    class GetParkingDetails extends AsyncTask<String,String,String> {
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
                url_ =new URL(EConstants.URL_GENERIC_Production+params[0]);
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
            Parking_Server = ParkingJSON.parseFeed(result);
            if(Parking_Server.isEmpty()){
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
