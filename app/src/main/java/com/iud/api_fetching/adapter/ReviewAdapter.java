package com.iud.api_fetching.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iud.api_fetching.R;
import com.iud.api_fetching.model.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder>{

    private List<Review> reviewList;
    private Context context;

    public ReviewAdapter(List<Review> reviewList, Context context) {
        this.reviewList = reviewList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_ui, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {

        private TextView rating, reviewer, comment, date;

        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            rating = itemView.findViewById(R.id.rating);
            reviewer = itemView.findViewById(R.id.reviewer);
            comment = itemView.findViewById(R.id.comment);
            date = itemView.findViewById(R.id.date);
        }

        public void bind(final Review review) {
            rating.setText(review.getRating());
            reviewer.setText(review.getReviewerName());
            comment.setText(review.getComment());
            date.setText(review.getDate());
        }
    }
}
