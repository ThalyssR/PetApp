package com.example.petapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CadastroPetActivity extends AppCompatActivity {

    EditText editTextNomePet, editTextEspecie, editTextRaca;
    Button btnSalvarPet;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pet);

        editTextNomePet = findViewById(R.id.editTextNomePet);
        editTextEspecie = findViewById(R.id.editTextEspecie);
        editTextRaca = findViewById(R.id.editTextRaca);
        btnSalvarPet = findViewById(R.id.btnSalvarPet);

        dbHelper = new DatabaseHelper(this);

        btnSalvarPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarPet();
            }
        });
    }

    private void salvarPet() {
        String nome = editTextNomePet.getText().toString().trim();
        String especie = editTextEspecie.getText().toString().trim();
        String raca = editTextRaca.getText().toString().trim();

        if (nome.isEmpty() || especie.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha nome e espécie!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("especie", especie);
        values.put("raca", raca);

        // Agora inserimos na tabela "Pet"
        long newRowId = db.insert("Pet", null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Erro ao salvar pet.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Pet salvo com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}