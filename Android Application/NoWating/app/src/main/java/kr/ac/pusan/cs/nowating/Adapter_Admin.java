package kr.ac.pusan.cs.nowating;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kr.ac.pusan.cs.nowating.Object.Obj_AdminAccount;

public class Adapter_Admin extends RecyclerView.Adapter<Adapter_Admin.ViewHolder> {
    private Activity activity;
    private ArrayList<Obj_AdminAccount> dataList;
    //private ArrayList<Bitmap> imageList;
    public Adapter_Admin(Activity activity, List<Obj_AdminAccount> dataList) {
        this.activity = (Activity) activity;
        this.dataList = (ArrayList<Obj_AdminAccount>) dataList;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //ImageView image;
        CircularImageView image;
        TextView pubName;
        TextView description;
        TextView enroll;
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
            description = (TextView) itemView.findViewById(R.id.description);
            enroll = (TextView) itemView.findViewById(R.id.enroll);
            //price = (TextView) itemView.findViewById(R.id.price);
            //pubdate = (TextView) itemView.findViewById(R.id.pubdate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(view.getContext(), "click " + dataList.get(getAdapterPosition()).Admin_Public_ID, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(view.getContext(), Activity_lineList.class);
                    intent.putExtra("admin", (Serializable) dataList.get(getAdapterPosition()));

                    view.getContext().startActivity(intent);
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

    public Adapter_Admin.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin, parent, false);
        Adapter_Admin.ViewHolder viewHolder = new Adapter_Admin.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Obj_AdminAccount tmpData = dataList.get(position);
        final Bitmap[] bitmap = new Bitmap[1];
        holder.pubName.setText(tmpData.Admin_Public_ID);
        holder.description.setText(tmpData.Description);

        Glide.with(holder.itemView.getContext()).load(tmpData.Image).into(holder.image);

    }

    private void removeItem(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataList.size()); // 지워진 만큼 다시 채워넣기.
    }
}
