package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import Utils.EConstants;

public class Navigation_Drawer_Main_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView parkingname,parkingidentifier;
    Button back,in,out,inbox,outbox;

    String ParkingID=null,Parking_Name=null,Capacity=null,thrashhold=null,Identifier=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation__drawer__main_);



        SharedPreferences prfs = getSharedPreferences(EConstants.PREF_NAME, Context.MODE_PRIVATE);

        ParkingID  = prfs.getString("ParkingID","");
        Parking_Name = prfs.getString("Parking_Name","");
        Capacity = prfs.getString("Capacity","");
        thrashhold = prfs.getString("thrashhold","");
        Identifier = prfs.getString("Identifier","");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);
/*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
      TextView  Parking_Name_tv = (TextView)header.findViewById(R.id.parkingname);
       TextView Parking_Landmark_tv = (TextView)header.findViewById(R.id.landmark);
        Parking_Name_tv.setText(Parking_Name);
        Parking_Landmark_tv.setText(Identifier);


        back = (Button)findViewById(R.id.back);
        in = (Button)findViewById(R.id.in);
        out = (Button)findViewById(R.id.out);
        inbox = (Button)findViewById(R.id.inbox);
        outbox = (Button)findViewById(R.id.outbox);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation_Drawer_Main_Activity.this.finish();
            }
        });



        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get Parking ID
                Intent i = new Intent(Navigation_Drawer_Main_Activity.this, Inbox_Activity.class);
                i.putExtra("ID",ParkingID);
                startActivity(i);

                //Go to Request Inward
            }
        });

        outbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Navigation_Drawer_Main_Activity.this, Outbox_Activity.class);
                i.putExtra("ID",ParkingID);
                startActivity(i);

            }
        });




        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Navigation_Drawer_Main_Activity.this, IN_Activity.class);
                i.putExtra("ID",ParkingID);
                startActivity(i);


            }
        });

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Navigation_Drawer_Main_Activity.this, OUT_Activity.class);
                i.putExtra("ID",ParkingID);
                startActivity(i);

            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation__drawer__main_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Log.e("This",Integer.toString(id));
        if (id == R.id.nav_collection_report) {

            Intent i  = new Intent(Navigation_Drawer_Main_Activity.this,Reports_Activity.class);
            i.putExtra("ID",ParkingID);
            startActivity(i);

            return true;
        } else{
            Toast.makeText(getApplicationContext(),"No idea whats going on.",Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
