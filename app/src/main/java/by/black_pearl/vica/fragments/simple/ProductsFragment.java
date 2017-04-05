package by.black_pearl.vica.fragments.simple;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import by.black_pearl.vica.R;
import by.black_pearl.vica.activities.MainActivity;
import by.black_pearl.vica.adapters.simple.GridViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsFragment extends Fragment {

    private static final String ID = "id";

    public ProductsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ProductsFragment.
     * @param id
     */
    public static ProductsFragment newInstance(int id) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.fragment_products_gridView);
        int id = 0;
        if(getArguments() != null) {
            id = getArguments().getInt(ID);
        }
        gridView.setAdapter(new GridViewAdapter(getContext(), id));
        gridView.setOnItemClickListener(getOnItemClickListener());
        return view;
    }

    private AdapterView.OnItemClickListener getOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.FragmentChanger.getFragmentChanger().changeFragment(ProductFragment.newInstance((int) id), true);
            }
        };
    }
}
