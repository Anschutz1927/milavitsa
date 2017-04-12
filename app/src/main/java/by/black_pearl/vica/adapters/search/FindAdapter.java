package by.black_pearl.vica.adapters.search;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import io.realm.RealmResults;

/**
 * Created by BLACK_Pearl.
 */

public class FindAdapter extends RecyclerView.Adapter<FindAdapter.Holder> {

    private final ArrayList<ProductDb> mProducts;
    private final Context mContext;

    public FindAdapter(Context context) {
        this.mContext = context;
        this.mProducts = new ArrayList<>();
    }

    public void changeData(RealmResults<ProductDb> products) {
        int size = mProducts.size();
        this.mProducts.clear();
        notifyItemRangeRemoved(0, size);
        if (products != null && products.size() != 0) {
            this.mProducts.addAll(products);
        }
        notifyItemRangeChanged(0, mProducts.size());
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.view_series_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        int pos1 = 2 * position;
        int pos2 = 2 * position + 1;
        String image_url = "http://www.milavitsa.com/i/photo/" + mProducts.get(pos1).getImage();
        Glide.with(mContext).load(image_url).diskCacheStrategy(DiskCacheStrategy.RESULT)
                .fitCenter().placeholder(android.R.drawable.ic_menu_camera)
                .crossFade().into(holder.imageView1);
        holder.textView1.setText(mProducts.get(pos1).getArticle());
        if(mProducts.size() > pos2) {
            holder.imageView2.setVisibility(View.VISIBLE);
            holder.textView2.setVisibility(View.VISIBLE);
            image_url = "http://www.milavitsa.com/i/photo/" + mProducts.get(pos2).getImage();
            Glide.with(mContext).load(image_url).diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .fitCenter().placeholder(android.R.drawable.ic_menu_camera)
                    .crossFade().into(holder.imageView2);
            holder.textView2.setText(mProducts.get(pos2).getArticle());
        }
        else {
            holder.imageView2.setVisibility(View.INVISIBLE);
            holder.textView2.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        int size =  mProducts.size();
        return (size / 2) + (size % 2);
    }

    class Holder extends RecyclerView.ViewHolder {
        private final ImageView imageView1;
        private final ImageView imageView2;
        private final TextView textView1;
        private final TextView textView2;

        Holder(View itemView) {
            super(itemView);
            CardView.LayoutParams params =
                    new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.findViewById(R.id.cv_dual_column).setLayoutParams(params);
            imageView1 = (ImageView) itemView.findViewById(R.id.view_series_cardview_image1);
            imageView2 = (ImageView) itemView.findViewById(R.id.view_series_cardview_image2);
            textView1 = (TextView) itemView.findViewById(R.id.view_series_cardview_name1);
            textView2 = (TextView) itemView.findViewById(R.id.view_series_cardview_name2);
            imageView1.setOnClickListener(getClickListener(0));
            imageView2.setOnClickListener(getClickListener(1));
        }

        View.OnClickListener getClickListener(final int numberOfView) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = mProducts.get(getAdapterPosition() * 2 + numberOfView).getId();
                    MainActivity.FragmentChanger.getFragmentChanger().changeFragment(ScrollProductFragment.newInstance(id), true);
                }
            };
        }
    }
}
