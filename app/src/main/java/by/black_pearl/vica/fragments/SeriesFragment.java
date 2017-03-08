package by.black_pearl.vica.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import by.black_pearl.vica.R;
import by.black_pearl.vica.adapters.SeriesAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeriesFragment extends Fragment {

    private static final String MENU_ID = "menuId";

    public SeriesFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SeriesFragment.
     * @param menuId
     */
    public static SeriesFragment newInstance(int menuId) {
        SeriesFragment fragment = new SeriesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MENU_ID, menuId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_series, container, false);
        ListView listView = (ListView) view.findViewById(R.id.fragment_series_listView);
        int menuId = 0;
        if(getArguments() != null) {
            menuId = getArguments().getInt(MENU_ID);
        }
        listView.setAdapter(new SeriesAdapter(getContext(), menuId));
        listView.setOnItemClickListener(getOnItemClickListener(listView));
        return view;
    }

    private AdapterView.OnItemClickListener getOnItemClickListener(ListView listView) {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = ProductsFragment.newInstance((int) id);
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
