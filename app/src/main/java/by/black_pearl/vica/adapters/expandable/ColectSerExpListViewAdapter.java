package by.black_pearl.vica.adapters.expandable;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.HashMap;

import by.black_pearl.vica.R;
import by.black_pearl.vica.activities.MainActivity;
import by.black_pearl.vica.fragments.expandable.ProductsByTypeFragment;
import by.black_pearl.vica.realm_db.CollectionDb;
import by.black_pearl.vica.realm_db.ProductDb;
import by.black_pearl.vica.realm_db.ProductSeriesDb;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by BLACK_Pearl.
 */
public class ColectSerExpListViewAdapter extends BaseExpandableListAdapter {

    private final RealmResults<CollectionDb> mContentCollections;
    private final HashMap<CollectionDb, RealmResults<ProductSeriesDb>> mContentSeriesMap;
    private final Context mContext;

    public ColectSerExpListViewAdapter(Context context) {
        mContext = context;
        mContentSeriesMap = new HashMap<>();

        Realm realm = Realm.getDefaultInstance();
        mContentCollections = realm.where(CollectionDb.class).findAll();

        fillContentSeriesMap(realm);

        realm.close();
    }

    private void fillContentSeriesMap(Realm realm) {
        for(int i = 0; i < mContentCollections.size(); i++) {
            int menuId = mContentCollections.get(i).getMenuId();
            RealmResults<ProductSeriesDb> contentSeriesList =
                    realm.where(ProductSeriesDb.class).equalTo("IdMenu", menuId).findAll();
            mContentSeriesMap.put(mContentCollections.get(i), contentSeriesList);
        }
    }

    @Override
    public int getGroupCount() {
        return mContentCollections.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size = mContentSeriesMap.get(mContentCollections.get(groupPosition)).size();
        return (size / 2) + (size % 2);
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mContentCollections.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mContentSeriesMap.get(mContentCollections.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mContentSeriesMap.get(mContentCollections.get(groupPosition)).get(childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_collections_cardview, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.view_collections_cardview_nameCollectionTextView))
                .setText(mContentCollections.get(groupPosition).getName());
        String image_url = "http://www.milavitsa.com/i/photo/" + mContentCollections.get(groupPosition).getImage();
        Glide.with(mContext).load(image_url).diskCacheStrategy(DiskCacheStrategy.RESULT)
                .fitCenter().placeholder(android.R.drawable.ic_menu_camera)
                .crossFade().into((ImageView) convertView.findViewById(R.id.view_collections_cardview_image));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_series_cardview, parent, false);
        }
        int pos1 = 2 * childPosition;
        int pos2 = 2 * childPosition + 1;
        View view_imageviews = convertView.findViewById(R.id.view_series_cardview_imageviews);
        View view_textviews = convertView.findViewById(R.id.view_series_cardview_textviews);
        String image_url = "http://www.milavitsa.com/i/photo/" + mContentSeriesMap
                .get(mContentCollections.get(groupPosition)).get(pos1).getImage();
        ImageView imageView1 = (ImageView) view_imageviews.findViewById(R.id.view_series_cardview_image1);
        ImageView imageView2 = (ImageView) view_imageviews.findViewById(R.id.view_series_cardview_image2);
        TextView textView1 = (TextView) view_textviews.findViewById(R.id.view_series_cardview_name1);
        TextView textView2 = (TextView) view_textviews.findViewById(R.id.view_series_cardview_name2);
        Glide.with(mContext).load(image_url).diskCacheStrategy(DiskCacheStrategy.RESULT)
                .fitCenter().placeholder(android.R.drawable.ic_menu_camera)
                .crossFade().into(imageView1);
        textView1.setText(mContentSeriesMap.get(mContentCollections.get(groupPosition)).get(pos1).getName());
        setOnClickListener(imageView1, mContentSeriesMap.get(mContentCollections.get(groupPosition)).get(pos1).getId());
        setOnLongClickListener(imageView1, mContentSeriesMap.get(mContentCollections.get(groupPosition)).get(pos1).getId());
        if(mContentSeriesMap.get(mContentCollections.get(groupPosition)).size() > pos2) {
            imageView2.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            image_url = "http://www.milavitsa.com/i/photo/" + mContentSeriesMap
                    .get(mContentCollections.get(groupPosition)).get(pos2).getImage();
            Glide.with(mContext).load(image_url).diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .fitCenter().placeholder(android.R.drawable.ic_menu_camera)
                    .crossFade().into(imageView2);
            textView2.setText(mContentSeriesMap.get(mContentCollections.get(groupPosition)).get(pos2).getName());
            setOnClickListener(imageView2, mContentSeriesMap.get(mContentCollections.get(groupPosition)).get(pos2).getId());
            setOnLongClickListener(imageView2, mContentSeriesMap.get(mContentCollections.get(groupPosition)).get(pos2).getId());
        }
        else {
            imageView2.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    private void setOnLongClickListener(View view, final int id) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Запрос на удаление!")
                        .setMessage("Удалить серию?")
                        .setIcon(android.R.drawable.ic_delete)
                        .setCancelable(false)
                        .setPositiveButton("Да",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Realm realm = Realm.getDefaultInstance();
                                        realm.beginTransaction();
                                        realm.where(ProductDb.class).equalTo("IdRubric", id).findAll().deleteAllFromRealm();
                                        realm.where(ProductSeriesDb.class).equalTo("Id", id).findAll().deleteAllFromRealm();
                                        realm.commitTransaction();
                                        fillContentSeriesMap(realm);
                                        notifyDataSetChanged();
                                    }
                                })
                        .setNegativeButton("Нет",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });
    }

    private void setOnClickListener(View view, final int id) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.FragmentChanger.getFragmentChanger().changeFragment(ProductsByTypeFragment.newInstance(id), true);
            }
        });
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }
}
