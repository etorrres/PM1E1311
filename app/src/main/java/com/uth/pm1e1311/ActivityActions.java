package com.uth.pm1e1311;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uth.pm1e1311.Configuracion.SQLiteConexion;
import com.uth.pm1e1311.Configuracion.Transacciones;

public class ActivityActions extends AppCompatActivity {

    EditText pais, nombre, telefono, nota;
    Button btnActualizar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions);


        pais = (EditText) findViewById(R.id.editPais);
        nombre = (EditText) findViewById(R.id.editNombre);
        telefono = (EditText) findViewById(R.id.editTelefono);
        nota = (EditText) findViewById(R.id.editNota);
        btnActualizar = (Button) findViewById(R.id.btnactualizardatos);


        String id_contacto = getIntent().getStringExtra("id");
        String pais2 = getIntent().getStringExtra("pais");
        String nombre2 = getIntent().getStringExtra("nombre");
        String telefono2 = getIntent().getStringExtra("telefono");
        String nota2 = getIntent().getStringExtra("nota");

        if (pais != null && nombre != null && telefono != null && nota != null) {
            // Aquí puedes hacer lo que necesites con la información extra recibida
            // Por ejemplo, puedes mostrarla en un TextView
            pais.setText(pais2);
            nombre.setText(nombre2);
            telefono.setText(telefono2);
            nota.setText(nota2);
        }


        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarPersona(id_contacto, pais.getText().toString(), nombre.getText().toString()
                        , telefono.getText().toString(), nota.getText().toString());
            }
        });




    }

    private void actualizarPersona(String id_contacto, String pais2, String nombre2, String telefono2, String nota2) {
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.DBName, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Transacciones.pais, pais2);
        valores.put(Transacciones.nombre, nombre2);
        valores.put(Transacciones.telefono, telefono2);
        valores.put(Transacciones.nota, nota2);

        Long resultado = Long.valueOf(db.update(Transacciones.TablaContactos, valores, Transacciones.id_contacto + "=?", new String[]{String.valueOf(id_contacto)}));
        db.close();
        Toast.makeText(getApplicationContext(), "Registro actualizado correctamente " + resultado.toString(),
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ActivityActions.this, ActivityListContact.class);
        startActivity(intent);
        finish();

    }
}