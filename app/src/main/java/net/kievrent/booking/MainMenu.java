package net.kievrent.booking;
/*Developed by Sheriff © */

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class MainMenu extends Activity implements View.OnClickListener {
    private static long back_pressed;
    private MediaPlayer WarningSoundBack;
    private MediaPlayer WarningSoundButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mainmenu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        WarningSoundBack = MediaPlayer.create(MainMenu.this, R.raw.back);
        WarningSoundButton = MediaPlayer.create(MainMenu.this, R.raw.button);
        Button buttonCalendar = (Button) findViewById(R.id.ButtonCalendar);
        buttonCalendar.setOnClickListener(this);
        Button buttonCheck = (Button) findViewById(R.id.ButtonCheck);
        buttonCheck.setOnClickListener(this);
        Button buttonPause = (Button) findViewById(R.id.ButtonPause);
        buttonPause.setOnClickListener(this);
        Button buttonContacts = (Button) findViewById(R.id.ButtonContacts);
        buttonContacts.setOnClickListener(this);
        Button buttonList = (Button) findViewById(R.id.ButtonList);
        buttonList.setOnClickListener(this);
        Button buttonManagers = (Button) findViewById(R.id.ButtonManagers);
        buttonManagers.setOnClickListener(this);
        Button buttonAbout = (Button) findViewById(R.id.ButtonAbout);
        buttonAbout.setOnClickListener(this);
    }

    public void onClick(View v) {
        Intent intent = new
                Intent("kievrent.Browser");
        switch (v.getId()) {
            case R.id.ButtonCalendar:
                WarningSoundButton.start();
                intent.setData(Uri.parse("http://apartment.kiev.ua/partner/partner_top.php"));
                startActivity(intent);
                break;
            case R.id.ButtonCheck:
                WarningSoundButton.start();
                intent.setData(Uri.parse("http://apartment.kiev.ua/new_calendar/bottom/bottom.php"));
                startActivity(intent);
                break;
            case R.id.ButtonPause:
                WarningSoundButton.start();
                intent.setData(Uri.parse("http://apartment.kiev.ua/new_calendar/top/add_preorder.php"));
                startActivity(intent);
                break;
            case R.id.ButtonList:
                WarningSoundButton.start();
                intent.setData(Uri.parse("http://apartment.kiev.ua/new_calendar/menu/partners.php"));
                startActivity(intent);
                break;
            case R.id.ButtonContacts:
                WarningSoundButton.start();
                Intent intentList = new Intent(this, ViewContacts.class);
                startActivity(intentList);
                break;
            case R.id.ButtonManagers:
                WarningSoundButton.start();
                intent.setData(Uri.parse("http://apartment.kiev.ua/new_calendar/top/orders_by_men.php"));
                startActivity(intent);
                break;
            case R.id.ButtonAbout:
                WarningSoundButton.start();
                Intent intentListFotos = new Intent(this, ViewLinks.class);
                startActivity(intentListFotos);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 1000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            WarningSoundBack.start();
            Toast.makeText(getBaseContext(), R.string.quit,
                    Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }
}
