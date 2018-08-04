package kr.ac.pusan.cs.sinbaram.nolinerforadmin;

import android.Manifest;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegistActivity extends AppCompatActivity {
    Button btnRegist;
    Button btnCancel;
    EditText admin_ID;
    EditText admin_Pwd;
    EditText public_ID;
    ImageView image;
    Button btn1;
    FirebaseDatabase database;
    DatabaseReference mRef;
    FirebaseStorage storage;
    private static final int GALLERY_CODE1 = 111;
    String photoPath;

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
        storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        test = findViewById(R.id.textView);
        image = findViewById(R.id.image);
        /*권한*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }


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

        final InputFilter filterAlphaNum = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
                if(!ps.matcher(source).matches()) {
                    //Toast.makeText(getApplicationContext(),"영문, 숫자만 입력 가능합니다." , Toast.LENGTH_SHORT).show();
                    return "";
                }
                return null;
            }
        };

        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (admin_ID.getText().toString().isEmpty() || admin_Pwd.getText().toString().isEmpty() || public_ID.getText().toString().isEmpty()) {
                    Toast.makeText(RegistActivity.this, "빈칸을 채우세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                EditText editAdminID=findViewById(R.id.AdminID);
                editAdminID.setFilters(new InputFilter[]{filterAlphaNum});

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

                    //DB upload
                    final Admin_Account newAccount = new Admin_Account();
                    newAccount.Admin_Private_ID = admin_ID.getText().toString();
                    newAccount.Admin_Private_password = admin_Pwd.getText().toString();
                    newAccount.Admin_Public_ID = public_ID.getText().toString();
                    final StorageReference storageRef = storage.getReference();
                    Uri file = Uri.fromFile(new File((String) photoPath));
                    StorageReference riversRef = storageRef.child("Admin/" + newAccount.Admin_Public_ID + "/" +  file.getLastPathSegment());
                    UploadTask uploadTask = riversRef.putFile(file);
                    final String[] stringuri = new String[1];
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VIsibleForTests")
                            Uri downloadUri = taskSnapshot.getDownloadUrl();
                            System.out.println("uri: "+downloadUri.toString());

                            newAccount.Image = downloadUri.toString();
                            mRef.child("Admin_Account").child(newAccount.Admin_Public_ID).setValue(newAccount);

                            Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
                            //loading.progressOFF();
                            finish();
                        }
                    });



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
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(1);
            }
        });
    }
    void show(final int imageNum) {
        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //sendTakePhotoIntent(imageNum);
            }
        };
        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadImagefromGallery(imageNum);
            }
        };
        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
        new AlertDialog.Builder(this).setTitle("업로드할 이미지 선택")
                .setNegativeButton("사진촬영", cameraListener)
                .setPositiveButton("앨범선택", albumListener)
                .setNeutralButton("취소", cancelListener)
                .show();
    }
    public void loadImagefromGallery(int imageNum) {
        //Intent 생성
        Intent intent = new Intent(Intent.ACTION_PICK); //ACTION_PIC과 차이점?
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);

        if(imageNum==1)startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_CODE1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == GALLERY_CODE1) {

            super.onActivityResult(requestCode, resultCode, data);
            try {
                //이미지를 하나 골랐을때
                if (resultCode == RESULT_OK && null != data) {
                    //data에서 절대경로로 이미지를 가져옴
                    photoPath = getPath(data.getData());
                    System.out.println(photoPath);
                    if (requestCode == GALLERY_CODE1) {
                        //((ImageView) findViewById(R.id.image1)).setImageBitmap(scaled);
                        image.setImageURI(Uri.fromFile(new File(photoPath)));


                    } else {
                        Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
                    }

                }
            } catch (Exception e) {
                Toast.makeText(this, "Oops! 로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
    public String getPath(Uri uri){

        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }

    /*public void fileUpload() {

        final Obj_book_info registObj = new Obj_book_info();
        registObj.init();

        registObj.book = objbook;
        registObj.userID = userID;
        registObj.description = description.getText().toString();
        final StorageReference storageRef = storage.getReference();
        final ArrayList downloadUriList = new ArrayList();
        for (int i = 0; i < 3; i++) {
            Uri file = Uri.fromFile(new File((String) uriList.get(i)));
            StorageReference riversRef = storageRef.child("book_images/" + userID + "/" + objbook.title + "/" + file.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(file);


            final String[] stringuri = new String[1];
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VIsibleForTests")
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    System.out.println("uri: "+downloadUri.toString());
                    downloadUriList.add(downloadUri.toString());
                    if(downloadUriList.size()==3) {
                        registObj.imageURI.imageUri1 = downloadUriList.get(0).toString();

                        mRef.child("Book").child(userID).push().setValue(registObj);
                        Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
                        loading.progressOFF();
                        finish();
                    }
                }

            });

        }
    }
    private void startProgress() {
        loading = new Loading_Application();
        loading.progressON(this,"Uploading...");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //loading.progressOFF();
            }
        }, 3500);

    }*/


}
