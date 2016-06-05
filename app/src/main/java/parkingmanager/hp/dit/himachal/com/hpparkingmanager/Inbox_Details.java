package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Inbox_Details extends AppCompatActivity {

    private TextView tv_ParkingId,tv_RegisterId,tv_VehicleNo,tv_PhoneNumber,tv_RequestTime,tv_RequestStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox__details);

        //GTO

        Intent getRoomDetailsIntent = getIntent();
        final InboxPOJO Inbox_Details =  (InboxPOJO) getRoomDetailsIntent.getSerializableExtra("INBOX");

         tv_ParkingId = (TextView)findViewById(R.id.ParkingId);
       tv_RegisterId = (TextView)findViewById(R.id.RegisterId);
        tv_VehicleNo = (TextView)findViewById(R.id.VehicleNo);
       tv_PhoneNumber = (TextView)findViewById(R.id.PhoneNumber);
        tv_RequestTime = (TextView)findViewById(R.id.RequestTime);
       tv_RequestStatus = (TextView)findViewById(R.id.RequestStatus);

                tv_ParkingId.setText(Inbox_Details.getParkingId());
                tv_RegisterId.setText(Inbox_Details.getRegisterId());
                tv_VehicleNo.setText(Inbox_Details.getVehicleNo());
                tv_PhoneNumber.setText(Inbox_Details.getPhoneNumber());
                tv_RequestTime.setText(Inbox_Details.RequestTime);
                tv_RequestStatus.setText(Inbox_Details.RequestStatus);




    }
}
