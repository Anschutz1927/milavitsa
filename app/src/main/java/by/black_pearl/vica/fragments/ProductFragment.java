package by.black_pearl.vica.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import by.black_pearl.vica.R;
import by.black_pearl.vica.activities.FullscreenImageActivity;
import by.black_pearl.vica.realm_db.ProductDb;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {

    private static final String ID = "mId";
    private ProductDb mProduct;

    public ProductFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ProductFragment.
     */
    public static ProductFragment newInstance(int id) {
        ProductFragment fragment = new ProductFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm realm = Realm.getDefaultInstance();
        int mId = 0;
        if(getArguments() != null) {
            mId = getArguments().getInt(ID);
        }
        this.mProduct = realm.where(ProductDb.class).equalTo("Id", mId).findFirst();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_product_view, container, false);
        if(mProduct == null) {
            return view;
        }
        ((TextView) view.findViewById(R.id.view_product_view_descTextView))
                .setText("Описание: \n" + mProduct.getDescription());
        ImageView imageView = (ImageView) view.findViewById(R.id.view_product_view_imageView);
        final String image_url = "http://www.milavitsa.com/i/photo/" + mProduct.getImage();
        Picasso.with(getContext()).load(image_url).placeholder(R.drawable.ic_menu_camera).centerInside()
                .fit().into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), FullscreenImageActivity.class)
                        .putExtra(FullscreenImageActivity.IMAGE_URL, image_url));
            }
        });
        ((TextView) view.findViewById(R.id.view_product_view_sizesTextView))
                .setText("Размеры: \n" + mProduct.getSizes());
        ((TextView) view.findViewById(R.id.view_product_view_idRubricTextView))
                .setText("Рубрика: " + String.valueOf(mProduct.getIdRubric()));
        ((TextView) view.findViewById(R.id.view_product_view_articleTextView))
                .setText("Модель: " + mProduct.getArticle());
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
