package edu.aku.hassannaqvi.drig_survey.contracts;

import android.database.Cursor;
import android.provider.BaseColumns;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by javed.khan on 1/22/2018.
 */

public class ChildContract {

    private final String projectName = "DMU-TOICSCREENING";
    //private final String surveyType = "SN";
    private String _ID = "";
    private String _UID = "";
    private String _UUID = "";
    private String formDate = ""; // Date
    private String user = ""; // Interviewer
    //private String formtype = "";

    private String istatus = ""; // Interview Status
    //private String istatus88x = ""; // Interview Status

    //private String sA = "";     // Info Section
    private String sB = ""; // sB
    //private String sC = ""; //
    //private String sD = "";


    private String gpsLat = "";
    private String gpsLng = "";
    private String gpsDT = "";
    private String gpsAcc = "";
    private String deviceID = "";
    private String devicetagID = "";
    private String synced = "";
    private String synced_date = "";
    private String appversion;

    public ChildContract() {

    }


    public ChildContract Sync(JSONObject jsonObject) throws JSONException {
        this._ID = jsonObject.getString(FormsChildTable._ID);
        this._UID = jsonObject.getString(FormsChildTable.COLUMN_UID);
        this._UUID = jsonObject.getString(FormsChildTable.COLUMN_UUID);
        this.formDate = jsonObject.getString(FormsChildTable.COLUMN_FORMDATE);
        this.user = jsonObject.getString(FormsChildTable.COLUMN_USER);
        this.istatus = jsonObject.getString(FormsChildTable.COLUMN_ISTATUS);
        //this.istatus88x = jsonObject.getString(FormsContract.FormsChildTable.COLUMN_ISTATUS);
        //this.formtype = jsonObject.getString(FormsContract.FormsChildTable.COLUMN_FORMTYPE);
        //this.sA = jsonObject.getString(FormsContract.FormsChildTable.COLUMN_SA);
        this.sB = jsonObject.getString(FormsChildTable.COLUMN_SB);
        //this.sC = jsonObject.getString(FormsContract.FormsChildTable.COLUMN_SC);
        //this.sD = jsonObject.getString(FormsContract.FormsChildTable.COLUMN_SD);
        this.gpsLat = jsonObject.getString(FormsChildTable.COLUMN_GPSLAT);
        this.gpsLng = jsonObject.getString(FormsChildTable.COLUMN_GPSLNG);
        this.gpsDT = jsonObject.getString(FormsChildTable.COLUMN_GPSDATE);
        this.gpsAcc = jsonObject.getString(FormsChildTable.COLUMN_GPSACC);
        this.deviceID = jsonObject.getString(FormsChildTable.COLUMN_DEVICEID);
        this.devicetagID = jsonObject.getString(FormsChildTable.COLUMN_DEVICETAGID);
        this.synced = jsonObject.getString(FormsChildTable.COLUMN_SYNCED);
        this.synced_date = jsonObject.getString(FormsChildTable.COLUMN_SYNCED_DATE);
        this.appversion = jsonObject.getString(FormsChildTable.COLUMN_APP_VERSION);

        return this;

    }

    public ChildContract Hydrate(Cursor cursor) {
        this._ID = cursor.getString(cursor.getColumnIndex(FormsChildTable._ID));
        this._UID = cursor.getString(cursor.getColumnIndex(FormsChildTable.COLUMN_UID));
        this._UUID = cursor.getString(cursor.getColumnIndex(FormsChildTable.COLUMN_UUID));
        this.formDate = cursor.getString(cursor.getColumnIndex(FormsChildTable.COLUMN_FORMDATE));
        this.user = cursor.getString(cursor.getColumnIndex(FormsChildTable.COLUMN_USER));
        this.istatus = cursor.getString(cursor.getColumnIndex(FormsChildTable.COLUMN_ISTATUS));
        //this.istatus88x = cursor.getString(cursor.getColumnIndex(FormsChildTable.COLUMN_ISTATUS));
        //this.formtype = cursor.getString(cursor.getColumnIndex(FormsChildTable.COLUMN_FORMTYPE));
        //this.sA = cursor.getString(cursor.getColumnIndex(FormsChildTable.COLUMN_SA));
        this.sB = cursor.getString(cursor.getColumnIndex(FormsChildTable.COLUMN_SB));
        //this.sC = cursor.getString(cursor.getColumnIndex(FormsChildTable.COLUMN_SC));
        //this.sD = cursor.getString(cursor.getColumnIndex(FormsChildTable.COLUMN_SD));
        this.gpsLat = cursor.getString(cursor.getColumnIndex(FormsChildTable.COLUMN_GPSLAT));
        this.gpsLng = cursor.getString(cursor.getColumnIndex(FormsChildTable.COLUMN_GPSLNG));
        this.gpsDT = cursor.getString(cursor.getColumnIndex(FormsChildTable.COLUMN_GPSDATE));
        this.gpsAcc = cursor.getString(cursor.getColumnIndex(FormsChildTable.COLUMN_GPSACC));
        this.deviceID = cursor.getString(cursor.getColumnIndex(FormsChildTable.COLUMN_DEVICEID));
        this.devicetagID = cursor.getString(cursor.getColumnIndex(FormsChildTable.COLUMN_DEVICETAGID));
        this.synced = cursor.getString(cursor.getColumnIndex(FormsChildTable.COLUMN_SYNCED));
        this.synced_date = cursor.getString(cursor.getColumnIndex(FormsChildTable.COLUMN_SYNCED_DATE));
        this.appversion = cursor.getString(cursor.getColumnIndex(FormsChildTable.COLUMN_APP_VERSION));

        // TODO:

        return this;

    }


