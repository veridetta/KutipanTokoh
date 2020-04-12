package com.veridetta.kutipantokoh.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.veridetta.kutipantokoh.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class PalingAdapter extends RecyclerView.Adapter<PalingAdapter.MyViewHolder> {

    private ArrayList<String> textPaling = new ArrayList<>();
    private ArrayList<String> urlPaling = new ArrayList<>();
    private Activity mActivity;
    Intent intent;
    String dx;
    private Context context;
    private int lastPosition = -1;

    public PalingAdapter(ArrayList<String> textPaling,
                         ArrayList<String> urlPaling) {
        this.textPaling = textPaling;
        this.urlPaling = urlPaling;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtPaling;
        CardView cardBaru;

        public MyViewHolder(View view) {
            super(view);
            txtPaling = (TextView) view.findViewById(R.id.txt_paling_dicari);
            cardBaru = (CardView) view.findViewById(R.id.card_dicari);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_paling_dicari, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.txtPaling.setText(textPaling.get(position));
        holder.cardBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String movie ="Movie";
                SharedPreferences pref = view.getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                int pageNumber=pref.getInt("interCard", 0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return textPaling.size();
    }
}
