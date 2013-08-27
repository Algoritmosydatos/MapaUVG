package com.uvg.mapa.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class MapDB{

    private static final String DATABASE_NAME = "MapDB";
    
    /**Alarm table creation*/
    private static final String DATABASE_TABLE_BUILDINGS = "buildings";

	public static final String KEY_ROWID = "_id";
	public static final String KEY_LR_LATITUDE = "lr_latitude";
	public static final String KEY_LR_LONGITUDE = "lr_longitude";
	public static final String KEY_UL_LATITUDE = "ul_latitude";
	public static final String KEY_UL_LONGITUDE = "ul_longitude";
	public static final String KEY_NAME = "name";

    private static final String[] buildingComposition = new String[] {KEY_ROWID, KEY_LR_LATITUDE,
    	KEY_LR_LONGITUDE, KEY_UL_LATITUDE, KEY_UL_LONGITUDE, KEY_NAME};

    private static final String DATABASE_BUILDINGS_CREATE =
    		"CREATE TABLE "+ DATABASE_TABLE_BUILDINGS + " ("+
			KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_LR_LATITUDE + " INTEGER NOT NULL, " +
			KEY_LR_LONGITUDE + " INTEGER NOT NULL, " +
			KEY_UL_LATITUDE + " INTEGER NOT NULL, " +
			KEY_UL_LONGITUDE + " INTEGER NOT NULL, " +
			KEY_NAME + " TEXT);"	;
    
    /**Nap table creation*/
    private static final String DATABASE_TABLE_ROOMS = "rooms";  
	
	public static final String KEY_BUILDING_ID = "building_id";
	public static final String KEY_CODE = "code";
	public static final String KEY_PURPOSE = "purpose";
    
    private static final String DATABASE_ROOMS_CREATE =
    		"CREATE TABLE "+ DATABASE_TABLE_ROOMS + " ("+
			KEY_ROWID + " INTEGER PRIMARY KEY, " +
			KEY_BUILDING_ID + " INTEGER PRIMARY KEY, " +
			KEY_CODE + " TEXT, " +
			KEY_PURPOSE + " TEXT);";
    
    private static final String INITIALIZING_ROOMS = 
    		"INSERT INTO "+ DATABASE_TABLE_ROOMS+ " ("+
    		KEY_ROWID+","+KEY_BUILDING_ID+","+KEY_CODE+","+KEY_PURPOSE+") "+
    		"VALUES ('0','0','0','0');";
    
    public static final String[] roomsComposition = new String[] {KEY_ROWID,
        KEY_BUILDING_ID, KEY_CODE, KEY_PURPOSE};
    
    private static final String DATABASE_TABLE_CATEDRATICOS = "catedraticos";  
    
	public static final String KEY_ROOM_ID = "room_id";
	public static final String KEY_DEPARTMENT = "department";
	
    private static final String DATABASE_CATEDRATICOS_CREATE =
    		"CREATE TABLE "+ DATABASE_TABLE_CATEDRATICOS + " ("+
			KEY_ROWID + " INTEGER PRIMARY KEY, " +
			KEY_ROOM_ID + " INTEGER NOT NULL, " +
			KEY_NAME + " TEXT, " +
			KEY_DEPARTMENT + " TEXT);";
    
    private static final String INITIALIZING_CATEDRATICOS = 
    		"INSERT INTO "+ DATABASE_TABLE_CATEDRATICOS+ " ("+
    		KEY_ROWID+","+KEY_ROOM_ID+","+KEY_NAME+","+KEY_DEPARTMENT+") "+
    		"VALUES ('0','0','','0','0','0');";
    
    public static final String[] catedraticosComposition = new String[] {KEY_ROWID,
    	KEY_ROOM_ID, KEY_NAME, KEY_DEPARTMENT};
    
    private static final String TAG = "NotesDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_BUILDINGS_CREATE);
            db.execSQL(DATABASE_ROOMS_CREATE);
            db.execSQL(DATABASE_CATEDRATICOS_CREATE);
            db.execSQL(INITIALIZING_ROOMS);
            db.execSQL(INITIALIZING_CATEDRATICOS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the cont	ext to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public MapDB(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public MapDB open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @param title the title of the note
     * @param body the body of the note
     * @return rowId or -1 if failed
     */
    public long createAlarm(Alarm alarm) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_STATE, Utilities.booleanToInt(alarm.isState()));
        initialValues.put(KEY_HOUR, alarm.getHour());
        initialValues.put(KEY_MINUTE, alarm.getMinute());
        initialValues.put(KEY_MONDAY, Utilities.booleanToInt(alarm.getWeekdays().isMonday()));
        initialValues.put(KEY_TUESDAY, Utilities.booleanToInt(alarm.getWeekdays().isThursday()));
        initialValues.put(KEY_WEDNESDAY, Utilities.booleanToInt(alarm.getWeekdays().isWednesday()));
        initialValues.put(KEY_THURSDAY, Utilities.booleanToInt(alarm.getWeekdays().isThursday()));
        initialValues.put(KEY_FRIDAY, Utilities.booleanToInt(alarm.getWeekdays().isFriday()));
        initialValues.put(KEY_SATURDAY, Utilities.booleanToInt(alarm.getWeekdays().isSaturday()));
        initialValues.put(KEY_SUNDAY, Utilities.booleanToInt(alarm.getWeekdays().isSunday()));

        return mDb.insert(DATABASE_TABLE_ALARMS, null, initialValues);
    }

    /**
     * Delete the note with the given rowId
     * 
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteAlarm(long rowId) {

        return mDb.delete(DATABASE_TABLE_ALARMS, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public boolean deleteNap() {
        ContentValues args = new ContentValues();
        args.put(KEY_MINUTE, 0);
        args.put(KEY_STATE, 0);
        args.put(KEY_MILLISECONDS, 0);

        return mDb.update(DATABASE_TABLE_NAP, args, KEY_ROWID + "=" + 0, null) > 0;
    }
    
    public boolean deleteSettings() {
        ContentValues args = new ContentValues();
        args.put(KEY_LOOPING, 0);
        args.put(KEY_SONGID, "");
        args.put(KEY_INTENSITY, 0);
        args.put(KEY_FADEIN, 0);
        args.put(KEY_VIBRATION, 0);

        return mDb.update(DATABASE_TABLE_SETTINGS, args, KEY_ROWID + "=" + 0, null) > 0;
    }


    /**
     * Return a Cursor over the list of all notes in the database
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchAllAlarms() {
        return mDb.query(DATABASE_TABLE_ALARMS, alarmComposition, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchAlarm(long rowId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE_ALARMS, alarmComposition , KEY_ROWID + "=" + rowId, 
            		null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    public Cursor fetchNap() throws SQLException {
        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE_NAP, napComposition, KEY_ROWID + "=" + 0, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor fetchSettings() throws SQLException {
        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE_SETTINGS, settingsComposition,
            		KEY_ROWID + "=" + 0, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        
        return mCursor;
    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     * 
     * @param rowId id of note to update
     * @param title value to set note title to
     * @param body value to set note body to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateAlarm(Alarm alarm) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_STATE, Utilities.booleanToInt(alarm.isState()));
        initialValues.put(KEY_HOUR, alarm.getHour());
        initialValues.put(KEY_MINUTE, alarm.getMinute());
        initialValues.put(KEY_MONDAY, Utilities.booleanToInt(alarm.getWeekdays().isMonday()));
        initialValues.put(KEY_TUESDAY, Utilities.booleanToInt(alarm.getWeekdays().isThursday()));
        initialValues.put(KEY_WEDNESDAY, Utilities.booleanToInt(alarm.getWeekdays().isWednesday()));
        initialValues.put(KEY_THURSDAY, Utilities.booleanToInt(alarm.getWeekdays().isThursday()));
        initialValues.put(KEY_FRIDAY, Utilities.booleanToInt(alarm.getWeekdays().isFriday()));
        initialValues.put(KEY_SATURDAY, Utilities.booleanToInt(alarm.getWeekdays().isSaturday()));
        initialValues.put(KEY_SUNDAY, Utilities.booleanToInt(alarm.getWeekdays().isSunday()));

        return mDb.update(DATABASE_TABLE_ALARMS, initialValues, KEY_ROWID + "=" + alarm.getAlarmIndex(), null) > 0;
    }
    
    public boolean updateNap(Nap nap) {        
    	ContentValues args = new ContentValues();
	    args.put(KEY_STATE, Utilities.booleanToInt(nap.isState()));
	    args.put(KEY_MINUTE, nap.getMinute());
	    args.put(KEY_MILLISECONDS, nap.getCalendar().getTimeInMillis());
	
	    return mDb.update(DATABASE_TABLE_NAP, args, KEY_ROWID + "=" + 0, null) > 0;
    }
    
    public boolean updateSettings(Settings settings) { 
        ContentValues args = new ContentValues();
        args.put(KEY_LOOPING, Utilities.booleanToInt(settings.isLooping()));
        args.put(KEY_SONGID, settings.getSongId());
        args.put(KEY_INTENSITY, settings.getIntensity());
        args.put(KEY_FADEIN, Utilities.booleanToInt(settings.hasFadeIn()));
        args.put(KEY_VIBRATION, Utilities.booleanToInt(settings.hasVibration()));

        return mDb.update(DATABASE_TABLE_SETTINGS, args, KEY_ROWID + "=" + 0, null) > 0;
    }
}