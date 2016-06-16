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
import java.text.SimpleDateFormat;
import java.util.Calendar;

import HelperFunctions.AppStatus;
import JsonManager.Vehicle_In_Out_Json;
import Model.Out_Pojo;
import Presentation.Custom_Dialog;
import Utils.EConstants;

public class Vehicle_Out_Details_Activity extends Activity {

    TextView tv_parkingid,tv_drivername,tv_phonenumber,tv_vehiclenumber,tv_outtime , tv_message_from_server, tv_intimee;
    Button checkout,confirm,back;
    URL url_;
    HttpURLConnection conn_;
    StringBuilder sb = null;
    JSONStringer userJson = null;
    String OUT_TIME = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out__details);

        Intent getRoomDetailsIntent = getIntent();
        final Out_Pojo OUT_Details =  (Out_Pojo) getRoomDetailsIntent.getSerializableExtra("ADS_Details");


                tv_parkingid = (TextView)findViewById(R.id.parkingid);
                tv_drivername = (TextView)findViewById(R.id.drivername);
                tv_phonenumber = (TextView)findViewById(R.id.phonenumber);
                tv_vehiclenumber = (TextView)findViewById(R.id.vehiclenumber);
                tv_outtime = (TextView)findViewById(R.id.outtime);
                checkout = (Button)findViewById(R.id.checkout);
                tv_message_from_server = (TextView)findViewById(R.id.message_from_server);
                confirm = (Button)findViewById(R.id.confirm);
                tv_intimee = (TextView)findViewById(R.id.intime);

        back = (Button)findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vehicle_Out_Details_Activity.this.finish();
            }
        });





        tv_parkingid.setText(OUT_Details.getParkingId());
        tv_drivername.setText(OUT_Details.getDriverName());
        tv_phonenumber.setText(OUT_Details.getPhoneNumber());
        tv_vehiclenumber.setText(OUT_Details.getVehicleNo());
        tv_intimee.setText(OUT_Details.getParkInTime());




        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send data to server
                if(AppStatus.getInstance(Vehicle_Out_Details_Activity.this).isOnline()){

                    String parking_id = OUT_Details.getParkingId();
                    String drivername = OUT_Details.getDriverName();
                    String phone_number = OUT_Details.getPhoneNumber();
                    String vehicle_number = OUT_Details.getVehicleNo();
                  //  String out_time = tv_outtime.getText().toString().trim();
                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => "+c.getTime());

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());
                   OUT_TIME = formattedDate;
                    tv_outtime.setText(OUT_TIME);

                    CHECK_OUT_CAR COC = new CHECK_OUT_CAR();
                    COC.execute(parking_id,drivername,phone_number,vehicle_number,OUT_TIME,"checkout");

                }else{
                    Toast.makeText(Vehicle_Out_Details_Activity.this, "Connect to Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(AppStatus.getInstance(Vehicle_Out_Details_Activity.this).isOnline()){

                    if(tv_message_from_server.getText().length()!=0)
                    {
                        tv_message_from_server.setText("");
                    }
                    String parking_id = OUT_Details.getParkingId();
                    String drivername = OUT_Details.getDriverName();
                    String phone_number = OUT_Details.getPhoneNumber();
                    String vehicle_number = OUT_Details.getVehicleNo();
                    String out_time = tv_outtime.getText().toString().trim();

                    CHECK_OUT_CAR COC = new CHECK_OUT_CAR();
                    COC.execute(parking_id,drivername,phone_number,vehicle_number,out_time,"confirm");

                }else{
                    Toast.makeText(Vehicle_Out_Details_Activity.this, "Connect to Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }




    private class CHECK_OUT_CAR extends AsyncTask<String,String,String> {

        private String Parking_Id = null;
        private String Driver_Name = null;
        private String Phone_number = null;
        private String Vehicle_NO = null;
        private String OUT_Time = null;
        private String function_Name = null;
        private String flag_Function = null;
        private String Result_to_Show = null;
        private ProgressDialog dialog;
        String url = null;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Vehicle_Out_Details_Activity.this);
            this.dialog.setMessage("Please wait ..");
            this.dialog.show();
            this.dialog.setCancelable(false);
        }


        @Override
        protected String doInBackground(String... params) {
            Parking_Id = params[0];
            Driver_Name = params[1];
            Phone_number = params[2];
            Vehicle_NO = params[3];
            OUT_Time = params[4];
            flag_Function = params[5];

            if(flag_Function.equalsIgnoreCase("confirm")){
                function_Name =  "getConfirmPayment_JSON";
            }else{
                function_Name = "getParkingOut_JSON";
            }


            try {
                url_ =new URL(EConstants.Production_URL+function_Name);
                conn_ = (HttpURLConnection)url_.openConnection();
                conn_.setDoOutput(true);
                conn_.setRequestMethod("POST");
                conn_.setUseCaches(false);
                conn_.setConnectTimeout(20000);
                conn_.setReadTimeout(20000);
                conn_.setRequestProperty("Content-Type", "application/json");
                conn_.connect();

                 userJson = new JSONStringer()
                        .object().key("VehicleOuts")
                        .object()
                        .key("VehicleNo").value(Vehicle_NO)
                        .key("ParkingId").value(Parking_Id)
                        .key("DriverName").value(Driver_Name)
                        .key("PhoneNumber").value(Phone_number)
                        .key("OutTime").value(OUT_Time)
                        .endObject()
                        .endObject();


                System.out.println(userJson.toString());
                Log.e("Object",userJson.toString());
                OutputStreamWriter out = new OutputStreamWriter(conn_.getOutputStream());
                out.write(userJson.toString());
                out.close();

                try{


                    int HttpResult =conn_.getResponseCode();
                    Log.e("HttpResult",Integer.toString(HttpResult));
                    if(HttpResult ==HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                        String line = null;
                        sb = new StringBuilder();
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        System.out.println(sb.toString());

                    }else{
                        System.out.println("Server Connection failed." + HttpResult);
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

            if(function_Name.equalsIgnoreCase("getParkingOut_JSON")) {
                    Result_to_Show = Vehicle_In_Out_Json.Vehicle_Out_Parse(s);
                    dialog.dismiss();
                    Custom_Dialog CM = new Custom_Dialog();
                    CM.showDialog(Vehicle_Out_Details_Activity.this, Result_to_Show);
                    tv_message_from_server.setText(Result_to_Show);
                }else{
                    Result_to_Show = Vehicle_In_Out_Json.Vehicle_Out_Confirm_Parse(s);
                    dialog.dismiss();
                    Custom_Dialog CM = new Custom_Dialog();
                    CM.showDialog_Vehicle_IN_OUT(Vehicle_Out_Details_Activity.this, Result_to_Show);

                }
        }
    }
}