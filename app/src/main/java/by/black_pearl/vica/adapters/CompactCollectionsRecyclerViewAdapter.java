package by.black_pearl.vica.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import by.black_pearl.vica.realm_db.CollectionDb;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by BLACK_Pearl.
 */
public class CompactCollectionsRecyclerViewAdapter
        extends RecyclerView.Adapter<CompactCollectionsRecyclerViewAdapter.ViewHolder> {
    private final RealmResults<CollectionDb> mContentList;
    private final OnCollectionClick mListener;
    private Context mContext;

    public CompactCollectionsRecyclerViewAdapter(Context context, OnCollectionClick collectionClickListener) {
        mContext = context;
        mListener = collectionClickListener;
        Realm realm = Realm.getDefaultInstance();
        mContentList = realm.where(CollectionDb.class).findAll();
        realm.close();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView view = new TextView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 15);
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.WHITE);
        view.setTextColor(Color.BLACK);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mContentList.get(position).getName());
        holder.mTextView.setOnClickListener(onClickListener(mContentList.get(position).getMenuId()));
    }

    private View.OnClickListener onClickListener(final int menuId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(menuId);
            }
        };
    }

    @Override
    public int getItemCount() {
        return mContentList.size();
    }

    public int getDefaultMenuId() {
        if(mContentList.size() > 0) {
            return mContentList.get(0).getMenuId();
        }
        else {
            return 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTextView;

        ViewHolder(TextView itemView) {
            super(itemView);
            mTextView = itemView;
        }
    }

    interface OnCollectionClick {
        void onClick(int menuId);
    }
}
