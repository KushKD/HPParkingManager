package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kuush on 5/24/2016.
 */
public class OUT_Adapter extends ArrayAdapter<OUT_POJO> {

    private Context context;
    private List<OUT_POJO> userlist;

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
}
