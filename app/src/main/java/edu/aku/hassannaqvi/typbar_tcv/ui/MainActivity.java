package edu.aku.hassannaqvi.typbar_tcv.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import edu.aku.hassannaqvi.typbar_tcv.R;
import edu.aku.hassannaqvi.typbar_tcv.contracts.FormsContract;
import edu.aku.hassannaqvi.typbar_tcv.core.AndroidDatabaseManager;
import edu.aku.hassannaqvi.typbar_tcv.core.DatabaseHelper;
import edu.aku.hassannaqvi.typbar_tcv.core.MainApp;
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivityMainBinding;
import edu.aku.hassannaqvi.typbar_tcv.get.GetAllData;
import edu.aku.hassannaqvi.typbar_tcv.sync.SyncAllData;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    String dtToday = new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date().getTime());
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    AlertDialog.Builder builder;
    String m_Text = "";
    private Boolean exit = false;
    private String rSumText = "";
    DatabaseHelper db;

    ActivityMainBinding bi;

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

        builder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog dialog = builder.create();

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

    }

    public void openForm(int type) {

        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


            final Intent oF = new Intent(MainActivity.this,
                    type == 1 ? SectionSListingActivity.class :
                            type == 2 ? SectionCListingActivity.class :
                                    type == 3 ? Section01CRFCaseActivity.class :
                                            type == 4 ? SectionCRFControlActivity.class
                                                    : SectionMImmunizeActivity.class);
            if (sharedPref.getString("tagName", null) != "" && sharedPref.getString("tagName", null) != null && !MainApp.userName.equals("0000")) {
                startActivity(oF);
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
                                startActivity(oF);
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


    public void openA(View v) {
        Intent iA = new Intent(this, SectionSListingActivity.class);
        startActivity(iA);
    }

    public void testGPS(View v) {

        SharedPreferences sharedPref = getSharedPreferences("GPSCoordinates", Context.MODE_PRIVATE);
        Log.d("MAP", "testGPS: " + sharedPref.getAll().toString());
        Map<String, ?> allEntries = sharedPref.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("Map", entry.getKey() + ": " + entry.getValue().toString());
        }

    }

    public void updateApp(View v) {
        v.setBackgroundColor(Color.GREEN);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this);
        alertDialogBuilder
                .setMessage("Are you sure to download new app??")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                // this is how you fire the downloader
                                try {
                                    URL url = new URL(MainApp._UPDATE_URL);
                                    HttpURLConnection c = (HttpURLConnection) url.openConnection();
                                    c.setRequestMethod("GET");
                                    c.setDoOutput(true);
                                    c.connect();

                                    String PATH = Environment.getExternalStorageDirectory() + "/download/";
                                    File file = new File(PATH);
                                    file.mkdirs();
                                    File outputFile = new File(file, "app.apk");
                                    FileOutputStream fos = new FileOutputStream(outputFile);

                                    InputStream is = c.getInputStream();

                                    byte[] buffer = new byte[1024];
                                    int len1 = 0;
                                    while ((len1 = is.read(buffer)) != -1) {
                                        fos.write(buffer, 0, len1);
                                    }
                                    fos.close();
                                    is.close();//till here, it works fine - .apk is download to my sdcard in download file

                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + "app.apk")), "application/vnd.android.package-archive");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                } catch (IOException e) {
                                    Toast.makeText(getApplicationContext(), "Update error!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
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

            Toast.makeText(getApplicationContext(), "Syncing School Forms", Toast.LENGTH_SHORT).show();
            new SyncAllData(
                    this,
                    "School-Listings",
                    "updateSyncedForms",
                    FormsContract.class,
                    FormsContract.FormsTable._URL1,
                    db.getUnsyncedForms(MainApp.SCHOOLLISTINGTYPE)
            ).execute();

            Toast.makeText(getApplicationContext(), "Syncing Child Forms", Toast.LENGTH_SHORT).show();
            new SyncAllData(
                    this,
                    "Children-Listings",
                    "updateSyncedForms",
                    FormsContract.class,
                    FormsContract.FormsTable._URL2
                    ,
                    db.getUnsyncedForms(MainApp.CHILDLISTINGTYPE)
            ).execute();

            Toast.makeText(getApplicationContext(), "Syncing Mass Immunization Forms", Toast.LENGTH_SHORT).show();
            new SyncAllData(
                    this,
                    "CRF-MI's",
                    "updateSyncedForms",
                    FormsContract.class,
                    FormsContract.FormsTable._URL3,
                    db.getUnsyncedForms(MainApp.MASSIMMUNIZATIONTYPE)
            ).execute();

            Toast.makeText(getApplicationContext(), "Syncing CRF Case Forms", Toast.LENGTH_SHORT).show();
            new SyncAllData(
                    this,
                    "CRF-Case",
                    "updateSyncedForms",
                    FormsContract.class,
                    FormsContract.FormsTable._URL4,
                    db.getUnsyncedForms(MainApp.CRFCase)
            ).execute();

            Toast.makeText(getApplicationContext(), "Syncing CRF Control Forms", Toast.LENGTH_SHORT).show();
            new SyncAllData(
                    this,
                    "CRF-Control",
                    "updateSyncedForms",
                    FormsContract.class,
                    FormsContract.FormsTable._URL5,
                    db.getUnsyncedForms(MainApp.CRFControl)
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

}