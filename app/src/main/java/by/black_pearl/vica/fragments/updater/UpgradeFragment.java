package by.black_pearl.vica.fragments.updater;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import by.black_pearl.vica.R;
import by.black_pearl.vica.activities.MainActivity;
import by.black_pearl.vica.fragments.expandable.ExpandableCollectionsFragment;
import by.black_pearl.vica.tasks.SaveMilavitsaDataTask;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpgradeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpgradeFragment extends Fragment {
    private boolean mIsAttached;
    private boolean mIsFinished;

    private SaveMilavitsaDataTask mSaveMilavitsaDataTask;

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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mIsFinished) {
            changeFragmentAtFinish();
        }
        else {
            mIsAttached = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upgrade, container, false);
        TextView updateTextView = (TextView) view.findViewById(R.id.fragment_upgrade_updateTextView);
        this.mSaveMilavitsaDataTask = new SaveMilavitsaDataTask(getResources(), updateTextView, getCallback());
        mSaveMilavitsaDataTask.execute();
        return view;
    }

    @Override
    public void onDestroyView() {
        if(mSaveMilavitsaDataTask != null && mSaveMilavitsaDataTask.getStatus() == AsyncTask.Status.RUNNING) {
            mSaveMilavitsaDataTask.cancel(true);
        }
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        mIsAttached = false;
        super.onDetach();
    }

    private void changeFragmentAtFinish() {
        MainActivity.FragmentChanger.getFragmentChanger().addFragmentOnStart(ExpandableCollectionsFragment.newInstance());
    }

    private SaveMilavitsaDataTask.SaveMilavitsaDataTaskCallback getCallback() {
        return new SaveMilavitsaDataTask.SaveMilavitsaDataTaskCallback() {
            @Override
            public void onPostExecute() {
                if (mIsAttached) {
                    changeFragmentAtFinish();
                }
                else {
                    mIsFinished = true;
                }
            }
        };
    }
}
