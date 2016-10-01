package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import HelperFunctions.AppStatus;
import Presentation.Custom_Dialog;
import Utils.EConstants;

public class Navigation_Drawer_Main_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView parkingname,parkingidentifier , tv_TextView_ServerConnected;
    Button back,in,out,inbox,outbox;

    String ParkingID=null,Parking_Name=null,name=null,aadhaar=null,Identifier=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation__drawer__main_);


        tv_TextView_ServerConnected = (TextView)findViewById(R.id.tv_server);
        SharedPreferences prfs = getSharedPreferences(EConstants.PREF_NAME, Context.MODE_PRIVATE);

        ParkingID  = prfs.getString("ParkingID","");
        EConstants.ParkingID_Task = ParkingID;
        Parking_Name = prfs.getString("ParkingLocation","");
        Identifier = prfs.getString("ParkingLandmark","");
        name = prfs.getString("OperatorName","");
        aadhaar = prfs.getString("OperatorAadhaarNo","");

        Log.e("Parking ID IS:",ParkingID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      /* if(AppStatus.getInstance(this).isOnline()){

           tv_TextView_ServerConnected.setVisibility(View.VISIBLE);
       }else{
           tv_TextView_ServerConnected.invalidate();
       }*/





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
        TextView Name_Manager_Tv = (TextView)header .findViewById(R.id.name);
        TextView Aadhaar_Manager_Tv = (TextView)header .findViewById(R.id.aadhaar);
        Parking_Name_tv.setText(Parking_Name);
        Parking_Landmark_tv.setText(Identifier);
        Name_Manager_Tv.setText(name);
        Aadhaar_Manager_Tv.setText(aadhaar);


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
                Intent i = new Intent(Navigation_Drawer_Main_Activity.this, Online_Inbox_Activity.class);
                i.putExtra("ID",ParkingID);
                startActivity(i);

                //Go to Request Inward
            }
        });

        outbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Navigation_Drawer_Main_Activity.this, Online_Outbox_Activity.class);
                i.putExtra("ID",ParkingID);
                startActivity(i);

            }
        });




        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Navigation_Drawer_Main_Activity.this, Vehicle_In_Activity.class);
                i.putExtra("ID",ParkingID);
                startActivity(i);


            }
        });

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Navigation_Drawer_Main_Activity.this, Vehicle_Out_Activity.class);
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
        }else if (id == R.id.logout) {

            try{
                SharedPreferences settings = getSharedPreferences(EConstants.PREF_NAME, 0); // 0 - for private mode
                SharedPreferences.Editor editor = settings.edit();
                //Set "hasLoggedIn" to true
                editor.putBoolean("hasLoggedIn", false);
                editor.putString("ParkingID","");
                editor.putString("AlternateMobileNumber","");
                editor.putString("Email","");
                editor.putString("MobileNumber","");
                editor.putString("OperatorAadhaarNo","");
                editor.putString("OperatorName","");
                editor.putString("ParkingLandmark","");
                editor.putString("ParkingLocation","");
                // Commit the edits!
                editor.commit();
                Intent i  = new Intent(Navigation_Drawer_Main_Activity.this,Login_Activity.class);
                //i.putExtra("ID",ParkingID);
                startActivity(i);
                Navigation_Drawer_Main_Activity.this.finish();

            }catch(Exception e){
                Custom_Dialog CM  = new Custom_Dialog();
                CM.showDialog(Navigation_Drawer_Main_Activity.this,e.getLocalizedMessage().toString());
            }

            return true;
        } else if (id == R.id.individual_report) {

            //Toast.makeText(getApplicationContext(),"individual_report Clicked",Toast.LENGTH_LONG).show();
            Intent i  = new Intent(Navigation_Drawer_Main_Activity.this,Individual_Reports_Activity.class);
            i.putExtra("Aadhaar",aadhaar);
            startActivity(i);



            return true;
        } else {
            Toast.makeText(getApplicationContext(),"No idea whats going on.",Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
