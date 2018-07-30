package kr.ac.pusan.cs.sinbaram.nolinerforuser;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.content.Intent;

public class Activity_Splash extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(1000);
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        startActivity(new Intent(this, Activity_Main.class));
        finish();
    }
}