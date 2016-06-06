package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kuush on 6/6/2016.
 */
public class Outbox_Adapter  extends ArrayAdapter<OutboxPOJO> {

    private Context context;
    private List<OutboxPOJO> outbox_List;

    public Outbox_Adapter(Context context, int resource, List<OutboxPOJO> objects) {
        super(context, resource, objects);
        this.context = context;
        this.outbox_List = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_outbox, parent, false);
        OutboxPOJO u = outbox_List.get(position);
        TextView tv1 = (TextView)view.findViewById(R.id.itemone);
        TextView tv2 = (TextView)view.findViewById(R.id.itemtwo);
        tv1.setText(u.getVehicleNo());
        tv2.setText(u.getRequestTime());
        return view;
    }

    public OutboxPOJO getItem(int position) {
        return outbox_List.get(position);
    }

    public long getItemId(int position) {
        return outbox_List.get(position).hashCode();
    }


    @Override
    public int getCount() {
        return outbox_List.size();
    }
}
