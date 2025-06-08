package com.example.petapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "petapp.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TUTOR_TABLE = "CREATE TABLE Tutor (" +
                "id_tutor INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "email TEXT UNIQUE NOT NULL," +
                "telefone TEXT," +
                "data_nascimento TEXT)";

        String CREATE_PET_TABLE = "CREATE TABLE Pet (" +
                "id_pet INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "especie TEXT," +
                "raca TEXT," +
                "data_nascimento TEXT)";

        String CREATE_TUTORPET_TABLE = "CREATE TABLE TutorPet (" +
                "id_tutor INTEGER NOT NULL," +
                "id_pet INTEGER NOT NULL," +
                "PRIMARY KEY (id_tutor, id_pet)," +
                "FOREIGN KEY (id_tutor) REFERENCES Tutor(id_tutor) ON DELETE CASCADE," +
                "FOREIGN KEY (id_pet) REFERENCES Pet(id_pet) ON DELETE CASCADE)";

        db.execSQL(CREATE_TUTOR_TABLE);
        db.execSQL(CREATE_PET_TABLE);
        db.execSQL(CREATE_TUTORPET_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TutorPet");
        db.execSQL("DROP TABLE IF EXISTS Pet");
        db.execSQL("DROP TABLE IF EXISTS Tutor");
        onCreate(db);
    }
}