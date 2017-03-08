package by.black_pearl.vica.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.black_pearl.vica.R;
import by.black_pearl.vica.adapters.CollectionsRecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CollectionRecyclerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectionRecyclerFragment extends Fragment {

    public CollectionRecyclerFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * @return A new instance of fragment CollectionRecyclerFragment.
     */
    public static CollectionRecyclerFragment newInstance() {
        CollectionRecyclerFragment fragment = new CollectionRecyclerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_collection_recycler, container, false);
        RecyclerView recyclerView = ((RecyclerView) view.findViewById(R.id.fragment_collection_recycler_recyclerView));
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new CollectionsRecyclerViewAdapter(getContext(), getListener()));
        return view;
    }

    private CollectionsRecyclerViewAdapter.OnItemClickListener getListener() {
        return new CollectionsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int itemId) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = SeriesFragment.newInstance(itemId);
                fragmentTransaction.replace(
                        R.id.content_main,
                        fragment
                );
                fragmentTransaction.addToBackStack(fragment.getClass().getName());
                fragmentTransaction.commit();
            }
        };
    }

}
