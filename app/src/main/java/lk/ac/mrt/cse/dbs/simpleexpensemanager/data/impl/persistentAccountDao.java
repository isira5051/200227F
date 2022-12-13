package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class persistentAccountDao extends SQLiteOpenHelper implements AccountDAO {

    public static final String ACCOUNT_TABLE = "CUSTOMER_TABLE";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_EXPTYPE = "COLUMN_EXPTYPE";
    public static final String COLUMN_AMOUNT = "COLUMN_AMOUNT";
    public static final String COLUMN_ACCOUNTNO = "(COLUMN_ACCOUNTNO";
    public static final String COLUMN_BANK_NAME = "COLUMN_BANK_NAME";
    public static final String COLUMN_HOLDER_NAME = "COLUMN_HOLDER_NAME";
    public static final String COLUMN_BALANCE = "COLUMN_BALANCE";
    public static final String COLOUMN_NAME = "COLOUMN_NAME";

    public persistentAccountDao(@Nullable Context context)
    {
        super(context , "customer.db" , null , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        String createTableStatement = "CREATE TABLE " + ACCOUNT_TABLE + " " + COLUMN_ACCOUNTNO + "  PRIMARY KEY  , " + COLUMN_BANK_NAME + " TEXT , " + COLUMN_HOLDER_NAME + " TEXT , " + " TEXT , " + COLUMN_BALANCE + ")";
        database.execSQL(createTableStatement);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    @Override
    public List<String> getAccountNumbersList()
    {
        SQLiteDatabase database = getReadableDatabase();
        String[] temp = {
                COLUMN_ACCOUNTNO
        };

        Cursor cursor = database.query(
                ACCOUNT_TABLE,
                temp,
                null,null, null , null , null


        );
        List<String> accountNumbers = new ArrayList<String>();

        while(cursor.moveToNext()) {
            String accountNumber = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_ACCOUNTNO));
            accountNumbers.add(accountNumber);
        }
        cursor.close();
        return accountNumbers;

    }

    @Override
    public List<Account> getAccountsList() {

        List<Account> myList = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();

        String[] temp = {
                COLUMN_ACCOUNTNO,
                COLUMN_BANK_NAME,
                COLUMN_HOLDER_NAME,
                COLUMN_BALANCE

        };

        Cursor cursor = database.query(
                ACCOUNT_TABLE,
                temp,
                null,null, null , null , null


        );

        if (cursor.moveToFirst())
        {
            do {



                String acc = cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNTNO));
                String bankName = cursor.getString(cursor.getColumnIndex(COLUMN_BANK_NAME));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_HOLDER_NAME));
                double balance = cursor.getDouble(cursor.getColumnIndex(COLUMN_BALANCE));

                Account t  = new Account(acc , bankName , name , balance);
                myList.add(t);
                database.close();

            }
            while(cursor.moveToNext());
        }

        cursor.close();


        return  myList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException
    {
        SQLiteDatabase database = getReadableDatabase();
        String[] temp = {
                COLUMN_ACCOUNTNO,
                COLUMN_BANK_NAME,
                COLUMN_HOLDER_NAME,
                COLUMN_BALANCE

        };

        Cursor cursor = database.query(ACCOUNT_TABLE , new String[]{COLUMN_ACCOUNTNO , COLUMN_BANK_NAME ,COLUMN_HOLDER_NAME ,COLUMN_BALANCE }  , COLUMN_ACCOUNTNO + "=?",
                new String[]{COLUMN_ACCOUNTNO} ,null , null , null , null);

        if(cursor != null)
        {
            cursor.moveToFirst();
        }

        Account account = new Account(accountNo, cursor.getString(cursor.getColumnIndex(COLUMN_BANK_NAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_HOLDER_NAME)), cursor.getDouble(cursor.getColumnIndex(COLUMN_BALANCE)));
        return account;
    }

    @Override
    public void addAccount(Account account) {

        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ACCOUNTNO, account.getAccountNo());
        cv.put(COLUMN_BANK_NAME, account.getBankName());
        cv.put(COLOUMN_NAME, account.getAccountHolderName());
        cv.put(COLUMN_BALANCE,account.getBalance());
        database.insert(ACCOUNT_TABLE , null  , cv);
        database.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException
    {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(ACCOUNT_TABLE , COLUMN_ACCOUNTNO + " = ?" , new String[]{COLUMN_ACCOUNTNO});
        database.close();

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException
        {


            SQLiteDatabase database = getWritableDatabase();
            String[] temp=
                    {
                            COLUMN_BALANCE

                    };

            String a = COLUMN_ACCOUNTNO + " = ?";
            String[] b = { accountNo };

            Cursor cursor = database.query(
                    ACCOUNT_TABLE,   // The table to query
                    temp,             // The array of columns to return (pass null to get all)
                    a,              // The columns for the WHERE clause
                    b,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
            );


            double balance;


        }



}