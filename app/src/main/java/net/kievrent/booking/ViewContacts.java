package net.kievrent.booking;
/*Developed by Sheriff © */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class ViewContacts extends Activity implements View.OnClickListener {
    private ListView userList;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    private Cursor userCursor;
    private TextView tvName;
    private TextView tvPhone;
    private Button buttonAdd;
    private Button buttonNew;
    private MediaPlayer WarningSoundButton;
    private MediaPlayer WarningSoundClear;
    private MediaPlayer WarningSoundBad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_viewcontacts);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        databaseHelper = new DatabaseHelper(this);
        try {
            databaseHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
        db = databaseHelper.getWritableDatabase();

        WarningSoundButton = MediaPlayer.create(ViewContacts.this, R.raw.button);
        WarningSoundClear = MediaPlayer.create(ViewContacts.this, R.raw.clear);
        WarningSoundBad = MediaPlayer.create(ViewContacts.this, R.raw.bad);
        buttonAdd = (Button) findViewById(R.id.ButtonAdd);
        buttonAdd.setOnClickListener(this);
        buttonAdd.setVisibility(View.GONE);
        buttonNew = (Button) findViewById(R.id.ButtonNew);
        buttonNew.setOnClickListener(this);
        tvName = (TextView) findViewById(R.id.tvName);
        tvName.setVisibility(View.GONE);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvPhone.setVisibility(View.GONE);
        userList = (ListView) findViewById(R.id.list);
        userList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                WarningSoundButton.start();

                final CharSequence columnValue = String.valueOf(userCursor.getInt(0));
                AlertDialog.Builder deleteOrNot = new AlertDialog.Builder(ViewContacts.this);
                deleteOrNot.setMessage(R.string.deleteornot);
                deleteOrNot.setNegativeButton(R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                WarningSoundButton.start();
                                Toast.makeText(getApplicationContext(), R.string.saved, Toast.LENGTH_SHORT).show();
                            }
                        });
                deleteOrNot.setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                WarningSoundClear.start();
                                Toast.makeText(getApplicationContext(), R.string.deleted, Toast.LENGTH_SHORT).show();
                                db.delete(DatabaseHelper.TABLE_1, DatabaseHelper.COLUMN_ID + " = " + columnValue, null);
                                onResume();
                            }
                        });
                deleteOrNot.show();
                return false;
            }
        });

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WarningSoundButton.start();
                TextView phoneNumber = (TextView) view.findViewById(R.id.textDOWN);
                String phoneNumberCall = phoneNumber.getText().toString();
                final Intent callingMake = new Intent(Intent.ACTION_DIAL);
                callingMake.setData(Uri.parse("tel:" + phoneNumberCall));
                final Intent smsSend = new Intent(Intent.ACTION_VIEW);
                smsSend.putExtra("address", phoneNumberCall);
                smsSend.setType("vnd.android-dir/mms-sms");
                smsSend.putExtra("sms_body", "");

                AlertDialog.Builder smsOrCall = new AlertDialog.Builder(ViewContacts.this);
                smsOrCall.setMessage(R.string.smsorcall);
                smsOrCall.setNegativeButton(R.string.sms,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                WarningSoundButton.start();
                                startActivity(smsSend);
                            }
                        });
                smsOrCall.setPositiveButton(R.string.call,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                WarningSoundButton.start();
                                startActivity(callingMake);
                            }
                        });
                smsOrCall.show();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ButtonAdd:
                String name = tvName.getText().toString();
                String phone = tvPhone.getText().toString();
                WarningSoundButton.start();
                ContentValues insertData = new ContentValues();
                insertData.put("name", name);
                insertData.put("phone", phone);

                if (name.equals("") || phone.equals("") || phone.equals("+380")) {
                    WarningSoundBad.start();
                    Toast.makeText(getApplicationContext(), R.string.warning, Toast.LENGTH_SHORT).show();
                } else {
                    db.insert("contacts", null, insertData);
                    userList.setVisibility(View.VISIBLE);
                    buttonNew.setVisibility(View.VISIBLE);
                    buttonAdd.setVisibility(View.GONE);
                    tvName.setVisibility(View.GONE);
                    tvPhone.setVisibility(View.GONE);
                    if (getCurrentFocus() != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                    onResume();
                }
                break;
            case R.id.ButtonNew:
                WarningSoundButton.start();
                userList.setVisibility(View.GONE);
                buttonNew.setVisibility(View.GONE);
                buttonAdd.setVisibility(View.VISIBLE);
                tvName.setVisibility(View.VISIBLE);
                tvName.setText("");
                tvPhone.setVisibility(View.VISIBLE);
                tvPhone.setText(R.string.tvPhone);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_1 + " order by name", null);
        String[] headers = new String[]{DatabaseHelper.COLUMN_NAME_1, DatabaseHelper.COLUMN_PHONE};
        SimpleCursorAdapter userAdapter = new SimpleCursorAdapter(this, R.layout.list_item_contacts,
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