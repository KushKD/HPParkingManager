package JsonManager;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by kuush on 6/16/2016.
 */
public class Vehicle_In_Out_Json {

    public static String VehicleIn_Parse(String s) {

        String g_Table = null;
        try {
            Object json = new JSONTokener(s).nextValue();
            if (json instanceof JSONObject) {
                JSONObject obj = new JSONObject(s);
                g_Table = obj.optString("getParkingTransaction_JSONResult");
                return g_Table;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String GetPhoneNumber(String s){
        String g_Table = null;
        try {
            Object json = new JSONTokener(s).nextValue();
            if (json instanceof JSONObject) {
                JSONObject obj = new JSONObject(s);
                g_Table = obj.optString("getMobileNo_JSONResult");
               // Log.e("JSON Rwsult", g_Table);
                return g_Table;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String Vehicle_Out_Parse(String s) {

        String g_Table = null;
        try {
            Object json = new JSONTokener(s).nextValue();
            if (json instanceof JSONObject) {
                JSONObject obj = new JSONObject(s);
                g_Table = obj.optString("getParkingOut_JSONResult");
                return g_Table;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String Vehicle_Out_Confirm_Parse(String s) {

        String g_Table = null;
        try {
            Object json = new JSONTokener(s).nextValue();
            if (json instanceof JSONObject) {
                JSONObject obj = new JSONObject(s);
                g_Table = obj.optString("getConfirmPayment_JSONResult");
                return g_Table;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
