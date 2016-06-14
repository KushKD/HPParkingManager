package Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Model.Collection_Reports_Pojo;
import Model.Inbox_Pojo;
import parkingmanager.hp.dit.himachal.com.hpparkingmanager.R;

/**
 * Created by kuush on 6/14/2016.
 */
public class Reports_Collection_Adapter extends ArrayAdapter<Collection_Reports_Pojo> {

    private Context context;
    private List<Collection_Reports_Pojo> Reports_Collection_List;

    private Filter planetFilter;
    private List<Collection_Reports_Pojo> origUserList;

    public Reports_Collection_Adapter(Context context, int resource, List<Collection_Reports_Pojo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.Reports_Collection_List = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_reports_collection, parent, false);
        Collection_Reports_Pojo u = Reports_Collection_List.get(position);
        TextView tv1 = (TextView)view.findViewById(R.id.textView1_vehicles);
        TextView tv2 = (TextView)view.findViewById(R.id.textView2_collection);
        tv1.setText(u.getTotalVehicles());
        tv2.setText(u.getTotalCollection());
        return view;
    }

    public Collection_Reports_Pojo getItem(int position) {
        return Reports_Collection_List.get(position);
    }

    public long getItemId(int position) {
        return Reports_Collection_List.get(position).hashCode();
    }


    @Override
    public int getCount() {
        return Reports_Collection_List.size();
    }

    public void resetData() {
        Reports_Collection_List = origUserList;
    }



}
