package kr.ac.pusan.cs.sinbaram.nolinerforadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference mRef;
    private Button btnLogin;
    private Button btnRegist;
    private TextView textID;
    private TextView textPwd;
    private TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (Button)findViewById(R.id.loginBtn);
        btnRegist = (Button)findViewById(R.id.registBtn);
        textID =  findViewById(R.id.AdminID);
        textPwd =  findViewById(R.id.AdminPwd);
        show = findViewById(R.id.show);
        final List<Admin_Account> Admin_Accounts = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        mRef.child("Admin_Account").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Admin_Accounts.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Admin_Account tmp = snapshot.getValue(Admin_Account.class);
                    Admin_Accounts.add(tmp);

                }
                show.setText("이제 로그인이 가능합니다.");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RegistActivity.class);
                startActivity(intent);
                finish();
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               for(int i=0;i<Admin_Accounts.size();i++){
                   Admin_Account cmp = Admin_Accounts.get(i);

                   if(cmp.Admin_Private_ID.equals(textID.getText().toString())&&cmp.Admin_Private_password.equals(textPwd.getText().toString())){
                       Intent intent = new Intent(MainActivity.this,MenuList.class);
                       intent.putExtra("DB_Admin", cmp);
                       startActivity(intent);

                       return;
                   }
               }
               Toast.makeText(MainActivity.this, "다시 입력하세요.", Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public void onResume(){

        super.onResume();

        Intent intent = getIntent();

        if(intent.getStringExtra("id")!=null){
            String id = intent.getStringExtra("id");
            String password = intent.getStringExtra("password");
            if(id != null && !id.isEmpty())
            textID.setText(intent.getStringExtra("id"));
            if(password != null && !password.isEmpty())
            textPwd.setText(intent.getStringExtra("password"));
        }
    }

}
