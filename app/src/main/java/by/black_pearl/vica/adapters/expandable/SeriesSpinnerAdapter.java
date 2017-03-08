package by.black_pearl.vica.adapters.expandable;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import by.black_pearl.vica.realm_db.ProductSeriesDb;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by BLACK_Pearl.
 */

public class SeriesSpinnerAdapter implements android.widget.SpinnerAdapter {

    private final RealmResults<ProductSeriesDb> mSeries;
    private Context mContext;

    public SeriesSpinnerAdapter(Context context, int id) {
        mContext = context;
        Realm realm = Realm.getDefaultInstance();
        mSeries = realm.where(ProductSeriesDb.class).equalTo("IdMenu", id).findAll();
    }

    public int getCurrentPosition(int id) {
        int position = 0;
        for (int i = 0; i < mSeries.size(); i++) {
            if(mSeries.get(i).getId() == id) {
                position = i;
                break;
            }
        }
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        ((TextView) convertView).setText(mSeries.get(position).getName());
        return convertView;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return mSeries.size();
    }

    @Override
    public Object getItem(int position) {
        return mSeries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mSeries.get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) View.inflate(mContext, android.R.layout.simple_spinner_item, null);
        textView.setText(mSeries.get(position).getName());
        return textView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
