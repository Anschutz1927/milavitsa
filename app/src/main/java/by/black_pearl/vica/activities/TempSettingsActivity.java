package by.black_pearl.vica.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.MessageStatus;

import by.black_pearl.vica.R;

public class TempSettingsActivity extends AppCompatActivity {
    public static final String APP_EMAIL_KEY = "appEmail";
    private static final String sTestSubject = "Milavitsa TEST";
    private static final String sTestMessage = "You have got this message " +
            "because somebody tests this email address in Milavitsa App";

    private SharedPreferences mPrefs;
    private EditText mEmailEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_settings);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        mEmailEt = (EditText) findViewById(R.id.et_email);
        mEmailEt.setText(mPrefs.getString(APP_EMAIL_KEY, ""));

        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidEmail(mEmailEt.getText().toString())) {
                    incorrectMessage(TempSettingsActivity.this);
                } else {
                    Backendless.Messaging.sendTextEmail(sTestSubject, sTestMessage, mEmailEt.getText().toString(), getCallback());
                }
            }

            private  AsyncCallback<MessageStatus> getCallback() {
                return new AsyncCallback<MessageStatus>() {
                    @Override
                    public void handleResponse(MessageStatus response) {
                        Toast.makeText(getBaseContext(), "Письмо отправлено. Проверьте Ваш email.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(getBaseContext(), "Ошибка отправки тестового письма!", Toast.LENGTH_SHORT).show();
                    }
                };
            }
        });

        findViewById(R.id.btn_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidEmail(mEmailEt.getText().toString())) {
                    incorrectMessage(TempSettingsActivity.this);
                    return;
                }
                mPrefs.edit().putString(APP_EMAIL_KEY, mEmailEt.getText().toString()).apply();
                finish();
            }
        });
    }

    private static boolean isValidEmail(String input) {
        return !(input.equals("") ||
                !input.contains("@") ||
                !input.contains("."));
    }

    private static void incorrectMessage(Context context) {
        Toast.makeText(context, "Неправильно заполнено поле!", Toast.LENGTH_SHORT).show();
    }
}
