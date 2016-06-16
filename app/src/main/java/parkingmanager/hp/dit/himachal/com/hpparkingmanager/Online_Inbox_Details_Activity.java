package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import HelperFunctions.AppStatus;
import HelperFunctions.Date_Time;
import JsonManager.Manage_Json;
import Model.Inbox_Pojo;
import Presentation.Custom_Dialog;
import Utils.EConstants;

public class Online_Inbox_Details_Activity extends Activity {

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
        final Inbox_Pojo Inbox_Details =  (Inbox_Pojo) getRoomDetailsIntent.getSerializableExtra("INBOX");

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
                Online_Inbox_Details_Activity.this.finish();
            }
        });

        bt_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //Start Async Task

                if(AppStatus.getInstance(Online_Inbox_Details_Activity.this).isOnline()){
                    CHECKIN C_IN = new CHECKIN();
                    C_IN.execute(Inbox_Details);

                }else{
                    Toast.makeText(Online_Inbox_Details_Activity.this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    class CHECKIN extends AsyncTask<Object,String,String>{

        JSONStringer userJson = null;

        private ProgressDialog dialog;
        String url = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Online_Inbox_Details_Activity.this);
            this.dialog.setMessage("Please wait ..");
            this.dialog.show();
            this.dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(Object... objects) {
            Inbox_Pojo Inbox_Object_result = (Inbox_Pojo)objects[0];

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
                        .key("InTime").value(Date_Time.GetDateAndTime())
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

                 dialog.dismiss();
            Custom_Dialog DC = new Custom_Dialog();
            DC.showDialog_Vehicle_IN_OUT(Online_Inbox_Details_Activity.this,Result);



        }
    }
}
