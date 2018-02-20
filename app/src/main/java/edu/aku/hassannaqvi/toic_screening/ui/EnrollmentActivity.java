package edu.aku.hassannaqvi.toic_screening.ui;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.aku.hassannaqvi.toic_screening.R;
import edu.aku.hassannaqvi.toic_screening.contracts.EnrollmentContract;
import edu.aku.hassannaqvi.toic_screening.core.DatabaseHelper;
import edu.aku.hassannaqvi.toic_screening.core.MainApp;
import edu.aku.hassannaqvi.toic_screening.databinding.ActivitySecEnrollmentBinding;
import edu.aku.hassannaqvi.toic_screening.other.childData;
import edu.aku.hassannaqvi.toic_screening.validation.validatorClass;

public class EnrollmentActivity extends AppCompatActivity {

    private static final String TAG = EnrollmentActivity.class.getName();
    ActivitySecEnrollmentBinding binding;
    DatabaseHelper db;
    String dtToday = new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date().getTime());
    //String dtToday1 = new SimpleDateFormat("dd/MM/yy").format(System.currentTimeMillis());
    String dateToday = new SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis());

    String maxDate6Months = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTimeInMillis() - (MainApp.MILLISECONDS_IN_6_MONTH));
    String maxDate60Months = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTimeInMillis() - (MainApp.MILLISECONDS_IN_5Years));
    String max2Days = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTimeInMillis() - (MainApp.MILLISECONDS_IN_2DAYS));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //        Initializing
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sec_enrollment);
        binding.setCallback(this);
        db = new DatabaseHelper(this);

//        setting for daterpicker
        binding.toicc06aa.setManager(getSupportFragmentManager());
        binding.toicc06aa.setMaxDate(maxDate6Months);
        binding.toicc06aa.setMinDate(maxDate60Months);
        binding.toicc10.setManager(getSupportFragmentManager());
        binding.toicc13.setManager(getSupportFragmentManager());
        binding.toicc10.setMaxDate(dateToday);
        binding.toicc13.setMaxDate(dateToday);

//        Getting Extra

        childData data = (childData) getIntent().getBundleExtra("data").getSerializable("data");

        binding.toicc01.setText(data.getEnrollID());
        binding.toicc02.setText(data.getChild_name());
        binding.toicc03.setText(data.getF_name());

        binding.toicc10.setMinDate(max2Days);
        binding.toicc13.setMinDate(maxDate60Months);

        /*binding.toicc06.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                binding.toicc10.setText(null);
                binding.toicc13.setText(null);

                if (i == R.id.toicc06b) {
                    binding.toicc10.setMinDate(maxDate60Months);
                    binding.toicc13.setMinDate(maxDate60Months);
                }
            }
        });*/


        /*binding.toicc06aa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (binding.toicc06a.isChecked()) {
                    binding.toicc10.setMinDate(MainApp.convertDateFormat(binding.toicc06aa.getText().toString()));
                    binding.toicc13.setMinDate(MainApp.convertDateFormat(binding.toicc06aa.getText().toString()));

                } else {
                    binding.toicc10.setMinDate(maxDate60Months);
                    binding.toicc13.setMinDate(maxDate60Months);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

    }

    public Boolean formValidation() {

//        toicc01
        if (!validatorClass.EmptyTextBox(this, binding.toicc01, getString(R.string.toicc01))) {
            return false;
        }
//        toicc02
        if (!validatorClass.EmptyTextBox(this, binding.toicc02, getString(R.string.toicc02))) {
            return false;
        }
//        toicc03
        if (!validatorClass.EmptyTextBox(this, binding.toicc03, getString(R.string.toicc03))) {
            return false;
        }
//        toicc04
        if (!validatorClass.EmptyTextBox(this, binding.toicc04, getString(R.string.toicc04))) {
            return false;
        }
//        toicc05
        if (!validatorClass.EmptyRadioButton(this, binding.toicc05, binding.toicc05b, getString(R.string.toicc05))) {
            return false;
        }
//        toicc06
        if (!validatorClass.EmptyRadioButton(this, binding.toicc06, binding.toicc06b, getString(R.string.toicc06))) {
            return false;
        }

        if (binding.toicc06a.isChecked()) {
//        toicc06a
            if (!validatorClass.EmptyTextBox(this, binding.toicc06aa, getString(R.string.toicc06a))) {
                return false;
            }
        } else {
//        toicc06b
            if (!validatorClass.EmptyTextBox(this, binding.toicc06bb, getString(R.string.toicc06b))) {
                return false;
            }
            if (!validatorClass.RangeTextBox(this, binding.toicc06bb, 6, 60, getString(R.string.toicc06b), " for month")) {
                return false;
            }
        }

//        toicc07
        if (!validatorClass.EmptyTextBox(this, binding.toicc07, getString(R.string.toicc07))) {
            return false;
        }

        if (binding.toicc07.length() != 11) {
            Toast.makeText(this, "Error: " + getString(R.string.toicc07), Toast.LENGTH_SHORT).show();
            binding.toicc07.setError("Invalid(Pattern):Not correct.");
            Log.e(TAG, "Invalid(Pattern):Not correct.");
            return false;
        }else {
            binding.toicc07.setError(null);
        }

//        toicc08
        if (!validatorClass.EmptyTextBox(this, binding.toicc08, getString(R.string.toicc08))) {
            return false;
        }

        if (binding.toicc08.length() != 11) {
            Toast.makeText(this, "Error: " + getString(R.string.toicc08), Toast.LENGTH_SHORT).show();
            binding.toicc08.setError("Invalid(Pattern):Not correct.");
            Log.e(TAG, "Invalid(Pattern):Not correct.");
            return false;
        }else {
            binding.toicc08.setError(null);
        }

//        toicc09
        if (!validatorClass.EmptyTextBox(this, binding.toicc09, getString(R.string.toicc09))) {
            return false;
        }
//        toicc10
        if (!validatorClass.EmptyTextBox(this, binding.toicc10, getString(R.string.toicc10))) {
            return false;
        }
//        toicc11
        /*if (!validatorClass.EmptyTextBox(this, binding.toicc11, getString(R.string.toicc11))) {
            return false;
        }*/
