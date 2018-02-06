package com.example.filipe.qualquercoisadeextraordinario;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Filipe on 12/05/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "users.db";
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PASSWORD = "pass";
    private static final String COLUMN_POOLNAME = "pool";
    SQLiteDatabase db;
    private static final String TABLE_CREATE = "create table users (id integer primary key not null , "+
            "name text not null , pass text not null , pool text not null)";

    public DatabaseHelper(Context context)
    {
        super(context , DATABASE_NAME , null , DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) // Cria a base de dados
    {
        db.execSQL(TABLE_CREATE);
        this.db = db;
    }


    //Inserir a tabela com o utilizador -> Nome, password e nome da piscina
    public void insertUser(Contact u)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from "+TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_ID , count);
        values.put(COLUMN_NAME , u.getName());
        values.put(COLUMN_PASSWORD , u.getPassword());
        values.put(COLUMN_POOLNAME , u.getPoolName());


        System.out.println(values);

        db.insert(TABLE_NAME , null , values);
        System.out.println(db);
        db.close();

    }

    public void deleteUser(String username)
    {
        db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + "= '" + username + "'");
        db.close();
    }

    public String searchPass(String Uname)
    {
        db = this.getReadableDatabase();
        String query = "select * from "+TABLE_NAME;
        Cursor cursor = db.rawQuery(query , null);


        //Procurar o username e a password na base de dados
        String UserNameAux, PasswordAux;
        PasswordAux = "Not Found";

        if(cursor.moveToFirst())
        {
            do
            {
                UserNameAux = cursor.getString(1);
                System.out.println("Username -> "+ UserNameAux + "; Password -> "+cursor.getString(2));

                if(UserNameAux.equals(Uname))
                {
                    PasswordAux = cursor.getString(2);

                    break;
                }
            }
            while (cursor.moveToNext());
        }
        return PasswordAux; //Retorna a string password
    }

    public String searchUser(String Uname)
    {
        db = this.getReadableDatabase();
        String query = "select * from "+TABLE_NAME;
        Cursor cursor = db.rawQuery(query , null);

        String UserNameAux = "";


        System.out.println(cursor);
        //procurar username
        if(cursor.moveToFirst())
        {
            do
            {

                UserNameAux = cursor.getString(2);
                if(UserNameAux.equals(Uname))
                {
                    break;
                }
                else
                {
                    UserNameAux = "";
                }
            }
            while (cursor.moveToNext());
        }
        return UserNameAux;
    }

    public String searchPool(String username)
    {
        db = this.getReadableDatabase();
        String query = "select * from "+ TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);

        String PoolName = "";
        String UsernameAux;

        System.out.println(cursor);

        if(cursor.moveToFirst())
        {
            do
            {
                PoolName = cursor.getString(3);
                UsernameAux = cursor.getString(1);
                if(UsernameAux.equals(username))
                {
                    break;
                }
            }
            while(cursor.moveToNext());
        }
        return PoolName;
    }

    @Override
    //Se já existe a base de dados abre a base de dados existente
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String query = "DROP TABLE IF EXISTS"+TABLE_NAME;
        db.execSQL(query);
        this.onCreate(db);
    }
}
