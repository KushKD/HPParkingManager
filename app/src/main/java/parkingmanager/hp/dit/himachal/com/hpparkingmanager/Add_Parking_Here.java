package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import Adapters.SpinAdapter_District;
import Adapters.SpinAdapter_Tehsils;
import Adapters.SpinAdapter_Village_Town;
import HelperFunctions.AppStatus;
import Interfaces.AsyncTaskListener;
import Enum.TaskType;
import Model.CencusDistrict;
import Model.CencusTehsil;
import Model.CencusVillage_Town_New;
import Presentation.Custom_Dialog;
import Utils.Generic_Async_Get;

public class Add_Parking_Here extends Activity implements AsyncTaskListener {

    public String Latitude = null;
    public String Longitude = null;
    private TextView tv_latitude ,tv_Longitude;
    private EditText state_tv, address_tv, aadhaar_tv, mobile_number_tv, firstname_tv,capacity_tv,threshold_tv, identifier_tv,area_et ;
    private Button Back_bt, update_bt;
    private Spinner district_sp, tehsil_sp, village_sp;
    private String townId = null, villageId = null, districtID = null, tehsilID = null, blockID = null, panchayatID = null, wardID = null, municipalityID = null;

    protected List<CencusDistrict> Districts_Server = null;
    protected List<CencusTehsil> Tehsils_Server = null;
    protected List<CencusVillage_Town_New> Village_Town_New = null;


