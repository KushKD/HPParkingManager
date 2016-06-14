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
import parkingmanager.hp.dit.himachal.com.hpparkingmanager.R;

/**
 * Created by kuush on 6/5/2016.
 */
public class Inbox_Adapter  extends ArrayAdapter<Inbox_Pojo> implements Filterable {

    private Context context;
    private List<Inbox_Pojo> inbox_List;

    private Filter planetFilter;
    private List<Inbox_Pojo> origUserList;

    public Inbox_Adapter(Context context, int resource, List<Inbox_Pojo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.inbox_List = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_inbox, parent, false);
        Inbox_Pojo u = inbox_List.get(position);
        TextView tv1 = (TextView)view.findViewById(R.id.itemone);
        TextView tv2 = (TextView)view.findViewById(R.id.itemtwo);
        tv1.setText(u.getVehicleNo());
        tv2.setText(u.getRequestTime());
        return view;
    }

    public Inbox_Pojo getItem(int position) {
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

    /*
	 * We create our filter
	 */

    @Override
    public Filter getFilter() {
        if (planetFilter == null)
            planetFilter = new PlanetFilter();

        return planetFilter;
    }

    private class PlanetFilter  extends Filter {



        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = origUserList;
                results.count = origUserList.size();
            }
            else {
                // We perform filtering operation
                List<Inbox_Pojo> nPlanetList = new ArrayList<>();

                for (Inbox_Pojo p : inbox_List) {
                    if (p.getVehicleNo().toUpperCase().contains(constraint.toString().toUpperCase()))
                        nPlanetList.add(p);
                    //p.getPostName().toUpperCase().startsWith(constraint.toString().toUpperCase())
                }

                results.values = nPlanetList;
                results.count = nPlanetList.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                inbox_List = (List<Inbox_Pojo>) results.values;
                notifyDataSetChanged();
            }

        }

    }
}
