package by.black_pearl.vica;

import android.app.Application;

import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

/**
 * Created by BLACK_Pearl.
 */
@ReportsCrashes(mailTo = "anschutz1927@gmail.com",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)
public class MilavitsaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //ACRA.init(this);
    }
}
