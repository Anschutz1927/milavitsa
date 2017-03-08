package by.black_pearl.vica.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import by.black_pearl.vica.R;
import by.black_pearl.vica.realm_db.ProductDb;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by BLACK_Pearl.
 */
public class ProductDetailAdapter extends BaseAdapter {

    private final Context mContext;
    private final RealmResults<ProductDb> mProduct;

    public ProductDetailAdapter(Context context, long id) {
        this.mContext = context;
        Realm realm = Realm.getDefaultInstance();
        this.mProduct = realm.where(ProductDb.class).equalTo("Id", id).findAll();
        realm.close();
    }
    @Override
    public int getCount() {
        return mProduct.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position != 0) {
            return new View(mContext);
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_product_view, parent, false);
        ((TextView) view.findViewById(R.id.view_product_view_descTextView))
                .setText("Описание: \n" + mProduct.get(position).getDescription());
        String image_url = "http://www.milavitsa.com/i/photo/" + mProduct.get(position).getImage();
        Picasso.with(mContext).load(image_url).placeholder(R.drawable.ic_menu_camera).centerInside()
                .fit().into((ImageView) view.findViewById(R.id.view_product_view_imageView));
        ((TextView) view.findViewById(R.id.view_product_view_sizesTextView))
                .setText("Размеры: \n" + mProduct.get(position).getSizes());
        ((TextView) view.findViewById(R.id.view_product_view_idRubricTextView))
                .setText(String.valueOf(mProduct.get(position).getIdRubric()));
        ((TextView) view.findViewById(R.id.view_product_view_articleTextView))
                .setText("Артикль: " + mProduct.get(position).getArticle());
        return view;
    }
}
