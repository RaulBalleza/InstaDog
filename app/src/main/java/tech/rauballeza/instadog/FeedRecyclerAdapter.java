package tech.rauballeza.instadog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.ImageViewHolder> {

    private ArrayList<FeedPost> posts;

    public FeedRecyclerAdapter(ArrayList<FeedPost> data) {
        this.posts = data;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_post, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.post.setImageResource(posts.get(position).image);
        holder.username.setText(posts.get(position).username);
        holder.caption.setText(String.format("%s %s", posts.get(position).username, posts.get(position).comment));
        holder.likes.setText(String.format("%s likes", posts.get(position).likes));
        holder.user_image.setImageResource(posts.get(position).userImage);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView post;
        TextView username;
        TextView caption;
        TextView likes;
        ImageView user_image;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            post = itemView.findViewById(R.id.feed_post_image_post);
            username = itemView.findViewById(R.id.feed_post_username_top);
            caption = itemView.findViewById(R.id.feed_post_username_and_caption);
            likes = itemView.findViewById(R.id.feed_post_likes);
            user_image = itemView.findViewById(R.id.feed_post_profile_photo);
        }
    }
}
