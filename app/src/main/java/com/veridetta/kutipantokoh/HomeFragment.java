package com.veridetta.kutipantokoh;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.veridetta.kutipantokoh.adapter.CardAdapter;
import com.veridetta.kutipantokoh.adapter.DataAdapter;
import com.veridetta.kutipantokoh.adapter.PalingAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;


public class HomeFragment extends Fragment implements View.OnClickListener{
    Document mBlogDocument  = null, cardDoc = null;
    Dialog dialog;
    Button btn_banyak;

    private ArrayList<String> namaTokoh = new ArrayList<>();
    private ArrayList<String> tahunTokoh = new ArrayList<>();
    private ArrayList<String> urlKata = new ArrayList<>();
    private ArrayList<String> gambarTokoh = new ArrayList<>();
    private ArrayList<String> palingList = new ArrayList<>();
    private ArrayList<String> palingurlList = new ArrayList<>();
    private ArrayList<String> card_Tokoh = new ArrayList<>();
    private ArrayList<String> card_ket = new ArrayList<>();
    private ArrayList<String> card_kata = new ArrayList<>();
    private ArrayList<String> card_gambar = new ArrayList<>();
    public HomeFragment() {

    }
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_loading);
        btn_banyak = view.findViewById(R.id.btn_banyak);
        btn_banyak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                new CardGet().execute();
            }
        });
        //new DataGrabber().execute(); //execute the asynctask below
        new CardGet().execute();
        dialog.show();
        return view;
    }
    @Override
    public void onClick(View view) {

    }

    public static float dpToPixels(int dp, HomeFragment context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }
    @SuppressLint("StaticFieldLeak")
    private class CardGet extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // NO CHANGES TO UI TO BE DONE HERE
            try {
                String url = "https://jagokata.com/kata-bijak/acak.html";
                cardDoc  = Jsoup.connect(url).get();
                Document mBlogPagination = Jsoup.connect(url).get();
                // Using Elements to get the Meta data
                Elements mElementDataSize = mBlogPagination.select("ul[id=citatenrijen] li:not(#googleinpage)");
                // Locate the content attribute
                int mElementSize = mElementDataSize.size();
                for (int i = 0; i < mElementSize; i++) {
                    //Judul
                    Elements ElemenJudul = mElementDataSize.select("a.auteurfbnaam").eq(i);
                    String Nama= ElemenJudul.text();
                    //gambar
                    Elements elGambar = mElementDataSize.select("div.citatenlijst-auteur-container").eq(i);
                    String gambara = elGambar.select("img").eq(0).attr("src");
                    Elements elKet = mElementDataSize.select("span.auteur-beschrijving").eq(i);
                    String ket = elKet.text().trim();
                    //STATUS
                    Elements elKata = mElementDataSize.select("p.fbquote").eq(i);
                    String kata = elKata.text().trim();
                    card_Tokoh.add(Nama);
                    card_gambar.add(gambara);
                    card_kata.add(kata);
                    card_ket.add(ket);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //This is where we update the UI with the acquired data
            // Set description into TextView
            RecyclerView mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.rc_card);
            CardAdapter mDataAdapter = new CardAdapter( card_Tokoh, card_kata, card_gambar, card_ket);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),1);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mDataAdapter);
            dialog.dismiss();
        }
    }
    /*private class DataGrabber extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // NO CHANGES TO UI TO BE DONE HERE
            try {
                String url = "https://jagokata.com/";
                mBlogDocument  = Jsoup.connect(url).get();
                Document mBlogPagination = Jsoup.connect(url).get();
                // Using Elements to get the Meta data
                Elements mElementDataSize = mBlogPagination.select("div.index-box-container").eq(0).
                        select("ul").select("li");
                Elements palingUtama = mBlogPagination.select("div.index-box-container").eq(1).
                        select("ul").select("li");
                // Locate the content attribute
                int mElementSize = mElementDataSize.size();
                for (int i = 0; i < mElementSize; i++) {
                    //Judul
                    Elements ElemenJudul = mElementDataSize.select("a").eq(i);
                    String Judul[] = ElemenJudul.select("span").html().split("<em>");
                    String Judulnya = Judul[0].trim().replace("<br>","");
                    String Tahun = Judul[1].trim().replace("</em>","");
                    //URL
                    String urlnya = ElemenJudul.attr("href");
                    //STATUS
                    String gambar = ElemenJudul.select("img").attr("src");
                    namaTokoh.add(Judulnya);
                    tahunTokoh.add(Tahun);
                    urlKata.add(urlnya);
                    gambarTokoh.add(gambar);
                }
                int utamaSize = palingUtama.size();
                for (int j = 0; j < utamaSize; j++) {
                    //Judul
                    Elements elPaling = palingUtama.select("a").eq(j);
                    String txtPaling = elPaling.text();
                    String urlPaling = elPaling.attr("href");
                    palingList.add(txtPaling);
                    palingurlList.add(urlPaling);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //This is where we update the UI with the acquired data
            // Set description into TextView
            RecyclerView mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.rc_tokoh);
            DataAdapter mDataAdapter = new DataAdapter( namaTokoh, urlKata, tahunTokoh, gambarTokoh);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),1);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mDataAdapter);
            //rec paling
            RecyclerView palingRec = (RecyclerView) getActivity().findViewById(R.id.rc_dicari);
            PalingAdapter palingadapter = new PalingAdapter( palingList, palingurlList);
            RecyclerView.LayoutManager palingL = new GridLayoutManager(getActivity().getApplicationContext(),2);
            palingRec.setLayoutManager(palingL);
            palingRec.setAdapter(palingadapter);
        }
    }*/
}

