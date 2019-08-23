package edu.aku.hassannaqvi.drig_survey.contracts;


import android.database.Cursor;
import android.provider.BaseColumns;

import org.json.JSONException;
import org.json.JSONObject;

public class CHWContract {

    private static final String TAG = "HF_CONTRACT";
    String chwcode;
    String chwname;
    String uccode;

    public CHWContract() {
        // Default Constructor
    }

    public CHWContract Sync(JSONObject jsonObject) throws JSONException {
        this.chwcode = jsonObject.getString(CHWTable.COLUMN_CHW_CODE);
        this.uccode = jsonObject.getString(CHWTable.COLUMN_UC_CODE);
        this.chwname = jsonObject.getString(CHWTable.COLUMN_CHW_NAME);
        return this;
    }

    public CHWContract HydrateHF(Cursor cursor) {
        this.chwcode = cursor.getString(cursor.getColumnIndex(CHWTable.COLUMN_CHW_CODE));
        this.uccode = cursor.getString(cursor.getColumnIndex(CHWTable.COLUMN_UC_CODE));
        this.chwname = cursor.getString(cursor.getColumnIndex(CHWTable.COLUMN_CHW_NAME));
        return this;
    }

    public String getChwcode() {
        return chwcode;
    }

    public void setChwcode(String chwcode) {
        this.chwcode = chwcode;
    }

    public String getChwname() {
        return chwname;
    }

    public void setChwname(String chwname) {
        this.chwname = chwname;
    }

    public String getUccode() {
        return uccode;
    }

    public void setUccode(String uccode) {
        this.uccode = uccode;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(CHWTable.COLUMN_CHW_CODE, this.chwcode == null ? JSONObject.NULL : this.chwcode);
        json.put(CHWTable.COLUMN_UC_CODE, this.uccode == null ? JSONObject.NULL : this.uccode);
        json.put(CHWTable.COLUMN_CHW_NAME, this.chwname == null ? JSONObject.NULL : this.chwname);
        return json;
    }


    public static abstract class CHWTable implements BaseColumns {

        public static final String TABLE_NAME = "chw";
        public static final String COLUMN_CHW_CODE = "chw_id";
        public static final String COLUMN_CHW_NAME = "chw_name";
        public static final String COLUMN_UC_CODE = "uc_code";

        public static final String _URI = "chws.php";
    }
}