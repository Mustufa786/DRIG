package edu.aku.hassannaqvi.drig_survey.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import edu.aku.hassannaqvi.drig_survey.R;
import edu.aku.hassannaqvi.drig_survey.core.DatabaseHelper;
import edu.aku.hassannaqvi.drig_survey.core.MainApp;
import edu.aku.hassannaqvi.drig_survey.databinding.ActivitySectionCBinding;
import edu.aku.hassannaqvi.drig_survey.validation.ValidatorClass;

public class SectionCActivity {

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
//          cic3bcg
        sB.put("cic3bcg", bi.cic3bcga.isChecked() ? "1"
                : bi.cic3bcgb.isChecked() ? "2"
                : "0");
        sB.put("cic3bcgsrc", bi.cic3bcgsrca.isChecked() ? "1"
                : bi.cic3bcgsrcb.isChecked() ? "2"
                : "0");


//          cic3opv0
        sB.put("cic3opv0", bi.cic3opv0a.isChecked() ? "1"
                : bi.cic3opv0b.isChecked() ? "2"
                : "0");
        sB.put("cic3opv0src", bi.cic3opv0srca.isChecked() ? "1"
                : bi.cic3opv0srcb.isChecked() ? "2"
                : "0");


//       at age of 6

//          cic3opv1
        sB.put("cic3opv1", bi.cic3opv1a.isChecked() ? "1"
                : bi.cic3opv1b.isChecked() ? "2"
                : "0");
        sB.put("cic3opv1src", bi.cic3opv1srca.isChecked() ? "1"
                : bi.cic3opv1srcb.isChecked() ? "2"
                : "0");

//          cic3p1
        sB.put("cic3p1", bi.cic3p1a.isChecked() ? "1"
                : bi.cic3p1b.isChecked() ? "2"
                : "0");
        sB.put("cic3p1src", bi.cic3p1srca.isChecked() ? "1"
                : bi.cic3p1srcb.isChecked() ? "2"
                : "0");


//          cic3pcv1
        sB.put("cic3pcv1", bi.cic3pcv1a.isChecked() ? "1"
                : bi.cic3pcv1b.isChecked() ? "2"
                : "0");
        sB.put("cic3pcv1src", bi.cic3pcv1srca.isChecked() ? "1"
                : bi.cic3pcv1srcb.isChecked() ? "2"
                : "0");

//       at age of 10 weeks

//          cic3opv2
        sB.put("cic3opv2", bi.cic3opv2a.isChecked() ? "1"
                : bi.cic3opv2b.isChecked() ? "2"
                : "0");
        sB.put("cic3opv2src", bi.cic3opv2srca.isChecked() ? "1"
                : bi.cic3opv2srcb.isChecked() ? "2"
                : "0");

//          cic3p2
        sB.put("cic3p2", bi.cic3p2a.isChecked() ? "1"
                : bi.cic3p2b.isChecked() ? "2"
                : "0");
        sB.put("cic3p2src", bi.cic3p2srca.isChecked() ? "1"
                : bi.cic3p2srcb.isChecked() ? "2"
                : "0");


//          cic3pcv2
        sB.put("cic3pcv2", bi.cic3pcv2a.isChecked() ? "1"
                : bi.cic3pcv2b.isChecked() ? "2"
                : "0");
        sB.put("cic3pcv2src", bi.cic3pcv2srca.isChecked() ? "1"
                : bi.cic3pcv2srcb.isChecked() ? "2"
                : "0");


//       at age of 14 weeks

//          cic3opv3
        sB.put("cic3opv3", bi.cic3opv3a.isChecked() ? "1"
                : bi.cic3opv3b.isChecked() ? "2"
                : "0");
        sB.put("cic3opv3src", bi.cic3opv3srca.isChecked() ? "1"
                : bi.cic3opv3srcb.isChecked() ? "2"
                : "0");

//          cic3p3
        sB.put("cic3p3", bi.cic3p3a.isChecked() ? "1"
                : bi.cic3p3b.isChecked() ? "2"
                : "0");
        sB.put("cic3p3src", bi.cic3p3srca.isChecked() ? "1"
                : bi.cic3p3srcb.isChecked() ? "2"
                : "0");


//          cic3pcv3
        sB.put("cic3pcv3", bi.cic3pcv3a.isChecked() ? "1"
                : bi.cic3pcv3b.isChecked() ? "2"
                : "0");
        sB.put("cic3pcv3src", bi.cic3pcv3srca.isChecked() ? "1"
                : bi.cic3pcv3srcb.isChecked() ? "2"
                : "0");

//          cic3ipv
        sB.put("cic3ipv", bi.cic3ipva.isChecked() ? "1"
                : bi.cic3ipvb.isChecked() ? "2"
                : "0");
        sB.put("cic3ipvsrc", bi.cic3ipvsrca.isChecked() ? "1"
                : bi.cic3ipvsrcb.isChecked() ? "2"
                : "0");


//at the age of 9 months
//          cic3m1
        sB.put("cic3m1", bi.cic3m1a.isChecked() ? "1"
                : bi.cic3m1b.isChecked() ? "2"
                : "0");
        sB.put("cic3m1src", bi.cic3m1srca.isChecked() ? "1"
                : bi.cic3m1srcb.isChecked() ? "2"
                : "0");


//at age of 15 months
//          cic3m2
        sB.put("cic3m2", bi.cic3m2a.isChecked() ? "1"
                : bi.cic3m2b.isChecked() ? "2"
                : "0");
        sB.put("cic3m2src", bi.cic3m2srca.isChecked() ? "1"
                : bi.cic3m2srcb.isChecked() ? "2"
                : "0");

        MainApp.fc.setsB(String.valueOf(sB));

    }

    private boolean UpdateDB() {

        //Long rowId;
        DatabaseHelper db = new DatabaseHelper(this);

        int updcount = db.updateSB();

        if (updcount == 1) {
            return true;
        } else {
            Toast.makeText(this, "Updating Database... ERROR!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

}

