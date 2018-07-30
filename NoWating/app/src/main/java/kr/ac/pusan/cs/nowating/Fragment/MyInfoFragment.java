package kr.ac.pusan.cs.nowating.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import kr.ac.pusan.cs.nowating.Activity_Main;
import kr.ac.pusan.cs.nowating.Adapter_Admin;
import kr.ac.pusan.cs.nowating.Adapter_myLine;
import kr.ac.pusan.cs.nowating.DB.DB_User;
import kr.ac.pusan.cs.nowating.Object.Obj_AdminAccount;
import kr.ac.pusan.cs.nowating.Object.Obj_Line;
import kr.ac.pusan.cs.nowating.Object.Obj_MyLine;
import kr.ac.pusan.cs.nowating.Object.Obj_User;
import kr.ac.pusan.cs.nowating.R;


public class MyInfoFragment extends Fragment {
    FirebaseDatabase database;
    DatabaseReference mRef;
    RecyclerView rcv;
    Adapter_myLine rcvAdapter;
    private DB_User DB;
    Activity_Main activity = (Activity_Main)getActivity();
    private List<Obj_MyLine> list;
    private ArrayList<Obj_MyLine> arraylist;
    View view;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        view=inflater.inflate(R.layout.fragment_my_info,null);
        rcv = (RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        rcv.setLayoutManager(linearLayoutManager);
        DB = new DB_User(view.getContext(), "USER", null, 1);
        list = new ArrayList<Obj_MyLine>();
        arraylist=new ArrayList<Obj_MyLine>();
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsingToolbarLayout);
        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("No Wating... ");
                    collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER_HORIZONTAL);
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" No Wating... ");//carefull there should a space between double quote otherwise it wont work
                    collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER_HORIZONTAL);
                    isShow = false;
                }
            }
        });
        if(list.size()==0){
            getDB();
        }else{
            //rcvAdapter = new Adapter_myLine(activity, list);
            //rcv.setAdapter(rcvAdapter);
        }
        return view;
    }
    public void getDB(){
        mRef.child("Line List").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        Obj_Line line = (Obj_Line) snapshot1.child("INFO").getValue(Obj_Line.class);
                        if(DB.get(line.Line_Name,1)!=null){
                            final Obj_MyLine myLine = new Obj_MyLine();
                            myLine.lineInfo = line;
                            mRef.child("Admin_Account").child(line.Public_ID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Obj_AdminAccount adminAccount = dataSnapshot.getValue(Obj_AdminAccount.class);
                                    myLine.Image = adminAccount.Image;
                                    myLine.Admin_Public_ID = adminAccount.Admin_Public_ID;
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            for(DataSnapshot snapshot2 : snapshot1.child("USER LIST").getChildren()){
                                Obj_User user = snapshot2.getValue(Obj_User.class);
                                if(DB.get(line.Line_Name,2).equals(user.User_ID)){
                                    myLine.user = user;
                                    //System.out.println("상태: "+ user.State);
                                    break;
                                }
                            }
                            list.add(myLine);
                        }
                    }



                }
                //System.out.println("라인 수 : " + list.size());
                arraylist.addAll(list);
                rcvAdapter = new Adapter_myLine(activity, list);
                rcv.setAdapter(rcvAdapter);
                //loading.progressOFF();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(view.getContext(), "데이터 베이스 연동이 불가합니다.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
