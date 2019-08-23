package edu.aku.hassannaqvi.drig_survey.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import edu.aku.hassannaqvi.drig_survey.R;
import edu.aku.hassannaqvi.drig_survey.core.DatabaseHelper;
import edu.aku.hassannaqvi.drig_survey.core.MainApp;
import edu.aku.hassannaqvi.drig_survey.databinding.ActivityEndingBinding;
import edu.aku.hassannaqvi.drig_survey.validation.ValidatorClass;

public class EndingActivity extends AppCompatActivity {

    private static final String TAG = EndingActivity.class.getSimpleName();

    ActivityEndingBinding bi;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bi = DataBindingUtil.setContentView(this, R.layout.activity_ending);
        bi.setCallback(this);

        Boolean check = getIntent().getExtras().getBoolean("complete");
        type = getIntent().getIntExtra("type", 0);

        if (type == 1)
            this.setTitle("FORM-ENDING ACTIVITY");
        else
            this.setTitle("CHILD-ENDING ACTIVITY");


        if (check) {
            bi.istatusa.setEnabled(true);
            bi.istatusb.setEnabled(false);
            bi.istatusc.setEnabled(false);
            bi.istatusd.setEnabled(false);
        } else {
            bi.istatusa.setEnabled(false);
            bi.istatusb.setEnabled(true);
            bi.istatusc.setEnabled(true);
            bi.istatusd.setEnabled(true);
        }

        if (type == 1) {
            bi.istatusb.setText(R.string.dsa18b);
            bi.istatusc.setText(R.string.dsa18c);
            bi.istatusd.setText(R.string.dsa18d);
        } else {
            bi.istatusb.setText(R.string.istatusb);
            bi.istatusc.setVisibility(View.GONE);
            bi.istatusd.setVisibility(View.GONE);
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
        String statusCode = bi.istatusa.isChecked() ? "1" : bi.istatusb.isChecked() ? "2" : bi.istatusc.isChecked() ? "3" : bi.istatusd.isChecked() ? "4" : "0";

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
        return ValidatorClass.EmptyRadioButton(this, bi.istatus, bi.istatusb, getString(R.string.istatus));
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "You Can't go back", Toast.LENGTH_LONG).show();
    }


}