//        toicc12
        if (!validatorClass.EmptyTextBox(this, binding.toicc12, getString(R.string.toicc12))) {
            return false;
        }
//        toicc13
        /*if (!validatorClass.EmptyTextBox(this, binding.toicc13, getString(R.string.toicc13))) {
            return false;
        }*/

//        toicc14
        return validatorClass.EmptyRadioButton(this, binding.toicc14, binding.toicc14b, getString(R.string.toicc14));
    }


    public void BtnContinue() {

        Toast.makeText(this, "Processing This Section", Toast.LENGTH_SHORT).show();
        if (formValidation()) {
            try {
                SaveDraft();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (UpdateDB()) {
                Toast.makeText(this, "Starting Ending Section", Toast.LENGTH_SHORT).show();

                MainApp.endEnrollmentActivity(this, this, true);

            } else {
                Toast.makeText(this, "Failed to Update Database!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void BtnEnd() {
        Toast.makeText(this, "Processing EndActivity Section", Toast.LENGTH_SHORT).show();
        MainApp.endEnrollmentActivity(this, this, false);
    }

    private void SaveDraft() throws JSONException {
        Toast.makeText(this, "Saving Draft for  This Section", Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPref = getSharedPreferences("tagName", MODE_PRIVATE);
        MainApp.ec = new EnrollmentContract();


        MainApp.ec.setDevicetagID(sharedPref.getString("tagName", null));
        MainApp.ec.setFormDate(dtToday);
        MainApp.ec.setUser(MainApp.userName);
        MainApp.ec.setDeviceID(Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID));
        MainApp.ec.setAppversion(MainApp.versionName + "." + MainApp.versionCode);
        MainApp.ec.setUUID(MainApp.cc.getUID());

        JSONObject sc = new JSONObject();

        sc.put("toicc01", binding.toicc01.getText().toString());
        sc.put("toicc02", binding.toicc02.getText().toString());
        sc.put("toicc03", binding.toicc03.getText().toString());
        sc.put("toicc04", binding.toicc04.getText().toString());

        sc.put("toicc05", binding.toicc05a.isChecked() ? "1" : binding.toicc05b.isChecked() ? "2" : "0");
        sc.put("toicc06", binding.toicc06a.isChecked() ? "1" : binding.toicc06b.isChecked() ? "2" : "0");

        if (binding.toicc06a.isChecked()) {
            sc.put("toicc06age", binding.toicc06aa.getText().toString());
        } else {
            sc.put("toicc06age", binding.toicc06bb.getText().toString());
        }

        sc.put("toicc07", binding.toicc07.getText().toString());
        sc.put("toicc08", binding.toicc08.getText().toString());
        sc.put("toicc09", binding.toicc09.getText().toString());
        sc.put("toicc10", binding.toicc10.getText().toString());
        sc.put("toicc11", binding.toicc11.getText().toString());
        sc.put("toicc12", binding.toicc12.getText().toString());
        sc.put("toicc13", binding.toicc13.getText().toString());

        sc.put("toicc14", binding.toicc14a.isChecked() ? "1" : binding.toicc14b.isChecked() ? "2" : "0");


        MainApp.ec.setsC(String.valueOf(sc));

        Toast.makeText(this, "Validation Successful! - Saving Draft...", Toast.LENGTH_SHORT).show();
    }

    private boolean UpdateDB() {

        Long updcount = db.addEnrollmentForm(MainApp.ec);
        MainApp.ec.set_ID(String.valueOf(updcount));

        if (updcount != 0) {
            Toast.makeText(this, "Updating Database... Successful!", Toast.LENGTH_SHORT).show();

            MainApp.ec.setUID(
                    (MainApp.ec.getDeviceID() + MainApp.ec.get_ID()));
            db.updateFormEnrollmentID();

            return true;
        } else {
            Toast.makeText(this, "Updating Database... ERROR!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You Can't go back", Toast.LENGTH_LONG).show();
    }
}
