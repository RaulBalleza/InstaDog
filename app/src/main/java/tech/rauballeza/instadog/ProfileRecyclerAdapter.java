package tech.rauballeza.instadog;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileRecyclerAdapter extends RecyclerView.Adapter<ProfileRecyclerAdapter.ImageViewHolder> {

    private List<ModelPost> posts;

    public ProfileRecyclerAdapter(List<ModelPost> posts) {
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
        String pImage = posts.get(position).getpImage();
        //holder.post.setImageResource(Integer.parseInt(posts.get(position).pImage));
        Picasso.get().load(pImage).into(holder.post);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView post;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            post = itemView.findViewById(R.id.post);
        }
    }
}
