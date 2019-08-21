package edu.aku.hassannaqvi.drig_survey.get;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.aku.hassannaqvi.drig_survey.contracts.HFContract;
import edu.aku.hassannaqvi.drig_survey.contracts.SchoolContract;
import edu.aku.hassannaqvi.drig_survey.contracts.UCsContract;
import edu.aku.hassannaqvi.drig_survey.contracts.UsersContract;
import edu.aku.hassannaqvi.drig_survey.contracts.VersionAppContract;
import edu.aku.hassannaqvi.drig_survey.core.DatabaseHelper;
import edu.aku.hassannaqvi.drig_survey.core.MainApp;

/**
 * Created by ali.azaz on 7/14/2017.
 */

public class GetAllData extends AsyncTask<String, String, String> {

    HttpURLConnection urlConnection;
    private String TAG = "";
    private Context mContext;
    private ProgressDialog pd;
    private String syncClass;

    public GetAllData(Context context, String syncClass) {
        mContext = context;
        this.syncClass = syncClass;
        TAG = "Get" + syncClass;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(mContext);
        pd.setTitle("Syncing " + syncClass);
        pd.setMessage("Getting connected to server...");
        pd.show();
    }

    @Override
    protected String doInBackground(String... args) {
        StringBuilder result = null;

        for (String hostItem : MainApp.HOST) {

            URL url = null;
            try {
                switch (syncClass) {
                    case "User":
                        url = new URL(hostItem + UsersContract.UsersTable._URI);
                        break;
                    case "HF":
                        url = new URL(hostItem + HFContract.HFTable._URI);
                        break;
                    case "UC":
                        url = new URL(hostItem + UCsContract.UCsTable._URI);
                        break;
                    case "School":
                        url = new URL(hostItem + SchoolContract.SchoolTable._URI);
                        break;
                    case "CCChildren":
                        url = new URL(hostItem + CCChildrenContract.ChildrenEntry._URI);
                        break;
                    case "Appversion":
                        url = new URL(MainApp._UPDATE_URL + VersionAppContract.VersionAppTable._URL);
                        break;
                }

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(100000 /* milliseconds */);
                urlConnection.setConnectTimeout(150000 /* milliseconds */);
                Log.d(TAG, "doInBackground: " + urlConnection.getResponseCode());

                result = new StringBuilder();

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        Log.i(TAG, syncClass + " In: " + line);
                        result.append(line);
                    }

                    return result.toString();
                }
            } catch (java.net.SocketTimeoutException e) {
                continue;
            } catch (java.io.IOException e) {
                continue;
            }
        }

        urlConnection.disconnect();

        return result == null ? null : result.toString();
    }

    @Override
    protected void onPostExecute(String result) {

        //Do something with the JSON string
        if (result != null) {
            String json = result;
            if (json.length() > 0) {
                DatabaseHelper db = new DatabaseHelper(mContext);
                try {
                    JSONArray jsonArray = new JSONArray(json);

                    switch (syncClass) {
                        case "User":
                            db.syncUser(jsonArray);
                            break;
                        case "HF":
                            db.syncHF(jsonArray);
                            break;
                        case "UC":
                            db.syncUCs(jsonArray);
                            break;
                        case "School":
                            db.syncSchools(jsonArray);
                            break;
                        case "CCChildren":
                            db.syncCCChildren(jsonArray);
                            break;
                        case "Appversion":
                            MainApp.savingAppVersion(mContext, jsonArray);
                            break;
                    }

                    pd.setMessage("Received: " + jsonArray.length());
                    pd.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                pd.setMessage("Received: " + json.length() + "");
                pd.show();
            }
        } else {
            pd.setTitle("Connection Error");
            pd.setMessage("Server not found!");
            pd.show();
        }
    }

}
