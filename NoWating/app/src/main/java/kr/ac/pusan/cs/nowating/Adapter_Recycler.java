package kr.ac.pusan.cs.nowating;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kr.ac.pusan.cs.nowating.Object.Obj_AdminAccount;
import kr.ac.pusan.cs.nowating.Object.Obj_Line;

public class Adapter_Recycler extends RecyclerView.Adapter<Adapter_Recycler.ViewHolder> {
    private Activity activity;
    private ArrayList<Obj_AdminAccount> dataList;
    //private ArrayList<Bitmap> imageList;
    public Adapter_Recycler(Activity activity, List<Obj_AdminAccount> dataList) {
        this.activity = (Activity) activity;
        this.dataList = (ArrayList<Obj_AdminAccount>) dataList;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView lineName;
        TextView maxNum;
        TextView enroll;
        //TextView price;
        //TextView pubdate;

        public ViewHolder(final View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            lineName = (TextView) itemView.findViewById(R.id.lineName);
            maxNum = (TextView) itemView.findViewById(R.id.maxNum);
            enroll = (TextView) itemView.findViewById(R.id.enroll);
            //price = (TextView) itemView.findViewById(R.id.price);
            //pubdate = (TextView) itemView.findViewById(R.id.pubdate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(activity, "click " + dataList.get(getAdapterPosition()).title, Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(activity, Regist_Book_Info.class);
                    //intent.putExtra("book", (Serializable) dataList.get(getAdapterPosition()));
                    //intent.putExtra("image",imageList.get(getAdapterPosition()));
                    //activity.startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(activity, "remove " + dataList.get(getAdapterPosition()).Admin_Public_ID, Toast.LENGTH_SHORT).show();
                    removeItem(getAdapterPosition());
                    return false;
                }
            });
        }
    }

    public Adapter_Recycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        Adapter_Recycler.ViewHolder viewHolder = new Adapter_Recycler.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final Adapter_Recycler.ViewHolder holder, final int position) {
        final Obj_AdminAccount tmpData = dataList.get(position);
        final Bitmap[] bitmap = new Bitmap[1];
        holder.lineName.setText(tmpData.Admin_Public_ID);
        holder.maxNum.setText("Description");


    }

    private void removeItem(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataList.size()); // 지워진 만큼 다시 채워넣기.
    }
}
