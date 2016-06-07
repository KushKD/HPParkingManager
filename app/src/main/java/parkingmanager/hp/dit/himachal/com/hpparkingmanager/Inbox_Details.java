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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONStringer;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import HelperFunctions.GetDateAndTime;
import JsonManager.Manage_Json;

public class Inbox_Details extends AppCompatActivity {

    private TextView tv_ParkingId,tv_RegisterId,tv_VehicleNo,tv_PhoneNumber,tv_RequestTime,tv_RequestStatus;
    private TextView tv_EstimatedTime , tv_VehicleType;
    private Button bt_back, bt_reject, bt_checkin;

    URL url_;
    HttpURLConnection conn_;
    StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox__details);

        //GTO

        Intent getRoomDetailsIntent = getIntent();
        final InboxPOJO Inbox_Details =  (InboxPOJO) getRoomDetailsIntent.getSerializableExtra("INBOX");

         tv_ParkingId = (TextView)findViewById(R.id.ParkingId);
       tv_RegisterId = (TextView)findViewById(R.id.RegisterId);
        tv_VehicleNo = (TextView)findViewById(R.id.VehicleNo);
       tv_PhoneNumber = (TextView)findViewById(R.id.PhoneNumber);
        tv_RequestTime = (TextView)findViewById(R.id.RequestTime);
       tv_RequestStatus = (TextView)findViewById(R.id.RequestStatus);
        tv_EstimatedTime = (TextView)findViewById(R.id.EstimatedTime);
        tv_VehicleType = (TextView)findViewById(R.id.VehicleType);
        bt_back = (Button)findViewById(R.id.backinbox);
        bt_reject = (Button)findViewById(R.id.reject);
        bt_checkin = (Button)findViewById(R.id.checkin);


                tv_ParkingId.setText(Inbox_Details.getParkingId());
                tv_RegisterId.setText(Inbox_Details.getRegisterId());
                tv_VehicleNo.setText(Inbox_Details.getVehicleNo());
                tv_PhoneNumber.setText(Inbox_Details.getPhoneNumber());
                tv_RequestTime.setText(Inbox_Details.getRequestTime());
                tv_RequestStatus.setText(Inbox_Details.getRequestStatus());
                tv_EstimatedTime.setText(Inbox_Details.getEstimatedTime());
                tv_VehicleType.setText(Inbox_Details.getVehicleType());


bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Inbox_Details.this.finish();
            }
        });

        bt_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //Start Async Task

                if(isOnline()){
                    CHECKIN C_IN = new CHECKIN();
                    C_IN.execute(Inbox_Details);

                }else{
                    Toast.makeText(Inbox_Details.this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
                }
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

    class CHECKIN extends AsyncTask<Object,String,String>{

        JSONStringer userJson = null;

        private ProgressDialog dialog;
        String url = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Inbox_Details.this);
            this.dialog.setMessage("Please wait ..");
            this.dialog.show();
            this.dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(Object... objects) {
            InboxPOJO Inbox_Object_result = (InboxPOJO)objects[0];

            try {
                url_ =new URL(EConstants.Production_URL+"getConfirmParkinStatus_JSON");
                conn_ = (HttpURLConnection)url_.openConnection();
                conn_.setDoOutput(true);
                conn_.setRequestMethod("POST");
                conn_.setUseCaches(false);
                conn_.setConnectTimeout(10000);
                conn_.setReadTimeout(10000);
                conn_.setRequestProperty("Content-Type", "application/json");
                conn_.connect();

                userJson = new JSONStringer()
                        .object().key("ParkInRequst")
                        .object()
                        .key("EstimatedTime").value(Inbox_Object_result.getEstimatedTime())
                        .key("InTime").value(GetDateAndTime.GetDateAndTime())
                        .key("ParkingId").value(Inbox_Object_result.getParkingId())
                        .key("PhoneNumber").value(Inbox_Object_result.getPhoneNumber())
                        .key("RegisterId").value(Inbox_Object_result.getRegisterId())
                        .key("RequestStatus").value("Comfirm")
                        .key("RequestTime").value(Inbox_Object_result.getRequestTime())
                        .key("VehicleNo").value(Inbox_Object_result.getVehicleNo())
                        .key("VehicleType").value(Inbox_Object_result.getVehicleType())
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("Message",s);

            String Result = Manage_Json.parseInward(s);

            Log.e("Message IS",Result);

                Toast.makeText(getApplicationContext(),Result,Toast.LENGTH_LONG).show();
                Inbox_Details.this.finish();
            dialog.dismiss();



        }
    }
}
