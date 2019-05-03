package edu.aku.hassannaqvi.typbar_tcv.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
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
import edu.aku.hassannaqvi.typbar_tcv.core.CheckingID;
import edu.aku.hassannaqvi.typbar_tcv.core.DatabaseHelper;
import edu.aku.hassannaqvi.typbar_tcv.core.MainApp;
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivitySectionCListingBinding;
import edu.aku.hassannaqvi.typbar_tcv.validation.ClearClass;
import edu.aku.hassannaqvi.typbar_tcv.validation.ValidatorClass;

public class SectionCListingActivity extends AppCompatActivity {

    ActivitySectionCListingBinding bi;
    DatabaseHelper db;
    Map<String, SchoolContract> schoolMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_c_listing);
        bi.setCallback(this);

        setContentUI();
        setListeners();
    }

    private void setContentUI() {
        this.setTitle(R.string.sec_clisting);

        // Initialize db
        db = new DatabaseHelper(this);
        filledSpinners();
    }

    private void filledSpinners() {
        bi.tcvcl00.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Arrays.asList(MainApp.schTypes)));
    }

    private void setListeners() {
        bi.tcvcl11.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == bi.tcvcl11b.getId())
                    ClearClass.ClearAllFields(bi.childSec01);
            }
        });

        //setting dates
        String dateToday = new SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis());
        bi.tcvcl03.setManager(getSupportFragmentManager());
        bi.tcvcl03.setMaxDate(dateToday);
        bi.tcvcl19.setManager(getSupportFragmentManager());
        bi.tcvcl19.setMaxDate(dateToday);
        bi.tcvcl20.setManager(getSupportFragmentManager());

        //settting spinner listeners
        bi.tcvcl00.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ArrayList<String> schNames = new ArrayList<>();

                if (i != 0) {

                    ArrayList<SchoolContract> schoolContract = db.getSchoolWRTType(String.valueOf(bi.tcvcl00.getSelectedItemPosition()), "0");
                    schoolMap = new HashMap<>();

                    for (SchoolContract school : schoolContract) {
                        schoolMap.put(school.getSch_name().toUpperCase(), school);
                        schNames.add(school.getSch_name().toUpperCase());
                    }

                } else {
                    bi.childSec00.setVisibility(View.GONE);
                    ClearClass.ClearAllFields(bi.childSec00);
                    bi.childSec00a.setVisibility(View.GONE);
                }

                bi.tcvcl01.setText(null);
                bi.tcvcl01.setAdapter(new ArrayAdapter<>(SectionCListingActivity.this, android.R.layout.simple_spinner_dropdown_item, schNames));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bi.tcvcl01.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bi.childSec00.setVisibility(View.GONE);
                bi.childSec00a.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        bi.tcvcl11.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i != bi.tcvcl11a.getId())
                    ClearClass.ClearAllFields(bi.childSec01);
            }
        });

        bi.tcvcl17.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i != bi.tcvcl17.getId())
                    ClearClass.ClearAllFields(bi.childSec02);
            }
        });

    }

    public void BtnCheckSchool() {

        if (!formValidation()) return;

        if (schoolMap.get(bi.tcvcl01.getText().toString()) == null) {
            ValidatorClass.EmptyTextBoxCustom(this, bi.tcvcl01, "This data is not accurate!!");
            return;
        }

        SchoolContract schoolContract = db.getSchoolWRTTypeAndCode(
                String.valueOf(bi.tcvcl00.getSelectedItemPosition()),
                schoolMap.get(bi.tcvcl01.getText().toString()).getSch_code());

        bi.childSec00.setVisibility(View.GONE);
        ClearClass.ClearAllFields(bi.childSec00);
        bi.childSec00a.setVisibility(View.GONE);

        if (schoolContract == null) {
            Toast.makeText(SectionCListingActivity.this, "School not found!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (schoolContract.getSch_status() == null) {
            Toast.makeText(SectionCListingActivity.this, "School not found!!", Toast.LENGTH_SHORT).show();
            return;
        }


        if (CheckingID.checkFile(SectionCListingActivity.this)) {
            String vacID = CheckingID.accessingFile(getSharedPreferences("tagName", MODE_PRIVATE).getString("tagName", null), false);
            bi.tcvcl18.setText(vacID);
        }


        bi.childSec00.setVisibility(View.VISIBLE);
        bi.childSec00a.setVisibility(View.VISIBLE);

    }

    public void BtnContinue() {
        try {

            if (!formValidation()) return;

//            if (!MainApp.checkingGPSRules(this)) return;

            SaveDraft();

            if (!UpdateDB()) {
                Toast.makeText(this, "Error in updating db!!", Toast.LENGTH_SHORT).show();
                return;
            }


            finish();
            startActivity(new Intent(this, EndingActivity.class).putExtra("complete", true));

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

            if (bi.tcvcl12a.isChecked() &&
                    bi.tcvcl13a.isChecked() &&
                    bi.tcvcl14a.isChecked() &&
                    bi.tcvcl15a.isChecked() &&
                    bi.tcvcl16a.isChecked()
            )
                CheckingID.accessingFile(null, true);

            return true;
        }

        return false;
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
        MainApp.fc.setFormtype("cl");

        JSONObject child = new JSONObject();
//        child.put("tcvcl00", bi.tcvcl00.getSelectedItem());

        child.put("sch_code", schoolMap.get(bi.tcvcl01.getText().toString()).getSch_code());
        child.put("sch_add", schoolMap.get(bi.tcvcl01.getText().toString()).getSch_add());
        child.put("sch_type", schoolMap.get(bi.tcvcl01.getText().toString()).getSch_type());
        child.put("tcvcl01", bi.tcvcl01.getText().toString());

        child.put("tcvcl02", bi.tcvcl02.getText().toString());
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
        )
            child.put("tcvcl18", bi.tcvcl18.getText().toString());
        else
            child.put("tcvcl18", "");

        child.put("tcvcl19", new SimpleDateFormat("dd-MM-yyyy").format(new Date().getTime()));
        child.put("tcvcl20", new SimpleDateFormat("HH:MM:SS").format(new Date().getTime()));

        MainApp.fc.setsA(String.valueOf(child));

    }

    private boolean formValidation() {
        return ValidatorClass.EmptyCheckingContainer(this, bi.childSec);
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

    //

}