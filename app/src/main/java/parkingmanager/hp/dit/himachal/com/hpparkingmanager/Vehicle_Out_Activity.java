package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Adapters.Out_Adapter;
import HelperFunctions.AppStatus;
import JsonManager.Out_Json;
import Model.Out_Pojo;
import Presentation.Custom_Dialog;
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
    List<Out_Pojo> ads_Server;
    Out_Adapter adapter;
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


        }else{
            Toast.makeText(Vehicle_Out_Activity.this, "Please connect to Internet.", Toast.LENGTH_SHORT).show();
            //SMS Goes Here
            //// TODO: 7/6/2016
            //HP PARK OUT <ParkingId> <VehicleNo> <MobileNo>



            //Send an SMS Alert
            ShowAlert_SMS("");
          //  Log.e("SMS :",DATASEND);
          //  Log.e("SMS Length",Integer.toString(DATASEND.length()));
        }



        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Out_Pojo Ads_Details = (Out_Pojo) parent.getItemAtPosition(position);
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


    @Override
    protected void onResume() {
        super.onResume();

       if(adapter!=null){
           //Reload data
           ads_Server.clear();
           adapter = null;
           //itemList = getOrderList();
           if(AppStatus.getInstance(this).isOnline()){

               GET_CARS_FOR_OUT asy_Get_Ads = new GET_CARS_FOR_OUT();
               asy_Get_Ads.execute(ID);

              // updateDisplay();



           }else{
               Toast.makeText(Vehicle_Out_Activity.this, "No Network", Toast.LENGTH_SHORT).show();
           }


       }

    }



    private void ShowAlert_SMS(String x) {

       // Log.d("SMS is ==========",DataSend);
        final Dialog dialog = new Dialog(Vehicle_Out_Activity.this); // Context, this, etc.
        dialog.setContentView(R.layout.dialog_sms_out);
        dialog.setTitle("Send SMS");
        dialog.setCancelable(false);
        dialog.show();



        EditText carnumber = (EditText)dialog.findViewById(R.id.carnumber);
        EditText phonenumber = (EditText)dialog.findViewById(R.id.phonenumber);
        Button agree = (Button)dialog.findViewById(R.id.dialog_ok);
        Button disagree = (Button)dialog.findViewById(R.id.dialog_cancel);
        TextView SMS = (TextView)dialog.findViewById(R.id.sms);


        //Send SMS
        StringBuilder SB = new StringBuilder();
        SB.append("HP PARK OUT");SB.append(" ");
        SB.append(ID);SB.append(" ");
        SB.append(carnumber.getText().toString().trim()); SB.append(" ");
        SB.append(phonenumber.getText().toString().trim());
       final String DataSend = SB.toString().trim();

       // SMS.setText(DataSend);

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Phone Number
                 * Message
                 */
                sendSMS("9223166166",DataSend);
                dialog.dismiss();
            }
        });

        disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    private void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        //  Toast.makeText(getBaseContext(), "SMS delivered",Toast.LENGTH_SHORT).show();
                        Custom_Dialog CM_SMS_Delievered = new Custom_Dialog();
                        CM_SMS_Delievered.showDialog_Vehicle_IN_OUT(Vehicle_Out_Activity.this, "SMS delivered ");                     //  Toast.makeText(getBaseContext(), \"SMS delivered\",Toast.LENGTH_SHORT).show();\n");
                        break;
                    case Activity.RESULT_CANCELED:
                        Custom_Dialog CM_SMS_Delievered_Not = new Custom_Dialog();
                        CM_SMS_Delievered_Not.showDialog_Vehicle_IN_OUT(Vehicle_Out_Activity.this, "SMS not delivered");
                       /* Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();*/
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
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
            ads_Server = Out_Json.parseFeed(result);
            if(ads_Server.isEmpty()){
                Toast.makeText(getApplicationContext(),"List Empty",Toast.LENGTH_LONG).show();
            }else
            {
                LGone.setVisibility(View.VISIBLE);
                adapter = new Out_Adapter(Vehicle_Out_Activity.this, R.layout.item_out_list, ads_Server);
                listv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                adapter.setNotifyOnChange (true);
            }
            tasks.remove(this);
            if (tasks.size() == 0) {
                pb.setVisibility(View.INVISIBLE);
            }
        }
    }
}
