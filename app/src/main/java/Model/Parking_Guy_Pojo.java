package Model;

import java.io.Serializable;

/**
 * Created by kuush on 6/16/2016.
 */
public class Parking_Guy_Pojo implements Serializable {

    public String OperatorName;
    public String OperatorAadhaarNo;
    public String MobileNumber;
    public String AlternateMobileNumber;
    public String Email;
    public String P_id;
    public String ParkingLocation;
    public String ParkingLandmark ;

    public String getOperatorName() {
        return OperatorName;
    }

    public void setOperatorName(String operatorName) {
        OperatorName = operatorName;
    }

    public String getOperatorAadhaarNo() {
        return OperatorAadhaarNo;
    }

    public void setOperatorAadhaarNo(String operatorAadhaarNo) {
        OperatorAadhaarNo = operatorAadhaarNo;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getAlternateMobileNumber() {
        return AlternateMobileNumber;
    }

    public void setAlternateMobileNumber(String alternateMobileNumber) {
        AlternateMobileNumber = alternateMobileNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getP_id() {
        return P_id;
    }

    public void setP_id(String p_id) {
        P_id = p_id;
    }

    public String getParkingLocation() {
        return ParkingLocation;
    }

    public void setParkingLocation(String parkingLocation) {
        ParkingLocation = parkingLocation;
    }

    public String getParkingLandmark() {
        return ParkingLandmark;
    }

    public void setParkingLandmark(String parkingLandmark) {
        ParkingLandmark = parkingLandmark;
    }


}
