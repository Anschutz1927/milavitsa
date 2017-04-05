package by.black_pearl.vica.adapters.simple;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import by.black_pearl.vica.R;
import by.black_pearl.vica.realm_db.CollectionDb;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by BLACK_Pearl.
 */
public class CollectionsRecyclerViewAdapter extends RecyclerView.Adapter<CollectionsRecyclerViewAdapter.CollectionView> {

    private final RealmResults<CollectionDb> mCollections;
    private final Context mContext;
    private OnItemClickListener mListener;

    public CollectionsRecyclerViewAdapter(Context context, OnItemClickListener listener) {
        this.mContext = context;
        this.mListener = listener;
        Realm realm = Realm.getDefaultInstance();
        this.mCollections = realm.where(CollectionDb.class).findAll();
        realm.close();
    }
    @Override
    public CollectionView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_collection, parent, false);
        return new CollectionView(view);
    }

    @Override
    public void onBindViewHolder(CollectionView holder, final int position) {
        holder.collectionNameTextView.setText(mCollections.get(position).getName());
        String image_url = "http://www.milavitsa.com/i/photo/" + mCollections.get(position).getImage();
        Glide.with(mContext).load(image_url).diskCacheStrategy(DiskCacheStrategy.RESULT)
                .fitCenter().placeholder(R.drawable.ic_menu_camera).crossFade().into(holder.collectionImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(mCollections.get(position).getMenuId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCollections.size();
    }

    public static class CollectionView extends RecyclerView.ViewHolder {
        private final TextView collectionNameTextView;
        private final ImageView collectionImage;

        public CollectionView(View itemView) {
            super(itemView);
            this.collectionNameTextView = (TextView) itemView.findViewById(R.id.fragment_collection_NameTextView);
            this.collectionImage = (ImageView) itemView.findViewById(R.id.fragment_collection_imageView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int itemId);
    }
}
