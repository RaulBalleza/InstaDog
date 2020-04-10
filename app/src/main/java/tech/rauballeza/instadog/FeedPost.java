package tech.rauballeza.instadog;

import android.graphics.Bitmap;

public class FeedPost {
    String username;
    int userImage;
    String comment;
    int likes;
    String[] comments;
    int image;

    public FeedPost(String username, String comment, int likes, int image, int userImage) {
        this.username = username;
        this.comment = comment;
        this.likes = likes;
        //this.comments = comments;
        this.image = image;
        this.userImage = userImage;
    }
}
