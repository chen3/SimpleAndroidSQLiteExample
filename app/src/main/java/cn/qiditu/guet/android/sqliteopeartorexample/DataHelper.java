package cn.qiditu.guet.android.sqliteopeartorexample;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DataHelper extends SQLiteOpenHelper {

    private static final String dbName = "storage.db3";
    private static final int version = 1;

    /**
     * Create a helper object to create, open, and/or manage a database.
     * The database is not actually created or opened until one of
     * {@link #getWritableDatabase} or {@link #getReadableDatabase} is called.
     * <p>
     * <p>Accepts input param: a concrete instance of {@link DatabaseErrorHandler} to be
     * used to handle corruption when sqlite reports database corruption.</p>
     *
     * @param context      to use to open or create the database
     * @param errorHandler the {@link DatabaseErrorHandler} to be used when sqlite reports database
     */
    @SuppressWarnings("unused")
    DataHelper(Context context, DatabaseErrorHandler errorHandler) {
        super(context, dbName, null, version, errorHandler);
    }

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     */
    DataHelper(Context context) {
        super(context, name, null, version);
    }

    static final String tableName = "Interests";
    static final String id = "ID";
    static final String name = "Name";
    static final String interest = "Interest";

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE [" + tableName + "](\n" +
                "    [" + id + "] INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                "    [" + name + "] NTEXT NOT NULL, \n" +
                "    [" + interest + "] NTEXT NOT NULL)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
