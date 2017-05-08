package by.black_pearl.vica;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

//import com.github.junrar.extract.ExtractArchive;
//import com.github.junrar.rarfile.FileHeader;
//import org.apache.commons.logging.impl.SimpleLog;

/**
 * Created by BLACK_Pearl.
 */

public class WinRarFileWorker {
    private static final String TMP_PREFIX = "ost";
    private static final String TMP_SUFFIX = ".rar";
    private static final String EXTRACT_DIR = "extracted";

    /**
     * Method save Rar package to local store from Retrofit2 ResponceBody.
     * @param body Retrofit2 ResponseBody ()
     * @param directory directory for storage Rar-file.
     * @param callback to react on work
     * @return File object to downloaded rar-file.
     */
    public static File saveResponseBody(ResponseBody body, File directory, WinRarCallback callback) {
        try {
            callback.onMessage("Prepare to save...");
            File downloadedFile = new File(directory + File.separator + TMP_PREFIX + TMP_SUFFIX);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(downloadedFile);
                callback.onMessage("Saving...");
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    callback.onMessage("Saved " + fileSizeDownloaded + " of " + fileSize);
                    Log.d("RETROFIT::", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
                callback.onMessage("Saved!");
                return downloadedFile;
            } catch (IOException e) {
                e.printStackTrace();
                callback.onMessage("Save rar is failed! Terminated.");
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        }
        catch (IOException e) {
            return null;
        }
    }

    public static File getUnraredXlsFile(File destinationDir, @Nullable WinRarCallback callback) {
        if(callback != null)
            callback.onMessage("Scaning files...");
        File extDir = new File(destinationDir, EXTRACT_DIR);
        if(!extDir.exists())
            return null;
        File[] extFiles = extDir.listFiles();
        if(callback != null)
            callback.onMessage("Checking files...");
        for (File file : extFiles) {
            String[] name = file.getName().split("\\.");
            for (String s : name) {
                if (s.equals("xls")) {
                    if(callback != null)
                        callback.onMessage("Find complete.");
                    return file;
                }
            }
        }
        if(callback != null)
            callback.onMessage("Unfortunately, file not found!");
        return null;
    }

    /**
     * Call to extract rar-file.
     * @param archiveFile File object - link for rar-file in local storage
     * @param directory directory for storage unrar-files
     * @param callback callback method to sent messages
     */
    public static File unrarFile(final File archiveFile, File directory, WinRarCallback callback) {
        try {
            final File extDirectory = new File(directory, EXTRACT_DIR);
            extDirectory.mkdir();
            callback.onMessage("Prepare to unpack...");
            //ExtractArchive extractArchive = new ExtractArchive();
            //org.apache.commons.logging.Log logger = new SimpleLog("ArchiveLog::");
            //extractArchive.setLogger(logger);
            callback.onMessage("Starting unpack...");
            //extractArchive.extractArchive(archiveFile, extDirectory);
            callback.onMessage("Unpacked!");
            return extDirectory;
        } catch (Exception e) {
            e.printStackTrace();
            callback.onMessage("Error unpack! ;(");
        }
        return null;
    }

    /*private static void createDirectory(FileHeader fileHeader, File destination) {
        File file;
        if(fileHeader.isDirectory() && fileHeader.isUnicode()) {
            file = new File(destination, fileHeader.getFileNameW());
            if(!file.exists()) {
                makeDirectory(destination, fileHeader.getFileNameW());
            }
        }
        else if (fileHeader.isDirectory() && !fileHeader.isUnicode()) {
            file = new File(destination, fileHeader.getFileNameString());
            if(!file.exists()) {
                makeDirectory(destination, fileHeader.getFileNameString());
            }
        }
    }*/

    private static void makeDirectory(File destination, String name) {
        String[] dirs = name.split("\\\\");
        if (dirs == null) {
            return;
        }
        String path = "";
        for (String dir : dirs) {
            path = path + File.separator + dir;
            new File(destination, path).mkdir();
        }
    }

    /*private static File createFile(FileHeader fileHeader, File destination) {
        String name;
        if (fileHeader.isFileHeader() && fileHeader.isUnicode()) {
            name = fileHeader.getFileNameW();
        }
        else {
            name = fileHeader.getFileNameString();
        }
        File file = new File(destination, name);
        if (!file.exists()) {
            file = makeFile(destination, name);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }*/

    private static File makeFile(File destination, String name) {
        String[] dirs = name.split("\\\\");
        if (dirs == null) {
            return null;
        }
        String path = "";
        int size = dirs.length;
        if (size == 1) {
            return new File(destination, name);
        } else if (size > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                path = path + File.separator + dirs[i];
                new File(destination, path).mkdir();
            }
            path = path + File.separator + dirs[dirs.length - 1];
            return new File(destination, path);
        } else {
            return null;
        }
    }

    public interface WinRarCallback {
        void onMessage(String msg);
    }
}
