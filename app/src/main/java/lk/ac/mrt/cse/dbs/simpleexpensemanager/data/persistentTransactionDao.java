package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class persistentTransactionDao extends SQLiteOpenHelper implements TransactionDAO {

    public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_ACCOUNTNO = "ACCNO";
    public static final String COLUMN_EXPTYPE = "COLUMN_EXPTYPE";
    public static final String COLUMN_AMOUNT = "COLUMN_AMOUNT";

    public persistentTransactionDao(@Nullable Context context) {
        super(context, "customer.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        String createTableStatement = "CREATE TABLE " + CUSTOMER_TABLE + " " + COLUMN_ACCOUNTNO + "  PRIMARY KEY  , " + COLUMN_DATE + "  TEXT , " + COLUMN_EXPTYPE + " TEXT , " + COLUMN_AMOUNT + " INT)";
        database.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        DateFormat tempDate = new SimpleDateFormat("dd-MM-yyyy");
        cv.put(COLUMN_DATE, tempDate.format(date));
        cv.put(COLUMN_ACCOUNTNO, accountNo);
        cv.put(COLUMN_EXPTYPE, String.valueOf(expenseType));
        cv.put(COLUMN_AMOUNT, amount);

        database.insert(CUSTOMER_TABLE, null, cv);
        database.close();

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {


        List<Transaction> myList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String[] temp = {
                COLUMN_DATE,
                COLUMN_ACCOUNTNO,
                COLUMN_EXPTYPE,
                COLUMN_AMOUNT
        };


        Cursor cursor = database.query(
                CUSTOMER_TABLE,
                temp,
                null, null, null, null, null


        );

        if (cursor.moveToFirst()) {
            do {

                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                Date tempDate = new SimpleDateFormat("dd-MM-yyyy").parse(date);


                String acc = cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNTNO));
                ExpenseType exp = ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_EXPTYPE)));
                double amouunt = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
                Transaction t = new Transaction(tempDate, acc, exp, amouunt);
                myList.add(t);
                database.close();

            }
            while (cursor.moveToNext());
        }


        return null;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> myList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String[] temp = {
                COLUMN_DATE,
                COLUMN_ACCOUNTNO,
                COLUMN_EXPTYPE,
                COLUMN_AMOUNT
        };


        Cursor cursor = database.query(
                CUSTOMER_TABLE,
                temp,
                null, null, null, null, null


        );

        int length = cursor.getCount();


        if (cursor.moveToFirst()) {
            do {

                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                Date tempDate = new SimpleDateFormat("dd-MM-yyyy").parse(date);


                String acc = cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNTNO));
                ExpenseType exp = ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_EXPTYPE)));
                double amouunt = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
                Transaction t = new Transaction(tempDate, acc, exp, amouunt);
                myList.add(t);


            }
            while (cursor.moveToNext());

            if (length <= limit) {
                return myList;

            }



        }
        return myList.subList(length - limit, length);

    }

}





