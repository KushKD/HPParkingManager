package JsonManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import Model.Outbox_Pojo;

/**
 * Created by kuush on 6/6/2016.
 */
public class Outbox_JSON {
    public static List<Outbox_Pojo> parseFeed(String content) {

        try {
            String g_Table = null;
            Object json = new JSONTokener(content).nextValue();
            if (json instanceof JSONObject){
                JSONObject obj = new JSONObject(content);
                g_Table = obj.optString("getAllParkOutReqest_JSONResult");   //We need to change this
            }
            else if (json instanceof JSONArray){
            }
            JSONArray ar = new JSONArray(g_Table);
            List<Outbox_Pojo>OutboxList = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Outbox_Pojo pojo_ads = new Outbox_Pojo();
                pojo_ads.setParkingId(obj.getString("ParkingId"));
                pojo_ads.setPhoneNumber(obj.getString("PhoneNumber"));
                pojo_ads.setRegisterId(obj.getString("RegisterId"));
                pojo_ads.setRequestTime(obj.getString("RequestTime"));
                pojo_ads.setVehicleNo(obj.getString("VehicleNo"));
                pojo_ads.setRequestStatus(obj.getString("RequestStatus"));
                pojo_ads.setEstimatedTime(obj.getString("EstimatedTime"));
                pojo_ads.setVehicleType(obj.getString("VehicleType"));
                OutboxList.add(pojo_ads);
            }
            return OutboxList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
