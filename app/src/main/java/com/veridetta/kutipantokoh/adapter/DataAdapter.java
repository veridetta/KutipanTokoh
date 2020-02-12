package com.veridetta.kutipantokoh.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.veridetta.kutipantokoh.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    private ArrayList<String> tokohList = new ArrayList<>();
    private ArrayList<String> urlList = new ArrayList<>();
    private ArrayList<String> gambarList = new ArrayList<>();
    private ArrayList<String> tahunList = new ArrayList<>();
    private Activity mActivity;
    Intent intent;
    String dx;
    private Context context;
    private int lastPosition = -1;

    public DataAdapter(ArrayList<String> tokohList,
                       ArrayList<String> urlList,
                       ArrayList<String> tahunList,
                       ArrayList<String> gambarList) {

        this.tokohList = tokohList;
        this.urlList = urlList;
        this.gambarList = gambarList;
        this.tahunList = tahunList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtTokoh, txtTahun, txtNamaD;
        ImageView gambarTokoh;
        CardView cardBaru;

        public MyViewHolder(View view) {
            super(view);
            txtTokoh = (TextView) view.findViewById(R.id.nama_satu);
            txtNamaD = (TextView) view.findViewById(R.id.nama_dua);
            txtTahun = (TextView) view.findViewById(R.id.txt_tahun);
            cardBaru = (CardView) view.findViewById(R.id.tokoh_list);
            gambarTokoh = view.findViewById(R.id.foto_toko);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_home, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String tokoh[] = tokohList.get(position).split(" ");
        String tokohD = tokohList.get(position).replace(tokoh[0]+" ","");
        holder.txtTokoh.setText(tokoh[0]);
        holder.txtNamaD.setText(tokohD);
        holder.txtTahun.setText(tahunList.get(position));
        Glide.with(holder.gambarTokoh.getContext())
                .load(Uri.parse(gambarList.get(position)))
                .into(holder.gambarTokoh);
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
        return tokohList.size();
    }
}
