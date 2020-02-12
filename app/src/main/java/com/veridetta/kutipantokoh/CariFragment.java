package com.veridetta.kutipantokoh;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.veridetta.kutipantokoh.adapter.CardAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public class CariFragment extends Fragment implements View.OnClickListener{
    Document  cardDoc = null;
    Dialog dialog;
    Button btn_banyak, btn_cari;
    EditText cari;
    LinearLayout ly_awal, ly_cari;
    String katanya;
    CardAdapter mDataAdapter;
    int page=1;
    int hasil=0;
    private ArrayList<String> card_Tokoh = new ArrayList<>();
    private ArrayList<String> card_ket = new ArrayList<>();
    private ArrayList<String> card_kata = new ArrayList<>();
    private ArrayList<String> card_gambar = new ArrayList<>();
    public CariFragment() {

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
        final View view = inflater.inflate(R.layout.fragment_cari, container, false);
        dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_loading);
        cari = view.findViewById(R.id.txt_cari);
        ly_awal = view.findViewById(R.id.ly_awal);
        ly_cari =view.findViewById(R.id.cari_keluar);
        btn_cari = view.findViewById(R.id.btn_cari);
        btn_cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                katanya = cari.getText().toString().replace(" ","+");
                if(katanya.length()<3){
                    Toast.makeText(getContext(),"Minimal 3 kata", Toast.LENGTH_LONG ).show();
                }else{
                    if(card_Tokoh.size()>0){
                        card_gambar.clear();
                        card_kata.clear();
                        card_ket.clear();
                        card_Tokoh.clear();
                        mDataAdapter.notifyDataSetChanged();
                        ly_awal.setVisibility(View.VISIBLE);
                        ly_cari.setVisibility(View.GONE);
                    }
                    page=1;
                    dialog.show();
                    new CardGet().execute();
                }
            }
        });
        btn_banyak = view.findViewById(R.id.btn_banyak);
        btn_banyak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page++;
                dialog.show();
                new CardGet().execute();
            }
        });
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
                String url = "https://jagokata.com/kata-bijak/kata-"+katanya+".html?page="+page;
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
            if(card_Tokoh.size()>0){
                //This is where we update the UI with the acquired data
                // Set description into TextView
                RecyclerView mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.cari_list);
                mDataAdapter = new CardAdapter( card_Tokoh, card_kata, card_gambar, card_ket);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),1);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mDataAdapter);
                ly_awal.setVisibility(View.GONE);
                ly_cari.setVisibility(View.VISIBLE);
                hasil=1;
            }else{
                Toast.makeText(getActivity(), "Tidak ada data untuk dimuat", Toast.LENGTH_LONG).show();
                page=1;
                hasil=0;
            }
            dialog.dismiss();
        }
    }
}