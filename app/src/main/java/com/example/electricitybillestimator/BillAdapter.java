package com.example.electricitybillestimator;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BillAdapter extends
        RecyclerView.Adapter<BillAdapter.MyViewHolder> {

    Context context;

    ArrayList<BillModel> billList;

    public BillAdapter(
            Context context,
            ArrayList<BillModel> billList) {

        this.context = context;

        this.billList = billList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_bill,
                                parent,
                                false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull MyViewHolder holder,
            int position) {

        BillModel model =
                billList.get(position);

        holder.tvMonth.setText(
                model.getMonth());

        holder.tvCost.setText(

                "RM " +

                        String.format("%.2f",
                                model.getFinalCost()));

        // CLICK ITEM

        holder.itemView.setOnClickListener(

                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent intent =
                                new Intent(
                                        context,
                                        DetailActivity.class);

                        intent.putExtra(
                                "id",
                                model.getId());

                        intent.putExtra(
                                "month",
                                model.getMonth());

                        intent.putExtra(
                                "unit",
                                model.getUnit());

                        intent.putExtra(
                                "rebate",
                                model.getRebate());

                        intent.putExtra(
                                "totalCharges",
                                model.getTotalCharges());

                        intent.putExtra(
                                "finalCost",
                                model.getFinalCost());

                        context.startActivity(intent);
                    }
                });
    }

    @Override
    public int getItemCount() {

        return billList.size();
    }

    public static class MyViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvMonth, tvCost;

        public MyViewHolder(
                @NonNull View itemView) {

            super(itemView);

            tvMonth =
                    itemView.findViewById(R.id.tvMonth);

            tvCost =
                    itemView.findViewById(R.id.tvCost);
        }
    }
}