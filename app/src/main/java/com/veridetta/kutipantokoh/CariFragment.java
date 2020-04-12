package com.veridetta.kutipantokoh;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.veridetta.kutipantokoh.adapter.CardAdapter;
import com.veridetta.kutipantokoh.db.DBHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import static com.google.android.gms.internal.zzahn.runOnUiThread;


public class CariFragment extends Fragment implements View.OnClickListener{
    Document  cardDoc = null;
    Dialog dialog;
    Button  btn_cari;
    TextView txt_cari_judul;
    SearchView cari;
    LinearLayout ly_awal, ly_cari, no_result, ly_button;
    String katanya,cariVal,urlNext;
    CardAdapter mDataAdapter;
    CardView btn_banyak;
    DBHelper helper;
    int page=1;
    int hasil=0;
    String url = "";
    private ArrayList<String> card_Tokoh = new ArrayList<>();
    private ArrayList<String> card_ket = new ArrayList<>();
    private ArrayList<String> card_kata = new ArrayList<>();
    private ArrayList<String> card_gambar = new ArrayList<>();
    private ArrayList<String> id_kata = new ArrayList<>();
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
        btn_banyak = view.findViewById(R.id.card_next);
        no_result = view.findViewById(R.id.no_result);
        txt_cari_judul = view.findViewById(R.id.cari_judul);
        ly_button = view.findViewById(R.id.ly_buton);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cari.clearFocus();
            }
        }, 300);
        cari.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cariVal = cari.getQuery().toString();
                katanya = cari.getQuery().toString().replace(" ","+");
                if(cariVal.length()<3){
                    Toast.makeText(getContext(),"Minimal kata minimal 3 huruf",Toast.LENGTH_LONG).show();
                }else if(cariVal.length()>10){
                    Toast.makeText(getContext(),"Maksimal 10 huruf",Toast.LENGTH_LONG).show();
                }else{
                    url = "https://jagokata.com/kata-bijak/kata-"+katanya+".html?page="+page;
                    if(card_Tokoh.size()>0){
                        card_gambar.clear();
                        card_kata.clear();
                        card_ket.clear();
                        card_Tokoh.clear();
                        id_kata.clear();
                        mDataAdapter.notifyDataSetChanged();
                    }
                    page=1;
                    dialog.show();
                    new CardGet().execute();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        btn_banyak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = urlNext;
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
    @SuppressLint("StaticFieldLeak")
    private class CardGet extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // NO CHANGES TO UI TO BE DONE HERE
            try {
                cardDoc  = Jsoup.connect(url).get();
                Document mBlogPagination = Jsoup.connect(url).get();
                // Using Elements to get the Meta data
                Elements mElementDataSize = mBlogPagination.select("ul[id=citatenrijen] li:not(#googleinpage)");
                Elements mPageSize = mBlogPagination.select("div[class=pageslist pageslist-right] a");
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
                    String idKata = mElementDataSize.eq(i).attr("id");
                    card_Tokoh.add(Nama);
                    card_gambar.add(gambara);
                    card_kata.add(kata);
                    card_ket.add(ket);
                    id_kata.add(idKata);
                }
                for(int h=0; h<mPageSize.size();h++){
                    if (h==1){
                        setVisibility(ly_button, "muncul");
                        urlNext=mPageSize.eq(h).attr("href");
                    }

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
                RecyclerView mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.rc_cari);
                mDataAdapter = new CardAdapter( getContext(), card_Tokoh, card_kata, card_gambar, card_ket,id_kata);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),1);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mDataAdapter);
                no_result.setVisibility(View.GONE);
                txt_cari_judul.setVisibility(View.VISIBLE);
                txt_cari_judul.setText("Menampilkan hasil dari kata "+cari.getQuery().toString());
                hasil=1;
            }else{
                ly_button.setVisibility(View.GONE);
                txt_cari_judul.setVisibility(View.GONE);
                no_result.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Tidak ada data untuk dimuat", Toast.LENGTH_LONG).show();
                page=1;
                hasil=0;
            }
            dialog.dismiss();
        }
    }
}