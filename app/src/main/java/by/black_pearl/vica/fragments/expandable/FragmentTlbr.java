package by.black_pearl.vica.fragments.expandable;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import by.black_pearl.vica.activities.MainActivity;

/**
 * Created by BLACK_Pearl.
 */

abstract class FragmentTlbr extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.ToolbarManager toolbarMngr = MainActivity.ToolbarManager.getToolbarManager();
        if (toolbarMngr.getToolbarLayout().getChildCount() != 0) {
            toolbarMngr.removeViews();
        }
    }
}
