package kr.ac.pusan.cs.nowating.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kr.ac.pusan.cs.nowating.Activity_Main;
import kr.ac.pusan.cs.nowating.Activity_Search;
import kr.ac.pusan.cs.nowating.Adapter_Recycler;
import kr.ac.pusan.cs.nowating.Object.Obj_AdminAccount;
import kr.ac.pusan.cs.nowating.Object.Obj_Line;
import kr.ac.pusan.cs.nowating.Object.Obj_LineInfo;
import kr.ac.pusan.cs.nowating.R;

import static android.app.Activity.RESULT_OK;


public class HomeFragment extends Fragment {
    View view;
    FirebaseDatabase database;
    DatabaseReference mRef;
    RecyclerView rcv;
    Adapter_Recycler rcvAdapter;
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private List<Obj_AdminAccount> lineList;
    private ArrayList<Obj_AdminAccount> arraylist;
    private static final int SEARCHTITLE = 111;
    private final String PERSISTENT_VARIABLE_BUNDLE_KEY = "persistentVariable";
    int tabIndex;
    Activity_Main activity = (Activity_Main)getActivity();
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        lineList = new ArrayList<Obj_AdminAccount>();
        arraylist=new ArrayList<Obj_AdminAccount>();

        //setHasOptionsMenu(true);
        //rcv.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        view=inflater.inflate(R.layout.fragment_home,null);
        rcv = (RecyclerView)view.findViewById(R.id.recycler_view);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        rcv.setLayoutManager(linearLayoutManager);

        //setHasOptionsMenu(true);

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
                    collapsingToolbarLayout.setTitle("Main Menu");
                    collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER_HORIZONTAL);
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" 등록된 가게들... ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingButtonClick(v);
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingButtonClick(v);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingButtonClick(v);
            }
        });

        if(lineList.size()==0){
            getDB();
        }else{
            rcvAdapter = new Adapter_Recycler(activity, lineList);
            rcv.setAdapter(rcvAdapter);
        }

        return view;
    }


    public void getDB(){
        mRef.child("Admin_Account").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lineList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                Obj_AdminAccount tmp = snapshot.getValue(Obj_AdminAccount.class);
                lineList.add(tmp);
                }

                arraylist.addAll(lineList);
                rcvAdapter = new Adapter_Recycler(activity, lineList);
                rcv.setAdapter(rcvAdapter);
                //loading.progressOFF();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(view.getContext(), "데이터 베이스 연동이 불가합니다.", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void floatingButtonClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                //anim();
                Toast.makeText(view.getContext(), "검색", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), Activity_Search.class);
                intent.putExtra("list", (Serializable) arraylist);
                startActivityForResult(intent,SEARCHTITLE);
                break;
            case R.id.fab1:
                anim();
                //Intent intent = new Intent(Activity_RealMain.this, Class_SearchActivity.class);
                //intent.putExtra("list", (Serializable) arraylist);
                //startActivityForResult(intent,SEARCHTITLE);
                break;
            case R.id.fab2:
                anim();
                Toast.makeText(view.getContext(), "Button2", Toast.LENGTH_SHORT).show();
                break;
        }


    }

    public void anim() {

        if (isFabOpen) {
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {

            switch (requestCode) {

                case SEARCHTITLE:
                    String pubID = data.getStringExtra("title");
                    System.out.println(pubID);
                    lineList.clear();
                    for (int i = 0; i < arraylist.size(); i++) {

                        if (arraylist.get(i).Admin_Public_ID.equals(pubID)) {
                            // 검색된 데이터를 리스트에 추가한다.
                            lineList.add(arraylist.get(i));
                        }
                    }
                    //rcvAdapter = new MyRecyclerAdapter(activity, bookInfoList);
                    //rcv.setAdapter(rcvAdapter);
                    rcvAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;

            }
        }

    }
   /* @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("tabIndex", tabIndex);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){      //액션바에 메뉴추가
        inflater.inflate(R.menu.actionmenu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    public boolean onOptionsItemSelected(MenuItem item){ //액션바 메뉴 선택시 이벤트
        int id=item.getItemId();
        if(id==R.id.action_search){
            Toast.makeText(view.getContext(),"검색클릭", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
