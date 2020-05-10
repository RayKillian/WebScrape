package com.test.webscrape;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.test.webscrape.Adapters.RecentAdapter;
import com.test.webscrape.DataModel.Recent;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    static EditText editSearch;
    static String tokopedUrl;
    static String lazadaUrl;
    static String shopeeUrl;
    private static final int RECOGNIZER_RESULT = 1;
    static ArrayList<Recent> recentSearch = new ArrayList<Recent>();

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    RecentAdapter recentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        //used to display the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.rvRecent);
        gridLayoutManager = new GridLayoutManager(this, 1);
        recentAdapter = new RecentAdapter(recentSearch, gridLayoutManager);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(recentAdapter);

        recentAdapter.setOnItemClickListener(new RecentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Recent recent = recentSearch.get(position);
                editSearch.setText(recent.getName());

                if (!editSearch.getText().toString().trim().isEmpty()) {
                    buildLazadaUrl();
                    buildShopeeUrl();
                    buildTokopedUrl();

                    Intent i = new Intent(Search.this, ProductList.class);
                    //i.putExtra("LazadaUrl", lazadaUrl);
                    i.putExtra("TokopediaUrl", tokopedUrl);
                    //i.putExtra("ShopeeUrl", shopeeUrl);
                    i.putExtra("ProductName", editSearch.getText().toString());
                    startActivity(i);
                } else {
                    Toast.makeText(Search.this,"Search can not be empty",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDeleteClick(int position) {
                Recent recent = recentSearch.get(position);
                recentSearch.remove(recent);
                recentAdapter.notifyDataSetChanged();
            }
        });

        editSearch = findViewById(R.id.searchProduct);
        editSearch.setOnEditorActionListener(onEditorActionListener);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && !s.toString().trim().isEmpty()){
                    ArrayList<Recent> found = new ArrayList<Recent>();
                    for (Recent item : recentSearch){
                        if (item.getName().contains(s.toString())){
                            found.add(item);
                        }
                    }
                    recentAdapter = new RecentAdapter(found, gridLayoutManager);
                    recyclerView.setAdapter(recentAdapter);
                    RecentListener(found);
                } else {
                    recentAdapter = new RecentAdapter(recentSearch, gridLayoutManager);
                    recyclerView.setAdapter(recentAdapter);
                    RecentListener(recentSearch);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId){
                case EditorInfo.IME_ACTION_SEARCH:
                    if (!editSearch.getText().toString().trim().isEmpty()){
                        buildLazadaUrl();
                        buildTokopedUrl();
                        buildShopeeUrl();

                        recentSearch.add(new Recent(editSearch.getText().toString(), R.drawable.ic_suggest, R.drawable.ic_close));

                        Intent i = new Intent(Search.this,ProductList.class);
                        //i.putExtra("LazadaUrl", lazadaUrl);
                        i.putExtra("TokopediaUrl", tokopedUrl);
                        //i.putExtra("ShopeeUrl", shopeeUrl);
                        i.putExtra("ProductName", editSearch.getText().toString());
                        startActivity(i);
                    } else {
                        Toast.makeText(Search.this,"Search can not be empty",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            return false;
        }
    };

    public void buildShopeeUrl(){
        shopeeUrl = "https://shopee.co.id/search?keyword=";
        shopeeUrl += buildEndUrl();
    }

    public void buildTokopedUrl(){
        tokopedUrl = "https://www.tokopedia.com/search?q=";
        tokopedUrl += buildEndUrl();
    }

    public void buildLazadaUrl(){
        lazadaUrl = "https://www.lazada.co.id/catalog/?q=";
        lazadaUrl += buildEndUrl();
    }

    public String buildEndUrl(){
        String s = editSearch.getText().toString().trim();
        s = s.replace(" ", "+");
        return s;
    }

    public void RecentListener(final  ArrayList<Recent> list){
        recentAdapter.setOnItemClickListener(new RecentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Recent recent = list.get(position);
                editSearch.setText(recent.getName());

                if (!editSearch.getText().toString().trim().isEmpty()) {
                    buildLazadaUrl();
                    buildShopeeUrl();
                    buildTokopedUrl();

                    Intent i = new Intent(Search.this,ProductList.class);
                    //i.putExtra("LazadaUrl", lazadaUrl);
                    i.putExtra("TokopediaUrl", tokopedUrl);
                    //i.putExtra("ShopeeUrl", shopeeUrl);
                    i.putExtra("ProductName", editSearch.getText().toString());
                    startActivity(i);
                } else {
                    Toast.makeText(Search.this,"Search can not be empty",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDeleteClick(int position) {
                Recent recent = list.get(position);
                list.remove(recent);
                recentAdapter.notifyDataSetChanged();

                if (recentSearch != null){
                    if (recentSearch.indexOf(recent) != -1){
                        recentSearch.remove(recent);
                    }
                }
            }
        });
    }
}
