package parkingmanager.hp.dit.himachal.com.hpparkingmanager;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ReceiveMessage extends Activity {


    Button close;
    TextView m;

    private String Message , PhoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_receive_message);

        Bundle bundle = getIntent().getExtras();

        Message = bundle.getString("Message");
        PhoneNumber = bundle.getString("Sender_Number");

        close = (Button)findViewById(R.id.close);
        m = (TextView)findViewById(R.id.m);

        m.setText(Message);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReceiveMessage.this.finish();
            }
        });
    }
}
