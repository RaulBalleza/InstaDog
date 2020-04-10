package tech.rauballeza.instadog;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileRecyclerAdapter extends RecyclerView.Adapter<ProfileRecyclerAdapter.ImageViewHolder> {

    private int[] posts;

    public ProfileRecyclerAdapter(int[] posts) {
        this.posts = posts;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_post, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.post.setImageResource(posts[position]);
    }

    @Override
    public int getItemCount() {
        return posts.length;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView post;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            post = itemView.findViewById(R.id.post);
        }
    }
}
