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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OUT_DETAILS extends AppCompatActivity {

    //PArking ID
    //Driver Name
    //Phone Number
    //VehicleNumber
    //Out Time

    TextView tv_parkingid,tv_drivername,tv_phonenumber,tv_vehiclenumber,tv_outtime;
    Button checkout;

    URL url_;
    HttpURLConnection conn_;
    StringBuilder sb = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out__details);

        Intent getRoomDetailsIntent = getIntent();
        final OUT_POJO OUT_Details =  (OUT_POJO) getRoomDetailsIntent.getSerializableExtra("ADS_Details");


                tv_parkingid = (TextView)findViewById(R.id.parkingid);
                tv_drivername = (TextView)findViewById(R.id.drivername);
                tv_phonenumber = (TextView)findViewById(R.id.phonenumber);
                tv_vehiclenumber = (TextView)findViewById(R.id.vehiclenumber);
                tv_outtime = (TextView)findViewById(R.id.outtime);
                checkout = (Button)findViewById(R.id.checkout);



        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
        Toast.makeText(getApplicationContext(), formattedDate, Toast.LENGTH_SHORT).show();

        String OUT_TIME = formattedDate;

        tv_parkingid.setText(OUT_Details.getParkingId());
        tv_drivername.setText(OUT_Details.getDriverName());
        tv_phonenumber.setText(OUT_Details.getPhoneNumber());
        tv_vehiclenumber.setText(OUT_Details.getVehicleNo());
        tv_outtime.setText(OUT_TIME);




        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send data to server
                if(isOnline()){

                    String parking_id = OUT_Details.getParkingId();
                    String drivername = OUT_Details.getDriverName();
                    String phone_number = OUT_Details.getPhoneNumber();
                    String vehicle_number = OUT_Details.getVehicleNo();
                    String out_time = tv_outtime.getText().toString().trim();

                    CHECK_OUT_CAR COC = new CHECK_OUT_CAR();
                    COC.execute(parking_id,drivername,phone_number,vehicle_number,out_time);

                }else{
                    Toast.makeText(OUT_DETAILS.this, "Connect to Internet", Toast.LENGTH_SHORT).show();
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


    private class CHECK_OUT_CAR extends AsyncTask<String,String,String> {

        private String Parking_Id = null;
        private String Driver_Name = null;
        private String Phone_number = null;
        private String Vehicle_NO = null;
        private String OUT_Time = null;

        private ProgressDialog dialog;
        String url = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(OUT_DETAILS.this);
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


            try {
                url_ =new URL("http://192.168.0.171/HPParking/HPParking.svc/getParkingOut_JSON");
                conn_ = (HttpURLConnection)url_.openConnection();
                conn_.setDoOutput(true);
                conn_.setRequestMethod("POST");
                conn_.setUseCaches(false);
                conn_.setConnectTimeout(10000);
                conn_.setReadTimeout(10000);
                conn_.setRequestProperty("Content-Type", "application/json");
                conn_.connect();

                JSONStringer userJson = new JSONStringer()
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

            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

            Parking_Id = null;
            Driver_Name = null;
            Vehicle_NO = null;
            Phone_number = null;
            OUT_Time = null;

            Log.e("Result",s);

            dialog.dismiss();
            OUT_DETAILS.this.finish();
          /*  JsonParser JP;
            String finalResult = null;

            if(s.equalsIgnoreCase("Server Connection failed.")){
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Server Connection failed.", Toast.LENGTH_SHORT).show();
            }else{
                JP = new JsonParser();
                finalResult = JP.POST_ISSUE(s);
                if(finalResult.length()>50){
                    // clearData();
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), finalResult, Toast.LENGTH_SHORT).show();
                    Issues_Feedback.this.finish();
                }
                else{
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), finalResult, Toast.LENGTH_SHORT).show();

                }

            }*/




        }
    }
}
