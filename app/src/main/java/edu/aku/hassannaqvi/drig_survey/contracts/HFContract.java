package edu.aku.hassannaqvi.drig_survey.contracts;


import android.database.Cursor;
import android.provider.BaseColumns;

import org.json.JSONException;
import org.json.JSONObject;

public class HFContract {

    private static final String TAG = "HF_CONTRACT";
    String hfcode;
    String hfname;

    public HFContract() {
        // Default Constructor
    }

    public HFContract Sync(JSONObject jsonObject) throws JSONException {
        this.hfcode = jsonObject.getString(HFTable.COLUMN_HF_CODE);
        this.hfname = jsonObject.getString(HFTable.COLUMN_HF_NAME);
        return this;
    }

    public HFContract HydrateHF(Cursor cursor) {
        this.hfcode = cursor.getString(cursor.getColumnIndex(HFTable.COLUMN_HF_CODE));
        this.hfname = cursor.getString(cursor.getColumnIndex(HFTable.COLUMN_HF_NAME));
        return this;
    }

    public String getHfcode() {
        return hfcode;
    }

    public void setHfcode(String hfcode) {
        this.hfcode = hfcode;
    }

    public String getHfname() {
        return hfname;
    }

    public void setHfname(String hfname) {
        this.hfname = hfname;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(HFTable.COLUMN_HF_CODE, this.hfcode == null ? JSONObject.NULL : this.hfcode);
        json.put(HFTable.COLUMN_HF_NAME, this.hfname == null ? JSONObject.NULL : this.hfname);
        return json;
    }


    public static abstract class HFTable implements BaseColumns {

        public static final String TABLE_NAME = "hf";
        public static final String COLUMN_HF_CODE = "hf_code";
        public static final String COLUMN_HF_NAME = "hf_name";

        public static final String _URI = "hf.php";
    }
}