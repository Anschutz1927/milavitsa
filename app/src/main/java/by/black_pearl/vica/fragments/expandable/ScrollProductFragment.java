package by.black_pearl.vica.fragments.expandable;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import by.black_pearl.vica.R;
import by.black_pearl.vica.activities.MainActivity;
import by.black_pearl.vica.adapters.expandable.ScrollProductAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScrollProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScrollProductFragment extends Fragment {

    public static String ID = "id";
    private TextView mToolbarTv;

    public ScrollProductFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
      * @return A new instance of fragment ScrollProductFragment.
     */
    public static ScrollProductFragment newInstance(int id) {
        ScrollProductFragment fragment = new ScrollProductFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbarTv();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.ToolbarManager.getToolbarManager().setView(mToolbarTv);
        View view = inflater.inflate(R.layout.fragment_scroll_product, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.fragment_scroll_product_viewPager);
        int id = 0;
        if(getArguments() != null) {
            id = getArguments().getInt(ID, 0);
        }
        ScrollProductAdapter adapter = new ScrollProductAdapter(getChildFragmentManager(), id);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(getOnaPageChangeListener(adapter));
        viewPager.setCurrentItem(adapter.getCurrentPos());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.ToolbarManager.getToolbarManager().removeView(mToolbarTv);
    }

    private void setupToolbarTv() {
        mToolbarTv = new TextView(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        mToolbarTv.setLayoutParams(params);
        mToolbarTv.setText(R.string.app_name);
    }

    private ViewPager.OnPageChangeListener getOnaPageChangeListener(final ScrollProductAdapter adapter) {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mToolbarTv.setText("Модель: " + adapter.getItemArticle(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };
    }
}
