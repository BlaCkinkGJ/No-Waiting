package kr.ac.pusan.cs.sinbaram.nolinerforuser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends BaseAdapter {
    FirebaseDatabase database;
    DatabaseReference mRef;
    private Context context;
    private List<String> list;
    private LayoutInflater inflate;
    private ViewHolder viewHolder;
    public SearchAdapter(List<String> list, Context context){
        this.list = list;
        this.context = context;
        this.inflate = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        if(convertView == null){
            convertView = inflate.inflate(R.layout.item_public_id,null);

            viewHolder = new ViewHolder();
            viewHolder.label = (TextView) convertView.findViewById(R.id.label);
            viewHolder.num = (TextView) convertView.findViewById(R.id.lineNum);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // 리스트에 있는 데이터를 리스트뷰 셀에 뿌린다.
        viewHolder.label.setText(list.get(position));
        //viewHolder.num.setText(String.valueOf(listNum.size()));
        /*mRef.child("Line List").child(list.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count=0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Line line = (Line)snapshot.child("INFO").getValue(Line.class);
                    count++;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


        return convertView;
    }

    static class ViewHolder{
        public TextView label;
        public TextView num;
    }

}
