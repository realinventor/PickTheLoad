package co.realinventor.picktheload.Admin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import co.realinventor.picktheload.Common.Constants;
import co.realinventor.picktheload.Common.LoadPost;
import co.realinventor.picktheload.R;

public class PostLoadAdapter extends RecyclerView.Adapter<PostLoadAdapter.MyViewHolder> {
    private List<LoadPost> loadPostList;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    public PostLoadAdapter(List<LoadPost> loadPostList){
        this.loadPostList = loadPostList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, locationTo, locationFrom, capacity, goodsType, truckType, expPrice, phone, date;
        public Button approveButton, removeButton;

        public MyViewHolder(View view) {
            super(view);
            nameTextView = (TextView) view.findViewById(R.id.nameTextView);
            locationTo = (TextView) view.findViewById(R.id.locationTo);
            locationFrom = (TextView) view.findViewById(R.id.locationFrom);
            capacity = (TextView) view.findViewById(R.id.capacity);
            goodsType = (TextView) view.findViewById(R.id.goodsType);
            truckType = (TextView) view.findViewById(R.id.truckType);
            expPrice = (TextView) view.findViewById(R.id.expPrice);
            phone = (TextView) view.findViewById(R.id.phone);
            date = (TextView) view.findViewById(R.id.date);
            approveButton = (Button) view.findViewById(R.id.approveButton);
            removeButton = (Button) view.findViewById(R.id.removeButton);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_load_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final LoadPost loadPost = loadPostList.get(position);
        holder.nameTextView.setText("Name : "+loadPost.getName());
        holder.locationTo.setText("To : "+loadPost.getLocation_to());
        holder.locationFrom.setText("From : "+loadPost.getLocation_from());
        holder.date.setText("Date : "+loadPost.getDate());
        holder.phone.setText("Phone No. : "+loadPost.getPhone());
        holder.expPrice.setText("Expected Price : "+loadPost.getExpected_price());
        holder.truckType.setText("Truck Type : "+loadPost.getTruck_type());
        holder.goodsType.setText("Goods Type : "+loadPost.getGoods_type());
        holder.capacity.setText("Capacity : "+loadPost.getCapacity());

        if(Constants.CURRENT_USER.equals(Constants.ADMIN)) {
            holder.approveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //approve the request
                    Log.d("Approve button", "position " + position);
                    ref.child("Post_Load").child(loadPost.getUnique_id()).child("approved").setValue("yes");
                    loadPostList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, loadPostList.size());

                }
            });

            holder.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Remove the request from database
                    ref.child("Post_Load").child(loadPost.getUnique_id()).removeValue();
                    loadPostList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, loadPostList.size());

                }
            });
        }
        else if(Constants.CURRENT_USER.equals(Constants.DRIVER)){
            holder.approveButton.setVisibility(View.GONE);
            holder.removeButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return loadPostList.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
