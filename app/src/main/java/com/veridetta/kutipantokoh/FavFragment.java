package com.veridetta.kutipantokoh;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.veridetta.kutipantokoh.adapter.CardAdapter;
import com.veridetta.kutipantokoh.db.DBHelper;
import com.veridetta.kutipantokoh.db.DBModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import static com.google.android.gms.internal.zzahn.runOnUiThread;


public class FavFragment extends Fragment implements View.OnClickListener{
    private ArrayList<String> judulList= new ArrayList<>();
    private ArrayList<String> gambarList= new ArrayList<String>();
    private ArrayList<String> penerbitList = new ArrayList<>();
    private ArrayList<String> waktuList = new ArrayList<>();
    private ArrayList<String> urlList = new ArrayList<>();
    private ArrayList<String> kategoriList = new ArrayList<>();
    private ArrayList<Integer> favList = new ArrayList<Integer>();
    private ArrayList<String> desList = new ArrayList<String>();
    RecyclerView rc_fav;
    int totalData=0, mentok=0;
    DBHelper helper;
    LinearLayout no_result;
    TextView favJudul;
    List<DBModel> dbList;
    public FavFragment() {

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
        final View view = inflater.inflate(R.layout.fragment_fav, container, false);
        rc_fav = view.findViewById(R.id.rc_fav);
        no_result = view.findViewById(R.id.no_result);
        favJudul = view.findViewById(R.id.fav_judul);
        //Cursor cursor = db.fetchMahasiswa(tnim);
        helper = new DBHelper(getContext());
        totalData = helper.getTotalFav();
        dbList= new ArrayList<DBModel>();
        dbList = helper.getFromDB();
        System.out.println("List"+totalData);
        if(totalData<1){
            favJudul.setVisibility(View.GONE);
            rc_fav.setVisibility(View.GONE);
            no_result.setVisibility(View.VISIBLE);
        }else{
            for (int i = 0; i < totalData; i++) {
                judulList.add(dbList.get(i).getJudul());
                gambarList.add(dbList.get(i).getGambar());
                penerbitList.add(dbList.get(i).getPenerbit());
                waktuList.add(dbList.get(i).getWaktu());;
                urlList.add(dbList.get(i).getUrl());
                kategoriList.add(dbList.get(i).getKategori());
                favList.add(Integer.parseInt(dbList.get(i).getFavorit()));
                desList.add(dbList.get(i).getDes());
                System.out.println("Masuk while "+judulList);
                if(totalData - i == 1){
                    mentok=1;
                }
            }
            if(mentok>0){
                CardAdapter mDataAdapter = new CardAdapter( getContext(), judulList, desList, gambarList,
                        kategoriList,urlList);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(),1, LinearLayoutManager.VERTICAL, false);
                rc_fav.setLayoutManager(mLayoutManager);
                rc_fav.setAdapter(mDataAdapter);
                System.out.println("Mentok"+judulList);

            }

        }
        return view;
    }
    @Override
    public void onClick(View view) {

    }

    public static float dpToPixels(int dp, HomeFragment context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }
}