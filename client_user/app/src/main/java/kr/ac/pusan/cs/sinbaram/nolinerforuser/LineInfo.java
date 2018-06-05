package kr.ac.pusan.cs.sinbaram.nolinerforuser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
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
import java.util.Date;

import kr.ac.pusan.cs.sinbaram.nolinerforuser.DB.DB_User;
import kr.ac.pusan.cs.sinbaram.nolinerforuser.RSA.RSA;

import static java.lang.String.valueOf;

public class LineInfo extends AppCompatActivity {
    private TextView PubID;
    private TextView LineName;
    private TextView State;
    private TextView EnrollNum;
    private Line line;
    private String Public_ID;

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
        line = (Line) intent.getSerializableExtra("line");
        Public_ID = intent.getStringExtra("pubID");
        PubID = findViewById(R.id.pubID);
        LineName = findViewById(R.id.lineName);
        State = findViewById(R.id.state);
        PubID.setText(Public_ID);
        LineName.setText(line.Line_Name);
        RegistBtn = findViewById(R.id.registbtn);
        EnrollNum = findViewById(R.id.enroll_num);

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();

        State.setText(stateCheck());

        EnrollNum.setText(String.valueOf(line.Current_Enrollment_State)+" / "+String.valueOf(line.Max_Number));
        DB = new DB_User(getApplicationContext(), "USER", null, 1);
        //DB.delete(line.Line_Name);
        User_ID = DB.get(line.Line_Name,2);

        //Toast.makeText(LineInfo.this,User_ID,Toast.LENGTH_LONG).show();
        if(State.getText().equals("마감")){
            RegistBtn.setVisibility(View.INVISIBLE);
        }
        else if(User_ID==null){
            RegistBtn.setText("등록");
        }else{
            RegistBtn.setText("불러오기");

        }
        RegistBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String time = getTime();
                if(RegistBtn.getText().equals("등록")){
                    try {
                        RSA rsa = new RSA();
                        rsa.setPublicKey(line.Public_Key);
                        rsa.setBuffer(time);
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

                    mRef.child("Line List").child(Public_ID).child(line.Line_Name).child("INFO").child("Current_Enrollment_State").setValue(line.Current_Enrollment_State+1);
                    RegistUser registUser = new RegistUser();
                    registUser.make(User_ID,"wait");
                    mRef.child("Line List").child(Public_ID).child(line.Line_Name).child("USER LIST").push().setValue(registUser);
                    RegistBtn.setText("불러오기");
                }else{
                    Intent intent1 = new Intent(LineInfo.this,QRactivity.class);
                    intent1.putExtra("userID",User_ID);
                    intent1.putExtra("pubID",Public_ID);
                    intent1.putExtra("line",line);
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

    /*public void bHandler(View v){
        switch (v.getId()){
            case R.id.basic:
                showBasicNotification();
                break;
            case R.id.expanded:
                showExpandedlayoutNotification();
                break;
            case R.id.custom:
                showCustomLayoutNotification();
                break;
            default:
                break;
        }
    }
    public void showExpandedlayoutNotification(){}
    NotificationCompat.Builder mBuilder = createNotification();
    NotificationCompat.InboxStyle inboxStyle = new android.support.v4.app.NotificationCompat.InboxStyle();
    inboxStyle.setBigContentTitle("Event tracker details:");
    inboxStyle.setSummaryText("Events summary");





    private NotificationCompat.Builder createNotification() {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(icon)
                .setContentTitle("StatusBar Title")
                .setContentText("StatusBar subTitle")
                .setSmallIcon(R.mipmap.ic_launcher*//*스와이프 전 아이콘*//*)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }
        return builder;


    }*/
}
