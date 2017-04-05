package by.black_pearl.vica.fragments.updater;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import by.black_pearl.vica.R;
import by.black_pearl.vica.tasks.SaveDataTask;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpgradeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpgradeFragment extends Fragment {

    private SaveDataTask mSaveDataTask;

    public UpgradeFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment UpgradeFragment.
     */
    public static UpgradeFragment newInstance() {
        return new UpgradeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upgrade, container, false);
        TextView updateTextView = (TextView) view.findViewById(R.id.fragment_upgrade_updateTextView);
        this.mSaveDataTask = new SaveDataTask(getResources(), updateTextView);
        mSaveDataTask.execute();
        return view;
    }

    @Override
    public void onDestroyView() {
        if(mSaveDataTask != null && mSaveDataTask.getStatus() == AsyncTask.Status.RUNNING) {
            mSaveDataTask.cancel();
        }
        super.onDestroyView();
    }
}
