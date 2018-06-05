package kr.ac.pusan.cs.sinbaram.nolinerforadmin;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserStateList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String Pub_ID;
    private String Line_Name;
    FirebaseDatabase database;
    DatabaseReference mRef;
    private List<User> userList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_state_list);
        Intent intent = getIntent();
        Pub_ID = intent.getStringExtra("Pub_ID");
        Line_Name = intent.getStringExtra("Line_Name");

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final BoardRecyclerViewAdater boardRecyclerViewAdater = new BoardRecyclerViewAdater();
        recyclerView.setAdapter(boardRecyclerViewAdater);
 /*       DB_User inputUser = new DB_User();
        inputUser.User_ID = "test01";
        inputUser.User_Password = "pwd01";
        inputUser.User_State = "wait";
        mRef.child("Line List").child(keyValue).child("Current_Enrollment_List").push().setValue(inputUser);*/

        //Toast.makeText(UserStateList.this, Pub_ID+" "+Line_Name, Toast.LENGTH_LONG).show();

        mRef.child("Line List").child(Pub_ID).child(Line_Name).child("USER_LIST").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    userList.add(user);
                }
                boardRecyclerViewAdater.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    class BoardRecyclerViewAdater extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);

            return new UserStateList.BoardRecyclerViewAdater.CustomViewHolder(view);
        }
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position){
            ((CustomViewHolder)holder).count.setText(String.valueOf(position+1));
            ((CustomViewHolder)holder).userId.setText(userList.get(position).User_ID);
            ((CustomViewHolder)holder).userState.setText(userList.get(position).User_State);

        }
        @Override
        public  int getItemCount(){
            return userList.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            TextView count;
            TextView userId;
            TextView userState;

            public CustomViewHolder(View view) {
                super(view);
                count = (TextView) view.findViewById(R.id.count);
                userId = (TextView) view.findViewById(R.id.user_id);
                userState = (TextView) view.findViewById(R.id.user_state);
            }
        }
    }

}
