package com.mogere.books;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private ProgressBar mLoading;
    private RecyclerView booksRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoading = (ProgressBar) findViewById(R.id.loading);
        booksRecycler = (RecyclerView) findViewById(R.id.booksRecycler);
        LinearLayoutManager booksLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        booksRecycler.setLayoutManager(booksLayoutManager);

        Intent intent = getIntent();
        String query = intent.getStringExtra("Query");
        URL bookUrl;


        try {
            if(query == null || query.isEmpty()) {
                bookUrl = ApiUtil.buildUrl("cooking");
            }
            else
            {
                bookUrl = new URL(query);
            }
            new BooksQueryTask().execute(bookUrl);
        }
        catch (Exception e){
            Log.d("error", e.getMessage());
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_list_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_advanced_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try{
            URL bookUrl = ApiUtil.buildUrl(query);
            new BooksQueryTask().execute(bookUrl);
        }
        catch(Exception e){
            Log.d("Error", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public class BooksQueryTask extends AsyncTask<URL, Void, String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String result = null;

            try{
                result = ApiUtil.getJson(searchUrl);
            }
            catch(Exception e){
                Log.e("Error", e.getMessage());
            }
            return result;
        }


        @Override
        protected void onPostExecute(String result){
            TextView mError = (TextView) findViewById(R.id.error);
            mLoading.setVisibility(View.INVISIBLE);
            if (result == null){
                booksRecycler.setVisibility(View.INVISIBLE);
                mError.setVisibility(View.VISIBLE);
            }
            else{
                booksRecycler.setVisibility(View.VISIBLE);
                mError.setVisibility(View.INVISIBLE);
                ArrayList<Book> books = ApiUtil.getBookFromJson(result);
                BooksAdapter adapter = new BooksAdapter(books);
                booksRecycler.setAdapter(adapter);
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.setVisibility(View.VISIBLE);
        }
    }
}
