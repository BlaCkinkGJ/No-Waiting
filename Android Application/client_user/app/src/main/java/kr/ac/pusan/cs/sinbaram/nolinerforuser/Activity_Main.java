package kr.ac.pusan.cs.sinbaram.nolinerforuser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Activity_Main extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference mRef;

    private List<String> list;          // 데이터를 넣은 리스트변수
    private List<Obj_Line> listNum;
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private Adapter_Search adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<String> arraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();

        editSearch = (EditText) findViewById(R.id.editSearch);
        listView = (ListView) findViewById(R.id.listView);
        // 리스트를 생성한다.
        list = new ArrayList<String>();
        //listNum = new ArrayList<Obj_Line>();

        //리스트에 public id 저장
        mRef.child("Obj_AdminAccount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Obj_AdminAccount tmp = snapshot.getValue(Obj_AdminAccount.class);
                    list.add(tmp.Admin_Public_ID);
                }

                //Toast.makeText(Activity_Main.this, String.valueOf(list.size()), Toast.LENGTH_LONG).show();
                // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
                arraylist = new ArrayList<String>();
                arraylist.addAll(list);

                // 리스트에 연동될 아답터를 생성한다.
                adapter = new Adapter_Search(list, Activity_Main.this);

                // 리스트뷰에 아답터를 연결한다.
                listView.setAdapter(adapter);

                // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
                editSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        // input창에 문자를 입력할때마다 호출된다.
                        // search 메소드를 호출한다.
                        String text = editSearch.getText().toString();
                        search(text);
                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent(Activity_Main.this, Activity_ListMenu.class);
                        intent.putExtra("Pub_ID", list.get(position));
                        startActivity(intent);
                        //Toast.makeText(Activity_Main.this, arraylist.get(position), Toast.LENGTH_LONG).show();
                    }
                });

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < arraylist.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }
}
