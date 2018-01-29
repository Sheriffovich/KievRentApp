package net.kievrent.booking;
/*Developed by Sheriff © */

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class ViewLinks extends Activity {
    private ListView userList;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    private Cursor userCursor;
    private MediaPlayer WarningSoundButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_viewlinks);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        databaseHelper = new DatabaseHelper(this);
        try {
            databaseHelper.updateDataBase();
        } catch (IOException mIOException) {
            Toast.makeText(getApplicationContext(), "UnableToUpdateDatabase", Toast.LENGTH_LONG).show();
            throw new Error("UnableToUpdateDatabase");
        }
        db = databaseHelper.getWritableDatabase();

        WarningSoundButton = MediaPlayer.create(ViewLinks.this, R.raw.button);
        userList = (ListView) findViewById(R.id.list);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WarningSoundButton.start();
                TextView urlStringDown = (TextView) findViewById(R.id.textDOWN);
                TextView urlStringUp = (TextView) findViewById(R.id.textUP);
                String urlDown = urlStringDown.getText().toString();
                String urlUp = urlStringUp.getText().toString();
                Toast.makeText(getApplicationContext(), urlDown + "\n" + urlUp, Toast.LENGTH_SHORT).show();
                /*Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, url);
                startActivity(Intent.createChooser(share, getApplicationContext().getString(R.string.share))); */
            }
        });
        databaseHelper = new DatabaseHelper(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        WarningSoundButton.start();
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_2, null);
        String[] headers = new String[]{DatabaseHelper.COLUMN_NAME_2, DatabaseHelper.COLUMN_URL};
        SimpleCursorAdapter userAdapter = new SimpleCursorAdapter(this, R.layout.list_item_links,
                userCursor, headers, new int[]{R.id.textUP, R.id.textDOWN}, 0);
        userAdapter.notifyDataSetChanged();
        userList.setAdapter(userAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
        userCursor.close();
    }
}