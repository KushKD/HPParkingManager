package JsonManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.util.ArrayList;
import java.util.List;

import Model.Parking_Pojo;

/**
 * Created by kuush on 6/3/2016.
 */
public class Parking_Json {
    public static List<Parking_Pojo> parseFeed(String content) {

        try {
            String g_Table = null;
            Object json = new JSONTokener(content).nextValue();
            if (json instanceof JSONObject){
                JSONObject obj = new JSONObject(content);
                g_Table = obj.optString("getParking_JSONResult");
            }
            else if (json instanceof JSONArray){
            }
            JSONArray ar = new JSONArray(g_Table);
            List<Parking_Pojo>VacancyList = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Parking_Pojo pojo_Vacancy = new Parking_Pojo();
                pojo_Vacancy.setCapacity(obj.getString("Capacity"));
                pojo_Vacancy.setContactNumber1(obj.getString("ContactNumber1"));
                pojo_Vacancy.setContactNumber2(obj.getString("ContactNumber2"));
                pojo_Vacancy.setContactNumber3(obj.getString("ContactNumber3"));
                pojo_Vacancy.setContactPerson1(obj.getString("ContactPerson1"));
                pojo_Vacancy.setContactPerson2(obj.getString("ContactPerson2"));
                pojo_Vacancy.setContactPerson3(obj.getString("ContactPerson3"));
                pojo_Vacancy.setIdentifier(obj.getString("Identifier"));

                pojo_Vacancy.setImage(obj.getString("Image"));
                pojo_Vacancy.setImage1(obj.getString("Image1"));
                pojo_Vacancy.setImage2(obj.getString("Image2"));
                pojo_Vacancy.setLatitude(obj.getDouble("Latitude"));
                pojo_Vacancy.setLongitude(obj.getDouble("Longitude"));
                pojo_Vacancy.setParkingArea(obj.getString("ParkingArea"));
                pojo_Vacancy.setParkingFullTag(obj.getString("ParkingFullTag").trim());
                pojo_Vacancy.setParkingPlace(obj.getString("ParkingPlace"));
                pojo_Vacancy.setRemarks(obj.getString("Remarks"));
                pojo_Vacancy.setSutedFor(obj.getString("SutedFor"));

                pojo_Vacancy.setThrashholdValue(obj.getString("ThrashholdValue"));
                pojo_Vacancy.setMinimumParkingFeeSmallCar(obj.getString("MinimumParkingFeeSmallCar"));
                pojo_Vacancy.setMinimumParkingFeebigCar(obj.getString("MinimumParkingFeebigCar"));
                pojo_Vacancy.setMinimumParkingTime(obj.getString("MinimumParkingTime"));
                pojo_Vacancy.setParkingID(obj.getString("ParkingId"));



                VacancyList.add(pojo_Vacancy);
            }
            return VacancyList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
