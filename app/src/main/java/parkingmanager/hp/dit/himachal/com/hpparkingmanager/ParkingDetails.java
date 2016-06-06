package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ParkingDetails extends Activity {

    TextView parkingname,parkingidentifier;
    Button back,in,out,inbox,outbox;

    String ParkingID=null,Parking_Name=null,Capacity=null,thrashhold=null,Identifier=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_details);
        SharedPreferences prfs = getSharedPreferences(EConstants.PREF_NAME, Context.MODE_PRIVATE);

         ParkingID  = prfs.getString("ParkingID","");
         Parking_Name = prfs.getString("Parking_Name","");
         Capacity = prfs.getString("Capacity","");
         thrashhold = prfs.getString("thrashhold","");
         Identifier = prfs.getString("Identifier","");

        parkingname = (TextView)findViewById(R.id.parkingname);
        parkingidentifier = (TextView)findViewById(R.id.parkingidentifier);

        back = (Button)findViewById(R.id.back);
        in = (Button)findViewById(R.id.in);
        out = (Button)findViewById(R.id.out);
        inbox = (Button)findViewById(R.id.inbox);
        outbox = (Button)findViewById(R.id.outbox);

        //Intent getParkingDetailsIntent = getIntent();
        //final ParkingPOJO ParkingDetails =  (ParkingPOJO) getParkingDetailsIntent.getSerializableExtra("DETAILS");
        parkingname.setText(Parking_Name);
        parkingidentifier.setText(Identifier);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParkingDetails.this.finish();
            }
        });



        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get Parking ID
                Intent i = new Intent(ParkingDetails.this, Inbox.class);
                i.putExtra("ID",ParkingID);
                startActivity(i);

                //Go to Request Inward
            }
        });

        outbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ParkingDetails.this, Outbox.class);
                i.putExtra("ID",ParkingID);
                startActivity(i);

            }
        });




        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ParkingDetails.this, IN_Activity.class);
                i.putExtra("ID",ParkingID);
                startActivity(i);


            }
        });

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ParkingDetails.this, OUT_Activity.class);
                i.putExtra("ID",ParkingID);
                startActivity(i);

            }
        });


    }
}
