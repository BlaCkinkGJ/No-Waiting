package kr.ac.pusan.cs.nowating;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import kr.ac.pusan.cs.nowating.DB.DB_User;
import kr.ac.pusan.cs.nowating.Object.Obj_AdminAccount;
import kr.ac.pusan.cs.nowating.Object.Obj_Line;
import kr.ac.pusan.cs.nowating.Object.Obj_LineInfo;
import kr.ac.pusan.cs.nowating.Object.Obj_User;

public class Activity_lineList extends AppCompatActivity {
    Obj_AdminAccount adminAccount;
    private DB_User DB;
    private String User_ID;
    private List<Obj_Line> lineList;
    private List<Integer> userList;
    FirebaseDatabase database;
    DatabaseReference mRef;
    RecyclerView rcv;
    Adapter_Line rcvAdapter;
    CircularImageView imageView;
    TextView pubID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_list);

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        lineList = new ArrayList<>();
        userList = new ArrayList<>();
        Intent intent = getIntent();
        adminAccount = (Obj_AdminAccount) intent.getSerializableExtra("admin");
        rcv = findViewById(R.id.recycler_view);
        imageView = findViewById(R.id.image);
        pubID = findViewById(R.id.pubID);

        pubID.setText(adminAccount.Admin_Public_ID);
        Glide.with(this).load(adminAccount.Image).into(imageView);

        final Activity activity = this;
        mRef.child("Line List").child(adminAccount.Admin_Public_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lineList.clear();
                userList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Obj_Line line = snapshot.child("INFO").getValue(Obj_Line.class);
                    //System.out.println(line.Line_Name);
                    lineList.add(line);

                    int count=0;
                    for(DataSnapshot snapshot1 : snapshot.child("USER LIST").getChildren()){
                        System.out.println(snapshot1);
                        Obj_User user = snapshot1.getValue(Obj_User.class);
                        if(user.State.equals("wait"))count++;
                    }
                    System.out.println(count);
                    userList.add(count);
                }


                LinearLayoutManager manager = new LinearLayoutManager(activity);
                rcv.setLayoutManager(manager);
                rcv.setHasFixedSize(true);
                rcvAdapter = new Adapter_Line(activity, lineList, userList, adminAccount);
                rcv.setAdapter(rcvAdapter);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), manager.getOrientation());
                rcv.addItemDecoration(dividerItemDecoration);
                //container.addView(view);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
