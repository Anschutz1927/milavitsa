package by.black_pearl.vica.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Random;

import by.black_pearl.vica.R;
import by.black_pearl.vica.realm_db.ProductSeriesDb;
import io.realm.Realm;
import io.realm.RealmResults;

public class SlideshowActivity extends AppCompatActivity {

    private ImageSwitcher mImageSwitcher;
    private Handler mHandler;
    private ImageView mImageView1;
    private ImageView mImageView2;
    private Runnable mSlideRunnable;
    private RealmResults<ProductSeriesDb> mSeries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_slideshow);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.hide();
        }
        findViewById(R.id.activity_slideshow_onTouchLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SlideshowActivity.this.finish();
                return false;
            }
        });
        mImageSwitcher = (ImageSwitcher) findViewById(R.id.activity_slideshow_imageSwitcher);
        mImageView1 = (ImageView) findViewById(R.id.activity_slideshow_image1);
        mImageView2 = (ImageView) findViewById(R.id.activity_slideshow_image2);
        Realm realm = Realm.getDefaultInstance();
        this.mSeries = realm.where(ProductSeriesDb.class).findAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startSlideshow();
    }

    @Override
    protected void onPause() {
        if(mSlideRunnable != null && mHandler != null) {
            mHandler.removeCallbacks(mSlideRunnable);
        }
        super.onPause();
    }

    private void startSlideshow() {
        if(mHandler == null) {
            this.mHandler = new Handler();
        }
        mSlideRunnable = new Runnable() {
            @Override
            public void run() {
                nextSlide();
                mHandler.postDelayed(mSlideRunnable, 3000);
            }
        };
        mHandler.post(mSlideRunnable);
    }

    private void nextSlide() {
        int nextPos = getRandProductId();
        String image_url = "http://www.milavitsa.com/i/photo/" + mSeries.get(nextPos).getImage();
        if(mImageSwitcher.getCurrentView().getId() == mImageView1.getId()) {
            Glide.with(this).load(image_url).diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .fitCenter().placeholder(mImageView1.getDrawable()).crossFade().into(mImageView2);
            mImageSwitcher.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mImageSwitcher.showNext();
                }
            }, 1500);
        }
        else {
            Glide.with(this).load(image_url).diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .fitCenter().placeholder(mImageView2.getDrawable()).crossFade().into(mImageView1);
            mImageSwitcher.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mImageSwitcher.showNext();
                }
            }, 1500);
        }
    }

    private int getRandProductId() {
        Random random = new Random();
        return random.nextInt(mSeries.size());
    }
}
