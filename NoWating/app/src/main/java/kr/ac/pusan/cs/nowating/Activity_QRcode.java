package kr.ac.pusan.cs.nowating;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.text.SimpleDateFormat;
import java.util.Date;

import kr.ac.pusan.cs.nowating.DB.DB_User;
import kr.ac.pusan.cs.nowating.Object.Obj_AdminAccount;
import kr.ac.pusan.cs.nowating.Object.Obj_Line;
import kr.ac.pusan.cs.nowating.Object.Obj_User;

public class Activity_QRcode extends AppCompatActivity {
    private ImageView qrcodeImg;
    private Button cancelBtn;

    private Obj_Line line;
    private Obj_AdminAccount adminAccount;
    Date mDate;
    long mNow;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    FirebaseDatabase database;
    DatabaseReference mRef;
    Obj_User user;
    DB_User DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__qrcode);
        qrcodeImg = findViewById(R.id.qrcode_img);
        cancelBtn = findViewById(R.id.cancel_btn);
        Intent intent = getIntent();

        line = (Obj_Line)intent.getSerializableExtra("line");
        user = (Obj_User)intent.getSerializableExtra("user");
        adminAccount = (Obj_AdminAccount) intent.getSerializableExtra("admin");


        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        //userIDs = new ArrayList<String>();
        DB = new DB_User(getApplicationContext(), "USER", null, 1);
        generateRQCode(DB.get(line.Line_Name,2));

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef.child("Line List").child(adminAccount.Admin_Public_ID).child(line.Line_Name).child("USER LIST").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String key = null;
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Obj_User tmp = snapshot.getValue(Obj_User.class);
                            if(tmp.User_ID.equals(DB.get(line.Line_Name,2))){
                                key = snapshot.getKey();
                                DB.delete(line.Line_Name);
                                mRef.child("Line List").child(adminAccount.Admin_Public_ID).child(line.Line_Name).child("USER LIST").child(key).setValue(null);
                                mRef.child("Line List").child(adminAccount.Admin_Public_ID).child(line.Line_Name).child("INFO").child("Current_Enrollment_State").setValue(line.Current_Enrollment_State-1);
                                break;
                            }

                        }
                        //Toast.makeText(Activity_QRactivity.this,DB.get(line_Name,2),Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //line.Current_Enrollment_State--;
                Toast.makeText(Activity_QRcode.this,"취소되었습니다.",Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }

    public void generateRQCode(String contents){
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try{
            Bitmap bitmap = toBitmap(qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE,800,800));
            qrcodeImg.setImageBitmap(bitmap);
        }catch(WriterException e){
            e.printStackTrace();
        }
    }
    public static Bitmap toBitmap(BitMatrix matrix){
        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                bmp.setPixel(x,y, matrix.get(x,y)? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

}
