package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuush on 6/5/2016.
 */
public class Inbox_JSON {

    public static List<InboxPOJO> parseFeed(String content) {

        try {
            String g_Table = null;
            Object json = new JSONTokener(content).nextValue();
            if (json instanceof JSONObject){
                JSONObject obj = new JSONObject(content);
                g_Table = obj.optString("getAllParkReqest_JSONResult");   //We need to change this
            }
            else if (json instanceof JSONArray){
            }
            JSONArray ar = new JSONArray(g_Table);
            List<InboxPOJO>InboxList = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                InboxPOJO pojo_ads = new InboxPOJO();
                pojo_ads.setParkingId(obj.getString("ParkingId"));
                pojo_ads.setPhoneNumber(obj.getString("PhoneNumber"));
                pojo_ads.setRegisterId(obj.getString("RegisterId"));
                pojo_ads.setRequestTime(obj.getString("RequestTime"));
                pojo_ads.setVehicleNo(obj.getString("VehicleNo"));
                pojo_ads.setRequestStatus(obj.getString("RequestStatus"));
                pojo_ads.setEstimatedTime(obj.getString("EstimatedTime"));
                pojo_ads.setVehicleType(obj.getString("VehicleType"));
                InboxList.add(pojo_ads);
            }
            return InboxList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
