package Model;

import java.io.Serializable;

/**
 * Created by kuush on 12/24/2016.
 */

public class Notifications implements Serializable {


    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getNotification() {
        return Notification;
    }

    public void setNotification(String notification) {
        Notification = notification;
    }

    public String MobileNumber ;
    public String Notification;

}
