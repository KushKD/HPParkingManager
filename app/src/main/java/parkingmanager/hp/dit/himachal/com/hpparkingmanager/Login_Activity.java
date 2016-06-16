package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Text;

import java.net.HttpURLConnection;
import java.net.URL;

import HTTP.HttpManager;
import HelperFunctions.AppStatus;
import JsonManager.Login_Json;
import Model.Parking_Guy_Pojo;
import Presentation.Custom_Dialog;
import Utils.EConstants;

public class Login_Activity extends Activity {

    private int backButtonCount = 0;
    Boolean Flag_Initialize = false;
    private Button button_submit, button_getOTP;
    private EditText editText_aadhaarLogin, editText_otpLogin;
    private LinearLayout l1;
    private TextView tv_header;
    private  String aadhaar , otp = null;
    RelativeLayout Login_Header;

    URL url;
    HttpURLConnection conn;
    StringBuilder sb = new StringBuilder();
    String HeaderText;
    String HeaderColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);



        Flag_Initialize = InitializeControls();
        if (Flag_Initialize){



            button_getOTP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAadhaar();
                }
            });

            button_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getOtpandAadhaa();
                }
            });

        }else{
            Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {

        EditText etAadhaar = (EditText)findViewById(R.id.et_aadhaar);
        String Save_Aadhaar = etAadhaar.getText().toString().trim();
        savedInstanceState.putString("AADHAAR", Save_Aadhaar);
        //savedInstanceState.putString("OTP", otp);

        super.onSaveInstanceState(savedInstanceState);


    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        aadhaar = savedInstanceState.getString("AADHAAR");
        editText_aadhaarLogin.setText(aadhaar);
        // otp = savedInstanceState.getString("OTP");
        // ... recover more data
    }

    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1) {
            Login_Activity.this.finish();
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application" , Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }



    private void getOtpandAadhaa() {

        otp = editText_otpLogin.getText().toString().trim();
        String aadhaar_a = editText_aadhaarLogin.getText().toString().trim();
        if(!otp.isEmpty()){
            if(otp.length()== 6){
                if(AppStatus.getInstance(this).isOnline()) {
                    OTP_Async OA = new OTP_Async();
                    OA.execute(aadhaar_a, otp);
                } else {
                    Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getApplicationContext(),"Please Ensure that the OTP is Valid",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Please enter the OTP that has been sent to your Mobile number",Toast.LENGTH_LONG).show();
        }

    }
    private void getAadhaar() {



        aadhaar = editText_aadhaarLogin.getText().toString().trim();
        if(!aadhaar.isEmpty() ){
            if(aadhaar.length() == 12 ){

                if(AppStatus.getInstance(this).isOnline()){
                    Login_Async LA  = new Login_Async();
                    LA.execute(aadhaar);}
                else {
                    Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
                }



            }else{
                Toast.makeText(getApplicationContext(), "Please Enter a valid Aadhaar No.",Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(getApplicationContext(),"Please enter your Aadhaar No.",Toast.LENGTH_LONG).show();
        }
    }

    private Boolean InitializeControls() {
        try{
            button_submit = (Button)findViewById(R.id.bt_login);
            button_getOTP = (Button)findViewById(R.id.bt_getotp);
            editText_aadhaarLogin = (EditText)findViewById(R.id.et_aadhaar);
            editText_otpLogin = (EditText)findViewById(R.id.et_otp);
            l1 = (LinearLayout)findViewById(R.id.otp);
            Login_Header = (RelativeLayout)findViewById(R.id.header_login);
            tv_header = (TextView)findViewById(R.id.tvheader);


            return true;
        }catch (Exception e){
            System.out.println("Something Went Wrong" + e.getLocalizedMessage());
            return false;
        }
    }

    class Login_Async extends AsyncTask<String,String,String>{

        String url = null;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Login_Activity.this);
            dialog.setMessage("Connecting to Server .. Please Wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            String aadhaar = params[0];
            StringBuilder sb = new StringBuilder();
            sb.append(EConstants.Production_URL);
            sb.append("getOTP_JSON"); //OTP MEthord
            sb.append("/");
            sb.append(aadhaar);
            url = sb.toString();

            HttpManager jParser = new HttpManager();
            String result  = jParser.GetData(url);

            return result;


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Login_Json JP = new Login_Json();

            String finalResult = JP.ParseString(s);
            Log.e("Final Result",finalResult);

            this.dialog.dismiss();

            Custom_Dialog alert = new Custom_Dialog();
            alert.showDialog(Login_Activity.this, finalResult);

/*
            if(finalResult.equalsIgnoreCase("OTP has been sent on registered mobile number")){
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), finalResult, Toast.LENGTH_SHORT).show();
                editText_aadhaarLogin.setEnabled(false);
                editText_otpLogin.setEnabled(true);
            }
            else{
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), finalResult, Toast.LENGTH_SHORT).show();
            }
*/
        }
    }

    class OTP_Async extends AsyncTask<String,String,String>{

        private ProgressDialog dialog;
        String url2 = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Login_Activity.this);
            dialog.setMessage("Connecting to Server .. Please Wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String aadhaar = params[0];
            String otp = params[1];
            StringBuilder sb = new StringBuilder();
            sb.append(EConstants.Production_URL);
            sb.append("getValidateOTP_JSON");    //Methord Name
            sb.append("/");
            sb.append(aadhaar);
            sb.append("/");
            sb.append(otp);
            url2 = sb.toString();
            HttpManager jParser = new HttpManager();
            String result  = jParser.GetData(url2);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Login_Json JP = new Login_Json();

            String finalResult = JP.ParseStringOTP(s);

            Object json = null;
            try {
                json = new JSONTokener(finalResult).nextValue();
                if (json instanceof JSONObject){
                    //Check Weather the String is object or array
                    JSONObject myJson = null;
                    Parking_Guy_Pojo PGP = null;
                    try {
                        PGP = new Parking_Guy_Pojo();
                        myJson = new JSONObject(finalResult);
                        PGP.setAlternateMobileNumber(myJson.optString("AlternateMobileNumber"));
                        PGP.setEmail(myJson.optString("Email"));
                        PGP.setMobileNumber(myJson.optString("MobileNumber"));
                        PGP.setOperatorAadhaarNo(myJson.optString("OperatorAadhaarNo"));
                        PGP.setOperatorName(myJson.optString("OperatorName"));
                        PGP.setP_id(myJson.optString("ParkingId"));
                        PGP.setParkingLandmark(myJson.optString("ParkingLandmark"));
                        PGP.setParkingLocation(myJson.optString("ParkingLocation"));

                     /*   SharedPreferences settings = getSharedPreferences(EConstants.PREF_NAME, 0); // 0 - for private mode
                        SharedPreferences.Editor editor = settings.edit();
                        //Set "hasLoggedIn" to true
                        editor.putBoolean("hasLoggedIn", true);
                        // Commit the edits!
                        editor.commit();*/

                        try{
                            SharedPreferences settings = getSharedPreferences(EConstants.PREF_NAME, 0); // 0 - for private mode
                            SharedPreferences.Editor editor = settings.edit();
                            //Set "hasLoggedIn" to true
                            editor.putBoolean("hasLoggedIn", true);
                            editor.putString("ParkingID",PGP.getP_id());
                            editor.putString("AlternateMobileNumber",PGP.getAlternateMobileNumber());
                            editor.putString("Email",PGP.getEmail());
                            editor.putString("MobileNumber",PGP.getMobileNumber());
                            editor.putString("OperatorAadhaarNo",PGP.getOperatorAadhaarNo());
                            editor.putString("OperatorName",PGP.getOperatorName());
                            editor.putString("ParkingLandmark",PGP.getParkingLandmark());
                            editor.putString("ParkingLocation",PGP.getParkingLocation());
                            // Commit the edits!
                            editor.commit();
                            Intent Details_Intent = new Intent(Login_Activity.this,Navigation_Drawer_Main_Activity.class);
                            startActivity(Details_Intent);
                            Login_Activity.this.finish();


                        }catch (Exception e){
                            Log.e("ERROR",e.getLocalizedMessage().toString());
                        }

                        this.dialog.dismiss();
                        //Write it to Shared Prefrences and then exit the Screen.
                      //  Custom_Dialog CD= new Custom_Dialog();
                      //  CD.showDialog(Login_Activity.this,"Login Successful");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else if (json instanceof JSONArray){
                    this.dialog.dismiss();
                    Custom_Dialog CD= new Custom_Dialog();
                    CD.showDialog(Login_Activity.this,"Error while getting the data from Server.");
                }else{
                    this.dialog.dismiss();
                    Custom_Dialog CD= new Custom_Dialog();
                    CD.showDialog(Login_Activity.this,s);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }










            /*

            if(finalResult.equalsIgnoreCase("Successful")){
                SharedPreferences settings = getSharedPreferences(EConstants.PREF_NAME, 0); // 0 - for private mode
                SharedPreferences.Editor editor = settings.edit();
                //Set "hasLoggedIn" to true
                editor.putBoolean("hasLoggedIn", true);
                // Commit the edits!
                editor.commit();
                dialog.dismiss();
                Intent i = new Intent(Login_Activity.this,Main_Activity.class);
                startActivity(i);
                Login_Activity.this.finish();


            } else{
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), finalResult, Toast.LENGTH_SHORT).show();
            }*/


        }
    }

}
