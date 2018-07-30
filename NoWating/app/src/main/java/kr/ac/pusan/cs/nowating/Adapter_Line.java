package kr.ac.pusan.cs.nowating;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kr.ac.pusan.cs.nowating.Object.Obj_AdminAccount;
import kr.ac.pusan.cs.nowating.Object.Obj_Line;
import kr.ac.pusan.cs.nowating.Object.Obj_User;

public class Adapter_Line extends RecyclerView.Adapter<Adapter_Line.ViewHolder> {
    private Activity activity;
    private ArrayList<Obj_Line> dataList;
    private ArrayList<Integer> userList;
    private Obj_AdminAccount adminAccount;
    public Adapter_Line(Activity activity, List<Obj_Line> dataList, List<Integer> userList, Obj_AdminAccount adminAccount) {

        this.activity = (Activity) activity;
        this.dataList = (ArrayList<Obj_Line>) dataList;
        this.userList = (ArrayList<Integer>) userList;
        this.adminAccount = adminAccount;
    }

    @Override
    public int getItemCount() {
        //System.out.println("테스트"+dataList.size());
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView lineName;
        TextView openTime;
        TextView closeTime;
        TextView waiting;


        public ViewHolder(final View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            lineName = (TextView) itemView.findViewById(R.id.lineName);
            openTime = (TextView) itemView.findViewById(R.id.openTime);
            closeTime = (TextView) itemView.findViewById(R.id.closeTime);
            waiting = (TextView) itemView.findViewById(R.id.users);
            //price = (TextView) itemView.findViewById(R.id.price);
            //pubdate = (TextView) itemView.findViewById(R.id.pubdate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(activity, "click " + dataList.get(getAdapterPosition()).Line_Name, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), Activity_LineInfo.class);
                    intent.putExtra("line", dataList.get(getAdapterPosition()));
                    intent.putExtra("admin", adminAccount);
                    view.getContext().startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(activity, "remove " + dataList.get(getAdapterPosition()).Line_Name, Toast.LENGTH_SHORT).show();
                    //removeItem(getAdapterPosition());
                    return false;
                }
            });
        }
    }

    public Adapter_Line.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_line, parent, false);
        Adapter_Line.ViewHolder viewHolder = new Adapter_Line.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final Adapter_Line.ViewHolder holder, final int position) {
        final Obj_Line tmpData = dataList.get(position);
        final Bitmap[] bitmap = new Bitmap[1];
        holder.lineName.setText(tmpData.Line_Name);
        holder.openTime.setText(tmpData.Opening_Time);
        holder.closeTime.setText(tmpData.Closing_Time);
        holder.waiting.setText("대기 인원: "+userList.get(position));
        holder.image.setImageResource(R.drawable.open);
    }

    private void removeItem(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataList.size()); // 지워진 만큼 다시 채워넣기.
    }
}
