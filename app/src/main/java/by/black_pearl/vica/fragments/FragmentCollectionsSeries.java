package by.black_pearl.vica.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.black_pearl.vica.R;

/**
 * Created by BLACK_Pearl.
 */

public class FragmentCollectionsSeries extends Fragment {

    public FragmentCollectionsSeries() {
    }

    public static FragmentCollectionsSeries newInstance() {
        FragmentCollectionsSeries fragmentCollectionsSeries = new FragmentCollectionsSeries();
        return fragmentCollectionsSeries;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compact_recycler_view, container, false);
        RecyclerView collectionsRecyclerView =
                (RecyclerView) view.findViewById(R.id.fragment_compact_recycler_view_collectionsRecyclerView);
        collectionsRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
       // CompactCollectionsRecyclerViewAdapter collAdapter = new CompactCollectionsRecyclerViewAdapter(getContext(),
        //        getCollectionClickListener());
       // collectionsRecyclerView.setAdapter(collAdapter);
        RecyclerView seriesRecyclerView =
                (RecyclerView) view.findViewById(R.id.fragment_compact_recycler_view_seriesRecyclerView);
        seriesRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
       // int menuId = collAdapter.getDefaultMenuId();
       // seriesRecyclerView.setAdapter(new CompactSeriesRecyclerViewAdapter(getContext(), menuId,
       //         getSerieClickListener()));
        return view;
    }
}
