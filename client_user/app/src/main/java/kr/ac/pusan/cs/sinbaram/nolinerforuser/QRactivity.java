package kr.ac.pusan.cs.sinbaram.nolinerforuser;

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

import kr.ac.pusan.cs.sinbaram.nolinerforuser.DB.DB_User;

import static java.lang.String.valueOf;

public class QRactivity extends AppCompatActivity {
    private ImageView qrcodeImg;
    private Button cancelBtn;
    private String User_ID;
    private String line_Name;
    private String public_ID;
    private Line line;
    Date mDate;
    long mNow;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    FirebaseDatabase database;
    DatabaseReference mRef;
    DB_User DB;
    //private List<String> userIDs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qractivity);
        qrcodeImg = findViewById(R.id.qrcode_img);
        cancelBtn = findViewById(R.id.cancel_btn);
        Intent intent = getIntent();
        User_ID = intent.getStringExtra("userID");
        line_Name = intent.getStringExtra("lineName");
        public_ID = intent.getStringExtra("pubID");
        line = (Line)intent.getSerializableExtra("line");
        RegistUser registUser = (RegistUser)intent.getSerializableExtra("regist");

        generateRQCode(User_ID);
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        //userIDs = new ArrayList<String>();
        DB = new DB_User(getApplicationContext(), "USER", null, 1);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef.child("Line List").child(public_ID).child(line.Line_Name).child("USER LIST").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String key = null;
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            RegistUser tmp = new RegistUser();
                            tmp = snapshot.getValue(RegistUser.class);
                            if(tmp.User_ID.equals(User_ID)){
                                key = snapshot.getKey();
                            }

                        }
                        //Toast.makeText(QRactivity.this,DB.get(line_Name,2),Toast.LENGTH_LONG).show();
                        if(key != null) {
                            DB.delete(line_Name);
                            mRef.child("Line List").child(public_ID).child(line_Name).child("USER LIST").child(key).setValue(null);
                            mRef.child("Line List").child(public_ID).child(line_Name).child("INFO").child("Current_Enrollment_State").setValue(line.Current_Enrollment_State - 1);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //line.Current_Enrollment_State--;
                Toast.makeText(QRactivity.this,"취소되었습니다.",Toast.LENGTH_LONG).show();
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
