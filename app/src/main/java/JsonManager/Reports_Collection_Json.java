package JsonManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import Model.Collection_Reports_Pojo;
import Model.Outbox_Pojo;

/**
 * Created by kuush on 6/14/2016.
 */
public class Reports_Collection_Json {

    public static List<Collection_Reports_Pojo> parseFeed(String content) {

        try {
            String g_Table = null;
            Object json = new JSONTokener(content).nextValue();
            if (json instanceof JSONObject){
                JSONObject obj = new JSONObject(content);
                g_Table = obj.optString("getDailyReport_JSONResult");   //We need to change this
            }
            else if (json instanceof JSONArray){
            }
            JSONArray ar = new JSONArray(g_Table);
            List<Collection_Reports_Pojo> Report_C = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Collection_Reports_Pojo reports = new Collection_Reports_Pojo();
                reports.setTotalCollection(obj.getString("TotalCollection"));
                reports.setTotalVehicles(obj.getString("TotalVehicles"));
                Report_C.add(reports);
            }
            return Report_C;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
