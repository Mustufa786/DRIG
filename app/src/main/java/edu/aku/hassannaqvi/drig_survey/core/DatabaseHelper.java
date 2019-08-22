package edu.aku.hassannaqvi.drig_survey.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import edu.aku.hassannaqvi.drig_survey.contracts.ChildContract;
import edu.aku.hassannaqvi.drig_survey.contracts.ChildContract.ChildFormsTable;
import edu.aku.hassannaqvi.drig_survey.contracts.FormsContract;
import edu.aku.hassannaqvi.drig_survey.contracts.FormsContract.FormsTable;
import edu.aku.hassannaqvi.drig_survey.contracts.HFContract;
import edu.aku.hassannaqvi.drig_survey.contracts.HFContract.HFTable;
import edu.aku.hassannaqvi.drig_survey.contracts.SchoolContract;
import edu.aku.hassannaqvi.drig_survey.contracts.SchoolContract.SchoolTable;
import edu.aku.hassannaqvi.drig_survey.contracts.UCsContract;
import edu.aku.hassannaqvi.drig_survey.contracts.UCsContract.UCsTable;
import edu.aku.hassannaqvi.drig_survey.contracts.UsersContract;
import edu.aku.hassannaqvi.drig_survey.contracts.UsersContract.UsersTable;

