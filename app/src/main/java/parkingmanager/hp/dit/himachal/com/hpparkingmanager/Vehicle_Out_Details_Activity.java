package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import Enum.TaskType;
import Interfaces.AsyncTaskListener;
import Utils.Generic_Async_Post;


public class Vehicle_Out_Details_Activity extends Activity implements AsyncTaskListener {

    TextView tv_parkingid,tv_drivername,tv_phonenumber,tv_vehiclenumber,tv_outtime , tv_message_from_server, tv_intimee;
    Button checkout,confirm,back;
    URL url_;
    HttpURLConnection conn_;
    StringBuilder sb = null;
    JSONStringer userJson = null;
    String OUT_TIME = null;
    String Aadhaar = null;
    private String Server_MAin_Time = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out__details);

        Intent getRoomDetailsIntent = getIntent();
        final Out_Pojo OUT_Details =  (Out_Pojo) getRoomDetailsIntent.getSerializableExtra("ADS_Details");

        SharedPreferences settings = getSharedPreferences(EConstants.PREF_NAME, MODE_PRIVATE);

        // Writing data to SharedPreferences
      // SharedPreferences.Editor editor = settings.edit();
      //  editor.putString("key", "some value");
       // editor.commit();

        // Reading from SharedPreferences
        Aadhaar = settings.getString("OperatorAadhaarNo", "");
        Log.e(" New Aadhaar is", Aadhaar);


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

                   /* CHECK_OUT_CAR COC = new CHECK_OUT_CAR();
                    COC.execute(parking_id,drivername,phone_number,vehicle_number,OUT_TIME,"checkout");*/
                    String URL = EConstants.Production_URL+"getParkingOut_JSON";
                    new Generic_Async_Post(Vehicle_Out_Details_Activity.this, Vehicle_Out_Details_Activity.this, TaskType.VEHICLE_CHECK_OUT).execute("getParkingOut_JSON",URL,parking_id,drivername,phone_number,vehicle_number,OUT_TIME);

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
                    String URL = EConstants.Production_URL+"getConfirmPayment_JSON";
                    new Generic_Async_Post(Vehicle_Out_Details_Activity.this, Vehicle_Out_Details_Activity.this, TaskType.VEHICLE_CHECK_OUT_CONFIRM).execute("getConfirmPayment_JSON",URL,parking_id,drivername,phone_number,vehicle_number,Server_MAin_Time,Aadhaar);


                }else{
                    Toast.makeText(Vehicle_Out_Details_Activity.this, "Connect to Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public void onTaskCompleted(String result, TaskType taskType) {

        if(taskType == TaskType.VEHICLE_CHECK_OUT) {
            String Result_to_Show = null;
            Log.e("New Result",result);
            Server_MAin_Time = result.substring(result.indexOf("(")+1,result.indexOf(")"));
            Log.e("Server_MAin_Time",Server_MAin_Time);
            Result_to_Show = Vehicle_In_Out_Json.Vehicle_Out_Parse(result);
            Custom_Dialog CM = new Custom_Dialog();
            CM.showDialog(Vehicle_Out_Details_Activity.this, Result_to_Show);
            tv_message_from_server.setText(Result_to_Show);
        }else if(taskType == TaskType.VEHICLE_CHECK_OUT_CONFIRM){
            String Result_to_Show = null;
            Log.e("Check Out",taskType.toString());
            Result_to_Show = Vehicle_In_Out_Json.Vehicle_Out_Confirm_Parse(result);
            Custom_Dialog CM2 = new Custom_Dialog();
            CM2.showDialog_Vehicle_IN_OUT(Vehicle_Out_Details_Activity.this, Result_to_Show);
        }else{
            Custom_Dialog CM2 = new Custom_Dialog();
            CM2.showDialog_Vehicle_IN_OUT(Vehicle_Out_Details_Activity.this, "Something went wrong.");
        }
    }


}
