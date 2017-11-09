package org.abhayagiri.browseronly;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.LinearLayout;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import java.lang.Runnable;

public class MainActivity extends AppCompatActivity {

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setPropertiesFromPreferences();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText urlEdit = new EditText(this);
        urlEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        urlEdit.setText(url);
        urlEdit.setHint(getString(R.string.url_hint));
        layout.addView(urlEdit);

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(layout)
                .setTitle(R.string.app_name)
                .setPositiveButton(android.R.string.ok, null)
                .setCancelable(false)
                .create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = urlEdit.getText().toString().trim();
                if (url.isEmpty()) {
                    urlEdit.setError(getString(R.string.error_blank));
                };
                if (!url.isEmpty()) {
                    dialog.dismiss();
                    setPreferencesFromProperties();
                    ensureLockTask();
                    hideUI();
                    startWebView();
                }
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideUI();
                handler.postDelayed(this, 5000);
            }
        }, 5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideUI();
    }

    private void ensureLockTask() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager.getLockTaskModeState() == ActivityManager.LOCK_TASK_MODE_NONE) {
            startLockTask();
        }
    }

    private void setPropertiesFromPreferences() {
        SharedPreferences preferences = this.getSharedPreferences("preferences", MODE_PRIVATE);
        url = preferences.getString("url", "");
    }

    private void setPreferencesFromProperties() {
        SharedPreferences preferences = this.getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString("url", url);
        editor.commit();
    }

    private void startWebView() {
        WebView myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        myWebView.loadUrl(url);
    }

    private int hideFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    private void hideUI() {
        getWindow().getDecorView().setSystemUiVisibility(hideFlags);
    }
}
