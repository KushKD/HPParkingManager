package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

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
                    Intent mainIntent = new Intent(SplashScreen.this, ParkingDetails.class);
                    SplashScreen.this.startActivity(mainIntent);
                    SplashScreen.this.finish();
                }else{
                    Intent loginIntent = new Intent(SplashScreen.this, MainActivity.class);
                    SplashScreen.this.startActivity(loginIntent);
                    SplashScreen.this.finish();

                }




            }
        }, 3000);
    }
}
