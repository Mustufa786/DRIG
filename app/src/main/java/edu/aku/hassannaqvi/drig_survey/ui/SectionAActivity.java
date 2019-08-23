package edu.aku.hassannaqvi.drig_survey.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.aku.hassannaqvi.drig_survey.R;
import edu.aku.hassannaqvi.drig_survey.contracts.FormsContract;
import edu.aku.hassannaqvi.drig_survey.contracts.HFContract;
import edu.aku.hassannaqvi.drig_survey.core.DatabaseHelper;
import edu.aku.hassannaqvi.drig_survey.core.MainApp;
import edu.aku.hassannaqvi.drig_survey.databinding.ActivitySectionABinding;
import edu.aku.hassannaqvi.drig_survey.other.ChildrenCounter;
import edu.aku.hassannaqvi.drig_survey.validation.ClearClass;
import edu.aku.hassannaqvi.drig_survey.validation.ValidatorClass;

import static java.sql.Types.INTEGER;

public class SectionAActivity extends AppCompatActivity {

    private ActivitySectionABinding bi;
    private DatabaseHelper db;
    private Map<String, HFContract> hfMap;
    private List<String> hfName = new ArrayList<>(Arrays.asList("...."));
    private String screenID = "", caseID = "", tagID = "";
    private boolean eligibleFlag = false;
    public static ChildrenCounter ChildC;

    private final List<LinearLayout> childllArray16 = new ArrayList();
    private final List<EditText> weekArray16 = new ArrayList();
    private final List<EditText> monthArray16 = new ArrayList();
    private final List<TextView> durationLabelArray19 = new ArrayList();

