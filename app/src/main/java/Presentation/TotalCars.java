package Presentation;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import HTTP.HttpManager;
import JsonManager.Out_Json;
import Model.OUT_POJO;
import Utils.EConstants;

/**
 * Created by kuush on 10/1/2016.
 */
public class TotalCars extends TextView {


    private GetAvailability currentTask = null;
    List<OUT_POJO> ads_Server;

    public TotalCars(Context context) {
        super(context);
        SetUP_TextView(context);

    }

    public TotalCars(Context context, AttributeSet attrs) {
        super(context, attrs);
        SetUP_TextView(context);

    }

    public TotalCars(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SetUP_TextView(context);

    }


    public void SetUP_TextView(Context context) {
        // Typeface face= Typeface.createFromAsset(context.getAssets(), "GOTHICB.TTF");
        // this.setTypeface(face);
        this.setTextSize(16);
        this.setPadding(13, 3, 3, 3);
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
        timer.schedule(doAsynchronousTask, 0, 6000); //execute in every 50000 ms
    }


    class GetAvailability extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String Server_Value = null;
        String url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setText("Total Cars In Parking: " + "N/A");
        }

        @Override
        protected String doInBackground(String... params) {
            String value = params[0];

            try {
                StringBuilder sb = new StringBuilder();
                sb.append(EConstants.Production_URL);
                sb.append("getParkedVehiclelist_JSON");
                sb.append("/");
                sb.append(value);

                url = sb.toString();
                //Log.e("URL", url);

                try {
                    HttpManager HM = new HttpManager();
                    Server_Value = HM.GetData(url);
                    // Log.e("Server_Value",Server_Value);
                    sb.delete(0, sb.length());

                    Log.e("Server Value",Server_Value);
                    return Server_Value;
                } catch (Exception e) {
                    return Server_Value = "Error while connecting to server.";
                }
            } catch (Exception e) {
                return Server_Value = "Error while connecting to server.";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // setText(s);
try {
    ads_Server = Out_Json.parseFeed(s);

    if (ads_Server != null) {
        setText("Total Cars In Parking: " + Integer.toString(ads_Server.size()));
    } else {
        setText("Total Cars In Parking: " + "N/A");
    }
}catch(Exception e){
    setText("Total Cars In Parking: " + "N/A");
}

          /*  Object json = null;
            String G_Table = null;
            try {

                try {
                    json = new JSONTokener(s).nextValue();
                } catch (Exception e) {
                    setText("Toatal Cars In Parking: " + "N/A" );
                }
                if (json instanceof JSONObject) {
                    JSONObject obj = new JSONObject(s);
                    G_Table = obj.getString("getParkingAvailblity_JSONResult");
                    //Log.e("We are", G_Table);
                    JSONObject OJ = new JSONObject(G_Table);
                    setText("Toatal Cars In Parking: " + OJ.optString("Availability"));
                    // Log.e("Data", OJ.optString("Availability"));


                } else {
                    setText("Toatal Cars In Parking: " + "N/A" );
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Server_Value = "";
                setText("Availability: " + "N/A" );
            }

*/
        }

    }
}


