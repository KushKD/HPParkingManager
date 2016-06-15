package Presentation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import HTTP.HttpManager;
import Utils.EConstants;


/**
 * Created by kuush on 5/24/2016.
 */
public class TextView_ServerConnected extends TextView {

    private GetAvailability currentTask = null;

    public TextView_ServerConnected(Context context) {
        super(context);
        SetUP_TextView(context);

    }
    public TextView_ServerConnected(Context context, AttributeSet attrs) {
        super(context, attrs);
        SetUP_TextView(context);

    }

    public TextView_ServerConnected(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SetUP_TextView(context);

    }


    public void SetUP_TextView(Context context){
       // Typeface face= Typeface.createFromAsset(context.getAssets(), "GOTHICB.TTF");
       // this.setTypeface(face);
        this.setTextSize(12);
        this.setPadding(13,3,3,3);
        this.setBackgroundColor(Color.parseColor("#FF545454"));
        this.setTextColor(Color.parseColor("#FFFFFF"));
        //this.setText("Testing");

    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

      //  new Timer().schedule(new TimerTask() {
        //    @Override
        //    public void run() {

                // run AsyncTask here.
               // Log.e("ID",EConstants.ParkingID_Task);
                currentTask = new GetAvailability();
                currentTask.execute(EConstants.ParkingID_Task);

       //     }
      //  }, 2000);

    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (currentTask != null) {
            currentTask.cancel(true);
            currentTask = null;
        }
    }

    class GetAvailability extends AsyncTask<String,String,String>{

        private ProgressDialog progressDialog;
        private String Server_Value = null;
        String url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setText("Loading Availability.. Please wait!");
        }

        @Override
        protected String doInBackground(String... params) {
            String value = params[0];


            StringBuilder sb = new StringBuilder();
            sb.append(EConstants.Production_URL);
            sb.append("getParkingAvailblity_JSON");
            sb.append("/");
            sb.append(value);

            url = sb.toString();
            Log.e("URL",url);
            HttpManager HM = new HttpManager();
           String result =  HM.GetData(url);

            sb.delete(0, sb.length());


            return result;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setText(s);

            Object json ;
            String G_Table = null;
            try {

                json = new JSONTokener(s).nextValue();
                if (json instanceof JSONObject){
                    JSONObject obj = new JSONObject(s);
                    G_Table = obj.getString("getParkingAvailblity_JSONResult");
                    Log.e("We are",G_Table);
                    JSONObject  OJ = new JSONObject(G_Table);
                    setText("Availability: " +OJ.optString("Availability"));


                }
            } catch (JSONException e) {
                e.printStackTrace();
                Server_Value = "Error while fetching the latest notification";
            }


        }
    }


}