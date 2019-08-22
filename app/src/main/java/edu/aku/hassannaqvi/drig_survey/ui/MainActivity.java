package edu.aku.hassannaqvi.drig_survey.ui;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.aku.hassannaqvi.drig_survey.R;
import edu.aku.hassannaqvi.drig_survey.contracts.FormsContract;
import edu.aku.hassannaqvi.drig_survey.contracts.VersionAppContract;
import edu.aku.hassannaqvi.drig_survey.core.AndroidDatabaseManager;
import edu.aku.hassannaqvi.drig_survey.core.DatabaseHelper;
import edu.aku.hassannaqvi.drig_survey.core.MainApp;
import edu.aku.hassannaqvi.drig_survey.databinding.ActivityMainBinding;
import edu.aku.hassannaqvi.drig_survey.get.GetAllData;
import edu.aku.hassannaqvi.drig_survey.sync.SyncAllData;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    String dtToday = new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date().getTime());
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    private Boolean exit = false;
    private String rSumText = "";
    DatabaseHelper db;

    static File file;
    DownloadManager downloadManager;
    Long refID;
    SharedPreferences sharedPrefDownload;
    SharedPreferences.Editor editorDownload;
    ActivityMainBinding bi;
    VersionAppContract versionAppContract;
    String m_Text = "", preVer = "", newVer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Binding setting
        bi = DataBindingUtil.setContentView(this, R.layout.activity_main);
        bi.setCallback(this);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        bi.lblheader.setText("Welcome! " + MainApp.userName.toUpperCase());

        /*TagID Start*/
        sharedPref = getSharedPreferences("tagName", MODE_PRIVATE);
        editor = sharedPref.edit();

        sharedPrefDownload = getSharedPreferences("appDownload", MODE_PRIVATE);
        editorDownload = sharedPrefDownload.edit();


        builder = new AlertDialog.Builder(MainActivity.this);
        dialog = builder.create();

        ImageView img = new ImageView(getApplicationContext());
        img.setImageResource(R.drawable.tagimg);
        img.setPadding(0, 15, 0, 15);
        builder.setCustomTitle(img);

        final EditText input = new EditText(MainActivity.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.requestFocus();
        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                if (!m_Text.equals("")) {
                    editor.putString("tagName", "T-" + m_Text);
                    editor.commit();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        if (sharedPref.getString("tagName", null) == "" || sharedPref.getString("tagName", null) == null) {
            builder.show();
        }
        /*TagID End*/


        db = new DatabaseHelper(this);

//        Admin checking
        if (MainApp.admin) {
            bi.adminsec.setVisibility(View.VISIBLE);

            Collection<FormsContract> todaysForms = db.getTodayForms();
            Collection<FormsContract> unsyncedForms = db.getUnsyncedForms(null);

            rSumText += "TODAY'S RECORDS SUMMARY\r\n";

            rSumText += "=======================\r\n";
            rSumText += "\r\n";
            rSumText += "Total Forms Today: " + todaysForms.size() + "\r\n";
            rSumText += "\r\n";
            if (todaysForms.size() > 0) {
                rSumText += "\tFORMS' LIST: \r\n";
                String iStatus;
                rSumText += "--------------------------------------------------\r\n";
                rSumText += "[ DSS_ID ] \t[Form Status] \t[Sync Status]----------\r\n";
                rSumText += "--------------------------------------------------\r\n";

                for (FormsContract fc : todaysForms) {
                    if (fc.getIstatus() != null) {
                        switch (fc.getIstatus()) {
                            case "1":
                                iStatus = "\tComplete";
                                break;
                            case "2":
                                iStatus = "\tIncomplete";
                                break;
                            case "3":
                                iStatus = "\tRefused";
                                break;
                            case "4":
                                iStatus = "\tRefused";
                                break;
                            default:
                                iStatus = "\tN/A";
                        }
                    } else {
                        iStatus = "\tN/A";
                    }

                    //rSumText += fc.getDSSID();

                    rSumText += " " + iStatus + " ";

                    rSumText += (fc.getSynced() == null ? "\t\tNot Synced" : "\t\tSynced");
                    rSumText += "\r\n";
                    rSumText += "--------------------------------------------------\r\n";
                }
            }

            SharedPreferences syncPref = getSharedPreferences("SyncInfo", Context.MODE_PRIVATE);
            rSumText += "Last Data Download: \t" + syncPref.getString("LastDownSyncServer", "Never Updated");
            rSumText += "\r\n";
            rSumText += "Last Data Upload: \t" + syncPref.getString("LastUpSyncServer", "Never Synced");
            rSumText += "\r\n";
            rSumText += "\r\n";
            rSumText += "Unsynced Forms: \t" + unsyncedForms.size();
            rSumText += "\r\n";

            Log.d(TAG, "onCreate: " + rSumText);
            bi.recordSummary.setText(rSumText);

        } else {
            bi.adminsec.setVisibility(View.GONE);
        }

//        Testing visibility
        if (Integer.valueOf(MainApp.versionName.split("\\.")[0]) > 0) {
            bi.testing.setVisibility(View.GONE);
        } else {
            bi.testing.setVisibility(View.VISIBLE);
        }

//        Updating App
        updatingApp();

    }

    private void updatingApp() {

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {

                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(sharedPrefDownload.getLong("refID", 0));

                    downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Cursor cursor = downloadManager.query(query);
                    if (cursor.moveToFirst()) {
                        int colIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(colIndex)) {

                            editorDownload.putBoolean("flag", true);
                            editorDownload.commit();

                            Toast.makeText(context, "New App downloaded!!", Toast.LENGTH_SHORT).show();
                            bi.lblAppVersion.setText("Typbar App New Version " + newVer + "  Downloaded.");

                            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                            if (taskInfo.get(0).topActivity.getClassName().equals(MainActivity.class.getName())) {
//                                InstallNewApp(newVer, preVer);
                                showDialog(newVer, preVer);
                            }
                        }
                    }
                }
            }
        };

        versionAppContract = new Gson().fromJson(getSharedPreferences("main", Context.MODE_PRIVATE).getString("appVersion", ""), VersionAppContract.class);
        if (versionAppContract != null) {

            if (versionAppContract.getVersioncode() != null) {
                preVer = MainApp.versionName + "." + MainApp.versionCode;
                newVer = versionAppContract.getVersionname() + "." + versionAppContract.getVersioncode();

                if (MainApp.versionCode < Integer.valueOf(versionAppContract.getVersioncode())) {
                    bi.lblAppVersion.setVisibility(View.VISIBLE);

                    String fileName = MainApp.DATABASE_NAME.replace(".db", "-New-Apps");
                    file = new File(Environment.getExternalStorageDirectory() + File.separator + fileName, versionAppContract.getPathname());

                    if (file.exists()) {
                        bi.lblAppVersion.setText("Typbar New Version " + newVer + "  Downloaded.");
//                    InstallNewApp(newVer, preVer);
                        showDialog(newVer, preVer);
                    } else {
                        NetworkInfo networkInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected()) {

                            bi.lblAppVersion.setText("Typbar App New Version " + newVer + " Downloading..");
                            downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                            Uri uri = Uri.parse(MainApp._UPDATE_URL + versionAppContract.getPathname());
                            DownloadManager.Request request = new DownloadManager.Request(uri);
                            request.setDestinationInExternalPublicDir(fileName, versionAppContract.getPathname())
                                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                    .setTitle("Downloading Typbar App new App ver." + newVer);
                            refID = downloadManager.enqueue(request);

                            editorDownload.putLong("refID", refID);
                            editorDownload.putBoolean("flag", false);
                            editorDownload.commit();

                        } else {
                            bi.lblAppVersion.setText("Typbar App New Version " + newVer + "  Available..\n(Can't download.. Internet connectivity issue!!)");
                        }
                    }

                } else {
                    bi.lblAppVersion.setVisibility(View.GONE);
                    bi.lblAppVersion.setText(null);
                }
            }
        }

        registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    void showDialog(String newVer, String preVer) {
        FragmentManager ft = getSupportFragmentManager();
        FragmentTransaction transaction = ft.beginTransaction();
        Fragment prev = ft.findFragmentByTag("dialog");
        if (prev != null) {
            transaction.remove(prev);
        }
        transaction.addToBackStack(null);
        DialogFragment newFragment = MyDialogFragment.newInstance(newVer, preVer);
        newFragment.show(ft, "dialog");

    }

    private boolean permissiongrantedStuff() {
        if (versionAppContract.getVersioncode() != null) {
            if (MainApp.versionCode < Integer.valueOf(versionAppContract.getVersioncode())) {
                if (sharedPrefDownload.getBoolean("flag", true) && file.exists()) {
                    showDialog(newVer, preVer);
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            Toast.makeText(this, "First Sync data!!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void openForm() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (sharedPref.getString("tagName", null) != "" && sharedPref.getString("tagName", null) != null && !MainApp.userName.equals("0000")) {

                if (!permissiongrantedStuff()) return;

                startActivity(new Intent(MainActivity.this, SectionAActivity.class));

            } else {
                builder = new AlertDialog.Builder(MainActivity.this);
                final AlertDialog dialog = builder.create();
                ImageView img = new ImageView(getApplicationContext());
                img.setImageResource(R.drawable.tagimg);
                img.setPadding(0, 15, 0, 15);
                builder.setCustomTitle(img);

                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (b) {
                            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        }
                    }
                });
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        if (!m_Text.equals("")) {
                            editor.putString("tagName", "T-" + m_Text);
                            editor.commit();

                            if (!MainApp.userName.equals("0000")) {
//                                startActivity(oF);
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }

        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    MainActivity.this);
            alertDialogBuilder
                    .setMessage("GPS is disabled in your device. Enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Enable GPS",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(callGPSSettingIntent);
                                }
                            });
            alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();

        }

    }

    public void testGPS(View v) {

        SharedPreferences sharedPref = getSharedPreferences("GPSCoordinates", Context.MODE_PRIVATE);
        Log.d("MAP", "testGPS: " + sharedPref.getAll().toString());
        Map<String, ?> allEntries = sharedPref.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("Map", entry.getKey() + ": " + entry.getValue().toString());
        }

    }

    public void openDB(View v) {
        Intent dbmanager = new Intent(getApplicationContext(), AndroidDatabaseManager.class);
        startActivity(dbmanager);
    }

    public void CheckCluster(View v) {

    }

    public void downloadData(View view) {
        Toast.makeText(this, "Sync Schools", Toast.LENGTH_LONG).show();
        new GetAllData(this, "School").execute();
    }

    public void syncServer(View view) {

        // Require permissions INTERNET & ACCESS_NETWORK_STATE
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            Toast.makeText(getApplicationContext(), "Syncing Forms", Toast.LENGTH_SHORT).show();
            new SyncAllData(
                    this,
                    "Forms",
                    "updateSyncedForms",
                    FormsContract.class,
                    FormsContract.FormsTable._URL1,
                    db.getUnsyncedForms(MainApp.SFA)
            ).execute();

            Toast.makeText(getApplicationContext(), "Syncing Child Forms", Toast.LENGTH_SHORT).show();
            new SyncAllData(
                    this,
                    "Child-Forms",
                    "updateSyncedForms",
                    FormsContract.class,
                    FormsContract.FormsTable._URL2,
                    db.getUnsyncedForms(MainApp.SFB)
            ).execute();

            SharedPreferences syncPref = getSharedPreferences("SyncInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = syncPref.edit();

            editor.putString("LastUpSyncServer", dtToday);

            editor.apply();

        } else {
            Toast.makeText(this, "No network connection available.", Toast.LENGTH_SHORT).show();
        }

    }

    public void syncDevice(View view) {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            // Sync Random
            /*new GetBLRandom(this).execute();*/

            SharedPreferences syncPref = getSharedPreferences("SyncInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = syncPref.edit();

            editor.putString("LastDownSyncServer", dtToday);

            editor.apply();
        } else {
            Toast.makeText(this, "No network connection available.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity

            startActivity(new Intent(this, LoginActivity.class));

        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }

    public static class MyDialogFragment extends DialogFragment {

        String newVer, preVer;

        static MyDialogFragment newInstance(String newVer, String preVer) {
            MyDialogFragment f = new MyDialogFragment();

            Bundle args = new Bundle();
            args.putString("newVer", newVer);
            args.putString("preVer", preVer);
            f.setArguments(args);

            return f;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            newVer = getArguments().getString("newVer");
            preVer = getArguments().getString("preVer");

            return new AlertDialog.Builder(getActivity())
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle("Typbar App is available!")
                    .setMessage("Typbar App " + newVer + " is now available. Your are currently using older version " + preVer + ".\nInstall new version to use this app.")
                    .setPositiveButton("INSTALL!!",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }
                    )
                    .create();
        }

    }

}