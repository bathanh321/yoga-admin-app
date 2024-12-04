package com.example.yogaapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yogaapp.R;
import com.example.yogaapp.models.OrderModel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final List<OrderModel> orderList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public OrderAdapter(List<OrderModel> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderModel order = orderList.get(position);

        String formattedDate = order.getOrderAt() != null
                ? dateFormat.format(order.getOrderAt())
                : "";

        holder.tvOrderId.setText(order.getId());
        holder.tvUsername.setText(order.getUser().getUsername());
        holder.tvCartId.setText(order.getCartId());
        holder.tvTotal.setText(String.format("$%.2f", order.getTotal()));
        holder.tvOrderAt.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvUsername, tvCartId, tvTotal, tvOrderAt;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvCartId = itemView.findViewById(R.id.tvCartId);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvOrderAt = itemView.findViewById(R.id.tvOrderAt);
        }
    }
}
