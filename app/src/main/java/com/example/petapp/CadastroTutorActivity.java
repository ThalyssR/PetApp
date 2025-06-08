package com.example.petapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CadastroTutorActivity extends AppCompatActivity {

    EditText editTextNome, editTextEmail, editTextTelefone;
    Button btnSalvarTutor;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_tutor);

        editTextNome = findViewById(R.id.editTextNome);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextTelefone = findViewById(R.id.editTextTelefone);
        btnSalvarTutor = findViewById(R.id.btnSalvarTutor);

        dbHelper = new DatabaseHelper(this);

        btnSalvarTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarTutor();
            }
        });
    }

    private void salvarTutor() {
        String nome = editTextNome.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String telefone = editTextTelefone.getText().toString().trim();

        if (nome.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha nome e e-mail!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("email", email);
        values.put("telefone", telefone);

        long newRowId = db.insert("Tutor", null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Erro ao salvar tutor. O e-mail já existe?", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Tutor salvo com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}