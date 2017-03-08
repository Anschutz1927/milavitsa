package by.black_pearl.vica.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class CompactSeriesRecyclerViewAdapter
        extends RecyclerView.Adapter<CompactSeriesRecyclerViewAdapter.ViewHolder> {
    private final Context mContext;
    private RealmResults<ProductSeriesDb> mContentList;
    private final OnSerieClick mListener;

    public CompactSeriesRecyclerViewAdapter(Context context, int menuId, OnSerieClick serieClickListener) {
        mContext = context;
        mListener = serieClickListener;
        Realm realm = Realm.getDefaultInstance();
        mContentList = realm.where(ProductSeriesDb.class).equalTo("IdMenu", menuId).findAll();
        realm.close();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_listview_series, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mNameTextView.setText(mContentList.get(position).getName());
        String image_url = "http://www.milavitsa.com/i/photo/" + mContentList.get(position).getImage();
        Picasso.with(mContext).load(image_url).placeholder(R.drawable.ic_menu_camera).centerInside()
                .fit().into(holder.mImageView);
        holder.mItemView.setOnClickListener(onClickListener(mContentList.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        return mContentList.size();
    }

    public int getDefaultSerieId() {
        if(mContentList.size() > 0) {
            return mContentList.get(0).getId();
        }
        else {
            return 0;
        }
    }

    private View.OnClickListener onClickListener(final int id) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(id);
            }
        };
    }

    public void changeData(int menuId) {
        Realm realm = Realm.getDefaultInstance();
        mContentList = realm.where(ProductSeriesDb.class).equalTo("IdMenu", menuId).findAll();
        realm.close();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View mItemView;
        private ImageView mImageView;
        private TextView mNameTextView;

        ViewHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.view_listview_series_nameTextView);
            mImageView = (ImageView) itemView.findViewById(R.id.view_listview_series_imageView);
            mItemView = itemView;
        }
    }

    interface OnSerieClick {
        void onClick(int idRubric);
    }
}
