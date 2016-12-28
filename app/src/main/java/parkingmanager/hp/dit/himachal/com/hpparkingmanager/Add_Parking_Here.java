package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
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

import org.json.JSONException;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import Adapters.SpinAdapter_District;
import Adapters.SpinAdapter_Tehsils;
import Adapters.SpinAdapter_Village_Town;
import HelperFunctions.AppStatus;
import HelperFunctions.Date_Time;
import Interfaces.AsyncTaskListener;
import Enum.TaskType;
import JsonManager.AddParkingData;
import JsonManager.Manage_Json;
import Model.AddParkingPOJO;
import Model.CencusDistrict;
import Model.CencusTehsil;
import Model.CencusVillage_Town_New;
import Presentation.Custom_Dialog;
import Utils.EConstants;
import Utils.Generic_Async_Get;

public class Add_Parking_Here extends Activity implements AsyncTaskListener {

    public String Latitude = null;
    public String Longitude = null;
    private TextView tv_latitude, tv_Longitude;
    private EditText state_tv,parkingname_et, address_tv, aadhaar_tv, mobile_number_tv, firstname_tv, capacity_tv, threshold_tv, identifier_tv, area_et;
    private Button Back_bt, update_bt;
    private Spinner district_sp, tehsil_sp, village_sp;
    private String districtID = null, tehsilID = null, villageId = null;
    private String districtName = null, tehsilName = null, villageName = null;

    AddParkingPOJO APJ = null;

    URL url_;
    HttpURLConnection conn_;
    StringBuilder sb = null;

    protected List<CencusDistrict> Districts_Server = null;
    protected List<CencusTehsil> Tehsils_Server = null;
    protected List<CencusVillage_Town_New> Village_Town_New = null;

    Custom_Dialog CD = new Custom_Dialog();

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

        Log.e("Latitude new Activity", Latitude);
        Log.e("Longitude new Activity", Longitude);

        parkingname_et = (EditText)findViewById(R.id.parkingname);
        tv_latitude = (TextView) findViewById(R.id.latitude);
        tv_Longitude = (TextView) findViewById(R.id.longitude);
        firstname_tv = (EditText) findViewById(R.id.firstname);
        aadhaar_tv = (EditText) findViewById(R.id.aadhaarno);
        mobile_number_tv = (EditText) findViewById(R.id.mobilenumber);
        state_tv = (EditText) findViewById(R.id.state);
        district_sp = (Spinner) findViewById(R.id.district);
        tehsil_sp = (Spinner) findViewById(R.id.tehsil);
        village_sp = (Spinner) findViewById(R.id.village);
        address_tv = (EditText) findViewById(R.id.address);
        capacity_tv = (EditText) findViewById(R.id.capacity);
        threshold_tv = (EditText) findViewById(R.id.threshold);
        identifier_tv = (EditText) findViewById(R.id.identifier);
        area_et = (EditText) findViewById(R.id.area);
        Back_bt = (Button) findViewById(R.id.back);
        update_bt = (Button) findViewById(R.id.update);

        tv_latitude.setText(Latitude);
        tv_Longitude.setText(Longitude);


