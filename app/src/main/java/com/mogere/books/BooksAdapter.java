package com.mogere.books;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder>{

    ArrayList<Book> books;

    public BooksAdapter(ArrayList<Book> books){
        this.books = books;
    }
    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.book_list_item, parent,false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {

        Book book = books.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView authorTv;
        TextView publishDate;
        TextView publisher;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            authorTv = (TextView) itemView.findViewById(R.id.authors);
            publishDate = (TextView) itemView.findViewById(R.id.publishedDate);
            publisher = (TextView) itemView.findViewById(R.id.publisher);

        }

        public void bind(Book book){
            title.setText(book.title);
            String authors = "";
            int i = 0;
            for (String author:book.authors){
                authors+=author;
                i++;
                if(i<book.authors.length){
                    authors+=" , ";
                }
            }
            authorTv.setText(authors);
            publishDate.setText(book.publishedDate);
            publisher.setText(book.publisher);
        }
    }
}
