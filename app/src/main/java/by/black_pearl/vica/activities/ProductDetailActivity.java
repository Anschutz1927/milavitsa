package by.black_pearl.vica.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import by.black_pearl.vica.R;
import by.black_pearl.vica.realm_db.ProductDb;
import io.realm.Realm;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String PRODUCT_ID = "productId";
    private ProductDb mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_product_view);
        int productId = getIntent().getIntExtra(PRODUCT_ID, 0);
        Realm realm = Realm.getDefaultInstance();
        mProduct = realm.where(ProductDb.class).equalTo("Id", productId).findFirst();
        realm.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((TextView) findViewById(R.id.view_product_view_articleTextView)).setText("Модель: " +
                String.valueOf(mProduct.getArticle()));
        ImageView imageView = (ImageView) findViewById(R.id.view_product_view_imageView);
        final String image_url = "http://www.milavitsa.com/i/photo/" + mProduct.getImage();
        Picasso.with(this).load(image_url).placeholder(R.drawable.ic_menu_camera).centerInside()
                .fit().into(imageView);
        ((TextView) findViewById(R.id.view_product_view_descTextView)).setText("Описание:\n" +
                mProduct.getDescription());
        ((TextView) findViewById(R.id.view_product_view_sizesTextView)).setText("Размеры:\n" +
                mProduct.getSizes());
        ((TextView) findViewById(R.id.view_product_view_idRubricTextView)).setText("Рубрика: " +
                String.valueOf(mProduct.getIdRubric()));
    }
}
