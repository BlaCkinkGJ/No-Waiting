package kr.ac.pusan.cs.sinbaram.nolinerforadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class MenuList extends AppCompatActivity {

    private Button state;
    private Button make;
    private Button exit;
    private TextView publicID;
    Admin_Account auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);
        state = findViewById(R.id.list_state);
        make = findViewById(R.id.make_list);
        exit = findViewById(R.id.exit);
        publicID = findViewById(R.id.publicID);
        Intent intent = getIntent();
        auth = (Admin_Account) intent.getSerializableExtra("DB_Admin");
        publicID.setText(auth.Admin_Public_ID);
        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MenuList.this,List_State.class);
                intent1.putExtra("DB_Admin",auth);
                startActivity(intent1);
            }
        });

        make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MenuList.this,Make_List.class);
                intent2.putExtra("DB_Admin",auth);
                startActivity(intent2);

            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    @Override
    public void onResume(){

        super.onResume();


    }
}
