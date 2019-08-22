package edu.aku.hassannaqvi.drig_survey.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import edu.aku.hassannaqvi.drig_survey.R;
import edu.aku.hassannaqvi.drig_survey.core.DatabaseHelper;
import edu.aku.hassannaqvi.drig_survey.core.MainApp;
import edu.aku.hassannaqvi.drig_survey.databinding.ActivityEndingBinding;
import edu.aku.hassannaqvi.drig_survey.validation.ValidatorClass;

public class EndingActivity extends AppCompatActivity {

    private static final String TAG = EndingActivity.class.getSimpleName();

    ActivityEndingBinding binding;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_ending);
        binding.setCallback(this);

        Boolean check = getIntent().getExtras().getBoolean("complete");
        type = getIntent().getIntExtra("type", 0);

        if (type == 1)
            this.setTitle("FORM-ENDING ACTIVITY");
        else
            this.setTitle("CHILD-ENDING ACTIVITY");


        if (check) {
            binding.istatusa.setEnabled(true);
            binding.istatusb.setEnabled(false);
        } else {
            binding.istatusa.setEnabled(false);
            binding.istatusb.setEnabled(true);
        }

/*        istatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (istatus88.isChecked()) {
                    istatus88x.setVisibility(View.VISIBLE);
                    //istatus88x.requestFocus();
                } else {
                    istatus88x.setText(null);
                    istatus88x.setVisibility(View.GONE);
                }
            }
        });*/

    }

    public void BtnEnd() {

        if (formValidation()) {
            SaveDraft();
            if (UpdateDB()) {

                finish();

                Intent endSec = new Intent(this, MainActivity.class);

                if (type != 1) {
                    if (SectionAActivity.ChildC.getTotal() > SectionAActivity.ChildC.getTotalCount()) {
                        endSec = new Intent(this, SectionBActivity.class);
                    } else
                        endSec = new Intent(this, EndingActivity.class).putExtra("complete", true).putExtra("type", 1);
                }

                startActivity(endSec);
            } else {
                Toast.makeText(this, "Failed to Update Database!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SaveDraft() {
        String statusCode = binding.istatusa.isChecked() ? "1" : binding.istatusb.isChecked() ? "2" : "0";

        if (type == 1)
            MainApp.fc.setIstatus(statusCode);
        else {
            MainApp.cc.setIstatus(statusCode);
            SectionAActivity.ChildC.setTotalCount(SectionAActivity.ChildC.getTotalCount() + 1);
        }

//        MainApp.fc.setIstatus88x(istatus88x.getText().toString());
    }

    private boolean UpdateDB() {
        DatabaseHelper db = new DatabaseHelper(this);

        int updcount = 0;

        if (type == 1)
            updcount = db.updateEnding();
        else
            updcount = db.updateChildEnding();

        if (updcount == 1) {
            return true;
        } else {
            Toast.makeText(this, "Updating Database... ERROR!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private boolean formValidation() {
        return ValidatorClass.EmptyRadioButton(this, binding.istatus, binding.istatusb, getString(R.string.istatus));
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "You Can't go back", Toast.LENGTH_LONG).show();
    }


}
