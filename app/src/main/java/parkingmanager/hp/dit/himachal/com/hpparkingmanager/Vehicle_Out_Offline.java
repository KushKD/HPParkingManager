package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Presentation.Custom_Dialog;

/**
 * Created by kuush on 7/30/2016.
 */
public class Vehicle_Out_Offline extends Activity{

    String ID = null;
    StringBuilder SB = null;
    String CarNumber = null;
    String DataSend_Out = null;
    Button bt_agree;
    Button disagree;
    EditText et_carnumber;
    TextView SMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sms_out);

        Bundle bundle = getIntent().getExtras();

        ID = bundle.getString("ID");
        Log.e("We are Here", ID);


        et_carnumber = (EditText)findViewById(R.id.car_);
         bt_agree = (Button)findViewById(R.id.agree);
         disagree = (Button)findViewById(R.id.dialog_cancel);
         SMS = (TextView)findViewById(R.id.sms);






        bt_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Car Number", "We Are Here");
                CarNumber = et_carnumber.getText().toString();
                System.out.println("Car Number is: "+ CarNumber);
                //Send SMS
                SB = new StringBuilder();
                SB.append("HP PARK OUT");
                SB.append(" ");
                SB.append(ID);
                SB.append(" ");
                SB.append(CarNumber);
                DataSend_Out = SB.toString().trim();
                System.out.println("SMS  "+ DataSend_Out);
                Log.e("SMS",DataSend_Out);

                 sendSMS("9223166166",DataSend_Out);
            }
        });



        disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vehicle_Out_Offline.this.finish();
            }
        });
    }


    private void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        //  Toast.makeText(getBaseContext(), "SMS delivered",Toast.LENGTH_SHORT).show();
                        Custom_Dialog CM_SMS_Delievered = new Custom_Dialog();
                        CM_SMS_Delievered.showDialog_Vehicle_IN_OUT(Vehicle_Out_Offline.this, "SMS delivered ");                     //  Toast.makeText(getBaseContext(), \"SMS delivered\",Toast.LENGTH_SHORT).show();\n");
                        break;
                    case Activity.RESULT_CANCELED:
                        Custom_Dialog CM_SMS_Delievered_Not = new Custom_Dialog();
                        CM_SMS_Delievered_Not.showDialog_Vehicle_IN_OUT(Vehicle_Out_Offline.this, "SMS not delivered");
                       /* Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();*/
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

}
