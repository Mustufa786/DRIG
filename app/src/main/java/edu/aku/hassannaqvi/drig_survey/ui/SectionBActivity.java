package edu.aku.hassannaqvi.drig_survey.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.aku.hassannaqvi.drig_survey.R;
import edu.aku.hassannaqvi.drig_survey.contracts.ChildContract;
import edu.aku.hassannaqvi.drig_survey.core.DatabaseHelper;
import edu.aku.hassannaqvi.drig_survey.core.MainApp;
import edu.aku.hassannaqvi.drig_survey.databinding.ActivitySectionCListingBinding;
import edu.aku.hassannaqvi.drig_survey.validation.ValidatorClass;

public class SectionBActivity extends AppCompatActivity {

    ActivitySectionCListingBinding bi;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_b);
        bi.setCallback(this);

        setContentUI();
        setListeners();
    }

    private void setContentUI() {
        this.setTitle(R.string.dsbh);

        // Initialize db
        db = new DatabaseHelper(this);
    }


    private void setListeners() {

    }

    public void BtnContinue() {
        try {

            if (!formValidation()) return;

            SaveDraft();

            if (!UpdateDB()) {
                Toast.makeText(this, "Error in updating db!!", Toast.LENGTH_SHORT).show();
                return;
            }

            finish();
            startActivity(new Intent(this, SectionBActivity.class));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean UpdateDB() {

        DatabaseHelper db = new DatabaseHelper(this);
        long updcount = db.addChildForm(MainApp.cc);
        MainApp.cc.set_ID(String.valueOf(updcount));
        if (updcount > 0) {
            MainApp.cc.set_UID((MainApp.cc.getDeviceID() + MainApp.cc.get_ID()));
            db.updateChildFormID();

            return true;
        }

        return false;
    }

    private void SaveDraft() throws JSONException {
        MainApp.cc = new ChildContract();
        MainApp.cc.setDevicetagID(getSharedPreferences("tagName", MODE_PRIVATE).getString("tagName", null));
        MainApp.cc.setFormDate(new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date().getTime()));
        MainApp.cc.setUser(MainApp.userName);
        MainApp.cc.setDeviceID(Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID));
        MainApp.cc.setAppversion(MainApp.versionName + "." + MainApp.versionCode);
        settingGPS(MainApp.cc);

        JSONObject child = new JSONObject();

        child.put("tcvcl02", bi.tcvcl02.getText().toString());
        child.put("tcvcl21", bi.tcvcl21.getSelectedItem().toString()); //Newly added when the app is already on field

        child.put("tcvcl03Age", bi.tcvcl03Agea.isChecked() ? "1" : bi.tcvcl03Ageb.isChecked() ? "2" : "0");
        child.put("tcvcl03", bi.tcvcl03.getText().toString());
        child.put("tcvcl04y", bi.tcvcl04y.getText().toString());
        child.put("tcvcl04m", bi.tcvcl04m.getText().toString());
        child.put("tcvcl05", bi.tcvcl05a.isChecked() ? "1" : bi.tcvcl05b.isChecked() ? "2" : "0");
        child.put("tcvcl06", bi.tcvcl06.getText().toString());
        child.put("tcvcl07", bi.tcvcl07.getText().toString());
        child.put("tcvcl08", bi.tcvcl08.getText().toString());
        child.put("tcvcl09", bi.tcvcl09.getText().toString());
        child.put("tcvcl10", bi.tcvcl10.getText().toString());
        child.put("tcvcl11", bi.tcvcl11a.isChecked() ? "1" : bi.tcvcl11b.isChecked() ? "2" : bi.tcvcl11c.isChecked() ? "3" : "0");
        child.put("tcvcl12", bi.tcvcl12a.isChecked() ? "1" : bi.tcvcl12b.isChecked() ? "2" : "0");
        child.put("tcvcl13", bi.tcvcl13a.isChecked() ? "1" : bi.tcvcl13b.isChecked() ? "2" : "0");
        child.put("tcvcl14", bi.tcvcl14a.isChecked() ? "1" : bi.tcvcl14b.isChecked() ? "2" : "0");
        child.put("tcvcl15", bi.tcvcl15a.isChecked() ? "1" : bi.tcvcl15b.isChecked() ? "2" : "0");
        child.put("tcvcl16", bi.tcvcl16a.isChecked() ? "1" : bi.tcvcl16b.isChecked() ? "2" : "0");
        child.put("tcvcl17", bi.tcvcl17a.isChecked() ? "1" : bi.tcvcl17b.isChecked() ? "2" : "0");

        if (bi.tcvcl12a.isChecked() &&
                bi.tcvcl13a.isChecked() &&
                bi.tcvcl14a.isChecked() &&
                bi.tcvcl15a.isChecked() &&
                bi.tcvcl16a.isChecked()
        ) {
            child.put("tcvcl18", bi.tcvcl18.getText().toString());
            child.put("tcvcl19", new SimpleDateFormat("dd-MM-yyyy").format(new Date().getTime()));
            child.put("tcvcl20", new SimpleDateFormat("HH:MM:SS").format(new Date().getTime()));
        } else
            child.put("tcvcl18", "");

        MainApp.cc.setsA(String.valueOf(child));
    }

    private boolean formValidation() {
        if (!ValidatorClass.EmptyCheckingContainer(this, bi.childSec))
            return false;

        if (bi.tcvcl03Ageb.isChecked()) {
            if (Integer.valueOf(bi.tcvcl04y.getText().toString()) == 0 && Integer.valueOf(bi.tcvcl04m.getText().toString()) < 6)
                return ValidatorClass.EmptyCustomeTextBox(this, bi.tcvcl04y, "Days and Months criteria not meet!!");
        }

        return true;
    }

    public void BtnEnd() {
        MainApp.endActivity(this, this, false);
    }

    public void settingGPS(ChildContract cc) {
        MainApp.LocClass locClass = MainApp.setGPS(this);
        cc.setGpsLat(locClass.getLatitude());
        cc.setGpsLng(locClass.getLongitude());
        cc.setGpsAcc(locClass.getAccuracy());
        cc.setGpsDT(locClass.getTime());
    }
}
