package com.veridetta.kutipantokoh.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.ClipData;
import android.content.ClipboardManager;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.target.SimpleTarget;

import com.bumptech.glide.request.transition.Transition;

import com.veridetta.kutipantokoh.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.support.constraint.Constraints.TAG;


public class
CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private ArrayList<String> tokohList = new ArrayList<>();
    private ArrayList<String> kataList = new ArrayList<>();
    private ArrayList<String> photoList = new ArrayList<>();
    private ArrayList<String> ketList = new ArrayList<>();
    private Activity mActivity;
    Intent intent;

    String dx;
    private Context context;
    private int lastPosition = -1;
    Bitmap  bitmap;
    public CardAdapter(ArrayList<String> tokohList,
                       ArrayList<String> kataList,
                       ArrayList<String> photoList,
                       ArrayList<String> ketList) {

        this.tokohList = tokohList;
        this.kataList = kataList;
        this.photoList = photoList;
        this.ketList = ketList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTokoh, txtKet, txtKata;
        ImageView photoTokoh, copy, share;
        CardView cardBaru;
        LinearLayout bg;
        public MyViewHolder(View view) {
            super(view);
            this.bg = view.findViewById(R.id.bg_img);
            txtTokoh = (TextView) view.findViewById(R.id.nama_tokoh);
            txtKet = (TextView) view.findViewById(R.id.keterangan_tokoh);
            txtKata = (TextView) view.findViewById(R.id.txt_kata);
            cardBaru = (CardView) view.findViewById(R.id.card_kata);
            photoTokoh = view.findViewById(R.id.profile_img);
            copy = view.findViewById(R.id.img_copy);
            share = view.findViewById(R.id.img_share);
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
        Glide.with(holder.photoTokoh.getContext())
                .load(Uri.parse(photoList.get(position)))
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.photoTokoh.setImageDrawable(resource);
                        holder.bg.setBackground(holder.photoTokoh.getDrawable());
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
                        saveBm,"title", "Sumber Aplikasi Kutipan Tokoh");
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
                String srcText = holder.txtKata.getText().toString()+" - "+holder.txtTokoh.getText().toString();
                // Create a new ClipData.
                ClipData clipData = ClipData.newPlainText("Source Text", srcText);
                // Set it as primary clip data to copy text to system clipboard.
                clipboardManager.setPrimaryClip(clipData);
                // Popup a snackbar.
                Snackbar snackbar = Snackbar.make(v, "Source text has been copied to system clipboard.", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return tokohList.size();
    }
}
