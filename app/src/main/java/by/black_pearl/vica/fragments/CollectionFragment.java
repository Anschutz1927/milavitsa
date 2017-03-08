package by.black_pearl.vica.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import by.black_pearl.vica.R;
import by.black_pearl.vica.realm_db.CollectionDb;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CollectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectionFragment extends Fragment {

    private static final String POSITION = "position";
    private int mPosition;
    private RealmResults<CollectionDb> mCollections;

    public CollectionFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment CollectionFragment.
     */
    public static CollectionFragment newInstance(int position) {
        CollectionFragment fragment = new CollectionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            this.mPosition = getArguments().getInt(POSITION);
        }
        Realm realm = Realm.getDefaultInstance();
        this.mCollections = realm.where(CollectionDb.class).findAll();
        realm.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);
        ((TextView) view.findViewById(R.id.fragment_collection_NameTextView))
                .setText(mCollections.get(mPosition).getName());
        String image_url = "http://www.milavitsa.com/i/photo/" + mCollections.get(mPosition).getImage();
        Picasso.with(getContext()).load(image_url).placeholder(R.drawable.ic_menu_camera).centerInside()
                .fit().into((ImageView) view.findViewById(R.id.fragment_collection_imageView));
        view.setOnClickListener(getOnClickListener());
        return view;
    }

    private View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(
                        R.id.content_main,
                        SeriesFragment.newInstance(mCollections.get(mPosition).getMenuId())
                );
                fragmentTransaction.commit();
            }
        };
    }

}
