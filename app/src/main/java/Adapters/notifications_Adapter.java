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

import Model.Inbox_Pojo;
import Model.Notifications;
import parkingmanager.hp.dit.himachal.com.hpparkingmanager.R;

/**
 * Created by kuush on 12/24/2016.
 */

public class notifications_Adapter extends ArrayAdapter<Notifications>  {

    private Context context;
    private List<Notifications> inbox_List;

    private Filter planetFilter;
    private List<Notifications> origUserList;

    public notifications_Adapter(Context context, int resource, List<Notifications> objects) {
        super(context, resource, objects);
        this.context = context;
        this.inbox_List = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_inbox, parent, false);
        Notifications u = inbox_List.get(position);
        TextView tv1 = (TextView)view.findViewById(R.id.itemone);
        TextView tv2 = (TextView)view.findViewById(R.id.itemtwo);
        tv1.setText(u.getMobileNumber());
        tv2.setText(u.getNotification());
        return view;
    }

    public Notifications getItem(int position) {
        return inbox_List.get(position);
    }

    public long getItemId(int position) {
        return inbox_List.get(position).hashCode();
    }


    @Override
    public int getCount() {
        return inbox_List.size();
    }

    public void resetData() {
        inbox_List = origUserList;
    }






}
