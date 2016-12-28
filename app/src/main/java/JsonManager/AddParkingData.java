package JsonManager;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import Model.CencusDistrict;
import Model.CencusTehsil;
import Model.CencusVillage_Town_New;
import Model.Notifications;

/**
 * Created by kuush on 12/28/2016.
 */

public class AddParkingData {

    public static List<CencusDistrict> parseFeedNotifications(String content) {

        try {
            String g_Table = null;
            Log.e("Error:", content );
            Object json = new JSONTokener(content).nextValue();
            if (json instanceof JSONObject){
                JSONObject obj = new JSONObject(content);
                g_Table = obj.optString("getDistrict_JSONResult");   //We need to change this
            }
            else if (json instanceof JSONArray){
            }
            JSONArray ar = new JSONArray(g_Table);
            List<CencusDistrict> notifications = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                CencusDistrict pojo_ads = new CencusDistrict();
                pojo_ads.setId(obj.getString("Id").trim());
                pojo_ads.setName(obj.getString("Name").trim());
                notifications.add(pojo_ads);
            }
            return notifications;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static List<CencusTehsil> getSubdistrict(String content) {

        try {
            String g_Table = null;
            Log.e("Error:", content );
            Object json = new JSONTokener(content).nextValue();
            if (json instanceof JSONObject){
                JSONObject obj = new JSONObject(content);
                g_Table = obj.optString("getSubDistrict_JSONResult");   //We need to change this
            }
            else if (json instanceof JSONArray){
            }
            JSONArray ar = new JSONArray(g_Table);
            List<CencusTehsil> notifications = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                CencusTehsil pojo_ads = new CencusTehsil();
                pojo_ads.setId(obj.getString("Id").trim());
                pojo_ads.setName(obj.getString("Name").trim());
                notifications.add(pojo_ads);
            }
            return notifications;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static List<CencusVillage_Town_New> getVillageTown(String content) {

        try {
            String g_Table = null;
            Log.e("Error:", content );
            Object json = new JSONTokener(content).nextValue();
            if (json instanceof JSONObject){
                JSONObject obj = new JSONObject(content);
                g_Table = obj.optString("getTowns_JSONResult");   //We need to change this
            }
            else if (json instanceof JSONArray){
            }
            JSONArray ar = new JSONArray(g_Table);
            List<CencusVillage_Town_New> notifications = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                CencusVillage_Town_New pojo_ads = new CencusVillage_Town_New();
                pojo_ads.setId(obj.getString("Id").trim());
                pojo_ads.setName(obj.getString("Name").trim());
                notifications.add(pojo_ads);
            }
            return notifications;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
