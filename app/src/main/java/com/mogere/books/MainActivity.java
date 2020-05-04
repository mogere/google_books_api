package com.mogere.books;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ProgressBar mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoading = (ProgressBar) findViewById(R.id.loading);
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
            TextView mResponse = (TextView) findViewById(R.id.response);
            TextView mError = (TextView) findViewById(R.id.error);
            mLoading.setVisibility(View.INVISIBLE);
            if (result == null){
                mResponse.setVisibility(View.INVISIBLE);
                mError.setVisibility(View.VISIBLE);
            }
            else{
                mResponse.setVisibility(View.VISIBLE);
                mError.setVisibility(View.INVISIBLE);
            }
            mResponse.setText(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.setVisibility(View.VISIBLE);
        }
    }
}
