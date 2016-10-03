package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import Interfaces.AsyncTaskListener;
import JsonManager.Vehicle_In_Out_Json;
import Presentation.Custom_Dialog;
import Utils.EConstants;
import Utils.Generic_Async_Post;
import Enum.TaskType;

public class Vehicle_In_Activity extends Activity implements AsyncTaskListener {

    String ID = null;
    Spinner s_typecar,s_estimatedtime;
    EditText carnumber_,drivername_,phonenumber_;
    Button park,back;
    String[] car_type,time_estimate;
    URL url_;
    HttpURLConnection conn_;
    StringBuilder sb = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_);

        Bundle bundle = getIntent().getExtras();

        ID = bundle.getString("ID");
        Log.e("id",ID);



        s_typecar = (Spinner)findViewById(R.id.typecar);
        s_estimatedtime = (Spinner)findViewById(R.id.estimatedtime);
        carnumber_ = (EditText)findViewById(R.id.carnumber);
        drivername_ = (EditText)findViewById(R.id.drivername);
        phonenumber_ =(EditText)findViewById(R.id.phonenumber);

        park = (Button) findViewById(R.id.park);
        back = (Button)findViewById(R.id.back);

        car_type = getResources().getStringArray(R.array.type_of_car);
        time_estimate = getResources().getStringArray(R.array.estimatedtime);


        park.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              final  String typecar = s_typecar.getSelectedItem().toString().trim();


              final  long estimated_Time = s_estimatedtime.getSelectedItemId();

                Log.e("Final Estimated Time",Long.toString(estimated_Time));
               final String car_number = carnumber_.getText().toString().trim();
               final String phonenumber = phonenumber_.getText().toString().trim();
               final String Parking_ID = ID.trim();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final  String formattedDate = df.format(c.getTime());
                String IN_TIME = formattedDate;

                if(phonenumber.length()==10 && phonenumber!=null){
                    if(car_number.length()!=0 && car_number!=null){
                        if(Parking_ID.length()!=0 && Parking_ID!=null){
                            if(AppStatus.getInstance(Vehicle_In_Activity.this).isOnline()) {
                              final String URL = EConstants.Production_URL+"getParkingTransaction_JSON";

                                //Add Alert
                                if(estimated_Time>0){
                                    String message = Long.toString(estimated_Time);
                                    String main_message = null;

                                    switch(message){

                                        case "1": message.equalsIgnoreCase("1");
                                            main_message = "Upto 3 Hours";
                                            break;

                                        case "2": message.equalsIgnoreCase("2");
                                            main_message = "Upto 4 Hours";
                                            break;

                                        case "3": message.equalsIgnoreCase("3");
                                            main_message = "Upto 6 Hours";
                                            break;

                                        case "4": message.equalsIgnoreCase("4");
                                            main_message = "Upto 8 Hours";
                                            break;

                                        case "5": message.equalsIgnoreCase("5");
                                            main_message = "Upto 10 Hours";
                                            break;

                                        case "6": message.equalsIgnoreCase("6");
                                            main_message = "Upto 12 Hours";
                                            break;

                                        case "7": message.equalsIgnoreCase("7");
                                            main_message = "Upto 24 Hours";
                                            break;

                                        case "8": message.equalsIgnoreCase("8");
                                            main_message = "Upto 1 Month";
                                            break;

                                        case "9": message.equalsIgnoreCase("9");
                                            main_message = "Upto 1 Hour";
                                            break;


                                        default: main_message = "Something Bad Happened";
                                            break;

                                    }

                                    Log.e("Time for alert",Long.toString(estimated_Time));
                                  // Custom_Dialog CD = new Custom_Dialog();
                                  //  CD.showDialog(Vehicle_In_Activity.this,"You have selected " + main_message +". \n Are You sure you want to continue?");
                                    Log.e("Time After Selection",Long.toString(estimated_Time));
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Vehicle_In_Activity.this);
                                    builder.setMessage("You have selected " + main_message +". \n Are You sure you want to continue?")
                                            .setCancelable(false)
                                            .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    new Generic_Async_Post(Vehicle_In_Activity.this, Vehicle_In_Activity.this, TaskType.VEHICLE_IN).execute("getParkingTransaction_JSON",URL,Parking_ID,typecar,car_number,"",phonenumber,Long.toString(estimated_Time),formattedDate);

                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();

                                   //   }
                                    // else{

                                   }else {
                                    Log.e("Time",Long.toString(estimated_Time));
                                    new Generic_Async_Post(Vehicle_In_Activity.this, Vehicle_In_Activity.this, TaskType.VEHICLE_IN).execute("getParkingTransaction_JSON",URL,Parking_ID,typecar,car_number,"",phonenumber,Long.toString(estimated_Time),formattedDate);

                                }




                            }else{
                                Toast.makeText(Vehicle_In_Activity.this, "Please connect to Internet.", Toast.LENGTH_SHORT).show();
                                //Send SMS
                                StringBuilder SB = null;
                                 SB = new StringBuilder();
                                SB.append("HP PARK IN");SB.append(" ");
                                SB.append(Parking_ID);SB.append(" ");
                                SB.append(car_number); SB.append(" ");
                                SB.append(phonenumber); SB.append(" ");
                               // SB.append(typecar);SB.append(" ");
                                if(typecar.equalsIgnoreCase("Small Car")){
                                    SB.append("1");SB.append(" ");
                                }else{
                                    SB.append("2");SB.append(" ");
                                }
                                SB.append(Long.toString(estimated_Time));
                                String DATASEND = SB.toString().trim();

                                //Send an SMS Alert
                                ShowAlert_SMS(DATASEND);
                                Log.e("SMS :",DATASEND);
                                Log.e("SMS Length",Integer.toString(DATASEND.length()));
                            }
                        }else{
                            Custom_Dialog CM = new Custom_Dialog();
                            CM.showDialog(Vehicle_In_Activity.this,"Something is Bad..");

                        }
                    }else{
                        Custom_Dialog CM1 = new Custom_Dialog();
                        CM1.showDialog(Vehicle_In_Activity.this,"Please enter vehicle number");
                       // Toast.makeText(Vehicle_In_Activity.this, "Please enter vehicle number", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Custom_Dialog CM2 = new Custom_Dialog();
                    CM2.showDialog(Vehicle_In_Activity.this,"Please enter valid 10 digit phone number.");
                  //  Toast.makeText(Vehicle_In_Activity.this, "Please enter valid 10 digit phone number.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vehicle_In_Activity.this.finish();
            }
        });

    }

    private void ShowAlert_SMS(final String DataSend) {

        Log.d("SMS is ==========",DataSend);
        final Dialog dialog = new Dialog(Vehicle_In_Activity.this); // Context, this, etc.
        dialog.setContentView(R.layout.dialog_sms);
        dialog.setTitle("Send SMS");
        dialog.setCancelable(false);
        dialog.show();

        Button agree = (Button)dialog.findViewById(R.id.dialog_ok);
        Button disagree = (Button)dialog.findViewById(R.id.dialog_cancel);
        TextView SMS = (TextView)dialog.findViewById(R.id.sms);
        SMS.setText(DataSend);

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
                        CM_SMS_Delievered.showDialog_Vehicle_IN_OUT(Vehicle_In_Activity.this, "SMS delivered ");                     //  Toast.makeText(getBaseContext(), \"SMS delivered\",Toast.LENGTH_SHORT).show();\n");
                        break;
                    case Activity.RESULT_CANCELED:
                        Custom_Dialog CM_SMS_Delievered_Not = new Custom_Dialog();
                        CM_SMS_Delievered_Not.showDialog_Vehicle_IN_OUT(Vehicle_In_Activity.this, "SMS not delivered");
                       /* Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();*/
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

    @ Override
    public void onTaskCompleted(String result, TaskType taskType) {

        if(taskType == TaskType.VEHICLE_IN) {
            String Result_to_Show = null;
            Result_to_Show = Vehicle_In_Out_Json.VehicleIn_Parse(result);
            Custom_Dialog CM = new Custom_Dialog();
            CM.showDialog_Vehicle_IN_OUT(Vehicle_In_Activity.this, Result_to_Show);
        }else{
            Custom_Dialog CM = new Custom_Dialog();
            CM.showDialog_Vehicle_IN_OUT(Vehicle_In_Activity.this, "Something went wrong.Please retry");

        }

    }
}