    public JSONObject toJSONObject() throws JSONException {

        JSONObject json = new JSONObject();

        json.put(FormsChildTable._ID, this._ID == null ? JSONObject.NULL : this._ID);
        json.put(FormsChildTable.COLUMN_UID, this._UID == null ? JSONObject.NULL : this._UID);
        json.put(FormsChildTable.COLUMN_UUID, this._UUID == null ? JSONObject.NULL : this._UUID);
        json.put(FormsChildTable.COLUMN_FORMDATE, this.formDate == null ? JSONObject.NULL : this.formDate);
        json.put(FormsChildTable.COLUMN_USER, this.user == null ? JSONObject.NULL : this.user);
        json.put(FormsChildTable.COLUMN_ISTATUS, this.istatus == null ? JSONObject.NULL : this.istatus);
        //json.put(FormsChildTable.COLUMN_ISTATUS88x, this.istatus88x == null ? JSONObject.NULL : this.istatus88x);
        //json.put(FormsChildTable.COLUMN_FORMTYPE, this.formtype == null ? JSONObject.NULL : this.formtype);

        /*if (!this.sA.equals("")) {

            json.put(FormsChildTable.COLUMN_SA, this.sA.equals("") ? JSONObject.NULL : new JSONObject(this.sA));
        }*/

        if (!this.sB.equals("")) {

            json.put(FormsChildTable.COLUMN_SB, this.sB.equals("") ? JSONObject.NULL : new JSONObject(this.sB));
        }

        /*if (!this.sC.equals("")) {

            json.put(FormsChildTable.COLUMN_SC, this.sC.equals("") ? JSONObject.NULL : new JSONObject(this.sC));
            // }
        }
        if (!this.sD.equals("")) {

            json.put(FormsChildTable.COLUMN_SD, this.sD.equals("") ? JSONObject.NULL : new JSONObject(this.sD));
        }*/

/*        json.put(FormsChildTable.COLUMN_GPSLAT, this.gpsLat == null ? JSONObject.NULL : this.gpsLat);
        json.put(FormsChildTable.COLUMN_GPSLNG, this.gpsLng == null ? JSONObject.NULL : this.gpsLng);
        json.put(FormsChildTable.COLUMN_GPSDATE, this.gpsDT == null ? JSONObject.NULL : this.gpsDT);
        json.put(FormsChildTable.COLUMN_GPSACC, this.gpsAcc == null ? JSONObject.NULL : this.gpsAcc);*/
        json.put(FormsChildTable.COLUMN_DEVICEID, this.deviceID == null ? JSONObject.NULL : this.deviceID);
        json.put(FormsChildTable.COLUMN_DEVICETAGID, this.devicetagID == null ? JSONObject.NULL : this.devicetagID);
        json.put(FormsChildTable.COLUMN_SYNCED, this.synced == null ? JSONObject.NULL : this.synced);
        json.put(FormsChildTable.COLUMN_SYNCED_DATE, this.synced_date == null ? JSONObject.NULL : this.synced_date);
        json.put(FormsChildTable.COLUMN_APP_VERSION, this.appversion == null ? JSONObject.NULL : this.appversion);


        return json;
    }

    public String getProjectName() {
        return projectName;
    }

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public String getUID() {
        return _UID;
    }

    public void setUID(String _UID) {
        this._UID = _UID;
    }


