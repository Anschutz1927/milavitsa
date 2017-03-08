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
import by.black_pearl.vica.realm_db.ProductSeriesDb;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by BLACK_Pearl.
 */
public class SeriesAdapter extends BaseAdapter {

    private final Context mContext;
    private final RealmResults<ProductSeriesDb> mProductSeries;

    public SeriesAdapter(Context context, int menuId) {
        this.mContext = context;
        Realm realm = Realm.getDefaultInstance();
        this.mProductSeries = realm.where(ProductSeriesDb.class).equalTo("IdMenu", menuId).findAll();
        realm.close();
    }

    @Override
    public int getCount() {
        return this.mProductSeries.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return mProductSeries.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.view_listview_series, parent, false);
        }
        else {
            view = convertView;
        }
        ((TextView) view.findViewById(R.id.view_listview_series_nameTextView))
                .setText(mProductSeries.get(position).getName());
        ((TextView) view.findViewById(R.id.view_listview_series_descTextView))
                .setText(mProductSeries.get(position).getDescription());
        String image_url = "http://www.milavitsa.com/i/photo/" + mProductSeries.get(position).getImage();
        Picasso.with(mContext).load(image_url).placeholder(R.drawable.ic_menu_camera).centerInside()
                .fit().into((ImageView) view.findViewById(R.id.view_listview_series_imageView));
        return view;
    }
}
