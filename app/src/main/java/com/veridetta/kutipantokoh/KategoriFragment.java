package com.veridetta.kutipantokoh;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.veridetta.kutipantokoh.adapter.CardAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static com.google.android.gms.internal.zzahn.runOnUiThread;


public class KategoriFragment extends Fragment implements View.OnClickListener{
    Document mBlogDocument  = null, cardDoc = null;
    LinearLayout islam, sejarawan, motivator, pahlawan, filsuf, anime,ly_button;
    Dialog dialog;
    CardView btn_banyak;
    TextView menampilkan;
    String url ="",urlNext;
    int data;
    private ArrayList<String> namaTokoh;
    private ArrayList<String> card_Tokoh;
    private ArrayList<String> card_ket ;
    private ArrayList<String> card_kata ;
    private ArrayList<String> card_gambar;
    private ArrayList<String> id_kata;
    String idKataString;
    public KategoriFragment() {

    }
    public static KategoriFragment newInstance(String param1, String param2) {
        KategoriFragment fragment = new KategoriFragment();
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
        final View view = inflater.inflate(R.layout.fragment_kategori, container, false);
        dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_loading);
        btn_banyak = view.findViewById(R.id.card_next);
        islam = view.findViewById(R.id.tokoh_islam);
        sejarawan= view.findViewById(R.id.sejarawan);
        motivator= view.findViewById(R.id.motivator);
        pahlawan= view.findViewById(R.id.pahlawan);
        filsuf= view.findViewById(R.id.filsafat);
        anime= view.findViewById(R.id.anime);
        menampilkan = view.findViewById(R.id.menampilkan);
        ly_button = view.findViewById(R.id.ly_buton);
        url = "https://jagokata.com/kata-bijak/kata-ilmuwan.html";
        btn_banyak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = urlNext;
                dialog.show();
                new CardGet().execute();
            }
        });
        islam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kategoriKlik(islam, "Menampilkan Kategori Ilmuwan",
                       "https://jagokata.com/kata-bijak/kata-ilmuwan.html" );
            }
        });
        sejarawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kategoriKlik(sejarawan, "Menampilkan Kategori Sejarawan",
                        "https://jagokata.com/kata-bijak/kata-sejarawan.html");
            }
        });
        motivator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kategoriKlik(motivator, "Menampilkan Kategori Motivator",
                        "https://jagokata.com/kata-bijak/kata-motivator.html");
            }
        });
        pahlawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kategoriKlik(pahlawan,"Menampilkan Kategori Pahlawan",
                        "https://jagokata.com/kata-bijak/kata-pahlawan.html");
            }
        });
        filsuf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kategoriKlik(filsuf, "Menampilkan Kategori Filsuf",
                        "https://jagokata.com/kata-bijak/kata-filsuf.html");
            }
        });
        anime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kategoriKlik(anime,"Menampilkan Kategori Anime",
                        "https://jagokata.com/kata-bijak/kata-anime.html");
            }
        });
        btn_banyak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                new CardGet().execute();
            }
        });
        //new DataGrabber().execute(); //execute the asynctask below
        ContentCek();
        return view;
    }
    private void ContentCek(){
        if(data>0){

        }else{
            namaTokoh = new ArrayList<>();
            card_Tokoh = new ArrayList<>();
            card_ket = new ArrayList<>();
            card_kata = new ArrayList<>();
            card_gambar = new ArrayList<>();
            id_kata= new ArrayList<>();
            dialog.show();
            new CardGet().execute();
        }
    }
    @Override
    public void onClick(View view) {

    }
    private void setVisibility(final LinearLayout linearLayout, final String status){
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                if(status.equals("hide")){
                    linearLayout.setVisibility(View.GONE);
                }else{
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    public void kategoriKlik(final LinearLayout cardView, final String Kategori,
                             final String urlKategori){
        url=urlKategori;
        float alpha = (float) 0.5;
        islam.setAlpha(alpha);
        sejarawan.setAlpha(alpha);
        motivator.setAlpha(alpha);
        pahlawan.setAlpha(alpha);
        filsuf.setAlpha(alpha);
        anime.setAlpha(alpha);
        cardView.setAlpha(1);
        menampilkan.setText(Kategori);
        dialog.show();
        namaTokoh = new ArrayList<>();
        card_Tokoh = new ArrayList<>();
        card_ket = new ArrayList<>();
        card_kata = new ArrayList<>();
        card_gambar = new ArrayList<>();
        id_kata= new ArrayList<>();
        new CardGet().execute();
    }
    public static float dpToPixels(int dp, KategoriFragment context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }
    @SuppressLint("StaticFieldLeak")
    private class CardGet extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            data=0;
            // NO CHANGES TO UI TO BE DONE HERE
            Document mBlogPagination = null;
            try {
                mBlogPagination = Jsoup.parse(new URL(url),50000);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Using Elements to get the Meta data
            Elements mElementDataSize = mBlogPagination.select("ul[id=citatenrijen] li:not(#googleinpage)");
            Elements mPageSize = mBlogPagination.select("div[class=pageslist pageslist-right] a");
            // Locate the content attribute
            int mElementSize = mElementDataSize.size();
            for (int i = 0; i < mElementSize; i++) {
                //Judul
                Elements ElemenJudul = mElementDataSize.select("a.auteurfbnaam").eq(i);
                String Nama = ElemenJudul.text();
                //gambar
                Elements elGambar = mElementDataSize.select("div.citatenlijst-auteur-container").eq(i);
                String gambara = elGambar.select("img").eq(0).attr("src");
                Elements elKet = mElementDataSize.select("span.auteur-beschrijving").eq(i);
                String ket = elKet.text().trim();
                //STATUS
                Elements elKata = mElementDataSize.select("q.fbquote").eq(i);
                String kata = elKata.text().trim();
                idKataString=mElementDataSize.eq(i).attr("id");
                card_Tokoh.add(Nama);
                card_gambar.add(gambara);
                card_kata.add(kata);
                card_ket.add(ket);
                id_kata.add(idKataString);
                data++;
            }
            for(int h=0; h<mPageSize.size();h++){
                if (h==1){
                    setVisibility(ly_button, "muncul");
                    urlNext=mPageSize.eq(h).attr("href");
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //This is where we update the UI with the acquired data
            // Set description into TextView
            RecyclerView mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.rc_card);
            CardAdapter mDataAdapter = new CardAdapter( getContext(), card_Tokoh, card_kata, card_gambar, card_ket,id_kata);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),1);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mDataAdapter);
            btn_banyak.setVisibility(View.VISIBLE);
            dialog.dismiss();
        }
    }
}

