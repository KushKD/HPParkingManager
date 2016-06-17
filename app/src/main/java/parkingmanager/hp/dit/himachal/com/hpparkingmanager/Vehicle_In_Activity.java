package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

                String typecar = s_typecar.getSelectedItem().toString().trim();
                long estimated_Time = s_estimatedtime.getSelectedItemId();
                Log.e("Time",Long.toString(estimated_Time));
                String car_number = carnumber_.getText().toString().trim();
                String phonenumber = phonenumber_.getText().toString().trim();
                String Parking_ID = ID.trim();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());
                String IN_TIME = formattedDate;

                if(phonenumber.length()==10 && phonenumber!=null){
                    if(car_number.length()!=0 && car_number!=null){
                        if(Parking_ID.length()!=0 && Parking_ID!=null){
                            if(AppStatus.getInstance(Vehicle_In_Activity.this).isOnline()) {
                               String URL = EConstants.Production_URL+"getParkingTransaction_JSON";
                                new Generic_Async_Post(Vehicle_In_Activity.this, Vehicle_In_Activity.this, TaskType.VEHICLE_IN).execute("getParkingTransaction_JSON",URL,Parking_ID,typecar,car_number,"",phonenumber,Long.toString(estimated_Time),formattedDate);
                            }
                        }else{
                            Toast.makeText(Vehicle_In_Activity.this, "Something Bad Happened", Toast.LENGTH_SHORT).show();

                        }
                    }else{
                        Toast.makeText(Vehicle_In_Activity.this, "Please enter vehicle number", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Vehicle_In_Activity.this, "Please enter valid 10 digit phone number.", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onTaskCompleted(String result, TaskType taskType) {

        if(taskType == TaskType.VEHICLE_IN) {
            String Result_to_Show = null;
            Result_to_Show = Vehicle_In_Out_Json.VehicleIn_Parse(result);
            Custom_Dialog CM = new Custom_Dialog();
            CM.showDialog_Vehicle_IN_OUT(Vehicle_In_Activity.this, Result_to_Show);
        }else{
            Custom_Dialog CM = new Custom_Dialog();
            CM.showDialog_Vehicle_IN_OUT(Vehicle_In_Activity.this, "Something went wrong.");
        }

    }
}
