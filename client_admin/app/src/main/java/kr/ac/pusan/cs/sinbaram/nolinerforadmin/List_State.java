package kr.ac.pusan.cs.sinbaram.nolinerforadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class List_State extends AppCompatActivity {
    Admin_Account auth;
    FirebaseDatabase database;
    DatabaseReference mRef;
    private RecyclerView recyclerView;
    private TextView publicID;
    private TextView lineNum;
    private List<Line> lineList = new ArrayList<>();
    //private List<String> uidList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__state);

        Intent intent = getIntent();
        auth = (Admin_Account) intent.getSerializableExtra("DB_Admin");
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        publicID =  findViewById(R.id.publicIdText);
        lineNum = findViewById(R.id.lineNumText);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final BoardRecyclerViewAdater boardRecyclerViewAdater = new BoardRecyclerViewAdater();
        recyclerView.setAdapter(boardRecyclerViewAdater);
        mRef.child("Line List").child(auth.Admin_Public_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lineList.clear();
                //uidList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Line line = (Line)snapshot.child("INFO").getValue(Line.class);
                    //String uidKey = snapshot.getKey();
                    lineList.add(line);
                }
                lineNum.setText("라인 수: "+ String.valueOf(lineList.size()));
                boardRecyclerViewAdater.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        publicID.setText(auth.Admin_Public_ID);

    }

    class BoardRecyclerViewAdater extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_line,parent,false);
            
            return new CustomViewHolder(view);
        }
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position){
            ((CustomViewHolder)holder).lineName.setText(lineList.get(position).Line_Name);
            ((CustomViewHolder)holder).Time.setText(lineList.get(position).Opening_Time+" ~ "+lineList.get(position).Closing_Time);
            ((CustomViewHolder)holder).Num.setText(lineList.get(position).Current_Enrollment_State+" / "+lineList.get(position).Max_Number);
            ((CustomViewHolder)holder).btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mRef.child("Line List").child(auth.Admin_Public_ID).child(lineList.get(position).Line_Name).setValue(null);
                }
            });
            ((CustomViewHolder)holder).btnList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(List_State.this, UserStateList.class);
                    //intent.putExtra("Line", lineList.get(position));
                    intent.putExtra("Pub_ID",auth.Admin_Public_ID);
                    intent.putExtra("Line_Name", lineList.get(position).Line_Name);
                    startActivity(intent);
                }
            });
        }
        @Override
        public  int getItemCount(){
            return lineList.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            TextView lineName;
            TextView Time;
            TextView Num;
            Button btnDelete;
            Button btnList;
            public CustomViewHolder(View view) {
                super(view);
                lineName = view.findViewById(R.id.lineName);
                Time = (TextView) view.findViewById(R.id.Time);
                Num = (TextView) view.findViewById(R.id.Num);
                btnDelete = (Button)view.findViewById(R.id.Delete);
                btnList = (Button)view.findViewById(R.id.List);
            }
        }
    }
}
