package kr.ac.pusan.cs.sinbaram.nolinerforuser;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class Notification extends Activity {
    private ImageView qrcord_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        qrcord_img = findViewById(R.id.qrcode_img);

    }
}
