package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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

import HelperFunctions.AppStatus;
import Interfaces.AsyncTaskListener;
import JsonManager.Login_Json;
import Model.Parking_Guy_Pojo;
import Presentation.Custom_Dialog;
import Utils.EConstants;
import Utils.Generic_Async_Get;
import Enum.TaskType;

public class Login_Activity extends Activity implements AsyncTaskListener {

    private int backButtonCount = 0;
    Boolean Flag_Initialize = false;
    private Button button_submit, button_getOTP;
    private EditText editText_aadhaarLogin, editText_otpLogin;
    private LinearLayout l1;
    private TextView tv_header;
    private  String aadhaar , otp = null;
    RelativeLayout Login_Header;


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

                    String url = null;
                    StringBuilder sb = new StringBuilder();
                    sb.append(EConstants.Production_URL);
                    sb.append("getValidateOTP_JSON");
                    sb.append("/");
                    sb.append(aadhaar);
                    sb.append("/");
                    sb.append(otp);
                    url = sb.toString();
                    new Generic_Async_Get(Login_Activity.this, Login_Activity.this, TaskType.USER_LOGIN_VALIDATE_OTP_AADHAAR).execute(url);
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

                    // Login_Async LA  = new Login_Async();
                    // LA.execute(aadhaar);
                    String url = null;
                    StringBuilder sb = new StringBuilder();
                    sb.append(EConstants.Production_URL);
                    sb.append("getOTP_JSON"); //OTP MEthord
                    sb.append("/");
                    sb.append(aadhaar);
                    url = sb.toString();

                    new Generic_Async_Get(Login_Activity.this, Login_Activity.this, TaskType.USER_LOGIN_GETOTP).execute(url);
                }

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

    @Override
    public void onTaskCompleted(String result, TaskType taskType) {

      //  Log.e("Type Task",taskType.toString());
        if(taskType == TaskType.USER_LOGIN_GETOTP) {
            Login_Json JP = new Login_Json();

            String finalResult = JP.ParseString(result);
            Log.e("Final Result", finalResult);


            Custom_Dialog alert = new Custom_Dialog();
            alert.showDialog(Login_Activity.this, finalResult);
        }else if(taskType == TaskType.USER_LOGIN_VALIDATE_OTP_AADHAAR){

            Login_Json JP = new Login_Json();
            String finalResult = JP.ParseStringOTP(result);
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

                        if(myJson.optString("OperatorName").equalsIgnoreCase("OTP did not match. Please try again."))
                        {

                            Custom_Dialog CD= new Custom_Dialog();
                            CD.showDialog(Login_Activity.this,myJson.optString("OperatorName"));
                        }else{
                            PGP.setAlternateMobileNumber(myJson.optString("AlternateMobileNumber"));
                            PGP.setEmail(myJson.optString("Email"));
                            PGP.setMobileNumber(myJson.optString("MobileNumber"));
                            PGP.setOperatorAadhaarNo(myJson.optString("OperatorAadhaarNo"));
                            PGP.setOperatorName(myJson.optString("OperatorName"));
                            PGP.setP_id(myJson.optString("ParkingId"));
                            PGP.setParkingLandmark(myJson.optString("ParkingLandmark"));
                            PGP.setParkingLocation(myJson.optString("ParkingLocation"));

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
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else if (json instanceof JSONArray){

                    Custom_Dialog CD= new Custom_Dialog();
                    CD.showDialog(Login_Activity.this,"Error while getting the data from Server.");
                }else{

                    Custom_Dialog CD= new Custom_Dialog();
                    CD.showDialog(Login_Activity.this,result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            Log.e("Type Task",taskType.toString());
        }

    }


}
