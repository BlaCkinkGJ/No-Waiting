package kr.ac.pusan.cs.nowating;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kr.ac.pusan.cs.nowating.Object.Obj_AdminAccount;
import kr.ac.pusan.cs.nowating.Object.Obj_MyLine;

public class Adapter_myLine  extends RecyclerView.Adapter<Adapter_myLine.ViewHolder> {
    private Activity activity;
    private ArrayList<Obj_MyLine> dataList;
    //private ArrayList<Bitmap> imageList;
    public Adapter_myLine(Activity activity, List<Obj_MyLine> dataList) {
        this.activity = (Activity) activity;
        this.dataList = (ArrayList<Obj_MyLine>) dataList;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //ImageView image;
        CircularImageView image;
        TextView pubName;
        TextView lineName;
        TextView state;
        //TextView price;
        //TextView pubdate;

        public ViewHolder(final View itemView) {
            super(itemView);
            //image = (ImageView) itemView.findViewById(R.id.image);
            image = (CircularImageView)itemView.findViewById(R.id.image);
            /*image.setBorderColor(activity.getResources().getColor(R.color.grey_50));*/
            image.setBorderWidth(10);
            image.addShadow();
            pubName = (TextView) itemView.findViewById(R.id.pubID);
            lineName = (TextView) itemView.findViewById(R.id.lineName);
            state = (TextView) itemView.findViewById(R.id.state);
            //price = (TextView) itemView.findViewById(R.id.price);
            //pubdate = (TextView) itemView.findViewById(R.id.pubdate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "click " + dataList.get(getAdapterPosition()).Admin_Public_ID, Toast.LENGTH_SHORT).show();

                    //Intent intent = new Intent(view.getContext(), Activity_lineList.class);
                    //intent.putExtra("admin", (Serializable) dataList.get(getAdapterPosition()));

                    //view.getContext().startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(view.getContext(), "remove " + dataList.get(getAdapterPosition()).Admin_Public_ID, Toast.LENGTH_SHORT).show();
                    //removeItem(getAdapterPosition());
                    return false;
                }
            });
        }
    }

    public Adapter_myLine.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myline, parent, false);
        Adapter_myLine.ViewHolder viewHolder = new Adapter_myLine.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final Adapter_myLine.ViewHolder holder, final int position) {
        final Obj_MyLine tmpData = dataList.get(position);
        final Bitmap[] bitmap = new Bitmap[1];
        holder.pubName.setText(tmpData.Admin_Public_ID);
        holder.lineName.setText(tmpData.lineInfo.Line_Name);
        holder.state.setText(tmpData.user.State);

        Glide.with(holder.itemView.getContext()).load(tmpData.Image).into(holder.image);

    }

    private void removeItem(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataList.size()); // 지워진 만큼 다시 채워넣기.
    }
}
