package kr.ac.pusan.cs.sinbaram.nolinerforuser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListMenu extends AppCompatActivity {
    private String Public_ID;

    FirebaseDatabase database;
    DatabaseReference mRef;

    private ListView listView;
    private ListViewAdapter adapter;
    private List<Line> lineList;
    private TextView PubID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Intent intent = getIntent();
        Public_ID = intent.getStringExtra("Pub_ID");
        PubID = findViewById(R.id.pubID);
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        lineList = new ArrayList<>();
        listView = findViewById(R.id.listview);
        PubID.setText(Public_ID);
        mRef.child("Line List").child(Public_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lineList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Line tmp = snapshot.child("INFO").getValue(Line.class);
                    lineList.add(tmp);
                }

                adapter = new ListViewAdapter(lineList,ListMenu.this);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent(ListMenu.this, LineInfo.class);
                        intent.putExtra("line", lineList.get(position));
                        intent.putExtra("pubID",Public_ID);
                        startActivity(intent);
                        finish();
                        //Toast.makeText(MainActivity.this, arraylist.get(position), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }
}
