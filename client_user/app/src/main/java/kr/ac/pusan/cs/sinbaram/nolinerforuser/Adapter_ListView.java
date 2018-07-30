package kr.ac.pusan.cs.sinbaram.nolinerforuser;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.String.*;

public class Adapter_ListView extends BaseAdapter {

    private List<Obj_Line> list = new ArrayList<>();

    Date mDate;
    long mNow;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    private LayoutInflater inflate;
    private ViewHolder viewHolder;
    private Context context;
    public Adapter_ListView(List<Obj_Line> alineList, Context context){
        this.context = context;
        this.inflate = LayoutInflater.from(context);
        this.list = alineList;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if(convertView == null){
            convertView = inflate.inflate(R.layout.item_line,null);

            viewHolder = new ViewHolder();
            viewHolder.lineName = (TextView) convertView.findViewById(R.id.lineName);
            viewHolder.Time = (TextView) convertView.findViewById(R.id.Time);
            viewHolder.Num = (TextView) convertView.findViewById(R.id.Num);
            viewHolder.State = (TextView) convertView.findViewById(R.id.State);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.lineName.setText(list.get(position).Line_Name);
        viewHolder.Time.setText(list.get(position).Opening_Time+" ~ "+list.get(position).Closing_Time);
        viewHolder.Num.setText(list.get(position).Current_Enrollment_State+"/"+list.get(position).Max_Number);
        Date date1 = null;
        try {
            date1 = mFormat.parse(valueOf(list.get(position).Closing_Time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2=null;
        try {
            date2 = mFormat.parse(getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date2.after(date1)||list.get(position).Current_Enrollment_State==list.get(position).Max_Number){
            viewHolder.State.setText("마감되었습니다.");
        }
        else{
            try {
                date1 = mFormat.parse(valueOf(list.get(position).Opening_Time));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(date2.after(date1)){
                viewHolder.State.setText("입장 중 입니다.");
            }
            else{
                viewHolder.State.setText("입장 전 입니다.");

            }
        }
        return convertView;
    }
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
    static class ViewHolder{
        public TextView lineName;
        public TextView Time;
        public TextView Num;
        public TextView State;
    }

}
