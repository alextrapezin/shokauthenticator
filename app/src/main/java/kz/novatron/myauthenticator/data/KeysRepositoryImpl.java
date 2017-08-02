package kz.novatron.myauthenticator.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SMustafa on 28.06.2017.
 */

public class KeysRepositoryImpl extends SQLiteOpenHelper implements KeysRepository{
    private final String LOG_TAG = this.getClass().getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AUTHENTICATOR";
    private static final String CREATE_USER_TABLE = "CREATE TABLE " + DBContract.AuthKey.TABLE_NAME + " (" +
            DBContract.AuthKey._ID + " INTEGER PRIMARY KEY, " +
            DBContract.AuthKey.COLUMN_NAME_SECRET_KEY + " TEXT," +
            DBContract.AuthKey.COLUMN_NAME_EMAIL + " TEXT," +
            DBContract.AuthKey.COLUMN_NAME_CREATE_DATE + " TEXT);";
    private static KeysRepositoryImpl sInstance;

    public static synchronized KeysRepositoryImpl getInstance(Context context){
        if(sInstance==null){
            sInstance = new KeysRepositoryImpl(context.getApplicationContext());
        }
        return sInstance;
    }
    private KeysRepositoryImpl(Context context){

        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(LOG_TAG, CREATE_USER_TABLE);
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }

    @Override
    public List<AuthKey> getAllKeys() {
        List<AuthKey> authKeys = new ArrayList<>();
        SQLiteDatabase readableDatabase = this.getReadableDatabase();
        Cursor result = readableDatabase.rawQuery("select * from " + DBContract.AuthKey.TABLE_NAME, null);
        try {
            while (result.moveToNext()) {
                AuthKey authKey = new AuthKey();
                authKey.setSecretKey(result.getString((result.getColumnIndex(DBContract.AuthKey.COLUMN_NAME_SECRET_KEY))));
                authKey.setEmail(result.getString((result.getColumnIndex(DBContract.AuthKey.COLUMN_NAME_EMAIL))));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strDate = result.getString((result.getColumnIndex(DBContract.AuthKey.COLUMN_NAME_CREATE_DATE)));
                authKey.setCreateDate(sdf.parse(strDate));
                authKeys.add(authKey);
            }
            result.close();
            readableDatabase.close();
        }catch (ParseException pe){
            Log.e(LOG_TAG, pe.getMessage());
        }
        return authKeys;
    }

    @Override
    public void saveKey(AuthKey authKey) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SQLiteDatabase writableDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBContract.AuthKey.COLUMN_NAME_SECRET_KEY, authKey.getSecretKey());
        cv.put(DBContract.AuthKey.COLUMN_NAME_EMAIL, authKey.getEmail());
        cv.put(DBContract.AuthKey.COLUMN_NAME_CREATE_DATE, sdf.format(authKey.getCreateDate()));
        long i = writableDatabase.insert(DBContract.AuthKey.TABLE_NAME,null,cv);
        writableDatabase.close();
    }

    @Override
    public void deleteKey(AuthKey authKey) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBContract.AuthKey.TABLE_NAME, DBContract.AuthKey.COLUMN_NAME_EMAIL + "='" + authKey.getEmail() + "'", null);
        db.close();
    }
}
