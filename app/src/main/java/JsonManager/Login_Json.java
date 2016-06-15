package JsonManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by kuush on 6/15/2016.
 */
public class Login_Json {

    public String ParseString(String s) {

        String g_Table = null;
        try {
            Object json = new JSONTokener(s).nextValue();
            if (json instanceof JSONObject) {
                JSONObject obj = new JSONObject(s);
                g_Table = obj.optString("LoginResult");
                return g_Table;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }



    public String POST(String s) {

        String g_Table = null;
        try {
            Object json = new JSONTokener(s).nextValue();
            if (json instanceof JSONObject) {
                JSONObject obj = new JSONObject(s);
                g_Table = obj.optString("Save_AWW_DetailsResult");
                return g_Table;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }



    public String ParseStringOTP(String s) {

        String g_Table = null;
        try {
            Object json = new JSONTokener(s).nextValue();
            if (json instanceof JSONObject) {
                JSONObject obj = new JSONObject(s);
                g_Table = obj.optString("CheckOTPResult");
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