        if (AppStatus.getInstance(Add_Parking_Here.this).isOnline()) {
            GetDaTaAsync();
        } else {
            CD.showDialog(Add_Parking_Here.this, "Internet not available.");
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
                    districtID = CD.getId().toString().trim();
                    districtName = CD.getName().toString().trim();
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

                CencusTehsil CT = adapter_tehsils.getItem(position);
                tehsilID = CT.getId().toString().trim();
                tehsilName = CT.getName().toString().trim();


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

        village_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                CencusVillage_Town_New VN = adapter_village_town.getItem(position);
                villageName = VN.getName().toString().trim();
                villageId = VN.getId().toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        update_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });


    }

    private void getData() {
        APJ = new AddParkingPOJO();
        try {
            APJ.setParkingName(parkingname_et.getText().toString().trim());
            APJ.setLatitude(tv_latitude.getText().toString().trim());
            APJ.setLongitude(tv_Longitude.getText().toString().trim());
            APJ.setContact_Person_Name(firstname_tv.getText().toString().trim());
            APJ.setAadhaar_Number(aadhaar_tv.getText().toString().trim());
            APJ.setMobile_number(mobile_number_tv.getText().toString().trim());
            APJ.setState("Himachal Pradesh");
            APJ.setDistrict(districtID);
            APJ.setSub_District(tehsilID);
            APJ.setVillage(villageId);
            APJ.setParkingAddress(address_tv.getText().toString().trim());
            APJ.setIdentifier(identifier_tv.getText().toString().trim());
            APJ.setArea(area_et.getText().toString().trim());
            APJ.setCapacity(capacity_tv.getText().toString().trim());
            APJ.setThreshold_Limit(threshold_tv.getText().toString().trim());
        } catch (Exception ex) {
            CD.showDialog(Add_Parking_Here.this, ex.getLocalizedMessage().toString().trim());
        }

        try {
            //Check the Values
            if (    APJ.getParkingName() != null && !APJ.getParkingName().isEmpty()&&
                    APJ.getLatitude() != null && !APJ.getLatitude().isEmpty() &&
                    APJ.getLongitude() != null && !APJ.getLongitude().isEmpty() &&
                    APJ.getContact_Person_Name() != null && !APJ.getContact_Person_Name().isEmpty() &&
                    APJ.getAadhaar_Number() != null && !APJ.getAadhaar_Number().isEmpty() &&
                    APJ.getMobile_number() != null && !APJ.getMobile_number().isEmpty() &&
                    APJ.getState() != null && !APJ.getState().isEmpty() &&
                    APJ.getDistrict() != null && !APJ.getDistrict().isEmpty() &&
                    APJ.getSub_District() != null && !APJ.getSub_District().isEmpty() &&
                    APJ.getVillage() != null && !APJ.getVillage().isEmpty() &&
                    APJ.getParkingAddress() != null && !APJ.getParkingAddress().isEmpty() &&
                    APJ.getIdentifier() != null && !APJ.getIdentifier().isEmpty() &&
                    APJ.getArea() != null && !APJ.getArea().isEmpty() &&
                    APJ.getCapacity() != null && !APJ.getCapacity().isEmpty() &&
                    APJ.getThreshold_Limit() != null && !APJ.getThreshold_Limit().isEmpty()) {

                if (AppStatus.getInstance(Add_Parking_Here.this).isOnline()) {

                    //Send Data To Server
                    SaveParking C_IN = new SaveParking();
                    C_IN.execute(APJ);

                } else {
                    CD.showDialog(Add_Parking_Here.this, "Please connect to Internet.");
                }

            } else {
                CD.showDialog(Add_Parking_Here.this, "Add fields are mandatory.");
            }


        } catch (Exception ex) {
            CD.showDialog(Add_Parking_Here.this, ex.getLocalizedMessage().toString().trim());
        }
    }

    private void GetDaTaAsync_Village_Town(String Cencus_Tehsil) {
        //CENCUS_TEHSIL
        StringBuilder SB = null;
        SB = new StringBuilder();  //http://localhost:1936/HPParking.svc/getTowns_JSON/00185
        SB.append(EConstants.Production_URL);
        SB.append("getTowns_JSON/");
        SB.append(Cencus_Tehsil);

        Log.d("Village/Town", SB.toString());

        new Generic_Async_Get(Add_Parking_Here.this, Add_Parking_Here.this, TaskType.GET_TOWN).execute(SB.toString());


    }

    private void GetDaTaAsync_Tehsil(String trim) {
        //CENCUS_TEHSIL
        StringBuilder SB = null;
        SB = new StringBuilder();
        SB.append(EConstants.Production_URL);
        SB.append("getSubDistrict_JSON/");
        SB.append(trim);
        Log.d("Tehsils", SB.toString());

        new Generic_Async_Get(Add_Parking_Here.this, Add_Parking_Here.this, TaskType.GET_SUBDISTRICT).execute(SB.toString());

    }

    private void GetDaTaAsync() {
        new Generic_Async_Get(Add_Parking_Here.this, Add_Parking_Here.this, TaskType.GET_DISTRICT).execute(EConstants.Production_URL + "getDistrict_JSON/06");
    }

    @Override
    public void onTaskCompleted(String result, TaskType taskType) {
        if (taskType == TaskType.GET_DISTRICT) {

            Log.e("Data", result);

            Districts_Server = AddParkingData.parseFeedNotifications(result);
            Log.e("Length", Integer.toString(Districts_Server.size()));
            adapter = new SpinAdapter_District(Add_Parking_Here.this, android.R.layout.simple_spinner_item, Districts_Server);
            district_sp.setAdapter(adapter);

        } else if (taskType == TaskType.GET_SUBDISTRICT) {
            Log.e("Data", result);
            Tehsils_Server = AddParkingData.getSubdistrict(result);
            Log.e("Length", Integer.toString(Tehsils_Server.size()));
            adapter_tehsils = new SpinAdapter_Tehsils(Add_Parking_Here.this, android.R.layout.simple_spinner_item, Tehsils_Server);
            tehsil_sp.setAdapter(adapter_tehsils);
        } else if (taskType == TaskType.GET_TOWN) {
            Log.e("Data", result);


            Village_Town_New = AddParkingData.getVillageTown(result);
            Log.e("Length", Integer.toString(Village_Town_New.size()));
            adapter_village_town = new SpinAdapter_Village_Town(Add_Parking_Here.this, android.R.layout.simple_spinner_item, Village_Town_New);
            village_sp.setAdapter(adapter_village_town);

        }
    }


    class SaveParking extends AsyncTask<Object, String, String> {

        JSONStringer userJson = null;

        private ProgressDialog dialog;
        String url = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Add_Parking_Here.this);
            this.dialog.setMessage("Please wait ..");
            this.dialog.show();
            this.dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(Object... objects) {
            AddParkingPOJO Outbox_Object_result = (AddParkingPOJO) objects[0];

            try {
                url_ = new URL(EConstants.Production_URL + "AddParkings_JSON");   //Might be changed
                conn_ = (HttpURLConnection) url_.openConnection();
                conn_.setDoOutput(true);
                conn_.setRequestMethod("POST");
                conn_.setUseCaches(false);
                conn_.setConnectTimeout(10000);
                conn_.setReadTimeout(10000);
                conn_.setRequestProperty("Content-Type", "application/json");
                conn_.connect();

                userJson = new JSONStringer()
                        .object().key("AddParking")    //Might be changed
                        .object()
                        .key("ParkingName").value(Outbox_Object_result.getParkingName())
                        .key("Latitude").value(Outbox_Object_result.getLatitude())
                        .key("Longitude").value(Outbox_Object_result.getLongitude())
                        .key("Name").value(Outbox_Object_result.getContact_Person_Name())
                        .key("AadhaarNumber").value(Outbox_Object_result.getAadhaar_Number())
                        .key("MobileNumber").value(Outbox_Object_result.getMobile_number())
                        .key("State").value(Outbox_Object_result.getState())
                        .key("District").value(Outbox_Object_result.getDistrict())
                        .key("SubDistrict").value(Outbox_Object_result.getSub_District())
                        .key("Village").value(Outbox_Object_result.getVillage())
                        .key("Address").value(Outbox_Object_result.getParkingAddress())
                        .key("Identifier").value(Outbox_Object_result.getIdentifier())
                        .key("Area").value(Outbox_Object_result.getArea())
                        .key("Capacity").value(Outbox_Object_result.getCapacity())
                        .key("ThresholdLimit").value(Outbox_Object_result.getThreshold_Limit())
                        .endObject()
                        .endObject();


                System.out.println(userJson.toString());
                Log.e("Object", userJson.toString());
                OutputStreamWriter out = new OutputStreamWriter(conn_.getOutputStream());
                out.write(userJson.toString());
                out.close();

                try {
                    int HttpResult = conn_.getResponseCode();
                    if (HttpResult == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(), "utf-8"));
                        String line = null;
                        sb = new StringBuilder();
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        System.out.println(sb.toString());

                    } else {
                        System.out.println("Server Connection failed.");
                    }

                } catch (Exception e) {
                    return "Server Connection failed.";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (conn_ != null)
                    conn_.disconnect();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String Result = Manage_Json.parseOutward(s);
            dialog.dismiss();
            Custom_Dialog D_C = new Custom_Dialog();
            D_C.showDialog_Vehicle_IN_OUT(Add_Parking_Here.this, s);


        }
    }

}

