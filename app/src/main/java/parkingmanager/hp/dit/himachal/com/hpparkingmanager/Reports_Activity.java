package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import HelperFunctions.Date_Time;
import Utils.EConstants;

public class Reports_Activity extends Activity implements View.OnClickListener {


    Button bt_back , bt_getreport;
    TextView tv_todate , tv_totime ,  tv_fromdate , tv_fromtime;

    static final int TIME_DIALOG_ID = 1111;
    static final int TIME_DIALOG_ID_TWO = 2222;


    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String GetFromDate = null;
    private String GetToDate = null;
    private int hour;
    private int minute;
    private String ID_Parking = null;

    URL url_;
    HttpURLConnection conn_;
    StringBuilder sb = new StringBuilder();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_);

        Bundle bundle = getIntent().getExtras();

        ID_Parking = bundle.getString("ID");
        Log.e("id",ID_Parking);

        bt_back = (Button)findViewById(R.id.back);
        bt_getreport = (Button)findViewById(R.id.getreport);
        tv_todate = (TextView) findViewById(R.id.todate);

        tv_totime = (TextView)findViewById(R.id.totime);
        tv_fromtime = (TextView)findViewById(R.id.fromtime);
        SimpleDateFormat todateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tv_todate.setText(todateFormat.format(new Date()));


        tv_fromdate = (TextView)findViewById(R.id.fromdate);
        SimpleDateFormat fromdateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tv_fromdate.setText(fromdateFormat.format(new Date()));

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        setDateTimeField();

        tv_fromtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(TIME_DIALOG_ID);

            }
        });

        tv_totime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(TIME_DIALOG_ID_TWO);

            }
        });



        final Calendar c = Calendar.getInstance();
        // Current Hour
        hour = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = c.get(Calendar.MINUTE);

        // set current time into output textview
        updateTime(hour, minute);
        updateTime_Two(hour, minute);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reports_Activity.this.finish();
            }
        });


        bt_getreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * Get Data
                 */

                String from_Date = null;
                String to_Date = null;
                String from_time = null;
                String to_time = null;
                String Current_from_date_time = null;
                String Current_to_date_time =null;
                String Changed_from_date_time = null;
                String Changed_to_date_time =null;

                from_Date = tv_fromdate.getText().toString();
                to_Date = tv_todate.getText().toString();
                from_time = tv_fromtime.getText().toString();
                to_time = tv_totime.getText().toString();
                 Current_from_date_time =  from_Date + " "+from_time;
                Current_to_date_time = to_Date + " "+to_time;

                try {

                   // Log.e(Current_from_date_time,Changed_from_date_time);
                    Changed_to_date_time = Date_Time.Change_Date_Format(Current_to_date_time);
                    Changed_from_date_time = Date_Time.Change_Date_Format(Current_from_date_time);
                   // Log.e("ParkingID",ID_Parking);

                    //Strat Async Thread
                    if(isOnline()){

                        GET_REPORT  GR = new GET_REPORT();
                        GR.execute(ID_Parking,Changed_from_date_time,Changed_to_date_time);
                    }else{
                        Toast.makeText(Reports_Activity.this, "Network not available.", Toast.LENGTH_SHORT).show();
                    }

                   // Log.e(Current_to_date_time,Changed_to_date_time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:

                // set time picker as current time
                return new TimePickerDialog(this, timePickerListener, hour, minute, false);

            case TIME_DIALOG_ID_TWO:
                // set time picker as current time
                return new TimePickerDialog(this, timePickerListener_two, hour, minute, false);

        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour   = hourOfDay;
            minute = minutes;

            updateTime(hour,minute);

        }

    };


    private TimePickerDialog.OnTimeSetListener timePickerListener_two = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            hour   = hourOfDay;
            minute = minute;

            updateTime_Two(hour,minute);
        }
    };

    private static String utilTime(int value) {

        if (value < 10)
            return "0" + String.valueOf(value);
        else
            return String.valueOf(value);
    }

    private void setDateTimeField() {
        tv_fromdate.setOnClickListener(this);
        tv_todate.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tv_fromdate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tv_todate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View v) {
        if(v == tv_fromdate) {
            fromDatePickerDialog.show();
        }else if(v == tv_todate) {
            toDatePickerDialog.show();
        }
    }

    // Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();


        tv_fromtime.setText(aTime);

    }

    private void updateTime_Two(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        tv_totime.setText(aTime);


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



    private class GET_REPORT extends AsyncTask<String,String,String>{

        String P_ID_Server = null;
        String FromDate_Server = null;
        String ToDate_Server = null;
        JSONStringer userJson = null;

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Reports_Activity.this);
            this.dialog.setMessage("Please wait ..");
            this.dialog.show();
            this.dialog.setCancelable(false);
            this.dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            P_ID_Server = params[0];
            FromDate_Server = params[1];
            ToDate_Server = params[2];


            try {
                url_ =new URL(EConstants.Production_URL+"getDailyReport_JSON");
                conn_ = (HttpURLConnection)url_.openConnection();
                conn_.setDoOutput(true);
                conn_.setRequestMethod("POST");
                conn_.setUseCaches(false);
                conn_.setConnectTimeout(10000);
                conn_.setReadTimeout(10000);
                conn_.setRequestProperty("Content-Type", "application/json");
                conn_.connect();

                userJson = new JSONStringer()
                        .object().key("fDate")
                        .object()
                        .key("ParkingId").value(P_ID_Server)
                        .key("FromDate").value(FromDate_Server)
                        .key("LastDate").value(ToDate_Server)
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
            Log.e("Value From Server",s);
            this.dialog.dismiss();
        }
    }
}
