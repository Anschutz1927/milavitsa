package by.black_pearl.vica.adapters.compact;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import by.black_pearl.vica.R;
import by.black_pearl.vica.activities.ProductDetailActivity;
import by.black_pearl.vica.realm_db.ProductDb;
import io.realm.Realm;
import io.realm.RealmResults;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by BLACK_Pearl.
 */
public class CompactRecyclerViewAdapter
        extends RecyclerView.Adapter<CompactRecyclerViewAdapter.ViewHolder> {

    private RealmResults<ProductDb> mContentList;
    private final CompactSeriesRecyclerViewAdapter mSeriesRecyclerViewAdapter;
    private LinearLayout mLinear;
    private CardView mCard;
    private Context mContext;
    private RecyclerView.LayoutParams mDefaultRecyclerParams;

    public CompactRecyclerViewAdapter(Context context) {
        mContext = context;
        mLinear = new LinearLayout(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        mLinear.setLayoutParams(params);
        mLinear.setOrientation(LinearLayout.VERTICAL);

        RecyclerView collectionRecyclerView = new RecyclerView(mContext);
        collectionRecyclerView.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        CompactCollectionsRecyclerViewAdapter mCollectionRecyclerViewAdapter =
                new CompactCollectionsRecyclerViewAdapter(mContext, getCollectionClickListener());
        collectionRecyclerView.setAdapter(mCollectionRecyclerViewAdapter);
        collectionRecyclerView.setBackgroundColor(Color.GRAY);
        mLinear.addView(collectionRecyclerView);

        RecyclerView seriesRecyclerView = new RecyclerView(mContext);
        seriesRecyclerView.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mSeriesRecyclerViewAdapter = new CompactSeriesRecyclerViewAdapter(
                mContext, mCollectionRecyclerViewAdapter.getDefaultMenuId(), getSerieClickListener());
        seriesRecyclerView.setAdapter(mSeriesRecyclerViewAdapter);
        ViewGroup.LayoutParams seriesParams =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        seriesParams.height = 500;
        seriesRecyclerView.setLayoutParams(seriesParams);
        mLinear.addView(seriesRecyclerView);

        mCard = (CardView) LayoutInflater.from(mContext)
                .inflate(R.layout.view_collections_cardview, mLinear, false);
        mCard.addView(mLinear);

        mCard.setId(R.id.fragment_compact_recyclerview);
        loadDefaultData();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView view = (CardView) LayoutInflater
                .from(mContext).inflate(R.layout.view_listview_series, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position == 0) {
            holder.mName.setVisibility(GONE);
            holder.mImage.setVisibility(GONE);
            holder.mDesc.setVisibility(GONE);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.mLayout.getLayoutParams();
            if(mDefaultRecyclerParams == null) {
                mDefaultRecyclerParams = new RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mDefaultRecyclerParams.setMargins(
                        params.leftMargin,
                        params.topMargin,
                        params.rightMargin,
                        params.bottomMargin
                );
            }
            params.setMargins(0, 0, 0, 0);
            holder.mLayout.setLayoutParams(params);
            try {
                holder.mLayout.addView(mCard);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        if(holder.mLayout.findViewById(R.id.fragment_compact_recyclerview) != null) {
            View view = holder.mLayout.findViewById(R.id.fragment_compact_recyclerview);
            holder.mLayout.removeView(view);
            holder.mName.setVisibility(VISIBLE);
            holder.mImage.setVisibility(VISIBLE);
            holder.mDesc.setVisibility(VISIBLE);
            holder.mLayout.setLayoutParams(mDefaultRecyclerParams);
        }
        holder.mName.setText("Модель: " + String.valueOf(mContentList.get(position - 1).getArticle()));
        String image_url = "http://www.milavitsa.com/i/photo/" + mContentList.get(position - 1).getImage();
        Glide.with(mContext).load(image_url).diskCacheStrategy(DiskCacheStrategy.RESULT)
                .fitCenter().placeholder(R.drawable.ic_menu_camera).crossFade().into(holder.mImage);
        holder.mDesc.setText("Описание:\n" + mContentList.get(position - 1).getDescription());
        holder.mLayout.setOnClickListener(getOnClickAtProductListener(mContentList.get(position - 1).getId()));
    }

    @Override
    public int getItemCount() {
        return mContentList.size() + 1;
    }

    private void loadDefaultData() {
        int idRubric = mSeriesRecyclerViewAdapter.getDefaultSerieId();
        Realm realm = Realm.getDefaultInstance();
        mContentList = realm.where(ProductDb.class).equalTo("IdRubric", idRubric).findAll();
        realm.close();
    }

    private CompactCollectionsRecyclerViewAdapter.OnCollectionClick getCollectionClickListener() {
        return new CompactCollectionsRecyclerViewAdapter.OnCollectionClick() {
            @Override
            public void onClick(int menuId) {
                mSeriesRecyclerViewAdapter.changeData(menuId);
                loadDefaultData();
                notifyDataSetChanged();
            }
        };
    }

    private CompactSeriesRecyclerViewAdapter.OnSerieClick getSerieClickListener() {
        return new CompactSeriesRecyclerViewAdapter.OnSerieClick() {
            @Override
            public void onClick(int idRubric) {
                Realm realm = Realm.getDefaultInstance();
                mContentList = realm.where(ProductDb.class).equalTo("IdRubric", idRubric).findAll();
                realm.close();
                notifyDataSetChanged();
            }
        };
    }

    private View.OnClickListener getOnClickAtProductListener(final int id) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext,
                        ProductDetailActivity.class).putExtra(ProductDetailActivity.PRODUCT_ID, id));
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mName;
        private final ImageView mImage;
        private final TextView mDesc;
        private CardView mLayout;

        ViewHolder(CardView itemView) {
            super(itemView);
            mLayout = itemView;
            mName = (TextView) itemView.findViewById(R.id.view_listview_series_nameTextView);
            mImage = (ImageView) itemView.findViewById(R.id.view_listview_series_imageView);
            mDesc = (TextView) itemView.findViewById(R.id.view_listview_series_descTextView);
        }
    }
}
