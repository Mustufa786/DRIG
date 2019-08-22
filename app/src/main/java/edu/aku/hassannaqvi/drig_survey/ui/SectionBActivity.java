package edu.aku.hassannaqvi.drig_survey.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import edu.aku.hassannaqvi.drig_survey.utils.DateUtils;
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
        fillView();
    }

    private void setContentUI() {
        this.setTitle(R.string.dsbh);

        // Initialize db
        db = new DatabaseHelper(this);

        bi.dsb04.setMinDate(DateUtils.getMonthsBack("dd/MM/yyyy", -24));
    }

    private void fillView() {
        bi.viewGroup01.totalB.setText(String.valueOf(SectionAActivity.ChildC.getTotalBoy()));
        bi.viewGroup01.totalG.setText(String.valueOf(SectionAActivity.ChildC.getTotalGirl()));
        bi.viewGroup01.boyC.setText(String.valueOf(SectionAActivity.ChildC.getTotalBCount()));
        bi.viewGroup01.girlC.setText(String.valueOf(SectionAActivity.ChildC.getTotalGCount()));

        if (SectionAActivity.ChildC.getTotalBoy() == SectionAActivity.ChildC.getTotalBCount())
            bi.dsb03a.setEnabled(false);
        else if (SectionAActivity.ChildC.getTotalGirl() == SectionAActivity.ChildC.getTotalGCount())
            bi.dsb03b.setEnabled(false);
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
            startActivity(new Intent(this, SectionCActivity.class));

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

        sfb.put("dsb04Age", bi.dsb04Agea.isChecked() ? "1" : bi.dsb04Ageb.isChecked() ? "2" : "0");
        sfb.put("dsb04", bi.dsb04.getText().toString());
        sfb.put("dsb05y", bi.dsb05y.getText().toString());
        sfb.put("dsb05m", bi.dsb05y.getText().toString());
        sfb.put("dsb05d", bi.dsb05y.getText().toString());

        sfb.put("dsb06", bi.dsb06a.isChecked() ? "1" : bi.dsb06b.isChecked() ? "2" : "0");
        sfb.put("dsb07", bi.dsb07.getText().toString());
        sfb.put("dsb08", bi.dsb08.getText().toString());

        MainApp.cc.setsA(String.valueOf(sfb));

        if (bi.dsb03a.isChecked())
            SectionAActivity.ChildC.setTotalBCount(SectionAActivity.ChildC.getTotalBCount() + 1);
        else
            SectionAActivity.ChildC.setTotalGCount(SectionAActivity.ChildC.getTotalGCount() + 1);

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
        if (!ValidatorClass.EmptyTextBox(this, bi.dsb01, getString(R.string.dsb01)))
            return;
        if (!ValidatorClass.EmptyTextBox(this, bi.dsb02, getString(R.string.dsb02)))
            return;
        if (!ValidatorClass.EmptyRadioButton(this, bi.dsb03, bi.dsb03a, getString(R.string.dsb03)))
            return;

        new AlertDialog.Builder(this)
                .setTitle("END INTERVIEW")
                .setIcon(R.drawable.ic_power_settings_new_black_24dp)
                .setCancelable(false)
                .setCancelable(false)
                .setMessage("Do you want to End Interview??")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            SaveDraft();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (!UpdateDB()) {
                            Toast.makeText(SectionBActivity.this, "Error in updating db!!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        startActivity(new Intent(SectionBActivity.this, EndingActivity.class).putExtra("complete", false).putExtra("type", 2));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    public void settingGPS(ChildContract cc) {
        MainApp.LocClass locClass = MainApp.setGPS(this);
        cc.setGpsLat(locClass.getLatitude());
        cc.setGpsLng(locClass.getLongitude());
        cc.setGpsAcc(locClass.getAccuracy());
        cc.setGpsDT(locClass.getTime());
    }
}
