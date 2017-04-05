package by.black_pearl.vica.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import by.black_pearl.vica.R;
import by.black_pearl.vica.realm_db.ProductDb;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by BLACK_Pearl.
 */
public class ProductAdapter extends BaseAdapter {

    private final Context mContext;
    private final RealmResults<ProductDb> mProducts;

    public ProductAdapter(Context context, long idRubric) {
        this.mContext = context;
        Realm realm = Realm.getDefaultInstance();
        this.mProducts = realm.where(ProductDb.class).equalTo("IdRubric", idRubric).findAll();
        realm.close();
    }
    @Override
    public int getCount() {
        return mProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return mProducts.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.view_listview_products, parent, false);
        }
        else {
            view = convertView;
        }
        ((TextView) view.findViewById(R.id.tv_product_model))
                .setText(mProducts.get(position).getDescription());
        String image_url = "http://www.milavitsa.com/i/photo/" + mProducts.get(position).getImage();
        Glide.with(mContext).load(image_url).diskCacheStrategy(DiskCacheStrategy.RESULT)
                .fitCenter().placeholder(R.drawable.ic_menu_camera).crossFade()
                .into((ImageView) view.findViewById(R.id.iv_product_image));
        return view;
    }
}
