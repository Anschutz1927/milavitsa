package by.black_pearl.vica.fragments.expandable;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.TextView;

import by.black_pearl.vica.R;
import by.black_pearl.vica.activities.MainActivity;
import by.black_pearl.vica.adapters.expandable.ColectSerExpListViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpandableCollectionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpandableCollectionsFragment extends Fragment {

    private TextView mToolbarNameTextView;

    public ExpandableCollectionsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ExpandableCollectionsFragment.
     */
    public static ExpandableCollectionsFragment newInstance() {
        return new ExpandableCollectionsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expandable_collections, container, false);
        final ExpandableListView listView = (ExpandableListView) view
                .findViewById(R.id.fragment_expandable_collections_expListView);
        listView.setAdapter(new ColectSerExpListViewAdapter(getContext()));
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for(int i = 0; i < listView.getExpandableListAdapter().getGroupCount(); i++) {
                    if(i != groupPosition && listView.isGroupExpanded(i)) {
                        listView.collapseGroup(i);
                    }
                }
            }
        });
        setupToolbar();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.ToolbarManager.getToolbarManager().removeView(mToolbarNameTextView);
    }

    private void setupToolbar() {
        mToolbarNameTextView = new TextView(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        mToolbarNameTextView.setLayoutParams(params);
        mToolbarNameTextView.setText(R.string.app_name);
        MainActivity.ToolbarManager.getToolbarManager().addView(mToolbarNameTextView);
    }

}