/**
 * Created by hassan.naqvi on 11/30/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "drig_survey.db";
    public static final String DB_NAME = DATABASE_NAME.replace(".", "_copy.");
    public static final String PROJECT_NAME = "DMU-DRIG-SURVEY";
    private static final int DATABASE_VERSION = 1;

    public static final String SQL_CREATE_USERS = "CREATE TABLE " + UsersContract.UsersTable.TABLE_NAME + "("
            + UsersTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + UsersTable.ROW_USERNAME + " TEXT,"
            + UsersTable.ROW_PASSWORD + " TEXT,"
            + UsersTable.ROW_TEAM + " TEXT"
            + " );";
    private static final String SQL_CREATE_FORMS = "CREATE TABLE "
            + FormsTable.TABLE_NAME + "("
            + FormsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + FormsTable.COLUMN_PROJECT_NAME + " TEXT,"
            + FormsTable.COLUMN_UID + " TEXT," +
            FormsTable.COLUMN_FORMDATE + " TEXT," +
            FormsTable.COLUMN_USER + " TEXT," +
            FormsTable.COLUMN_FORMTYPE + " TEXT," +
            FormsTable.COLUMN_SA + " TEXT," +
            FormsTable.COLUMN_SB + " TEXT," +
            FormsTable.COLUMN_SC + " TEXT," +
            FormsTable.COLUMN_SD + " TEXT," +
            FormsTable.COLUMN_ISTATUS + " TEXT," +
            FormsTable.COLUMN_ISTATUS88x + " TEXT," +
            FormsTable.COLUMN_GPSLAT + " TEXT," +
            FormsTable.COLUMN_GPSLNG + " TEXT," +
            FormsTable.COLUMN_GPSDATE + " TEXT," +
            FormsTable.COLUMN_GPSACC + " TEXT," +
            FormsTable.COLUMN_DEVICEID + " TEXT," +
            FormsTable.COLUMN_DEVICETAGID + " TEXT," +
            FormsTable.COLUMN_APP_VERSION + " TEXT," +
            FormsTable.COLUMN_SYNCED + " TEXT," +
            FormsTable.COLUMN_SYNCED_DATE + " TEXT"
            + " );";
    private static final String SQL_DELETE_TALUKAS = "DROP TABLE IF EXISTS " + HFTable.TABLE_NAME;
    private static final String SQL_CREATE_CHILD_FORMS = "CREATE TABLE "
            + ChildFormsTable.TABLE_NAME + "("
            + ChildContract.ChildFormsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ChildFormsTable.COLUMN_PROJECT_NAME + " TEXT,"
            + ChildFormsTable.COLUMN_UID + " TEXT," +
            ChildFormsTable.COLUMN_UUID + " TEXT," +
            ChildFormsTable.COLUMN_FORMDATE + " TEXT," +
            ChildFormsTable.COLUMN_USER + " TEXT," +
            ChildContract.ChildFormsTable.COLUMN_SB + " TEXT," +
            ChildFormsTable.COLUMN_ISTATUS + " TEXT," +
            ChildFormsTable.COLUMN_GPSLAT + " TEXT," +
            ChildContract.ChildFormsTable.COLUMN_GPSLNG + " TEXT," +
            ChildFormsTable.COLUMN_GPSDATE + " TEXT," +
            ChildFormsTable.COLUMN_GPSACC + " TEXT," +
            ChildContract.ChildFormsTable.COLUMN_DEVICEID + " TEXT," +
            ChildFormsTable.COLUMN_DEVICETAGID + " TEXT," +
            ChildContract.ChildFormsTable.COLUMN_APP_VERSION + " TEXT," +
            ChildFormsTable.COLUMN_SYNCED + " TEXT," +
            ChildContract.ChildFormsTable.COLUMN_SYNCED_DATE + " TEXT"
            + " );";
    private static final String SQL_DELETE_USERS = "DROP TABLE IF EXISTS " + UsersContract.UsersTable.TABLE_NAME;
    final String SQL_CREATE_UC = "CREATE TABLE " + UCsTable.TABLE_NAME + " (" +
            UCsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            UCsTable.COLUMN_UCCODE + " TEXT, " +
            UCsTable.COLUMN_UCS_NAME + " TEXT " +
            ");";

    private static final String SQL_DELETE_FORMS = "DROP TABLE IF EXISTS " + FormsTable.TABLE_NAME;
    private static final String SQL_DELETE_UCS = "DROP TABLE IF EXISTS " + UCsTable.TABLE_NAME;
    private static final String SQL_DELETE_SCHOOL = "DROP TABLE IF EXISTS " + SchoolTable.TABLE_NAME;
    private static final String SQL_DELETE_HF = "DROP TABLE IF EXISTS " + HFTable.TABLE_NAME;
    private static final String SQL_DELETE_CHILD_FORMS = "DROP TABLE IF EXISTS " + ChildContract.ChildFormsTable.TABLE_NAME;
    final String SQL_CREATE_HF = "CREATE TABLE " + HFTable.TABLE_NAME + " (" +
            HFTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            HFTable.COLUMN_HF_CODE + " TEXT, " +
            HFTable.COLUMN_HF_NAME + " TEXT " +
            ");";
    private final String SQL_CREATE_SCHOOL = "CREATE TABLE " + SchoolTable.TABLE_NAME + " (" +
            SchoolTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            SchoolTable.COLUMN_SCH_CODE + " TEXT," +
            SchoolTable.COLUMN_SCH_NAME + " TEXT," +
            SchoolTable.COLUMN_SCH_ADD + " TEXT," +
            SchoolTable.COLUMN_SCH_STATUS + " TEXT," +
            SchoolTable.COLUMN_SCH_TYPE + " TEXT" +
            ");";

    private final String TAG = "DatabaseHelper";
    public String spDateT = new SimpleDateFormat("dd-MM-yy").format(new Date().getTime());

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERS);
        db.execSQL(SQL_CREATE_FORMS);
        db.execSQL(SQL_CREATE_CHILD_FORMS);
        db.execSQL(SQL_CREATE_SCHOOL);
        db.execSQL(SQL_CREATE_UC);
        db.execSQL(SQL_CREATE_HF);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_USERS);
        db.execSQL(SQL_DELETE_FORMS);
        db.execSQL(SQL_DELETE_SCHOOL);
        db.execSQL(SQL_DELETE_UCS);
        db.execSQL(SQL_DELETE_HF);
        db.execSQL(SQL_DELETE_CHILD_FORMS);

    }

    public void syncHF(JSONArray Talukaslist) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(HFTable.TABLE_NAME, null, null);
        try {
            JSONArray jsonArray = Talukaslist;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectCC = jsonArray.getJSONObject(i);

                HFContract Vc = new HFContract();
                Vc.Sync(jsonObjectCC);

                ContentValues values = new ContentValues();

                values.put(HFTable.COLUMN_HF_CODE, Vc.getHfcode());
                values.put(HFTable.COLUMN_HF_NAME, Vc.getHfname());

                db.insert(HFTable.TABLE_NAME, null, values);
            }
        } catch (Exception e) {
        } finally {
            db.close();
        }
    }

    public void syncUCs(JSONArray UCslist) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(UCsTable.TABLE_NAME, null, null);
        try {
            JSONArray jsonArray = UCslist;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectCC = jsonArray.getJSONObject(i);

                UCsContract Vc = new UCsContract();
                Vc.Sync(jsonObjectCC);

                ContentValues values = new ContentValues();

                values.put(UCsTable.COLUMN_UCCODE, Vc.getUccode());
                values.put(UCsTable.COLUMN_UCS_NAME, Vc.getUcsName());

                db.insert(UCsTable.TABLE_NAME, null, values);
            }
        } catch (Exception e) {
        } finally {
            db.close();
        }
    }

    public void syncSchools(JSONArray Schoolslist) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SchoolTable.TABLE_NAME, null, null);
        try {
            JSONArray jsonArray = Schoolslist;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectCC = jsonArray.getJSONObject(i);

                SchoolContract sch = new SchoolContract();
                sch.sync(jsonObjectCC);

                ContentValues values = new ContentValues();

                values.put(SchoolTable.COLUMN_SCH_CODE, sch.getSch_code());
                values.put(SchoolTable.COLUMN_SCH_NAME, sch.getSch_name());
                values.put(SchoolTable.COLUMN_SCH_ADD, sch.getSch_add());
                values.put(SchoolTable.COLUMN_SCH_STATUS, sch.getSch_status());
                values.put(SchoolTable.COLUMN_SCH_TYPE, sch.getSch_type());

                db.insert(SchoolTable.TABLE_NAME, null, values);
            }
        } catch (Exception e) {
        } finally {
            db.close();
        }
    }

    public Collection<HFContract> getAllHF() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                HFTable.COLUMN_HF_CODE,
                HFTable.COLUMN_HF_NAME
        };

        String whereClause = null;
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;

        String orderBy = HFTable.COLUMN_HF_NAME + " ASC";

        Collection<HFContract> allDC = new ArrayList<>();
        try {
            c = db.query(
                    HFTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                allDC.add(new HFContract().HydrateHF(c));
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allDC;
    }

    public ArrayList<UCsContract> getAllUCs() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                UCsTable._ID,
                UCsTable.COLUMN_UCCODE,
                UCsTable.COLUMN_UCS_NAME,
        };

        String whereClause = null;
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;

        String orderBy = UCsTable.COLUMN_UCS_NAME + " ASC";

        ArrayList<UCsContract> allPC = new ArrayList<>();
        try {
            c = db.query(
                    UCsTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                allPC.add(new UCsContract().hydrate(c));
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allPC;
    }

    public void syncUser(JSONArray userlist) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(UsersTable.TABLE_NAME, null, null);
        try {
            JSONArray jsonArray = userlist;
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObjectUser = jsonArray.getJSONObject(i);

                UsersContract user = new UsersContract();
                user.Sync(jsonObjectUser);
                ContentValues values = new ContentValues();

                values.put(UsersContract.UsersTable.ROW_USERNAME, user.getUserName());
                values.put(UsersTable.ROW_PASSWORD, user.getPassword());
                values.put(UsersTable.ROW_TEAM, user.getROW_TEAM());
                db.insert(UsersTable.TABLE_NAME, null, values);
            }


        } catch (Exception e) {
            Log.d(TAG, "syncUser(e): " + e);
        } finally {
            db.close();
        }
    }

    public boolean Login(String username, String password) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {

//      New value for one column
            String[] columns = {
                    UsersTable._ID,
                    UsersTable.ROW_USERNAME,
                    UsersTable.ROW_TEAM
            };

// Which row to update, based on the ID
            String selection = UsersContract.UsersTable.ROW_USERNAME + " = ?" + " AND " + UsersContract.UsersTable.ROW_PASSWORD + " = ?";
            String[] selectionArgs = {username, password};
            cursor = db.query(UsersContract.UsersTable.TABLE_NAME, //Table to query
                    columns,                    //columns to return
                    selection,                  //columns for the WHERE clause
                    selectionArgs,              //The values for the WHERE clause
                    null,                       //group the rows
                    null,                       //filter by row groups
                    null);                      //The sort order

            if (cursor.getCount() > 0) {

                if (cursor.moveToFirst()) {
                    MainApp.teamNo = cursor.getString(cursor.getColumnIndex(UsersTable.ROW_TEAM));
                }
                return true;
            }
        } catch (Exception e) {

        } finally {
            cursor.close();
            db.close();
        }
        return false;
    }

    public Long addForm(FormsContract fc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FormsTable.COLUMN_PROJECT_NAME, fc.getProjectName());
        values.put(FormsTable.COLUMN_UID, fc.getUID());
        values.put(FormsTable.COLUMN_FORMDATE, fc.getFormDate());
        values.put(FormsTable.COLUMN_USER, fc.getUser());
        values.put(FormsTable.COLUMN_FORMTYPE, fc.getFormtype());
        values.put(FormsTable.COLUMN_ISTATUS, fc.getIstatus());
        values.put(FormsTable.COLUMN_ISTATUS88x, fc.getIstatus88x());
        values.put(FormsTable.COLUMN_SA, fc.getsA());
        values.put(FormsTable.COLUMN_SB, fc.getsB());
        values.put(FormsTable.COLUMN_SC, fc.getsC());
        values.put(FormsTable.COLUMN_SD, fc.getsD());
        values.put(FormsTable.COLUMN_GPSLAT, fc.getGpsLat());
        values.put(FormsTable.COLUMN_GPSLNG, fc.getGpsLng());
        values.put(FormsTable.COLUMN_GPSDATE, fc.getGpsDT());
        values.put(FormsTable.COLUMN_GPSACC, fc.getGpsAcc());
        values.put(FormsTable.COLUMN_DEVICETAGID, fc.getDevicetagID());
        values.put(FormsTable.COLUMN_DEVICEID, fc.getDeviceID());
        values.put(FormsTable.COLUMN_SYNCED, fc.getSynced());
        values.put(FormsTable.COLUMN_SYNCED_DATE, fc.getSynced_date());
        values.put(FormsTable.COLUMN_APP_VERSION, fc.getAppversion());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                FormsTable.TABLE_NAME,
                FormsTable.COLUMN_NAME_NULLABLE,
                values);
        return newRowId;
    }

    public Long addChildForm(ChildContract fc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ChildContract.ChildFormsTable.COLUMN_PROJECT_NAME, fc.getProjectName());
        values.put(ChildContract.ChildFormsTable.COLUMN_UID, fc.get_UID());
        values.put(ChildFormsTable.COLUMN_UID, fc.get_UID());
        values.put(ChildFormsTable.COLUMN_FORMDATE, fc.getFormDate());
        values.put(ChildFormsTable.COLUMN_USER, fc.getUser());
        values.put(ChildFormsTable.COLUMN_ISTATUS, fc.getIstatus());
//        values.put(ChildFormsTable.COLUMN_ISTATUS88x, fc.getIstatus88x());
        values.put(ChildFormsTable.COLUMN_SA, fc.getsA());
        values.put(ChildFormsTable.COLUMN_SB, fc.getsB());
        values.put(ChildFormsTable.COLUMN_GPSLAT, fc.getGpsLat());
        values.put(ChildFormsTable.COLUMN_GPSLNG, fc.getGpsLng());
        values.put(ChildContract.ChildFormsTable.COLUMN_GPSDATE, fc.getGpsDT());
        values.put(ChildContract.ChildFormsTable.COLUMN_GPSACC, fc.getGpsAcc());
        values.put(ChildFormsTable.COLUMN_DEVICETAGID, fc.getDevicetagID());
        values.put(ChildFormsTable.COLUMN_DEVICEID, fc.getDeviceID());
        values.put(ChildContract.ChildFormsTable.COLUMN_SYNCED, fc.getSynced());
        values.put(ChildFormsTable.COLUMN_SYNCED_DATE, fc.getSynced_date());
        values.put(ChildFormsTable.COLUMN_APP_VERSION, fc.getAppversion());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                ChildContract.ChildFormsTable.TABLE_NAME,
                ChildContract.ChildFormsTable.COLUMN_NAME_NULLABLE,
                values);
        return newRowId;
    }

    public void updateSyncedForms(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(FormsTable.COLUMN_SYNCED, true);
        values.put(FormsTable.COLUMN_SYNCED_DATE, new Date().toString());

// Which row to update, based on the title
        String where = FormsTable._ID + " = ?";
        String[] whereArgs = {id};

        int count = db.update(
                FormsTable.TABLE_NAME,
                values,
                where,
                whereArgs);
    }

    public void updateSyncedChildForms(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(ChildFormsTable.COLUMN_SYNCED, true);
        values.put(ChildFormsTable.COLUMN_SYNCED_DATE, new Date().toString());

// Which row to update, based on the title
        String where = ChildContract.ChildFormsTable._ID + " = ?";
        String[] whereArgs = {id};

        int count = db.update(
                ChildContract.ChildFormsTable.TABLE_NAME,
                values,
                where,
                whereArgs);
    }

    public int updateFormID() {
        SQLiteDatabase db = this.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(FormsTable.COLUMN_UID, MainApp.fc.getUID());

        // Which row to update, based on the ID
        String selection = FormsTable._ID + " = ?";
        String[] selectionArgs = {String.valueOf(MainApp.fc.get_ID())};

        int count = db.update(FormsTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return count;
    }

    public int updateChildFormID() {
        SQLiteDatabase db = this.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(ChildFormsTable.COLUMN_UID, MainApp.cc.get_UID());

        // Which row to update, based on the ID
        String selection = ChildFormsTable._ID + " = ?";
        String[] selectionArgs = {String.valueOf(MainApp.cc.get_ID())};

        int count = db.update(ChildFormsTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return count;
    }

    public Collection<FormsContract> getUnsyncedForms(String type) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                FormsTable._ID,
                FormsTable.COLUMN_UID,
                FormsTable.COLUMN_FORMDATE,
                FormsTable.COLUMN_USER,
                FormsTable.COLUMN_ISTATUS,
                FormsTable.COLUMN_FORMTYPE,
                FormsTable.COLUMN_SA,
                FormsTable.COLUMN_SB,
                FormsTable.COLUMN_SC,
                FormsTable.COLUMN_SD,
                FormsTable.COLUMN_GPSLAT,
                FormsTable.COLUMN_GPSLNG,
                FormsTable.COLUMN_GPSDATE,
                FormsTable.COLUMN_GPSACC,
                FormsTable.COLUMN_DEVICETAGID,
                FormsTable.COLUMN_DEVICEID,
                FormsTable.COLUMN_SYNCED,
                FormsTable.COLUMN_SYNCED_DATE,
                FormsTable.COLUMN_APP_VERSION
        };
        String whereClause = FormsTable.COLUMN_SYNCED + " is null OR " + FormsTable.COLUMN_SYNCED + " = '' ";
        String[] whereArgs = null;

        if (type != null) {
            whereClause += " AND " + FormsTable.COLUMN_FORMTYPE + " =?";
            whereArgs = new String[]{type};
        }

        String groupBy = null;
        String having = null;

        String orderBy = FormsTable._ID + " ASC";

        Collection<FormsContract> allFC = new ArrayList<>();
        try {
            c = db.query(
                    FormsTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                FormsContract fc = new FormsContract();
                allFC.add(fc.Hydrate(c));
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allFC;
    }

    public Collection<ChildContract> getUnsyncedChildForms() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                ChildContract.ChildFormsTable._ID,
                ChildFormsTable.COLUMN_UID,
                ChildContract.ChildFormsTable.COLUMN_UUID,
                ChildContract.ChildFormsTable.COLUMN_FORMDATE,
                ChildContract.ChildFormsTable.COLUMN_USER,
                ChildFormsTable.COLUMN_ISTATUS,
                ChildFormsTable.COLUMN_SA,
                ChildContract.ChildFormsTable.COLUMN_SB,
                ChildContract.ChildFormsTable.COLUMN_GPSLAT,
                ChildFormsTable.COLUMN_GPSLNG,
                ChildContract.ChildFormsTable.COLUMN_GPSDATE,
                ChildContract.ChildFormsTable.COLUMN_GPSACC,
                ChildFormsTable.COLUMN_DEVICETAGID,
                ChildFormsTable.COLUMN_DEVICEID,
                ChildFormsTable.COLUMN_SYNCED,
                ChildFormsTable.COLUMN_SYNCED_DATE,
                ChildFormsTable.COLUMN_APP_VERSION
        };
        String whereClause = ChildFormsTable.COLUMN_SYNCED + " is null OR " + ChildFormsTable.COLUMN_SYNCED + " = '' ";
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;

        String orderBy =
                ChildFormsTable._ID + " ASC";

        Collection<ChildContract> allFC = new ArrayList<ChildContract>();
        try {
            c = db.query(
                    ChildContract.ChildFormsTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                ChildContract fc = new ChildContract();
                allFC.add(fc.Hydrate(c));
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allFC;
    }

    public Collection<FormsContract> getTodayForms() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                FormsTable._ID,
                FormsTable.COLUMN_FORMDATE,
                FormsTable.COLUMN_ISTATUS,
                FormsTable.COLUMN_SYNCED,

        };
        String whereClause = FormsTable.COLUMN_FORMDATE + " Like ? ";
        String[] whereArgs = new String[]{"%" + spDateT.substring(0, 8).trim() + "%"};
        String groupBy = null;
        String having = null;

        String orderBy =
                FormsTable._ID + " ASC";

        Collection<FormsContract> allFC = new ArrayList<>();
        try {
            c = db.query(
                    FormsTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                FormsContract fc = new FormsContract();
                fc.set_ID(c.getString(c.getColumnIndex(FormsTable._ID)));
                fc.setFormDate(c.getString(c.getColumnIndex(FormsTable.COLUMN_FORMDATE)));
                fc.setIstatus(c.getString(c.getColumnIndex(FormsTable.COLUMN_ISTATUS)));
                fc.setSynced(c.getString(c.getColumnIndex(FormsTable.COLUMN_SYNCED)));
                allFC.add(fc);
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allFC;
    }

    // ANDROID DATABASE MANAGER
    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[]{"message"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (SQLException sqlEx) {
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        } catch (Exception ex) {

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        }
    }

    public int updateCSA() {
        SQLiteDatabase db = this.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(ChildFormsTable.COLUMN_SA, MainApp.cc.getsA());

        // Which row to update, based on the ID
        String selection = ChildFormsTable._ID + " = ?";
        String[] selectionArgs = {String.valueOf(MainApp.cc.get_ID())};

        int count = db.update(ChildFormsTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return count;
    }

    public int updateCSB() {
        SQLiteDatabase db = this.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(ChildFormsTable.COLUMN_SB, MainApp.cc.getsB());

        // Which row to update, based on the ID
        String selection = ChildFormsTable._ID + " = ?";
        String[] selectionArgs = {String.valueOf(MainApp.cc.get_ID())};

        int count = db.update(ChildFormsTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return count;
    }

    public int updateEnding() {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(FormsTable.COLUMN_ISTATUS, MainApp.fc.getIstatus());
        values.put(FormsTable.COLUMN_ISTATUS88x, MainApp.fc.getIstatus88x());

// Which row to update, based on the ID
        String selection = FormsTable._ID + " =? ";
        String[] selectionArgs = {String.valueOf(MainApp.fc.get_ID())};

        int count = db.update(FormsTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return count;
    }

    public int updateChildEnding() {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(ChildFormsTable.COLUMN_ISTATUS, MainApp.cc.getIstatus());
//        values.put(ChildFormsTable.COLUMN_ISTATUS88x, MainApp.cc.getIstatus88x());

// Which row to update, based on the ID
        String selection = ChildFormsTable._ID + " =? ";
        String[] selectionArgs = {String.valueOf(MainApp.cc.get_ID())};

        int count = db.update(ChildFormsTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return count;
    }

}