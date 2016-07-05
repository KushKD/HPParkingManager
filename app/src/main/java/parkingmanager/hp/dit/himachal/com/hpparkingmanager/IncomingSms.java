package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by kuush on 7/5/2016.
 */
public class IncomingSms extends BroadcastReceiver {
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);

                   /* int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "senderNum: "+ senderNum + ", message: " + message, duration);
                    toast.show();*/
                    //	CustomDialog.showDialog(context,message);
                    Intent mIntent = new Intent(context,ReceiveMessage.class); //Same as above two lines
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mIntent.putExtra("Message",message);
                    mIntent.putExtra("Sender_Number",senderNum);
                    context.startActivity(mIntent);
                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);
             int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context,"Exception smsReceiver" +e , duration);
            toast.show();

        }
    }

}
