package com.veridetta.kutipantokoh.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.ClipData;
import android.content.ClipboardManager;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.target.SimpleTarget;

import com.bumptech.glide.request.transition.Transition;

import com.veridetta.kutipantokoh.R;
import com.veridetta.kutipantokoh.db.DBHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.CLIPBOARD_SERVICE;



public class
CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private ArrayList<String> tokohList = new ArrayList<>();
    private ArrayList<String> kataList = new ArrayList<>();
    private ArrayList<String> photoList = new ArrayList<>();
    private ArrayList<String> ketList = new ArrayList<>();
    private ArrayList<String> idList = new ArrayList<>();
    private Activity mActivity;
    Intent intent;
    DBHelper helper;
    int success=0, favoritStatus=0, total;
    String dx;
    private Context context;
    private int lastPosition = -1;
    Bitmap  bitmap;
    public CardAdapter(Context context,ArrayList<String> tokohList,
                       ArrayList<String> kataList,
                       ArrayList<String> photoList,
                       ArrayList<String> ketList,ArrayList<String> idList) {

        this.tokohList = tokohList;
        this.kataList = kataList;
        this.photoList = photoList;
        this.ketList = ketList;
        this.idList = idList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTokoh, txtKet, txtKata,photoTokoh;
        ImageView  copy, share, download, favB, img_img;
        CardView cardBaru;
        LinearLayout bg;
        public MyViewHolder(View view) {
            super(view);
            this.bg = view.findViewById(R.id.bg_img);
            txtTokoh = (TextView) view.findViewById(R.id.nama_tokoh);
            txtKet = (TextView) view.findViewById(R.id.keterangan_tokoh);
            txtKata = (TextView) view.findViewById(R.id.txt_kata);
            cardBaru = (CardView) view.findViewById(R.id.card_kata);
            photoTokoh = view.findViewById(R.id.nama_pp);
            copy = view.findViewById(R.id.img_copy);
            share = view.findViewById(R.id.img_share);
            download = view.findViewById(R.id.download);
            favB = view.findViewById(R.id.fav);
            img_img = view.findViewById(R.id.bg_img_img);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_kata, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.txtTokoh.setText(tokohList.get(position));
        holder.txtKet.setText(ketList.get(position));
        holder.txtKata.setText(kataList.get(position));
        String pptoko= String.valueOf(tokohList.get(position).toUpperCase().charAt(0));
        holder.photoTokoh.setText(pptoko);
        Log.d("Adapter", "onBindViewHolder: "+photoList.get(position).charAt(0));
        helper = new DBHelper(context);
        success = helper.cekFav(idList.get(position));
        if(success>0){
            Glide.with(holder.favB)
                    .load(context.getResources()
                            .getIdentifier("fav", "drawable", context.getPackageName()))
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            holder.favB.setImageDrawable(resource);
                        }
                    });
            favoritStatus=1;
        }
        holder.favB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favoritStatus>0){
                    Glide.with(holder.favB)
                            .load(context.getResources()
                                    .getIdentifier("nofav", "drawable", context.getPackageName()))
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    holder.favB.setImageDrawable(resource);
                                }
                            });
                    helper.deletDB(idList.get(position));
                    favoritStatus=0;
                }else{
                    Glide.with(holder.favB)
                            .load(context.getResources()
                                    .getIdentifier("fav", "drawable", context.getPackageName()))
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    holder.favB.setImageDrawable(resource);
                                }
                            });
                    helper.insertIntoDB(1,tokohList.get(position),
                            photoList.get(position),idList.get(position),
                            kataList.get(position),ketList.get(position),"1",
                            "","");
                    favoritStatus=1;
                }
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LinearLayout v = holder.bg;
                v.setDrawingCacheEnabled(true);
                v.buildDrawingCache(true);
                Bitmap saveBm = Bitmap.createBitmap(v.getDrawingCache());
                v.setDrawingCacheEnabled(false);
                String bitmapPath = MediaStore.Images.Media.insertImage(v.getContext().getContentResolver(),
                        saveBm,tokohList.get(position), "Sumber Aplikasi Kutipan Tokoh");
                /*Uri bitmapUri = Uri.parse(bitmapPath);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/png");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_STREAM, bitmapUri );
                v.getContext().startActivity(Intent.createChooser(intent , "Share"));*/
                Intent shareIntent;

                Uri bmpUri = Uri.parse(bitmapPath);
                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                shareIntent.putExtra(Intent.EXTRA_TEXT,"Download aplikasi ini secara gratis " + "https://play.google.com/store/apps/details?id=" +view.getContext().getPackageName());
                shareIntent.setType("image/png");
                view.getContext().startActivity(Intent.createChooser(shareIntent,"Share with"));
            }
        });
        holder.copy.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SdCardPath")
            @Override
            public void onClick(View v) {
                // Get clipboard manager object.
                Object clipboardService = v.getContext().getSystemService(CLIPBOARD_SERVICE);
                final ClipboardManager clipboardManager = (ClipboardManager)clipboardService;
                String srcText = holder.txtKata.getText().toString()+" - "+holder.txtTokoh.getText().toString()+"\n"+"Download aplikasi ini secara gratis " + "https://play.google.com/store/apps/details?id=" +v.getContext().getPackageName();
                // Create a new ClipData.
                ClipData clipData = ClipData.newPlainText("Source Text", srcText);
                // Set it as primary clip data to copy text to system clipboard.
                clipboardManager.setPrimaryClip(clipData);
                // Popup a snackbar.
                Snackbar snackbar = Snackbar.make(v, "Kutipan berhasil di salin.", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
                final String currentTimeStamp = dateFormat.format(new Date());
                LinearLayout ly = holder.bg;
                ly.setDrawingCacheEnabled(true);
                ly.buildDrawingCache(true);
                Bitmap saveBm = Bitmap.createBitmap(ly.getDrawingCache());
                ly.setDrawingCacheEnabled(false);
                String bitmapPath = MediaStore.Images.Media.insertImage(ly.getContext().getContentResolver(),
                        saveBm,tokohList.get(position), "Sumber Aplikasi Kutipan Tokoh");
                Toast.makeText(context,"Gambar berhasil tersimpan di galeri",Toast.LENGTH_LONG).show();
            }

        });

    }
    @Override
    public int getItemCount() {
        return tokohList.size();
    }
}
