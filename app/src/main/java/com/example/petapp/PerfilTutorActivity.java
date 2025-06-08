package com.example.petapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class PerfilTutorActivity extends AppCompatActivity {

    TextView textViewNomePerfil, textViewEmailPerfil, textViewTelefonePerfil;
    ListView listViewPetsDoTutor;
    Button btnAdicionarPetAoTutor;

    DatabaseHelper dbHelper;
    long tutorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_tutor);

        tutorId = getIntent().getLongExtra("TUTOR_ID", -1);
        dbHelper = new DatabaseHelper(this);

        textViewNomePerfil = findViewById(R.id.textViewNomePerfil);
        textViewEmailPerfil = findViewById(R.id.textViewEmailPerfil);
        textViewTelefonePerfil = findViewById(R.id.textViewTelefonePerfil);
        listViewPetsDoTutor = findViewById(R.id.listViewPetsDoTutor);
        btnAdicionarPetAoTutor = findViewById(R.id.btnAdicionarPetAoTutor);

        if (tutorId == -1) {
            finish();
            return;
        }

        // Configurando o clique do botão
        btnAdicionarPetAoTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica toda colocada aqui dentro do clique
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                final List<String> nomesPets = new ArrayList<>();
                final List<Long> idsPets = new ArrayList<>();

                Cursor cursorPets = db.rawQuery("SELECT id_pet, nome FROM Pet", null);
                if (cursorPets.moveToFirst()) {
                    do {
                        idsPets.add(cursorPets.getLong(0));
                        nomesPets.add(cursorPets.getString(1));
                    } while (cursorPets.moveToNext());
                }
                cursorPets.close();
                // Não fechamos o 'db' ainda pois vamos precisar dele de novo

                AlertDialog.Builder builder = new AlertDialog.Builder(PerfilTutorActivity.this);
                builder.setTitle("Selecione um Pet");
                builder.setItems(nomesPets.toArray(new CharSequence[0]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long petIdSelecionado = idsPets.get(which);

                        // --- Verificação manual se a associação já existe ---
                        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
                        String[] selectionArgs = { String.valueOf(tutorId), String.valueOf(petIdSelecionado) };
                        Cursor cursorCheck = dbWrite.rawQuery("SELECT * FROM TutorPet WHERE id_tutor = ? AND id_pet = ?", selectionArgs);

                        if (cursorCheck.getCount() > 0) {
                            // Se getCount() > 0, significa que já encontrou um registro
                            Toast.makeText(PerfilTutorActivity.this, "Este pet já está associado.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Se não encontrou, insere o novo registro
                            ContentValues values = new ContentValues();
                            values.put("id_tutor", tutorId);
                            values.put("id_pet", petIdSelecionado);
                            dbWrite.insert("TutorPet", null, values);

                            Toast.makeText(PerfilTutorActivity.this, "Pet associado!", Toast.LENGTH_SHORT).show();
                            carregarPetsDoTutor(); // Atualiza a lista na tela
                        }
                        cursorCheck.close();
                        dbWrite.close();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarDadosTutor();
        carregarPetsDoTutor();
    }

    private void carregarDadosTutor() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nome, email, telefone FROM Tutor WHERE id_tutor = ?", new String[]{String.valueOf(tutorId)});
        if (cursor.moveToFirst()) {
            textViewNomePerfil.setText(cursor.getString(0));
            textViewEmailPerfil.setText(cursor.getString(1));
            textViewTelefonePerfil.setText(cursor.getString(2));
        }
        cursor.close();
        db.close();
    }

    private void carregarPetsDoTutor() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<String> listaNomesPets = new ArrayList<>();
        String query = "SELECT P.nome FROM Pet P INNER JOIN TutorPet TP ON P.id_pet = TP.id_pet WHERE TP.id_tutor = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(tutorId)});
        if (cursor.moveToFirst()) {
            do {
                listaNomesPets.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        ArrayAdapter<String> petAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listaNomesPets);
        listViewPetsDoTutor.setAdapter(petAdapter);
    }
}