package kr.ac.pusan.cs.sinbaram.nolinerforadmin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
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

public class Make_List extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference mRef;
    private Button btnCancel;
    private Button btnRegister;
    private Button btnOpen;
    private Button btnClose;
    private DatePicker datePicker;
    private Calendar calendar;
    private EditText lineName;
    private EditText closeTime;
    private EditText openTime;
    private EditText interTime;
    private TextView opentxt;
    private TextView closetxt;
    private EditText maxNum;
    private String public_id;
    private int openyear, openmonth, openday;
    private int closeyear, closemonth, closeday;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    private List lineList = new ArrayList();
    Admin_Account auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make__list);
        calendar = Calendar.getInstance();
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnOpen = findViewById(R.id.openBtn);
        btnClose = findViewById(R.id.closeBtn);
        opentxt = findViewById(R.id.opentxt);
        closetxt = findViewById(R.id.closetxt);
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        Intent intent = getIntent();
        auth = (Admin_Account) intent.getSerializableExtra("DB_Admin");

        lineName = (EditText) findViewById(R.id.lineName);
        closeTime = (EditText)findViewById(R.id.closeTime);
        openTime = (EditText)findViewById(R.id.openTime);
        interTime = (EditText)findViewById(R.id.intervalTime);
        maxNum = (EditText)findViewById(R.id.maxNum);
        public_id = auth.Admin_Public_ID;

        calendar = Calendar.getInstance();
        openyear = calendar.get(Calendar.YEAR);
        closeyear = calendar.get(Calendar.YEAR);
        openmonth = calendar.get(Calendar.MONTH);
        closemonth = calendar.get(Calendar.MONTH);
        openday = calendar.get(Calendar.DAY_OF_MONTH);
        closeday = calendar.get(Calendar.DAY_OF_MONTH);

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




        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String line_name = lineName.getText().toString();
                String close_time = closeTime.getText().toString();
                String open_time = openTime.getText().toString();
                String inter_time = interTime.getText().toString();
                String max_nums = maxNum.getText().toString();

                if(line_name.isEmpty()||close_time.isEmpty()||open_time.isEmpty()||inter_time.isEmpty()||max_nums.isEmpty()){
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
                    line.Closing_Time = close_time;
                    line.Opening_Time = open_time;
                    line.Personal_Interval = inter_time;
                    line.Current_Enrollment_State = 0;


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
    @SuppressWarnings("deprecation")
    public void openDate(View view) {
        showDialog(998);
        opentxt.setText(String.valueOf(openyear)+"/"+String.valueOf(openmonth+1)+"/"+String.valueOf(openday));

    }
    @SuppressWarnings("deprecation")
    public void closeDate(View view) {
        showDialog(999);
        closetxt.setText(String.valueOf(closeyear)+"/"+String.valueOf(closemonth+1)+"/"+String.valueOf(closeday));
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, openyear, openmonth, openday);
        }
        if (id == 998) {
            return new DatePickerDialog(this,
                    myDateListener, closeyear, closemonth, closeday);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    //showDate(arg1, arg2+1, arg3);
                    Toast.makeText(getApplicationContext(), String.valueOf(arg1)+"/"+String.valueOf(arg2+1)+"/"+String.valueOf(arg3),
                            Toast.LENGTH_SHORT)
                            .show();
                }
            };

}