    private SpinAdapter_Tehsils adapter_tehsils;
    private SpinAdapter_Village_Town adapter_village_town;
    private SpinAdapter_District adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__parking__here);

        Bundle bundle = getIntent().getExtras();
         Latitude = bundle.getString("LATITUDE");
         Longitude = bundle.getString("LONGITUDE");

        Log.e("Latitude new Activity",Latitude);
        Log.e("Longitude new Activity",Longitude);

        tv_latitude = (TextView)findViewById(R.id.latitude);
        tv_Longitude = (TextView)findViewById(R.id.longitude);
        firstname_tv = (EditText) findViewById(R.id.firstname);
        aadhaar_tv = (EditText) findViewById(R.id.aadhaarno);
        mobile_number_tv = (EditText) findViewById(R.id.mobilenumber);
        state_tv = (EditText) findViewById(R.id.state);
        district_sp = (Spinner) findViewById(R.id.district);
        tehsil_sp = (Spinner) findViewById(R.id.tehsil);
        village_sp = (Spinner) findViewById(R.id.village);
        address_tv = (EditText) findViewById(R.id.address);
        capacity_tv = (EditText)findViewById(R.id.capacity);
        threshold_tv = (EditText)findViewById(R.id.threshold);
        identifier_tv = (EditText)findViewById(R.id.identifier);
        area_et = (EditText)findViewById(R.id.area);
        Back_bt = (Button) findViewById(R.id.back);
        update_bt = (Button) findViewById(R.id.update);

        tv_latitude.setText(Latitude);
        tv_Longitude.setText(Longitude);


        if(AppStatus.getInstance(Add_Parking_Here.this).isOnline())
        {
            GetDaTaAsync();
        }else{

        }
        Back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add_Parking_Here.this.finish();
            }
        });


        district_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                CencusDistrict CD = adapter.getItem(position);
                if (AppStatus.getInstance(Add_Parking_Here.this).isOnline()) {
                    districtID = CD.getId().trim();
                    GetDaTaAsync_Tehsil(CD.getId().trim());
                } else {
                    Custom_Dialog C_D = new Custom_Dialog();
                    C_D.showDialog(Add_Parking_Here.this, "Please connect to Internet.");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        tehsil_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                CencusDistrict CD = adapter.getItem(position);
                CencusTehsil CT = adapter_tehsils.getItem(position);
                tehsilID = CT.getId().trim();


                if (AppStatus.getInstance(Add_Parking_Here.this).isOnline()) {
                    GetDaTaAsync_Village_Town(tehsilID);
                } else {
                    Custom_Dialog __CD = new Custom_Dialog();
                    __CD.showDialog(Add_Parking_Here.this, "Please Connect to Internet.");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });


    }
    private void GetDaTaAsync_Village_Town(String Cencus_Tehsil) {
        //CENCUS_TEHSIL
        StringBuilder SB = null;
        SB = new StringBuilder();  //http://localhost:1936/HPParking.svc/getTowns_JSON/00185
        SB.append("http://localhost:1936/HPParking.svc/");
        SB.append("getTowns_JSON/");
        SB.append(Cencus_Tehsil);

        Log.d("Village/Town", SB.toString());

        new Generic_Async_Get(Add_Parking_Here.this, Add_Parking_Here.this, TaskType.GET_TOWN).execute(SB.toString());


    }
    private void GetDaTaAsync_Tehsil(String trim) {
        //CENCUS_TEHSIL
        StringBuilder SB = null;
        SB = new StringBuilder();
        SB.append("http://localhost:1936/HPParking.svc/");
        SB.append("getSubDistrict_JSON/");
        SB.append(trim);
        Log.d("Tehsils", SB.toString());

        new Generic_Async_Get(Add_Parking_Here.this, Add_Parking_Here.this, TaskType.GET_SUBDISTRICT).execute(SB.toString());

    }

    private void GetDaTaAsync() {
        new Generic_Async_Get(Add_Parking_Here.this, Add_Parking_Here.this, TaskType.GET_DISTRICT).execute("http://localhost:1936/HPParking.svc/getDistrict_JSON/06");
    }

    @Override
    public void onTaskCompleted(String result, TaskType taskType) {
        if (taskType == TaskType.GET_DISTRICT) {

            Log.e("Data", result);
           // ObjectMapper mapper = new ObjectMapper();
          //  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
           // Districts_Server = mapper.readValue(result, new TypeReference<List<CencusDistrict>>() {
           // });
           // Log.e("Length", Integer.toString(Districts_Server.size()));
           // adapter = new SpinAdapter_District(Add_Parking_Here.this, android.R.layout.simple_spinner_item, Districts_Server);
           // district_sp.setAdapter(adapter);

        }else if (taskType == TaskType.GET_SUBDISTRICT) {
            Log.e("Data", result);
           // ObjectMapper mapper = new ObjectMapper();
            //mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
           // Tehsils_Server = mapper.readValue(result, new TypeReference<List<CencusTehsil>>() {
           // });
           // Log.e("Length", Integer.toString(Tehsils_Server.size()));
            //adapter_tehsils = new SpinAdapter_Tehsils(Add_Parking_Here.this, android.R.layout.simple_spinner_item, Tehsils_Server);
            //tehsil_sp.setAdapter(adapter_tehsils);
        }else if (taskType == TaskType.GET_TOWN) {
            Log.e("Data", result);


            /*ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Village_Town_Server = new HashMap<String, String>();
            Village_Town_Server = mapper.readValue(result, new TypeReference<Map<String, String>>() {
            });

            if (Village_Town_Server.size() == 0) {
                village_ui.setVisibility(View.GONE);
            } else {
                Village_Town_New = new ArrayList<>();
                CencusVillage_Town_New CVTN = null;
                for (Map.Entry<String, String> entry : Village_Town_Server.entrySet()) {
                    CVTN = new CencusVillage_Town_New();
                    CVTN.setCode(entry.getKey());
                    CVTN.setName(entry.getValue());
                    // System.out.println(entry.getKey() + "/" + entry.getValue());
                    Village_Town_New.add(CVTN);
                }*/
              //  adapter_village_town = new SpinAdapter_Village_Town(Add_Parking_Here.this, android.R.layout.simple_spinner_item, Village_Town_New);
              //  village_sp.setAdapter(adapter_village_town);
               // village_ui.setVisibility(View.VISIBLE);
            }
        }

    }

