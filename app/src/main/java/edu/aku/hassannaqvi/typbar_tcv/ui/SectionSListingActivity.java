package edu.aku.hassannaqvi.typbar_tcv.ui;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.aku.hassannaqvi.typbar_tcv.R;
import edu.aku.hassannaqvi.typbar_tcv.contracts.FormsContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.SchoolContract;
import edu.aku.hassannaqvi.typbar_tcv.core.DatabaseHelper;
import edu.aku.hassannaqvi.typbar_tcv.core.MainApp;
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivitySectionSListingBinding;
import edu.aku.hassannaqvi.typbar_tcv.validation.ValidatorClass;

public class SectionSListingActivity extends Activity {

    ActivitySectionSListingBinding bi;
    String deviceID;
    Map<String, SchoolContract> schoolMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_s_listing);
        bi.setCallback(this);

        setContentUI();
        setListeners();
    }

    private void setContentUI() {
        this.setTitle(R.string.sec_slisting);
        deviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        filledTypeSpinner();
    }

    private void filledTypeSpinner() {
        String[] schTypes = {"....", "Government Boys Secondary School", "Government Girls Secondary School",
                "Government Boys Primary School", "Government Girls Primary School", "Private"};
        bi.tcvsl00.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Arrays.asList(schTypes)));
    }

    private void setListeners() {

        final DatabaseHelper db = new DatabaseHelper(this);

        bi.tcvsl00.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ArrayList<String> schNames = new ArrayList<>();
                schNames.add("....");

                if (i != 0) {
                    ArrayList<SchoolContract> schoolContract = db.getSchoolWRTType(String.valueOf(bi.tcvsl00.getSelectedItemPosition()));
                    schoolMap = new HashMap<>();

                    for (SchoolContract school : schoolContract) {
                        schoolMap.put(school.getSch_name(), school);
                        schNames.add(school.getSch_name());
                    }
                }

                bi.tcvsl01.setAdapter(new ArrayAdapter<>(SectionSListingActivity.this, android.R.layout.simple_spinner_dropdown_item, schNames));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void BtnContinue() {
        try {

            if (!formValidation()) return;
            SaveDraft();

            if (UpdateDB()) MainApp.endActivity(this, this, true);
            Toast.makeText(this, "Error in updating db!!", Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean UpdateDB() {

        DatabaseHelper db = new DatabaseHelper(this);
        long updcount = db.addForm(MainApp.fc);
        MainApp.fc.set_ID(String.valueOf(updcount));
        if (updcount > 0) {
            MainApp.fc.setUID((MainApp.fc.getDeviceID() + MainApp.fc.get_ID()));
            db.updateFormID();
        } else {
            Toast.makeText(this, "Updating Database... ERROR!", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    private void SaveDraft() throws JSONException {
        MainApp.fc = new FormsContract();
        MainApp.fc.setDevicetagID(getSharedPreferences("tagName", MODE_PRIVATE).getString("tagName", null));
        MainApp.fc.setFormDate(new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date().getTime()));
        MainApp.fc.setUser(MainApp.userName);
        MainApp.fc.setDeviceID(Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID));
        MainApp.fc.setAppversion(MainApp.versionName + "." + MainApp.versionCode);
        settingGPS(MainApp.fc);

        JSONObject sA = new JSONObject();
        sA.put("tcvsl01", bi.tcvsl01.getSelectedItem());
        sA.put("tcvsl02", bi.tcvsl02a.isChecked() ? "1" : bi.tcvsl02b.isChecked() ? "2" :
                bi.tcvsl02c.isChecked() ? "3" : bi.tcvsl0296.isChecked() ? "96" : "0");
        sA.put("tcvsl03", bi.tcvsl03.getText().toString());
        sA.put("tcvsl04", bi.tcvsl04.getText().toString());
        sA.put("tcvsl05", bi.tcvsl05.getText().toString());
        sA.put("tcvsl06", bi.tcvsl06a.isChecked() ? "1" : bi.tcvsl06b.isChecked() ? "2" : "0");
        sA.put("tcvsl07", bi.tcvsl07.getText().toString());
        sA.put("tcvsl08", bi.tcvsl08.getText().toString());

        MainApp.fc.setsA(String.valueOf(sA));

    }

    private boolean formValidation() {
        return ValidatorClass.EmptyCheckingContainer(this, bi.fldGrpSecA01);
    }

    public void BtnEnd() {
        MainApp.endActivity(this, this, false);
    }

    public void settingGPS(FormsContract fc) {
        MainApp.LocClass locClass = MainApp.setGPS(this);
        fc.setGpsLat(locClass.getLatitude());
        fc.setGpsLng(locClass.getLongitude());
        fc.setGpsAcc(locClass.getAccuracy());
        fc.setGpsDT(locClass.getTime());
    }

}
