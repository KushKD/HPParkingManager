package Utils;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by kuush on 6/17/2016.
 */
public class Generic_Async extends AsyncTask<String,String,String> {


    Context context;

    Generic_Async(Context c){
        this.context = c;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }



}
