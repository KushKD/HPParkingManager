package JsonManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import Model.Out_Pojo;

/**
 * Created by kuush on 6/3/2016.
 */
public class Out_Json {
    public static List<Out_Pojo> parseFeed(String content) {

        try {
            String g_Table = null;
            Object json = new JSONTokener(content).nextValue();
            if (json instanceof JSONObject){
                JSONObject obj = new JSONObject(content);
                g_Table = obj.optString("getParkedVehiclelist_JSONResult");
            }
            else if (json instanceof JSONArray){
            }
            JSONArray ar = new JSONArray(g_Table);
            List<Out_Pojo>AdsList = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Out_Pojo pojo_ads = new Out_Pojo();
                pojo_ads.setDriverName(obj.getString("DriverName"));
                pojo_ads.setPhoneNumber(obj.getString("PhoneNumber"));
                pojo_ads.setVehicleNo(obj.getString("VehicleNo"));
                pojo_ads.setParkingId(obj.getString("ParkingId"));
                pojo_ads.setParkInTime(obj.getString("ParkInTime"));
                pojo_ads.setParkOutTime(obj.getString("ParkOutTime"));
                AdsList.add(pojo_ads);
            }
            return AdsList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
