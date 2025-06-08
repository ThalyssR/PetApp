package com.example.petapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView; // Import novo
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnIrParaCadastroTutor, btnIrParaCadastroPet;
    ListView listViewTutores;
    DatabaseHelper dbHelper;
    ArrayAdapter<String> tutorAdapter;

    // Vamos guardar os nomes e os IDs em listas separadas, mas na mesma ordem.
    List<String> listaNomesTutores;
    List<Long> listaIdsTutores; // Lista NOVA para guardar os IDs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        btnIrParaCadastroTutor = findViewById(R.id.btnIrParaCadastroTutor);
        btnIrParaCadastroPet = findViewById(R.id.btnIrParaCadastroPet);
        listViewTutores = findViewById(R.id.listViewTutores);

        btnIrParaCadastroTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CadastroTutorActivity.class);
                startActivity(intent);
            }
        });

        btnIrParaCadastroPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CadastroPetActivity.class);
                startActivity(intent);
            }
        });

        // NOVO: Listener para o clique em um item da lista
        listViewTutores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Pega o ID do tutor que foi clicado, usando a posição do clique
                long tutorId = listaIdsTutores.get(position);

                // Cria a intenção de abrir a tela de Perfil
                Intent intent = new Intent(MainActivity.this, PerfilTutorActivity.class);

                // Adiciona o ID do tutor como um "extra" na intenção.
                // É assim que passamos dados de uma tela para outra.
                intent.putExtra("TUTOR_ID", tutorId);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarTutores();
    }

    private void carregarTutores() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        listaNomesTutores = new ArrayList<>();
        listaIdsTutores = new ArrayList<>(); // Inicializa a lista de IDs

        // Agora selecionamos o ID e o NOME
        Cursor cursor = db.rawQuery("SELECT id_tutor, nome FROM Tutor ORDER BY nome ASC", null);

        if (cursor.moveToFirst()) {
            do {
                // Pega o ID da coluna 0 e o NOME da coluna 1
                listaIdsTutores.add(cursor.getLong(0));
                listaNomesTutores.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        tutorAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listaNomesTutores);

        listViewTutores.setAdapter(tutorAdapter);
    }
}