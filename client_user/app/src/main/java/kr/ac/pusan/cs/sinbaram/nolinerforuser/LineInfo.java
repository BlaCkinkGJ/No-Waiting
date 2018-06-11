package kr.ac.pusan.cs.sinbaram.nolinerforuser;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.ac.pusan.cs.sinbaram.nolinerforuser.DB.DB_User;
import kr.ac.pusan.cs.sinbaram.nolinerforuser.RSA.RSA;

import static java.lang.String.valueOf;

public class LineInfo extends AppCompatActivity {
    private TextView PubID;
    private TextView LineName;
    private TextView State;
    private TextView EnrollNum;
    private TextView EnterNum;
    private Line line;
    private String Public_ID;
    List<String> user_ids;
    private Button RegistBtn;
    FirebaseDatabase database;
    DatabaseReference mRef;
    private DB_User DB;
    private String User_ID;
    Date mDate;
    long mNow;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_info);
        Intent intent = getIntent();
        line = (Line)intent.getSerializableExtra("line");
        Public_ID = intent.getStringExtra("pubID");
        PubID = findViewById(R.id.pubID);
        LineName = findViewById(R.id.lineName);
        State = findViewById(R.id.state);
        EnterNum = findViewById(R.id.enter);
        PubID.setText(Public_ID);
        LineName.setText(line.Line_Name);
        RegistBtn = findViewById(R.id.registbtn);
        EnrollNum = findViewById(R.id.enroll_num);
        user_ids = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();

        State.setText(stateCheck());


        DB = new DB_User(getApplicationContext(), "USER", null, 1);
        //DB.delete(line.Line_Name);
        //User_ID = DB.get(line.Line_Name,2);

        mRef.child("Line List").child(Public_ID).child(line.Line_Name).child("INFO").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                line = dataSnapshot.getValue(Line.class);
                if(State.getText().equals("마감")){
                    RegistBtn.setVisibility(View.INVISIBLE);
                }
                EnrollNum.setText(String.valueOf(line.Current_Enrollment_State)+" / "+String.valueOf(line.Max_Number));
                if(DB.get(line.Line_Name, DB.LIST_NAME)== null) {
                    User_ID = null;
                    RegistBtn.setText("등록");
                }else{
                    RegistBtn.setText("불러오기");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRef.child("Line List").child(Public_ID).child(line.Line_Name).child("USER LIST").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count=0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User tmp = new User();
                    tmp = snapshot.getValue(User.class);
                    if((tmp.State).equals("Check"))count++;
                }
                EnterNum.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Toast.makeText(LineInfo.this,User_ID,Toast.LENGTH_LONG).show();

        RegistBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String time = getTime();

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

                    //Toast.makeText(LineInfo.this,User_ID,Toast.LENGTH_LONG).show();
                    String[] values = new String[3];
                    values[0] = line.Line_Name;
                    values[1] = User_ID;
                    values[2] = time;
                    DB.insert(values);
                    //line.Current_Enrollment_State = line.Current_Enrollment_State+1;
                    User user = new User();
                    user.make(DB.get(line.Line_Name,2),"wait");
                    line.Current_Enrollment_State++;
                    mRef.child("Line List").child(Public_ID).child(line.Line_Name).child("INFO").setValue(line);
                    mRef.child("Line List").child(Public_ID).child(line.Line_Name).child("USER LIST").push().setValue(user);
                    Intent intent1 = new Intent(LineInfo.this,QRactivity.class);
                    intent1.putExtra("regist", user);
                    intent1.putExtra("userID",DB.get(line.Line_Name,2));
                    intent1.putExtra("pubID",Public_ID);
                    intent1.putExtra("line",line);
                    intent1.putExtra("state","insert");
                    intent1.putExtra("lineName",line.Line_Name);
                    Toast.makeText(LineInfo.this,"등록되었습니다.",Toast.LENGTH_LONG).show();
                    startActivity(intent1);

                }else{
                    Intent intent1 = new Intent(LineInfo.this,QRactivity.class);
                    intent1.putExtra("userID",DB.get(line.Line_Name,2));
                    intent1.putExtra("pubID",Public_ID);
                    intent1.putExtra("line",line);
                    intent1.putExtra("state","no");
                    intent1.putExtra("lineName",line.Line_Name);
                    startActivity(intent1);

                }
            }
        });

    }public String stateCheck() {

        Date date1 = null;
        try {
            date1 = mFormat.parse(valueOf(line.Closing_Time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = mFormat.parse(getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date2.after(date1) || line.Current_Enrollment_State == line.Max_Number) {
            return "마감";
        } else {
            try {
                date1 = mFormat.parse(valueOf(line.Opening_Time));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date2.after(date1)) {
                return "입장 중";
            } else {
                return "입장 전";

            }
        }

    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRef.child("Line List").child(Public_ID).child(line.Line_Name).child("INFO").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                line = dataSnapshot.getValue(Line.class);
                EnrollNum.setText(String.valueOf(line.Current_Enrollment_State)+" / "+String.valueOf(line.Max_Number));
                if(DB.get(line.Line_Name, DB.LIST_NAME)== null) {
                    User_ID = null;
                    RegistBtn.setText("등록");
                }else{
                    RegistBtn.setText("불러오기");
                }
                if(State.getText().equals("마감")){
                    RegistBtn.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRef.child("Line List").child(Public_ID).child(line.Line_Name).child("USER LIST").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count=0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User tmp = new User();
                    tmp = snapshot.getValue(User.class);
                    if((tmp.State).equals("Check"))count++;
                }
                EnterNum.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
