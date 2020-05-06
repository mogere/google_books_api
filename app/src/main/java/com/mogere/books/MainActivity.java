package com.mogere.books;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
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


        try {
            URL bookUrl = ApiUtil.buildUrl("cooking");
            new BooksQueryTask().execute(bookUrl);
        }
        catch (Exception e){
            Log.d("error", e.getMessage());
        }


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
            }

            ArrayList<Book> books = ApiUtil.getBookFromJson(result);
            BooksAdapter adapter = new BooksAdapter(books);
            booksRecycler.setAdapter(adapter);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.setVisibility(View.VISIBLE);
        }
    }
}
