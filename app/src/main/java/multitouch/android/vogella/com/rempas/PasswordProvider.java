package multitouch.android.vogella.com.rempas;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

@SuppressWarnings("ConstantConditions")
public class PasswordProvider extends ContentProvider {

    //specify the query string in the form of a URI
    static final String PROVIDER_NAME = "multitouch.android.vogella.com.rempas.PasswordProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/passwords";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String _ID = "_id";
    static final String NAME = "name";
    static final String PASSWORD = "password";
    static final String NOTE = "note";

    private static HashMap<String, String> PASSWORDS_PROJECTION_MAP;

    static final int PASSWORDS = 1;
    static final int PASSWORD_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "passwords", PASSWORDS);
        uriMatcher.addURI(PROVIDER_NAME, "passwords/#", PASSWORD_ID);
    }

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "db_pass";
    static final String PASSWORDS_TABLE_NAME = "passwords";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + PASSWORDS_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " name TEXT NOT NULL, " +
                    " password TEXT NOT NULL, " +
                    " note TEXT NOT NULL);";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  PASSWORDS_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return db != null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long rowID = db.insert(	PASSWORDS_TABLE_NAME, "", values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(PASSWORDS_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case PASSWORDS:
                qb.setProjectionMap(PASSWORDS_PROJECTION_MAP);
                break;
            case PASSWORD_ID:
                qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder.equals("")) {
            sortOrder = NAME;
        }
        Cursor c = qb.query(db,	projection,	selection, selectionArgs,
                null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case PASSWORDS:
                count = db.delete(PASSWORDS_TABLE_NAME, selection, selectionArgs);
                break;
            case PASSWORD_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete( PASSWORDS_TABLE_NAME, _ID +  " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case PASSWORDS:
                count = db.update(PASSWORDS_TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case PASSWORD_ID:
                count = db.update(PASSWORDS_TABLE_NAME, values, _ID +
                        " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case PASSWORDS:
                return "vnd.android.cursor.dir/vnd.example.passwords";
            case PASSWORD_ID:
                return "vnd.android.cursor.item/vnd.example.passwords";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}
