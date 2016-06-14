package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import Utils.EConstants;

public class Splash_Screen_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences settings = getSharedPreferences(EConstants.PREF_NAME, 0);
                //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
                boolean hasLoggedIn = settings.getBoolean("hasParkingSelected", false);

                if(hasLoggedIn)
                {
                    Intent mainIntent = new Intent(Splash_Screen_Activity.this, Parking_Details_Activity.class);
                    Splash_Screen_Activity.this.startActivity(mainIntent);
                    Splash_Screen_Activity.this.finish();
                }else{
                    Intent loginIntent = new Intent(Splash_Screen_Activity.this, Main_Activity.class);
                    Splash_Screen_Activity.this.startActivity(loginIntent);
                    Splash_Screen_Activity.this.finish();

                }




            }
        }, 3000);
    }
}
