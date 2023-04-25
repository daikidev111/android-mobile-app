package com.example.android_app;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_app.provider.Book;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

//    ArrayList<Book> data;
    List<Book> data;

    public void setData(List<Book> data) {
        this.data = data;
    }

    public List<Book> getBooks() {
        return this.data;
    }

    @NonNull
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // card inflator
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_card_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewAdapter.ViewHolder holder, int position) {
        // use information from onCreateViewHolder to bind to view holder like onViewCreated
        // put them in the relavant posisiton in the cardview
        // data is data to get from the array
        // respective position's card data

        Book book = data.get(position);

        holder.bookPosition.setText("No. " + position);
        holder.bookID.setText("ID: " + book.getID());
        holder.bookISBN.setText("ISBN: " + book.getIsbn());
        holder.bookTitle.setText("Title: " + book.getTitle());
        holder.bookDescription.setText("Description: " + book.getDescription());
        holder.bookPrice.setText("Price: " + book.getPrice());
        holder.bookAuthor.setText("Author: " + book.getAuthor());
//        Log.d("week6App","onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        // Cannot return 0!! -> tell the recycler view that there is no data in the recycleView
        // return 0;

        if (this.data == null) {
           return 0;
        }
        return data.size(); // to keep track of the posistion
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
    // To bind all the view components in the cardview into the holder and this acts as a holder for the components
        public TextView bookID;
        public TextView bookISBN;
        public TextView bookTitle;
        public TextView bookAuthor;
        public TextView bookDescription;
        public TextView bookPrice;

        public TextView bookPosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookID = itemView.findViewById(R.id.book_id);
            bookISBN = itemView.findViewById(R.id.book_isbn);
            bookTitle = itemView.findViewById(R.id.book_title);
            bookAuthor = itemView.findViewById(R.id.book_author);
            bookPrice = itemView.findViewById(R.id.book_price);
            bookDescription = itemView.findViewById(R.id.book_description);
            bookPosition = itemView.findViewById(R.id.book_position);
        }
    }
}
