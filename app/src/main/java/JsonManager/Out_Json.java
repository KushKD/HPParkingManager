package JsonManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import Model.OUT_POJO;

/**
 * Created by kuush on 6/3/2016.
 */
public class Out_Json {
    public static List<OUT_POJO> parseFeed(String content) {

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
            List<OUT_POJO>AdsList = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                OUT_POJO pojo_ads = new OUT_POJO();
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
