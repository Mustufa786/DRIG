package edu.aku.hassannaqvi.drig_survey.contracts;

import android.database.Cursor;
import android.provider.BaseColumns;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by javed.khan on 1/22/2018.
 */

public class SchoolContract {

    private String sch_code;
    private String sch_name;
    private String sch_add;
    private String sch_status;
    private String sch_type;

    public SchoolContract() {
    }

    public String getSch_code() {
        return sch_code;
    }

    public String getSch_name() {
        return sch_name;
    }

    public String getSch_add() {
        return sch_add;
    }

    public String getSch_status() {
        return sch_status;
    }

    public String getSch_type() {
        return sch_type;
    }

    public SchoolContract sync(JSONObject jsonObject) throws JSONException {
        this.sch_code = jsonObject.getString(SchoolTable.COLUMN_SCH_CODE);
        this.sch_name = jsonObject.getString(SchoolTable.COLUMN_SCH_NAME);
        this.sch_add = jsonObject.getString(SchoolTable.COLUMN_SCH_ADD);
        if (!jsonObject.get(SchoolTable.COLUMN_SCH_STATUS).equals(null))
            this.sch_status = jsonObject.getString(SchoolTable.COLUMN_SCH_STATUS);
        this.sch_type = jsonObject.getString(SchoolTable.COLUMN_SCH_TYPE);
        return this;
    }

    public SchoolContract hydrate(Cursor cursor) {
        this.sch_code = cursor.getString(cursor.getColumnIndex(SchoolTable.COLUMN_SCH_CODE));
        this.sch_name = cursor.getString(cursor.getColumnIndex(SchoolTable.COLUMN_SCH_NAME));
        this.sch_add = cursor.getString(cursor.getColumnIndex(SchoolTable.COLUMN_SCH_ADD));
        this.sch_status = cursor.getString(cursor.getColumnIndex(SchoolTable.COLUMN_SCH_STATUS));
        this.sch_type = cursor.getString(cursor.getColumnIndex(SchoolTable.COLUMN_SCH_TYPE));
        return this;
    }

    public static abstract class SchoolTable implements BaseColumns {

        public static final String TABLE_NAME = "schools";
        public static final String COLUMN_NAME_NULLABLE = "nullColumnHack";
        public static final String _ID = "_id";
        public static final String COLUMN_SCH_CODE = "sch_code";
        public static final String COLUMN_SCH_NAME = "sch_name";
        public static final String COLUMN_SCH_ADD = "sch_add";
        public static final String COLUMN_SCH_STATUS = "sch_status";
        public static final String COLUMN_SCH_TYPE = "sch_type";

        public static final String _URI = "schools.php";

    }

}