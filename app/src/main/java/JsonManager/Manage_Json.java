package JsonManager;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by kuush on 6/6/2016.
 */
public class Manage_Json {
    String in;
    public JSONObject reader ;

    public static String parseInward(String content) {

        try {

            Log.e("We Are Here",content);
            String g_Table = null;
          //  JSONObject sys  = reader.getJSONObject("sys");
          //  country = sys.getString("country");
            JSONObject json= (JSONObject) new JSONTokener(content).nextValue();
            g_Table = (String) json.get("getConfirmParkinStatus_JSONResult");

            return g_Table;


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String parseOutward(String content) {

        try {

            Log.e("We Are Here",content);
            String g_Table = null;
            //  JSONObject sys  = reader.getJSONObject("sys");
            //  country = sys.getString("country");
            JSONObject json= (JSONObject) new JSONTokener(content).nextValue();
            g_Table = (String) json.get("getConfirmParkOutStatus_JSONResult");

            return g_Table;


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
