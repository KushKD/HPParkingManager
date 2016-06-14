package Adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.List;

import Model.Parking_Pojo;
import parkingmanager.hp.dit.himachal.com.hpparkingmanager.R;

/**
 * Created by HPZ231 on 24-07-2015.
 */
public class Parking_Adapter extends ArrayAdapter<Parking_Pojo> implements Filterable {

    private Context context;
    private List<Parking_Pojo> parkingList;



    public Parking_Adapter(Context context, int resource, List<Parking_Pojo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.parkingList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_parking, parent, false);
        Parking_Pojo u = parkingList.get(position);
        TextView tv1 = (TextView)view.findViewById(R.id.textView1_parkingname);
        TextView tv2 = (TextView)view.findViewById(R.id.textView2_landmark);
        tv1.setText(u.getParkingPlace());
        tv2.setText(u.getIdentifier());
        return view;
    }

    public Parking_Pojo getItem(int position) {
        return parkingList.get(position);
    }

    public long getItemId(int position) {
        return parkingList.get(position).hashCode();
    }


    @Override
    public int getCount() {
        return parkingList.size();
    }




}