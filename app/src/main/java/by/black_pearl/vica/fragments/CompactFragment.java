package by.black_pearl.vica.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.black_pearl.vica.R;
import by.black_pearl.vica.adapters.CompactRecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompactFragment extends Fragment {

    public CompactFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CompactFragment.
     */
    public static CompactFragment newInstance() {
        CompactFragment fragment = new CompactFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compact, container, false);
        RecyclerView contentRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_compact_contentRecyclerView);
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        contentRecyclerView.setAdapter(new CompactRecyclerViewAdapter(getContext()));
        return view;
    }

}
