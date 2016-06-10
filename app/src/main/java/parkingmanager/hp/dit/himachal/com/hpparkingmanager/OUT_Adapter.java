package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

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

/**
 * Created by kuush on 5/24/2016.
 */
public class OUT_Adapter extends ArrayAdapter<OUT_POJO> implements Filterable {

    private Context context;
    private List<OUT_POJO> userlist;

    private Filter planetFilter;
    private List<OUT_POJO> origUserList;

    public OUT_Adapter(Context context, int resource, List<OUT_POJO> objects) {
        super(context, resource, objects);
        this.context = context;
        this.userlist = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_out_list, parent, false);
        OUT_POJO u = userlist.get(position);
        TextView tv1 = (TextView)view.findViewById(R.id.textView1_name);
        TextView tv2 = (TextView)view.findViewById(R.id.textView2_number);
        tv1.setText(u.getVehicleNo());
        tv2.setText(u.getPhoneNumber());

        return view;
    }

    public OUT_POJO getItem(int position) {
        return userlist.get(position);
    }

    public long getItemId(int position) {
        return userlist.get(position).hashCode();
    }


    @Override
    public int getCount() {
        return userlist.size();
    }

    public void resetData() {
        userlist = origUserList;
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
                List<OUT_POJO> nPlanetList = new ArrayList<>();

                for (OUT_POJO p : userlist) {
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
                userlist = (List<OUT_POJO>) results.values;
                notifyDataSetChanged();
            }

        }

    }

}