    public String getUUID() {
        return _UUID;
    }

    public void setUUID(String _UUID) {
        this._UUID = _UUID;
    }


    public String getFormDate() {
        return formDate;
    }

    public void setFormDate(String formDate) {
        this.formDate = formDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getIstatus() {
        return istatus;
    }

    public void setIstatus(String istatus) {
        this.istatus = istatus;
    }

/*
    public String getIstatus88x() {
        return istatus88x;
    }

    public void setIstatus88x(String istatus88x) {
        this.istatus88x = istatus88x;
    }


    public String getsA() {
        return sA;
    }

    public void setsA(String sA) {
        this.sA = sA;
    }


    public String getFormtype() {
        return formtype;
    }

    public void setFormtype(String formtype) {
        this.formtype = formtype;
    }

    public String getsD() {
        return sD;
    }

    public void setsD(String sD) {
        this.sD = sD;
    }*//*public String getIstatus() {
        return istatus;
    }

    public void setIstatus(String istatus) {
        this.istatus = istatus;
    }


    public String getIstatus88x() {
        return istatus88x;
    }

    public void setIstatus88x(String istatus88x) {
        this.istatus88x = istatus88x;
    }


    public String getsA() {
        return sA;
    }

    public void setsA(String sA) {
        this.sA = sA;
    }


    public String getFormtype() {
        return formtype;
    }

    public void setFormtype(String formtype) {
        this.formtype = formtype;
    }

    public String getsD() {
        return sD;
    }

    public void setsD(String sD) {
        this.sD = sD;
    }*/

    public String getsB() {
        return sB;
    }

    public void setsB(String sB) {
        this.sB = sB;
    }

    /*public String getsC() {

        return sC;
    }

    public void setsC(String sC) {
        this.sC = sC;
    }*/

    public String getGpsLat() {
        return gpsLat;
    }

    public void setGpsLat(String gpsLat) {
        this.gpsLat = gpsLat;
    }

    public String getGpsLng() {
        return gpsLng;
    }

    public void setGpsLng(String gpsLng) {
        this.gpsLng = gpsLng;
    }

    public String getGpsDT() {
        return gpsDT;
    }

    public void setGpsDT(String gpsDT) {
        this.gpsDT = gpsDT;
    }

    public String getGpsAcc() {
        return gpsAcc;
    }

    public void setGpsAcc(String gpsAcc) {
        this.gpsAcc = gpsAcc;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDevicetagID() {
        return devicetagID;
    }

    public void setDevicetagID(String devicetagID) {
        this.devicetagID = devicetagID;
    }

    public String getSynced() {
        return synced;
    }

    public void setSynced(String synced) {
        this.synced = synced;
    }

    public String getSynced_date() {
        return synced_date;
    }

    public void setSynced_date(String synced_date) {
        this.synced_date = synced_date;
    }

    public String getAppversion() {
        return appversion;
    }

    public void setAppversion(String appversion) {
        this.appversion = appversion;
    }

    public static abstract class FormsChildTable implements BaseColumns {

        public static final String TABLE_NAME = "child";
        public static final String COLUMN_NAME_NULLABLE = "NULLHACK";
        public static final String COLUMN_PROJECT_NAME = "projectname";
        public static final String _ID = "_id";
        public static final String COLUMN_UID = "_uid";
        public static final String COLUMN_UUID = "_uuid";
        public static final String COLUMN_FORMDATE = "formdate";
        public static final String COLUMN_USER = "user";
        public static final String COLUMN_ISTATUS = "istatus";
        //public static final String COLUMN_ISTATUS88x = "istatus88x";
        //public static final String COLUMN_FORMTYPE = "formtype";
        //public static final String COLUMN_SA = "sa";
        public static final String COLUMN_SB = "sb";
        //public static final String COLUMN_SC = "sc";
        //public static final String COLUMN_SD = "sd";

        public static final String COLUMN_GPSLAT = "gpslat";
        public static final String COLUMN_GPSLNG = "gpslng";
        public static final String COLUMN_GPSDATE = "gpsdate";
        public static final String COLUMN_GPSACC = "gpsacc";


        public static final String COLUMN_DEVICEID = "deviceid";
        public static final String COLUMN_DEVICETAGID = "tagid";
        public static final String COLUMN_SYNCED = "synced";
        public static final String COLUMN_SYNCED_DATE = "synced_date";
        public static final String COLUMN_APP_VERSION = "appversion";

        public static String _URL = "childforms.php";
    }

}