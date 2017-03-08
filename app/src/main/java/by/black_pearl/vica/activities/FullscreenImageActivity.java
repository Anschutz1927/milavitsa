package by.black_pearl.vica.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import by.black_pearl.vica.R;
import uk.co.senab.photoview.PhotoView;

public class FullscreenImageActivity extends AppCompatActivity {
    public static final String IMAGE_URL = "imageUrl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);
        String url = getIntent().getExtras().getString(IMAGE_URL);
        PhotoView photoView = (PhotoView) findViewById(R.id.activity_fullscreen_photoView);
        Picasso.with(this).load(url).placeholder(R.drawable.ic_menu_camera).centerInside()
                .fit().into(photoView);
    }
}