package kr.ac.pusan.cs.nowating;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import kr.ac.pusan.cs.nowating.Fragment.HomeFragment;
import kr.ac.pusan.cs.nowating.Fragment.MapFragment;
import kr.ac.pusan.cs.nowating.Fragment.MyInfoFragment;


public class Activity_Main extends FragmentActivity {

    private HomeFragment homeFragment;
    private MyInfoFragment myInfoFragment;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__main);
        BottomBar bottomBar = findViewById(R.id.bottomBar);
        homeFragment = new HomeFragment();
        myInfoFragment = new MyInfoFragment();
        mapFragment = new MapFragment();

        initFragement();


        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if(tabId == R.id.tab_call_log){
                    transaction.replace(R.id.contentContainer, homeFragment).commit();
                }
                else if(tabId == R.id.tab_contacts){
                    transaction.replace(R.id.contentContainer, myInfoFragment).commit();
                }
                else if(tabId == R.id.tab_macro_setting){
                    transaction.replace(R.id.contentContainer, mapFragment).commit();
                }
            }
        });
    }


    public void initFragement(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.contentContainer, homeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
