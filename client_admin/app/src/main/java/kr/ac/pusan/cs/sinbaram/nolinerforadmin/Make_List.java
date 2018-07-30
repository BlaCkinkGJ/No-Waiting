package kr.ac.pusan.cs.sinbaram.nolinerforadmin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.ParseException;
import java.util.Date;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kr.ac.pusan.cs.sinbaram.nolinerforadmin.RSA.RSA;

import static java.lang.String.valueOf;

public class Make_List extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference mRef;
    final int OpenDate = 1;
    final int OpenTime = 3;
    final int CloseDate = 2;
    final int CloseTime = 4;
    private Button btnCancel;
    private Button btnRegister;
    private Button btnOpen;
    private Button btnClose;
    private Button btnOpenTime;
    private Button btnCloseTime;
    //private Calendar calendar;
    private EditText lineName;
    private EditText closeTime;
    private EditText openTime;
    private EditText interTime;
    private TextView opentxt;
    private TextView opentimetxt;
    private TextView closetxt;
    private TextView closetimetxt;
    private EditText maxNum;
    private String public_id;
    private int openyear, openmonth, openday;
    private int closeyear, closemonth, closeday;
    private String strOpenTime;
    private String strCloseTime;
    private String strOpenDate;
    private String strCloseDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    private List lineList = new ArrayList();
    Admin_Account auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make__list);
        //calendar = Calendar.getInstance();
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnOpen = findViewById(R.id.openBtn);
        btnClose = findViewById(R.id.closeBtn);
        opentxt = findViewById(R.id.opentxt);
        closetxt = findViewById(R.id.closetxt);
        btnCloseTime = findViewById(R.id.closetimebtn);
        btnOpenTime = findViewById(R.id.opentimebtn);
        opentimetxt = findViewById(R.id.opentimetxt);
        closetimetxt = findViewById(R.id.closetimetxt);
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        Intent intent = getIntent();
        auth = (Admin_Account) intent.getSerializableExtra("DB_Admin");

        lineName = (EditText) findViewById(R.id.lineName);

        interTime = (EditText)findViewById(R.id.intervalTime);
        maxNum = (EditText)findViewById(R.id.maxNum);
        public_id = auth.Admin_Public_ID;



        mRef.child("Line List").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lineList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Line line = snapshot.getValue(Line.class);
                    lineList.add(line);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(OpenDate);
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(CloseDate);
            }
        });
        btnOpenTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(OpenTime);
            }
        });
        btnOpenTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(OpenTime);
            }
        });
        btnCloseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(CloseTime);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String line_name = lineName.getText().toString();

                String inter_time = interTime.getText().toString();
                String max_nums = maxNum.getText().toString();
                Date date1=null;
                Date date2 = null;

                try {
                    date1 = mFormat.parse(valueOf(strCloseDate+" "+strCloseTime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    date2 = mFormat.parse(valueOf(strOpenDate+" "+ strOpenTime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                /*if(!date2.after(date1)){
                    Toast.makeText(Make_List.this,"시간을 다시 설정하세요.",Toast.LENGTH_LONG).show();
                    return;
                }*/


                if(line_name.isEmpty()||strOpenTime.isEmpty()||strCloseTime.isEmpty()||inter_time.isEmpty()||max_nums.isEmpty()||strOpenDate.isEmpty()||strCloseDate.isEmpty()){
                    Toast.makeText(Make_List.this,"빈칸을 채우세요.",Toast.LENGTH_LONG).show();
                    return;
                }
                for(int i=0;i<lineList.size();i++) {
                    Line line = (Line) lineList.get(i);
                    if (line_name.equals(line.Line_Name)&&public_id.equals(line.Public_Key)) {
                        Toast.makeText(Make_List.this, "이미 있는 줄 이름입니다.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                RSA rsa = null;
                try {
                    rsa = new RSA();
                    rsa.init();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                    int max_num = Integer.parseInt(max_nums);
                    Line line = new Line();
                    line.Public_Key = rsa.getPublicKey();
                    line.Line_Name = line_name;
                    line.Max_Number = max_num;
                    line.Closing_Time = strCloseDate+" "+strCloseTime;
                    line.Opening_Time = strOpenDate+" "+ strOpenTime;
                    line.Personal_Interval = inter_time;
                    line.Current_Enrollment_State = 0;
                    line.Public_ID = public_id;

                mRef.child("Line List").child(public_id).child(line.Line_Name).child("INFO").setValue(line);
                mRef.child("Line List").child(public_id).child(line.Line_Name).child("ADMIN_ID").setValue(auth.Admin_Private_ID);
                Toast.makeText(Make_List.this,"Success",Toast.LENGTH_LONG).show();

                    finish();

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        switch(id){
            case OpenDate :
                DatePickerDialog dpd = new DatePickerDialog
                        (Make_List.this, // 현재화면의 제어권자
                                new DatePickerDialog.OnDateSetListener() {
                                    public void onDateSet(DatePicker view,
                                                          int year, int monthOfYear,int dayOfMonth) {
                                        openyear=year;
                                        openmonth=monthOfYear+1;
                                        openday=dayOfMonth;
                                        opentxt.setText(openyear+". "+openmonth+". "+openday);
                                        String strmonth,strday;
                                        if(openmonth<10)strmonth = "0"+ valueOf(openmonth);
                                        else strmonth = valueOf(openmonth);
                                        if(openday<10)strday = "0"+ valueOf(openday);
                                        else strday = valueOf(openday);

                                        strOpenDate= valueOf(openyear)+"-"+strmonth+"-"+strday;

                                    }
                                }
                                , // 사용자가 날짜설정 후 다이얼로그 빠져나올때
                                //    호출할 리스너 등록
                                2018, 5, 21); // 기본값 연월일
                return dpd;
            case CloseDate :
                DatePickerDialog dpd2 = new DatePickerDialog
                        (Make_List.this, // 현재화면의 제어권자
                                new DatePickerDialog.OnDateSetListener() {
                                    public void onDateSet(DatePicker view,
                                                          int year, int monthOfYear,int dayOfMonth) {
                                        closeyear=year;
                                        closemonth=monthOfYear+1;
                                        closeday=dayOfMonth;
                                        closetxt.setText(closeyear+". "+closemonth+". "+closeday);
                                        String strmonth,strday;
                                        if(closemonth<10)strmonth = "0"+ valueOf(closemonth);
                                        else strmonth = valueOf(closemonth);
                                        if(closeday<10)strday = "0"+ valueOf(closeday);
                                        else strday = valueOf(closeday);

                                        strCloseDate= valueOf(closeyear)+"-"+strmonth+"-"+strday;


                                    }
                                }
                                , // 사용자가 날짜설정 후 다이얼로그 빠져나올때
                                //    호출할 리스너 등록
                                2018, 5, 21); // 기본값 연월일
                return dpd2;


            case OpenTime :
            TimePickerDialog tpd =
                    new TimePickerDialog(Make_List.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view,
                                                      int hourOfDay, int minute) {
                                    /*Toast.makeText(getApplicationContext(),
                                            hourOfDay +"시 " + minute+"분 을 선택했습니다",
                                            Toast.LENGTH_SHORT).show();*/
                                    String strhour,strmin;
                                    if(hourOfDay<10)strhour = "0"+ valueOf(hourOfDay);
                                    else strhour = valueOf(hourOfDay);
                                    if(minute<10)strmin = "0"+ valueOf(minute);
                                    else strmin = valueOf(minute);
                                    strOpenTime =strhour + ":"+strmin;
                                    opentimetxt.setText(strOpenTime);
                                }
                            }, // 값설정시 호출될 리스너 등록
                            4,19, false); // 기본값 시분 등록
            // true : 24 시간(0~23) 표시
            // false : 오전/오후 항목이 생김
            return tpd;

        case CloseTime :
        TimePickerDialog tpd2 =
                new TimePickerDialog(Make_List.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view,
                                                  int hourOfDay, int minute) {
                                String strhour,strmin;
                                if(hourOfDay<10)strhour = "0"+ valueOf(hourOfDay);
                                else strhour = valueOf(hourOfDay);
                                if(minute<10)strmin = "0"+ valueOf(minute);
                                else strmin = valueOf(minute);
                                strCloseTime = strhour + ":"+strmin;
                                closetimetxt.setText(strCloseTime);
                            }
                        }, // 값설정시 호출될 리스너 등록
                        4,19, false); // 기본값 시분 등록
        // true : 24 시간(0~23) 표시
        // false : 오전/오후 항목이 생김
            return tpd2;
    }





        return super.onCreateDialog(id);
    }




}
