package com.example.calculatriceavechistorique;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHistorique extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "calculatrice.db";
    private static final int DATABASE_VERSION = 1;

    // Nom de la table
    private static final String TABLE_NAME = "calculatrice";

    // Noms des colonnes
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_OPERATION = "operation";
    private static final String COLUMN_RESULT = "resultat";

    // Requête de création de table
    private static final String CREATE_TABLE_CALCULATRICE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_OPERATION + " TEXT,"
            + COLUMN_RESULT + " TEXT"
            + ")";

    public DatabaseHistorique(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CALCULATRICE);
        Log.d("Database", "Table calculatrice créée avec succès");
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (db.isReadOnly()) {
            Log.d("Database", "Base de données en lecture seule");
        } else {
            Log.d("Database", "Base de données accessible en lecture/écriture");
        }}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
    }


    public void saveCalculation(String operation, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OPERATION, operation);
        values.put(COLUMN_RESULT, result);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public void insertOperation(String operation, String result) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_OPERATION, operation);
        values.put(COLUMN_RESULT, result);

        // Insérer les valeurs dans la table des opérations
        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    public List<String> getAllOperations() {
        List<String> operations = new ArrayList<>();

        // Récupérer les opérations depuis la base de données
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_OPERATION, COLUMN_RESULT};
        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);

        // Récupérer les indices des colonnes
        int columnIndexOperation = cursor.getColumnIndex(COLUMN_OPERATION);
        int columnIndexResult = cursor.getColumnIndex(COLUMN_RESULT);

        // Parcourir les résultats pour extraire les opérations et résultats
        while (cursor.moveToNext()) {
            String operation = "";
            String result = "";

            // Vérifier si les indices des colonnes sont valides
            if (columnIndexOperation != -1) {
                operation = cursor.getString(columnIndexOperation);
            }

            if (columnIndexResult != -1) {
                result = cursor.getString(columnIndexResult);
            }

            int desiredLength = 80+result.length();
            result = String.format("%" + desiredLength + "s%n", result);

            String operationWithResult = operation + "\n" +result;
            operations.add(operationWithResult);
        }

        // Fermer le curseur et la base de données
        cursor.close();
        db.close();

        return operations;
    }



}



