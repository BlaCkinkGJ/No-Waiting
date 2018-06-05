package kr.ac.pusan.cs.sinbaram.nolinerforadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RegistActivity extends AppCompatActivity {
    Button btnRegist;
    Button btnCancel;
    EditText admin_ID;
    EditText admin_Pwd;
    EditText public_ID;
    FirebaseDatabase database;
    DatabaseReference mRef;



    TextView test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        btnRegist = findViewById(R.id.registBtn);
        btnCancel = findViewById(R.id.cancelBtn);
        admin_ID = findViewById(R.id.AdminID);
        admin_Pwd = findViewById(R.id.AdminPwd);
        public_ID = findViewById(R.id.PublicID);
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        test = findViewById(R.id.textView);
        final List<Admin_Account> Admin_Accounts = new ArrayList<>();
        //final List<String> Public_IDs = new ArrayList<>();
        /*mRef.child("test").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                test.setText(dataSnapshot.getValue().toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RegistActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });*/
        mRef.child("Admin_Account").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Admin_Accounts.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Admin_Account admin = snapshot.getValue(Admin_Account.class);
                    Admin_Accounts.add(admin);
                }
                test.setText("이제 회원가입이 가능합니다.");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        mRef.child("Admin_Public_Account").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Public_IDs.clear();
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    String tmp = snapshot.getValue().toString();
//                    Public_IDs.add(tmp);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });



        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (admin_ID.getText().toString().isEmpty() || admin_Pwd.getText().toString().isEmpty() || public_ID.getText().toString().isEmpty()) {
                    Toast.makeText(RegistActivity.this, "빈칸을 채우세요.", Toast.LENGTH_LONG).show();
                    return;
                }

               for(int i=0;i<Admin_Accounts.size();i++) {
                   Admin_Account tmp = Admin_Accounts.get(i);
                   if (tmp.Admin_Private_ID.equals(admin_ID.getText().toString())) {
                       Toast.makeText(RegistActivity.this, "이미 있는 관리자 아이디입니다.", Toast.LENGTH_LONG).show();
                       return;
                   }
                   if(tmp.Admin_Public_ID.equals(public_ID.getText().toString())){
                       Toast.makeText(RegistActivity.this, "이미 있는 공개 아이디입니다.", Toast.LENGTH_LONG).show();
                       return;
                   }
               }
                    Admin_Account newAccount = new Admin_Account();
                    newAccount.Admin_Private_ID = admin_ID.getText().toString();
                    newAccount.Admin_Private_password = admin_Pwd.getText().toString();
                    newAccount.Admin_Public_ID = public_ID.getText().toString();
                    mRef.child("Admin_Account").child(newAccount.Admin_Private_ID).setValue(newAccount);

                    Toast.makeText(RegistActivity.this, "Success", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegistActivity.this, MainActivity.class);
                    intent.putExtra("id", admin_ID.getText().toString());
                    intent.putExtra("password", admin_Pwd.getText().toString());
                    startActivity(intent);
                    finish();

            }

        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistActivity.this,MainActivity.class);
                intent.putExtra("id", admin_ID.getText().toString());
                intent.putExtra("password",admin_Pwd.getText().toString());
                startActivity(intent);
                finish();
            }
        });

    }
}
