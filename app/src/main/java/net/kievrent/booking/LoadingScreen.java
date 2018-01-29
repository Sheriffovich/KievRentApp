package net.kievrent.booking;
/*Developed by Sheriff © */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

public class LoadingScreen extends Activity {
    private MediaPlayer WarningSoundBad;
    private MediaPlayer WarningSoundGood;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_loading);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        WarningSoundGood = MediaPlayer.create(LoadingScreen.this, R.raw.button);
        WarningSoundBad = MediaPlayer.create(LoadingScreen.this, R.raw.bad);

        Thread kievRentSplashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(2000);
                    }
                } catch (InterruptedException ignored) {
                }
                finish();
                Intent mainMenu = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(mainMenu);
            }
        };
        isConnected();
        kievRentSplashThread.start();
    }


    private void isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            WarningSoundGood.start();
            Toast.makeText(getBaseContext(), R.string.granted, Toast.LENGTH_SHORT).show();
            return;
        }
        WarningSoundBad.start();
        Toast.makeText(getBaseContext(), R.string.denied, Toast.LENGTH_SHORT).show();
    }
}
