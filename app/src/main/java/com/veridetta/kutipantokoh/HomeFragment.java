package com.veridetta.kutipantokoh;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.veridetta.kutipantokoh.adapter.CardAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;



public class HomeFragment extends Fragment implements View.OnClickListener{
    Document mBlogDocument  = null, cardDoc = null;
    Dialog dialog;
    Button btn_banyak;
    int data;
    private ArrayList<String> namaTokoh;
    private ArrayList<String> card_Tokoh;
    private ArrayList<String> card_ket ;
    private ArrayList<String> card_kata ;
    private ArrayList<String> card_gambar;
    private ArrayList<String> id_kata;
    String idKataString;
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
        ContentCek();
        dialog.show();
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
            new CardGet().execute();
        }
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
            data=0;
            // NO CHANGES TO UI TO BE DONE HERE
            String url = "https://jagokata.com/kata-bijak/acak.html";
            Document mBlogPagination = null;
            try {
                mBlogPagination = Jsoup.parse(new URL(url),50000);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Using Elements to get the Meta data
            Elements mElementDataSize = mBlogPagination.select("ul[id=citatenrijen] li:not(#googleinpage)");
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
                System.out.println("Kata "+kata);
                Log.d("HOME", "doInBackground: "+kata);
                idKataString=mElementDataSize.eq(i).attr("id");
                card_Tokoh.add(Nama);
                card_gambar.add(gambara);
                card_kata.add(kata);
                card_ket.add(ket);
                id_kata.add(idKataString);
                data++;
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

