package Model;

import java.io.Serializable;

/**
 * Created by kuush on 12/28/2016.
 */

public class AddParkingPOJO implements Serializable {

    public String getParkingName() {
        return ParkingName;
    }

    public void setParkingName(String parkingName) {
        ParkingName = parkingName;
    }

    public String ParkingName;
    public String Latitude;
    public String Longitude;
    public String Contact_Person_Name;
    public String Aadhaar_Number;
    public String Mobile_number;
    public String State;
    public String District;
    public String Sub_District;
    public String Village;
    public String ParkingAddress;
    public String Identifier;
    public String Area;
    public String Capacity;
    public String Threshold_Limit;

    public String getParkingAddress() {
        return ParkingAddress;
    }

    public void setParkingAddress(String parkingAddress) {
        ParkingAddress = parkingAddress;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getContact_Person_Name() {
        return Contact_Person_Name;
    }

    public void setContact_Person_Name(String contact_Person_Name) {
        Contact_Person_Name = contact_Person_Name;
    }

    public String getAadhaar_Number() {
        return Aadhaar_Number;
    }

    public void setAadhaar_Number(String aadhaar_Number) {
        Aadhaar_Number = aadhaar_Number;
    }

    public String getMobile_number() {
        return Mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        Mobile_number = mobile_number;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getSub_District() {
        return Sub_District;
    }

    public void setSub_District(String sub_District) {
        Sub_District = sub_District;
    }

    public String getVillage() {
        return Village;
    }

    public void setVillage(String village) {
        Village = village;
    }

    public String getIdentifier() {
        return Identifier;
    }

    public void setIdentifier(String identifier) {
        Identifier = identifier;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getCapacity() {
        return Capacity;
    }

    public void setCapacity(String capacity) {
        Capacity = capacity;
    }

    public String getThreshold_Limit() {
        return Threshold_Limit;
    }

    public void setThreshold_Limit(String threshold_Limit) {
        Threshold_Limit = threshold_Limit;
    }


}
