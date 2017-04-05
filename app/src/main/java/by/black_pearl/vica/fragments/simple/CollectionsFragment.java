package by.black_pearl.vica.fragments.simple;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.black_pearl.vica.R;
import by.black_pearl.vica.adapters.simple.ViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CollectionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectionsFragment extends Fragment {

    public CollectionsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CollectionsFragment.
     */
    public static CollectionsFragment newInstance() {
        return new CollectionsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collections, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.fragment_collections_viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        return view;
    }
}
