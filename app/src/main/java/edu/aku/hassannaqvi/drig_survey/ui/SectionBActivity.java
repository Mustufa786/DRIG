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
import edu.aku.hassannaqvi.drig_survey.databinding.ActivitySectionBBinding;
import edu.aku.hassannaqvi.drig_survey.validation.ValidatorClass;

public class SectionBActivity extends AppCompatActivity {

    ActivitySectionBBinding bi;
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
        MainApp.cc.set_UUID(MainApp.fc.getUID());
        settingGPS(MainApp.cc);

        JSONObject sfb = new JSONObject();

        sfb.put("dsb01", bi.dsb01.getText().toString());
        sfb.put("dsb02", bi.dsb02.getText().toString());
        sfb.put("dsb03", bi.dsb03a.isChecked() ? "1" : bi.dsb03b.isChecked() ? "2" : "0");

        sfb.put("dsb06", bi.dsb06a.isChecked() ? "1" : bi.dsb06b.isChecked() ? "2" : "0");
        sfb.put("dsb07", bi.dsb07.getText().toString());
        sfb.put("dsb08", bi.dsb08.getText().toString());

        MainApp.cc.setsA(String.valueOf(sfb));
    }

    private boolean formValidation() {
        if (!ValidatorClass.EmptyCheckingContainer(this, bi.childSec))
            return false;

        if (bi.dsb04Ageb.isChecked()) {
            if (Integer.valueOf(bi.dsb05y.getText().toString()) == 0 && Integer.valueOf(bi.dsb05m.getText().toString()) == 0 && Integer.valueOf(bi.dsb05d.getText().toString()) == 0)
                return ValidatorClass.EmptyCustomeTextBox(this, bi.dsb05y, "Days, Months & Year criteria not meet!!");
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
