package org.abhayagiri.browseronly;

import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String url = retrieveUrl();
        final EditText urlEditor = new EditText(this);
        urlEditor.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        urlEditor.setText(url);
        urlEditor.setSelection(0, url.length());

        AlertDialog preferences = new AlertDialog.Builder(this)
                .setView(urlEditor)
                .setTitle(R.string.app_name)
                .setMessage(R.string.set_website_url)
                .setPositiveButton("Start",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                String url = urlEditor.getText().toString().trim();
                                storeUrl(url);
                                startWebView(url);
                        }
                    })
                .setCancelable(false)
                .create();
        preferences.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideUI();
    }

    private String retrieveUrl() {
        SharedPreferences preferences = this.getSharedPreferences("preferences", MODE_PRIVATE);
        String url = preferences.getString("url", getString(R.string.default_url));
        return url;
    }

    private void storeUrl(String url) {
        SharedPreferences preferences = this.getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString("url", url);
        editor.commit();
    }

    private void startWebView(String url) {
        if (setDeviceOwner()) {
            startLockTask();
        } else {
            Toast.makeText(getApplicationContext(),"Could not set device owner", Toast.LENGTH_LONG).show();
        }

        WebView myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        myWebView.loadUrl(url);
    }

    private boolean setDeviceOwner() {
        DevicePolicyManager myDevicePolicyManager =
                (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName myDPM = new ComponentName(this, DeviceAdmin.class);

        if (myDevicePolicyManager.isDeviceOwnerApp(this.getPackageName())) {
            String[] packages = {this.getPackageName()};
            myDevicePolicyManager.setLockTaskPackages(myDPM, packages);
            return true;
        } else {
            return false;
        }
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
