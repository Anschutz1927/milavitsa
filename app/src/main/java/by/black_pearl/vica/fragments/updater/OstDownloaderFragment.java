package by.black_pearl.vica.fragments.updater;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import by.black_pearl.vica.R;
import by.black_pearl.vica.tasks.SaveOstTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OstDownloaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OstDownloaderFragment extends Fragment {
    private TextView mTextView;
    private SaveOstTask mSaveOstTask;

    public OstDownloaderFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment OstDownloaderFragment.
     */
    public static OstDownloaderFragment newInstance() {
        OstDownloaderFragment fragment = new OstDownloaderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_upgrade, container, false);
        this.mTextView = (TextView) view.findViewById(R.id.fragment_upgrade_updateTextView);
        mTextView.setText("Ost updating...");
        mSaveOstTask = new SaveOstTask(mTextView);
        mSaveOstTask.execute();
        return view;
    }

    @Override
    public void onDestroyView() {
        if(mSaveOstTask.getStatus() == AsyncTask.Status.RUNNING) {
            mSaveOstTask.cancel(true);
        }
        super.onDestroyView();
    }
}
