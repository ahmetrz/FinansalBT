package net.azurewebsites.yemekyapp.yemekyapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static TextView internetStatus;


    WebView webView;

    private class WebViewClient extends android.webkit.WebViewClient {


    }

    @Override
    public boolean onKeyDown ( int keyCode, KeyEvent event ) {
        if(event.getAction()==KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    }
                    else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        internetStatus = (TextView) findViewById(R.id.internet_status);

        // At activity startup we manually check the internet status and change
        // the text status
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            changeTextStatus(true);

            webView = (WebView) findViewById(R.id.web1);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);


            webView.setWebViewClient(new MainActivity.WebViewClient(){

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url){
                    view.loadUrl(url);
                    return true;
                }
            });
            webView.loadUrl("https://yemekyapp.azurewebsites.net");

        } else {
            changeTextStatus(false);
        }

    }

    // Method to change the text status
    public void changeTextStatus(boolean isConnected) {

        // Change status according to boolean value
        if (isConnected) {
           internetStatus.setText("");
           internetStatus.setTextColor(Color.parseColor("#00ff00"));

        } else {
            AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(this); //Mesaj Penceresini Yaratalım
            alertDialogBuilder.setTitle("Lütfen internet bağlantınızı kontrol ediniz.").setCancelable(false).setPositiveButton("Kapat", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int id) { //Eğer evet butonuna basılırsa
                    dialog.dismiss();
                    android.os.Process.killProcess(android.os.Process.myPid());
//Uygulamamızı sonlandırıyoruz.
                }

            });
            internetStatus.setText("İnternet Bağlantınızı Kontrol Edin.");
            internetStatus.setTextColor(Color.parseColor("#7e0077"));
            internetStatus.setTextSize(0,25);
            internetStatus.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            alertDialogBuilder.create().show();
//son olarak alertDialogBuilder'ı oluşturup ekranda görüntületiyoruz.
            // return super.onKeyDown(keyCode, event);



        }
    }

    @Override
    protected void onPause() {

        super.onPause();
        MyApplication.activityPaused();// On Pause notify the Application
    }

    @Override
    protected void onResume() {

        super.onResume();
        MyApplication.activityResumed();// On Resume notify the Application
    }

}


