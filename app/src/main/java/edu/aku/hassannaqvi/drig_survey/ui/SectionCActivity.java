package edu.aku.hassannaqvi.drig_survey.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import edu.aku.hassannaqvi.drig_survey.R;
import edu.aku.hassannaqvi.drig_survey.core.DatabaseHelper;
import edu.aku.hassannaqvi.drig_survey.core.MainApp;
import edu.aku.hassannaqvi.drig_survey.databinding.ActivitySectionCBinding;
import edu.aku.hassannaqvi.drig_survey.validation.ValidatorClass;

public class SectionCActivity extends AppCompatActivity {

    ActivitySectionCBinding bi;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_c);
        ButterKnife.bind(this);

        this.setTitle(R.string.dsbh);

        db = new DatabaseHelper(this);
        bi.setCallback(this);

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
            startActivity(new Intent(this, EndingActivity.class).putExtra("complete", true));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void BtnEnd() {
        MainApp.endActivity(this, this, false);
    }

    private boolean formValidation() {
        return ValidatorClass.EmptyCheckingContainer(this, bi.fldGrpcic303);
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You can't go back.", Toast.LENGTH_SHORT).show();
    }

    private void SaveDraft() throws JSONException {

        JSONObject sB = new JSONObject();

//at birth
//          dsb09bcg
        sB.put("dsb09bcg", bi.dsb09bcga.isChecked() ? "1"
                : bi.dsb09bcgb.isChecked() ? "2"
                : "0");
        sB.put("dsb09bcgsrc", bi.dsb09bcgsrca.isChecked() ? "1"
                : bi.dsb09bcgsrcb.isChecked() ? "2"
                : "0");


//          dsb09opv0
        sB.put("dsb09opv0", bi.dsb09opv0a.isChecked() ? "1"
                : bi.dsb09opv0b.isChecked() ? "2"
                : "0");
        sB.put("dsb09opv0src", bi.dsb09opv0srca.isChecked() ? "1"
                : bi.dsb09opv0srcb.isChecked() ? "2"
                : "0");


//       at age of 6

//          dsb09opv1
        sB.put("dsb09opv1", bi.dsb09opv1a.isChecked() ? "1"
                : bi.dsb09opv1b.isChecked() ? "2"
                : "0");
        sB.put("dsb09opv1src", bi.dsb09opv1srca.isChecked() ? "1"
                : bi.dsb09opv1srcb.isChecked() ? "2"
                : "0");

//          dsb09p1
        sB.put("dsb09p1", bi.dsb09p1a.isChecked() ? "1"
                : bi.dsb09p1b.isChecked() ? "2"
                : "0");
        sB.put("dsb09p1src", bi.dsb09p1srca.isChecked() ? "1"
                : bi.dsb09p1srcb.isChecked() ? "2"
                : "0");


//          dsb09pcv1
        sB.put("dsb09pcv1", bi.dsb09pcv1a.isChecked() ? "1"
                : bi.dsb09pcv1b.isChecked() ? "2"
                : "0");
        sB.put("dsb09pcv1src", bi.dsb09pcv1srca.isChecked() ? "1"
                : bi.dsb09pcv1srcb.isChecked() ? "2"
                : "0");

//       at age of 10 weeks

//          dsb09opv2
        sB.put("dsb09opv2", bi.dsb09opv2a.isChecked() ? "1"
                : bi.dsb09opv2b.isChecked() ? "2"
                : "0");
        sB.put("dsb09opv2src", bi.dsb09opv2srca.isChecked() ? "1"
                : bi.dsb09opv2srcb.isChecked() ? "2"
                : "0");

//          dsb09p2
        sB.put("dsb09p2", bi.dsb09p2a.isChecked() ? "1"
                : bi.dsb09p2b.isChecked() ? "2"
                : "0");
        sB.put("dsb09p2src", bi.dsb09p2srca.isChecked() ? "1"
                : bi.dsb09p2srcb.isChecked() ? "2"
                : "0");


//          dsb09pcv2
        sB.put("dsb09pcv2", bi.dsb09pcv2a.isChecked() ? "1"
                : bi.dsb09pcv2b.isChecked() ? "2"
                : "0");
        sB.put("dsb09pcv2src", bi.dsb09pcv2srca.isChecked() ? "1"
                : bi.dsb09pcv2srcb.isChecked() ? "2"
                : "0");


//       at age of 14 weeks

//          dsb09opv3
        sB.put("dsb09opv3", bi.dsb09opv3a.isChecked() ? "1"
                : bi.dsb09opv3b.isChecked() ? "2"
                : "0");
        sB.put("dsb09opv3src", bi.dsb09opv3srca.isChecked() ? "1"
                : bi.dsb09opv3srcb.isChecked() ? "2"
                : "0");

//          dsb09p3
        sB.put("dsb09p3", bi.dsb09p3a.isChecked() ? "1"
                : bi.dsb09p3b.isChecked() ? "2"
                : "0");
        sB.put("dsb09p3src", bi.dsb09p3srca.isChecked() ? "1"
                : bi.dsb09p3srcb.isChecked() ? "2"
                : "0");


//          dsb09pcv3
        sB.put("dsb09pcv3", bi.dsb09pcv3a.isChecked() ? "1"
                : bi.dsb09pcv3b.isChecked() ? "2"
                : "0");
        sB.put("dsb09pcv3src", bi.dsb09pcv3srca.isChecked() ? "1"
                : bi.dsb09pcv3srcb.isChecked() ? "2"
                : "0");

//          dsb09ipv
        sB.put("dsb09ipv", bi.dsb09ipva.isChecked() ? "1"
                : bi.dsb09ipvb.isChecked() ? "2"
                : "0");
        sB.put("dsb09ipvsrc", bi.dsb09ipvsrca.isChecked() ? "1"
                : bi.dsb09ipvsrcb.isChecked() ? "2"
                : "0");


//at the age of 9 months
//          dsb09m1
        sB.put("dsb09m1", bi.dsb09m1a.isChecked() ? "1"
                : bi.dsb09m1b.isChecked() ? "2"
                : "0");
        sB.put("dsb09m1src", bi.dsb09m1srca.isChecked() ? "1"
                : bi.dsb09m1srcb.isChecked() ? "2"
                : "0");


//at age of 15 months
//          dsb09m2
        sB.put("dsb09m2", bi.dsb09m2a.isChecked() ? "1"
                : bi.dsb09m2b.isChecked() ? "2"
                : "0");
        sB.put("dsb09m2src", bi.dsb09m2srca.isChecked() ? "1"
                : bi.dsb09m2srcb.isChecked() ? "2"
                : "0");

        MainApp.fc.setsB(String.valueOf(sB));

    }

    private boolean UpdateDB() {

        //Long rowId;
        DatabaseHelper db = new DatabaseHelper(this);

        int updcount = db.updateCSB();

        if (updcount == 1) {
            return true;
        } else {
            Toast.makeText(this, "Updating Database... ERROR!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

}