    private final LinearLayout.LayoutParams mRparams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
    private final LinearLayout.LayoutParams mRparams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    private int totalU2 = 0, boysU2 = 0, girlsU2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_a);
        bi.setCallback(this);

        setContentUI();
        loadHFFromDB();
        setListeners();
    }

    private void setListeners() {
        bi.dsa15.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == bi.dsa15b.getId())
                    ClearClass.ClearAllFields(bi.fldGrpSecA02, null);
            }
        });

        bi.dsa16.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                childllArray16.clear();
                bi.llgrpsfu309.removeAllViews();
                monthArray16.clear();
                weekArray16.clear();

                if (bi.dsa16.getText().toString().isEmpty()) return;

                int noofboxes = Integer.valueOf(bi.dsa16.getText().toString());
                if (noofboxes == 0)
                    return;

                for (int i = 0; i < noofboxes; i++) {

                    TextView DurationLabelTextView = new TextView(SectionAActivity.this);
                    DurationLabelTextView.setLayoutParams(mRparams2);
                    int numberss = i + 1;
                    DurationLabelTextView.setText(getString(R.string.dsa13) + " # " + numberss);
                    DurationLabelTextView.setTextColor(getResources().getColor(R.color.white));
                    DurationLabelTextView.setTextSize(15);
                    DurationLabelTextView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryAlpha));
                    durationLabelArray19.add(DurationLabelTextView);
                    bi.llgrpsfu309.addView(DurationLabelTextView);

                    LinearLayout llchild = new LinearLayout(SectionAActivity.this);
                    llchild.setLayoutParams(mRparams2);
                    llchild.setOrientation(LinearLayout.HORIZONTAL);
                    childllArray16.add(llchild);
                    bi.llgrpsfu309.addView(llchild);


                    EditText weekEditText = new EditText(SectionAActivity.this);
                    weekEditText.setLayoutParams(mRparams1);
                    weekEditText.setHint("Week " + numberss);
                    weekEditText.setInputType(INTEGER);
                    /*secEditText.setType(1);
                    secEditText.setMaxvalue(36);
                    secEditText.setMinvalue(1);*/
                    weekEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                    weekEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
                    weekArray16.add(weekEditText);
                    llchild.addView(weekEditText);


                    EditText monthEditText = new EditText(SectionAActivity.this);
                    monthEditText.setLayoutParams(mRparams1);
                    monthEditText.setHint("Month " + numberss);
                    monthEditText.setInputType(INTEGER);
                    /*secEditText.setType(1);
                    secEditText.setMaxvalue(9);
                    secEditText.setMinvalue(0);*/
                    monthEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                    monthEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
                    monthArray16.add(monthEditText);
                    llchild.addView(monthEditText);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }

    private void loadHFFromDB() {
        Collection<HFContract> allHF = db.getAllHF();
        if (allHF.size() == 0) return;
        hfName = new ArrayList<>();
        hfName.add("....");
        hfMap = new HashMap<>();
        for (HFContract hf : allHF) {
            hfName.add(hf.getHfname());
            hfMap.put(hf.getHfname(), hf);
        }
        filledSpinners(hfName);
    }

    private void filledSpinners(List<String> hfNames) {
//        bi.hfcode.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, hfNames));
    }

    private void setContentUI() {
        this.setTitle(R.string.dsah);
        // Initialize db
        db = new DatabaseHelper(this);
        tagID = getSharedPreferences("tagName", MODE_PRIVATE).getString("tagName", null);
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
            startActivity(new Intent(this, totalU2 > 0 ? SectionBActivity.class : EndingActivity.class)
                    .putExtra("childCounter", ChildC)
                    .putExtra("type", 1)
                    .putExtra("complete", true));

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

            return true;
        }

        return false;
    }

    private void SaveDraft() throws JSONException {
        MainApp.fc = new FormsContract();
        MainApp.fc.setDevicetagID(tagID);
        MainApp.fc.setFormDate(new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date().getTime()));
        MainApp.fc.setUser(MainApp.userName);
        MainApp.fc.setDeviceID(Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID));
        MainApp.fc.setAppversion(MainApp.versionName + "." + MainApp.versionCode);
        settingGPS(MainApp.fc);

        JSONObject sfa = new JSONObject();

        /*sfa.put("dsa01", hfMap.get(bi.dsa01.getSelectedItem().toString()).getHfcode());
        sfa.put("dsa02", hfMap.get(bi.dsa02.getSelectedItem().toString()).getHfcode());
        sfa.put("dsa03", hfMap.get(bi.dsa03.getSelectedItem().toString()).getHfcode());*/
        sfa.put("dsa04", bi.dsa04.getText().toString());
        sfa.put("dsa05", bi.dsa05.getText().toString());
        sfa.put("dsa06", bi.dsa06.getText().toString());
        sfa.put("dsa07", bi.dsa07.getText().toString());
        sfa.put("dsa08", bi.dsa08.getText().toString());
        sfa.put("dsa09", bi.dsa09.getText().toString());
        sfa.put("dsa10", bi.dsa10.getText().toString());
        sfa.put("dsa11", bi.dsa11.getText().toString());
        sfa.put("dsa12", bi.dsa12.getText().toString());
        sfa.put("dsa13", bi.dsa13.getText().toString());
        sfa.put("dsa14", bi.dsa14.getText().toString());
        sfa.put("dsa15", bi.dsa15a.isChecked() ? "1" : bi.dsa15b.isChecked() ? "2" : "0");
        sfa.put("dsa16", bi.dsa16.getText().toString());

        for (int i = 0; i < childllArray16.size(); i++) {
            sfa.put("dsa16" + String.format("%02d", (i + 1)) + "m", monthArray16.get(i).getText().toString());
            sfa.put("dsa16" + String.format("%02d", (i + 1)) + "w", weekArray16.get(i).getText().toString());
        }

        MainApp.fc.setsA(String.valueOf(sfa));


        ChildC = new ChildrenCounter(totalU2, boysU2, girlsU2);

    }

    private boolean formValidation() {
        if (!ValidatorClass.EmptyCheckingContainer(this, bi.llcacrf01))
            return false;

        int boysU5 = Integer.valueOf(bi.dsa10.getText().toString()), girlsU5 = Integer.valueOf(bi.dsa11.getText().toString());
        int totalU5 = boysU5 + girlsU5;
        if (Integer.valueOf(bi.dsa09.getText().toString()) != totalU5)
            return ValidatorClass.EmptyCustomeTextBox(this, bi.dsa09, "Boys and Girls count not match!!");

        boysU2 = Integer.valueOf(bi.dsa13.getText().toString());
        girlsU2 = Integer.valueOf(bi.dsa14.getText().toString());
        totalU2 = boysU2 + girlsU2;
        if (Integer.valueOf(bi.dsa12.getText().toString()) != totalU2)
            return ValidatorClass.EmptyCustomeTextBox(this, bi.dsa12, "Boys and Girls count not match!!");
        if (totalU2 > totalU5)
            return ValidatorClass.EmptyCustomeTextBox(this, bi.dsa12, "Count can't be greater then Under 5 count!!");
        if (boysU2 > boysU5)
            return ValidatorClass.EmptyCustomeTextBox(this, bi.dsa12, "Boys count can't be greater then Under 5 count!!");
        if (girlsU2 > girlsU5)
            return ValidatorClass.EmptyCustomeTextBox(this, bi.dsa12, "Girls count can't be greater then Under 5 count!!");

        for (int i = 0; i < childllArray16.size(); i++) {
            int numbers = i + 1;
            if (!ValidatorClass.RangeTextBox02(SectionAActivity.this, monthArray16.get(i), 0, 9, getString(R.string.dsa13) + " month " + numbers, " Range")) {
                return false;
            }
            if (!ValidatorClass.RangeTextBox02(SectionAActivity.this, weekArray16.get(i), 1, 36, getString(R.string.dsa13) + " week " + numbers, " Range")) {
                return false;
            }
        }

        return true;

    }

    public void BtnEnd() {
        try {
            if (!ValidatorClass.EmptyCheckingContainer(this, bi.llcacrf02)) return;

            SaveDraft();

            if (!UpdateDB()) {
                Toast.makeText(this, "Error in updating db!!", Toast.LENGTH_SHORT).show();
                return;
            } else
                startActivity(new Intent(this, EndingActivity.class).putExtra("complete", false).putExtra("type", 1));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void settingGPS(FormsContract fc) {
        MainApp.LocClass locClass = MainApp.setGPS(this);
        fc.setGpsLat(locClass.getLatitude());
        fc.setGpsLng(locClass.getLongitude());
        fc.setGpsAcc(locClass.getAccuracy());
        fc.setGpsDT(locClass.getTime());
    }

}
