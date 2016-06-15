package Presentation;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.util.Timer;
import java.util.TimerTask;
import HTTP.HttpManager;
import HelperFunctions.AppStatus;
import Utils.EConstants;


/**
 * Created by kuush on 5/24/2016.
 */
public class Availablility_TextView extends TextView {

    private GetAvailability currentTask = null;

    public Availablility_TextView(Context context) {
        super(context);
        SetUP_TextView(context);

    }
    public Availablility_TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        SetUP_TextView(context);

    }

    public Availablility_TextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SetUP_TextView(context);

    }


    public void SetUP_TextView(Context context){
       // Typeface face= Typeface.createFromAsset(context.getAssets(), "GOTHICB.TTF");
       // this.setTypeface(face);
        this.setTextSize(16);
        this.setPadding(13,3,3,3);
        this.setBackgroundColor(Color.parseColor("#FFFFFF"));
        this.setTextColor(Color.parseColor("#000000"));
        //this.setText("Testing");

    }




    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        callAsynchronousTask();

    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (currentTask != null) {
            currentTask.cancel(true);
            currentTask = null;
        }

    }


    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {

                                currentTask = new GetAvailability();
                                currentTask.execute(EConstants.ParkingID_Task);

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 3000); //execute in every 50000 ms
    }



    class GetAvailability extends AsyncTask<String,String,String>{

        private ProgressDialog progressDialog;
        private String Server_Value = null;
        String url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setText("Availability: " );
        }

        @Override
        protected String doInBackground(String... params) {
            String value = params[0];

try {
    StringBuilder sb = new StringBuilder();
    sb.append(EConstants.Production_URL);
    sb.append("getParkingAvailblity_JSON");
    sb.append("/");
    sb.append(value);

    url = sb.toString();
    //Log.e("URL", url);

    try {
        HttpManager HM = new HttpManager();
        Server_Value = HM.GetData(url);
         Log.e("Server_Value",Server_Value);
        sb.delete(0, sb.length());


        return Server_Value;
    }catch (Exception e){
        return Server_Value="Error while connecting to server.";
    }
}catch(Exception e){
    return Server_Value="Error while connecting to server.";
}

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // setText(s);



                Object json;
                String G_Table = null;
                try {

                    json = new JSONTokener(s).nextValue();
                    if (json instanceof JSONObject) {
                        JSONObject obj = new JSONObject(s);
                        G_Table = obj.getString("getParkingAvailblity_JSONResult");
                        Log.e("We are", G_Table);
                        JSONObject OJ = new JSONObject(G_Table);
                        setText("Availability: " + OJ.optString("Availability"));
                       // Log.e("Data", OJ.optString("Availability"));


                    } else {
                        setText("Availability: " + "N/A");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Server_Value = "";
                    setText(s);
                }


            }

    }


}