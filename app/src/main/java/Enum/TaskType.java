package Enum;

/**
 * Created by kuush on 6/17/2016.
 */
public enum TaskType {

    USER_LOGIN_GETOTP(1),
    USER_LOGIN_VALIDATE_OTP_AADHAAR(2),
    VEHICLE_IN(3),
    VEHICLE_CHECK_OUT(4),
    VEHICLE_CHECK_OUT_CONFIRM(5),
    GET_DISTRICT(6),
    GET_SUBDISTRICT(7),
    GET_TOWN(8),
    GET_PHONE_NUMBER(9);

    int value; private TaskType(int value) { this.value = value; }


}
