package com.thuchanh.TTD_19DH111156.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Index;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.thuchanh.TTD_19DH111156.R;
import com.thuchanh.TTD_19DH111156.models.OrderFinished;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter {
    FirebaseDatabase fDatabase;
    StorageReference storageReference;
    ArrayList<OrderFinished> orderFinisheds;
    Context mContext;
    public OrderAdapter(ArrayList<OrderFinished> orderFinisheds, Context context){
        this.orderFinisheds = orderFinisheds;
        mContext = context;
    }
    @NonNull
    @Override
    public OrderFinishedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_order_finished, parent, false);
        return new OrderFinishedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        fDatabase = FirebaseDatabase.getInstance();

        OrderFinished orderFinished = orderFinisheds.get(position);
        OrderFinishedHolder holderOrder = (OrderFinishedHolder) holder;
        holderOrder.tvDate.setText(orderFinished.getOrderDate());
        holderOrder.tvPrice.setText(orderFinished.getOrderSum());
        holderOrder.tvAddress.setText("");
    }

    @Override
    public int getItemCount() {
        return orderFinisheds.size();
    }
    public class OrderFinishedHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvTitle, tvAddress, tvPrice, tvDate, tvTransportState;
        ImageView ivIcon;
        public OrderFinishedHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTransportState = itemView.findViewById(R.id.tvTransportState);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }
}
