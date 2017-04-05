package by.black_pearl.vica.adapters.expandable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import by.black_pearl.vica.R;
import by.black_pearl.vica.activities.MainActivity;
import by.black_pearl.vica.fragments.expandable.ScrollProductFragment;
import by.black_pearl.vica.realm_db.ProductDb;

/**
 * Created by BLACK_Pearl.
 */
public class AdapterProductType extends FragmentStatePagerAdapter {

    private final ArrayList<ProductDb> mProducts;

    public AdapterProductType(FragmentManager fm, ArrayList<ProductDb> productsType2) {
        super(fm);
        this.mProducts = productsType2;
    }

    @Override
    public Fragment getItem(int position) {
        return ItemFragment.newInstance(mProducts.get(position).getId(), mProducts.get(position).getImage(),
                mProducts.get(position).getArticle(), mProducts.get(position).getCustomMatherial());
    }

    @Override
    public int getCount() {
        return mProducts.size();
    }

    public int getType(int position) {
        return mProducts.get(position).getType();
    }

    public static class ItemFragment extends Fragment {

        private static final String ITEM_PRODUCT_IMAGE_URL = "imageUrl";
        private static final String ITEM_ARTICLE_TEXT = "article";
        private static final String ITEM_COLORS_IMAGE_URL = "colorsUrl";
        private static final String URL = "http://www.milavitsa.com/i/photo/";
        private static final String ID = "id";
        private String imageUrl;
        private String article;
        private String colorsUrl;
        private int id;

        public ItemFragment() {}

        public static Fragment newInstance(int id, String product_img_url,
                                           String product_article, String product_colors_url) {
            ItemFragment fragment = new ItemFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ITEM_PRODUCT_IMAGE_URL, product_img_url);
            bundle.putString(ITEM_ARTICLE_TEXT, product_article);
            bundle.putString(ITEM_COLORS_IMAGE_URL, product_colors_url);
            bundle.putInt(ID, id);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.imageUrl = getArguments() != null ? URL + getArguments().getString(ITEM_PRODUCT_IMAGE_URL) : null;
            this.article = getArguments() != null ? getArguments().getString(ITEM_ARTICLE_TEXT) : null;
            this.colorsUrl = getArguments() != null ? URL + getArguments().getString(ITEM_COLORS_IMAGE_URL) : null;
            this.id = getArguments() != null ? getArguments().getInt(ID) : 0;
            setRetainInstance(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.view_product_by_type, container, false);
            ImageView imageProduct = (ImageView) view.findViewById(R.id.view_by_type_productImage);
            TextView article = (TextView) view.findViewById(R.id.view_by_type_productArticle);
            ImageView imageColors = (ImageView) view.findViewById(R.id.view_by_type_colorsImage);
            Glide.with(getContext()).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .fitCenter().placeholder(R.drawable.ic_menu_camera)
                    .crossFade().into(imageProduct);
            article.setText(this.article);
            Glide.with(getContext()).load(colorsUrl).diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .fitCenter().placeholder(R.drawable.ic_menu_camera).crossFade().into(imageColors);
            view.setOnClickListener(getOnItemClickListener());
            return view;
        }

        private View.OnClickListener getOnItemClickListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.FragmentChanger.getFragmentChanger().changeFragment(ScrollProductFragment.newInstance(id), true);
                }
            };
        }
    }
}
