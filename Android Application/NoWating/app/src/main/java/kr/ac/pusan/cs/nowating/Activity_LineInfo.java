package kr.ac.pusan.cs.nowating;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.ac.pusan.cs.nowating.DB.DB_User;
import kr.ac.pusan.cs.nowating.Object.Obj_AdminAccount;
import kr.ac.pusan.cs.nowating.Object.Obj_Line;
import kr.ac.pusan.cs.nowating.Object.Obj_User;
import kr.ac.pusan.cs.nowating.RSA.RSA;

public class Activity_LineInfo extends AppCompatActivity {
    private TextView PubID;
    private TextView LineName;
    private TextView State;
    private TextView EnrollNum;
    private TextView EnterNum;
    private Obj_Line line;
    private Obj_AdminAccount adminAccount;

    List<Obj_User> userList;
    private Button RegistBtn;
    FirebaseDatabase database;
    DatabaseReference mRef;
    private DB_User DB;
    private String User_ID;
    private String DBstate;
    Date mDate;
    long mNow;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__line_info);

        Intent intent = getIntent();
        line = (Obj_Line) intent.getSerializableExtra("line");
        adminAccount = (Obj_AdminAccount) intent.getSerializableExtra("admin");

        LineName = findViewById(R.id.lineName);
        State = findViewById(R.id.state);
        EnterNum = findViewById(R.id.enter);
        RegistBtn = findViewById(R.id.registbtn);
        EnrollNum = findViewById(R.id.enroll_num);
        userList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        PubID = findViewById(R.id.pubID);

        PubID.setText(adminAccount.Admin_Public_ID);
        LineName.setText(line.Line_Name);

        DB = new DB_User(getApplicationContext(), "USER", null, 1);
        //DB.delete(line.Line_Name);
        //User_ID = DB.get(line.Line_Name,2);
        DBstate = DB.get(line.Line_Name,DB.LIST_NAME);
        if(DBstate == null){
            RegistBtn.setText("등록");
        }else{

            RegistBtn.setText("불러오기");
        }
        mRef.child("Line List").child(adminAccount.Admin_Public_ID).child(line.Line_Name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count=0;
                for(DataSnapshot snapshot : dataSnapshot.child("USER LIST").getChildren()){
                    Obj_User user = snapshot.getValue(Obj_User.class);
                    if((user.State).equals("Check"))count++;
                    userList.add(user);
                }
                line = dataSnapshot.child("INFO").getValue(Obj_Line.class);
                EnrollNum.setText(String.valueOf(line.Current_Enrollment_State) + " / "+String.valueOf(line.Max_Number));
                EnterNum.setText(String.valueOf(count));
                if(DB.get(line.Line_Name,1) == null){
                    RegistBtn.setText("등록");
                }else{
                    RegistBtn.setText("불러오기");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        RegistBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String time = getTime();
                //등록
                if(DB.get(line.Line_Name,2)==null){
                    RegistBtn.setText("불러오기");
                    try {
                        RSA rsa = new RSA();
                        rsa.setPublicKey(line.Public_Key);
                        rsa.setBuffer(time+String.valueOf(line.Current_Enrollment_State));
                        User_ID = rsa.encryption();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //Toast.makeText(Activity_LineInfo.this,User_ID,Toast.LENGTH_LONG).show();
                    String[] values = new String[3];
                    values[0] = line.Line_Name;
                    values[1] = User_ID;
                    values[2] = time;
                    DB.insert(values);

                    Obj_User user = new Obj_User();
                    user.Enter = "cant";
                    user.make(DB.get(line.Line_Name,2),"wait");
                    line.Current_Enrollment_State++;
                    mRef.child("Line List").child(adminAccount.Admin_Public_ID).child(line.Line_Name).child("INFO").child("Current_Enrollment_State").setValue(line.Current_Enrollment_State);
                    mRef.child("Line List").child(adminAccount.Admin_Public_ID).child(line.Line_Name).child("USER LIST").push().setValue(user);
                    Intent intent1 = new Intent(Activity_LineInfo.this,Activity_QRcode.class);
                    intent1.putExtra("user", user);
                    intent1.putExtra("line",line);
                    intent1.putExtra("admin",adminAccount);
                    //intent1.putExtra("state","insert");
                    Toast.makeText(Activity_LineInfo.this,"등록되었습니다.",Toast.LENGTH_LONG).show();
                    startActivity(intent1);

                }
                //불러오기
                else{
                    Intent intent1 = new Intent(Activity_LineInfo.this,Activity_QRcode.class);
                    intent1.putExtra("line",line);
                    //intent1.putExtra("state","no");
                    intent1.putExtra("admin",adminAccount);
                    startActivity(intent1);
                }
            }
        });

    }
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}
