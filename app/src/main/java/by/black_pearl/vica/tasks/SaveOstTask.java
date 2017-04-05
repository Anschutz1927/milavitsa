package by.black_pearl.vica.tasks;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import by.black_pearl.vica.WinRarFileWorker;
import by.black_pearl.vica.activities.MainActivity;
import by.black_pearl.vica.fragments.expandable.ExpandableCollectionsFragment;
import by.black_pearl.vica.retrofit.RetrofitManager;
import by.black_pearl.vica.retrofit.ServerApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by BLACK_Pearl.
 */

public class SaveOstTask extends AsyncTask<Void, String, Void> {
    private static final String SERVER_URL = "http://mi2b.milavitsa.by/";
    private static final String OST_URL = "/download_files/ostatki/Sv_ost.rar";
    private TextView mTextView = null;

    public SaveOstTask(TextView textView) {
        this.mTextView = textView;
    }

    @Override
    protected Void doInBackground(Void... params) {
        publishProgress("Starting download ost...");
        Retrofit retrofit = RetrofitManager.getRetrofit(SERVER_URL);
        ServerApi serverApi = retrofit.create(ServerApi.class);
        Call<ResponseBody> ostArchive = serverApi.getOstRarArchive(OST_URL);
        try {
            Response<ResponseBody> response = ostArchive.execute();
            publishProgress("Ost downloaded. Saving...");
            doWork(response.body());
        } catch (IOException e) {
            e.printStackTrace();
            publishProgress("Ost load failure! Clear cache and restart App.");
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... strings) {
        final String txt = strings[0];
        mTextView.post(new Runnable() {
            @Override
            public void run() {
                mTextView.setText(txt);
            }
        });
    }

    private void doWork(ResponseBody body) {
        File cacheDir = mTextView.getContext().getCacheDir();
        File archive = saveArchive(body, cacheDir);
        if(archive == null) {
            return;
        }
        publishProgress("Ost saved. Unpacking...");
        File unpuckedArchiveDir = unrarArchive(archive, cacheDir);
        publishProgress("Unpacked! Parsing osm data...");
        File xlsFile = WinRarFileWorker.getUnraredXlsFile(cacheDir, getWinRarCallback());
        parseXlsFile(xlsFile);
        MainActivity.FragmentChanger.getFragmentChanger().addFragmentOnStart(ExpandableCollectionsFragment.newInstance());
    }

    private File saveArchive(ResponseBody mBody, File cacheDir) {
        return WinRarFileWorker.saveResponseBody(mBody, cacheDir, getWinRarCallback());
    }

    private File unrarArchive(File archive, File dir) {
        return WinRarFileWorker.unrarFile(archive, dir, getWinRarCallback());
    }

    private void parseXlsFile(File xlsFile) {
        try {
            publishProgress("Creating POIFS.");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(xlsFile)));
            ArrayList<String> arr = new ArrayList<>();
            for(int i = 0; i < 200; i++) {
                String str = reader.readLine();
                if(str != null) {
                    arr.add(str);
                }
            }
            return;
            /*
            FileInputStream myInput = new FileInputStream(xlsFile);
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            publishProgress("Created POIFS. Opening workbook...");
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            publishProgress("Workbook opened.");
            int activeSheet = myWorkBook.getActiveSheetIndex();
            int numberOfSheets = myWorkBook.getNumberOfSheets();
            Log.i("workbook::", "activeSheet = " + activeSheet + "; numOfSheets = " + numberOfSheets);*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private WinRarFileWorker.WinRarCallback getWinRarCallback() {
        return new WinRarFileWorker.WinRarCallback() {
            @Override
            public void onMessage(String msg) {
                publishProgress(msg);
            }
        };
    }
}
