package Model;

import java.io.Serializable;

/**
 * Created by kuush on 6/3/2016.
 */
public class OUT_POJO implements Serializable{




    public String getParkInTime() {
        return ParkInTime;
    }

    public void setParkInTime(String parkInTime) {
        ParkInTime = parkInTime;
    }

    public String getParkOutTime() {
        return ParkOutTime;
    }

    public void setParkOutTime(String parkOutTime) {
        ParkOutTime = parkOutTime;
    }

    public  String DriverName;
    public String ParkInTime;
    public String ParkOutTime;
    public String PhoneNumber;
    public String VehicleNo;
    public String ParkingId;



    public String getVehicleNo() {
        return VehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        VehicleNo = vehicleNo;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }



    public String getParkingId() {
        return ParkingId;
    }

    public void setParkingId(String parkingId) {
        ParkingId = parkingId;
    }


}
