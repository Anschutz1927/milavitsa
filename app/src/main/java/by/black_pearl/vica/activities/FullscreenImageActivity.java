package by.black_pearl.vica.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import by.black_pearl.vica.R;
import uk.co.senab.photoview.PhotoView;

public class FullscreenImageActivity extends AppCompatActivity {
    public static final String IMAGE_URL = "imageUrl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        String url = getIntent().getExtras().getString(IMAGE_URL);
        PhotoView photoView = (PhotoView) findViewById(R.id.activity_fullscreen_photoView);
        Glide.with(this).load(url).diskCacheStrategy(DiskCacheStrategy.RESULT)
                .fitCenter().placeholder(android.R.drawable.ic_menu_camera)
                .crossFade().into(photoView);
    }
}