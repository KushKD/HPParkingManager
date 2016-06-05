package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import java.io.Serializable;

/**
 * Created by kuush on 6/5/2016.
 */
public class InboxPOJO implements Serializable {



            public  String ParkingId;
            public  String RegisterId;
            public  String VehicleNo;
            public  String PhoneNumber;
            public  String RequestTime;
            public  String RequestStatus;

    public  String getRequestStatus() {
        return RequestStatus;
    }

    public  void setRequestStatus(String requestStatus) {
        RequestStatus = requestStatus;
    }



    public  String getParkingId() {
        return ParkingId;
    }

    public  void setParkingId(String parkingId) {
        ParkingId = parkingId;
    }

    public  String getRegisterId() {
        return RegisterId;
    }

    public  void setRegisterId(String registerId) {
        RegisterId = registerId;
    }

    public  String getVehicleNo() {
        return VehicleNo;
    }

    public  void setVehicleNo(String vehicleNo) {
        VehicleNo = vehicleNo;
    }

    public  String getPhoneNumber() {
        return PhoneNumber;
    }

    public  void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public  String getRequestTime() {
        return RequestTime;
    }

    public  void setRequestTime(String requestTime) {
        RequestTime = requestTime;
    }


}
