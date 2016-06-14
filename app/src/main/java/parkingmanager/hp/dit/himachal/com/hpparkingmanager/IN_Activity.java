package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import Utils.EConstants;

public class IN_Activity extends Activity {

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
              //  String Driver_Name = drivername_.getText().toString().trim();
                String phonenumber = phonenumber_.getText().toString().trim();
                String Parking_ID = ID.trim();

                Calendar c = Calendar.getInstance();
              //  System.out.println("Current time => "+c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());
                // formattedDate have current date/time
               // Toast.makeText(getApplicationContext(), formattedDate, Toast.LENGTH_SHORT).show();

                String IN_TIME = formattedDate;


               // System.out.println("\t"+typecar + "\t"+ Long.toString(estimated_Time+1));




                if(phonenumber.length()==10 && phonenumber!=null){
                    if(car_number.length()!=0 && car_number!=null){
                        if(Parking_ID.length()!=0 && Parking_ID!=null){
                            if(isOnline()) {
                                PARK_CAR PC = new PARK_CAR();
                                PC.execute(Parking_ID, typecar, car_number, "", phonenumber, Long.toString(estimated_Time), formattedDate);
                            }
                        }else{
                            Toast.makeText(IN_Activity.this, "Something Bad Happened", Toast.LENGTH_SHORT).show();

                        }
                    }else{
                        Toast.makeText(IN_Activity.this, "Please enter vehicle number", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(IN_Activity.this, "Please enter valid 10 digit phone number.", Toast.LENGTH_SHORT).show();
                }







            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IN_Activity.this.finish();
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

    private class PARK_CAR extends AsyncTask<String,String,String>{

        private String Car_Type = null;
        private String Car_Number = null;
        private String Driver_Name = null;
        private String Phone_Number = null;
        private String ES_Parking_Time = null;
        private String Parking_ID = null;
        private String time = null;

        JSONStringer userJson = null;

        private ProgressDialog dialog;
        String url = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(IN_Activity.this);
            this.dialog.setMessage("Please wait ..");
            this.dialog.show();
            this.dialog.setCancelable(false);
        }


        @Override
        protected String doInBackground(String... params) {
            Parking_ID = params[0];
            Car_Type = params[1];
            Car_Number = params[2];
            Driver_Name = params[3];
            Phone_Number = params[4];
            ES_Parking_Time = params[5];
            time = params[6];

            try {
                url_ =new URL(EConstants.Production_URL+"getParkingTransaction_JSON");
                conn_ = (HttpURLConnection)url_.openConnection();
                conn_.setDoOutput(true);
                conn_.setRequestMethod("POST");
                conn_.setUseCaches(false);
                conn_.setConnectTimeout(10000);
                conn_.setReadTimeout(10000);
                conn_.setRequestProperty("Content-Type", "application/json");
                conn_.connect();

                 userJson = new JSONStringer()
                        .object().key("ParkTrans")
                        .object()
                        .key("ParkingId").value(Parking_ID)
                        .key("TypeofCar").value(Car_Type)
                        .key("VehicleNo").value(Car_Number)
                        .key("DriverName").value("")
                        .key("PhoneNumber").value(Phone_Number)
                        .key("EstimatedParkingtime").value(ES_Parking_Time)
                        .key("EstimatedFee").value("")
                        .key("InTime").value(time)
                        .key("OutTime").value("")
                        .key("ActualFee").value("")
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

            //Toast.makeText(getApplicationContext(),s + s.length(),Toast.LENGTH_LONG).show();

            if(s.length()>170 && s.length()<180)
            {
                Toast.makeText(getApplicationContext(),"Data sent to server.",Toast.LENGTH_LONG).show();
                Car_Type = null;
                Car_Number = null;
                Driver_Name = null;
                Phone_Number = null;
                ES_Parking_Time = null;
                Parking_ID = null;
                time = null;
                Log.e("Result",s);
                dialog.dismiss();
                IN_Activity.this.finish();
            }else{
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                Car_Type = null;
                Car_Number = null;
                Driver_Name = null;
                Phone_Number = null;
                ES_Parking_Time = null;
                Parking_ID = null;
                time = null;
                Log.e("Result",s);
                dialog.dismiss();
                IN_Activity.this.finish();
            }








        }
    }
}
