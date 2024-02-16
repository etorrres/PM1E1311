package com.uth.pm1e1311;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.uth.pm1e1311.Configuracion.SQLiteConexion;
import com.uth.pm1e1311.Configuracion.Transacciones;

public class MainActivity extends AppCompatActivity {

    EditText nombre, telefono, nota;
    Button btn_salvarContacto, btn_contactosSalvados;

    String mensaje="";
    Spinner spinnerPaises;

    Boolean validacion = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerPaises = findViewById(R.id.list_paises);
        nombre = (EditText) findViewById(R.id.text_nombre);
        telefono = (EditText) findViewById(R.id.text_telefono);
        nota = (EditText) findViewById(R.id.text_notas);
        btn_salvarContacto = (Button) findViewById(R.id.btn_salvarContacto);

        btn_salvarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreTxt = nombre.getText().toString().trim();
                String telefonoTxt = telefono.getText().toString().trim();
                String notaTxt = nota.getText().toString().trim();

                if (nombreTxt.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Debe escribir un nombre",
                            Toast.LENGTH_SHORT).show();
                } else if (telefonoTxt.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Debe escribir un numero de telefono",
                            Toast.LENGTH_SHORT).show();
                } else if (notaTxt.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Debe escribir una nota",
                            Toast.LENGTH_SHORT).show();
                } else {
                    RegistrarContacto();
                }
            }
        });
    }
    private void RegistrarContacto()
    {
        SQLiteConexion conn = new SQLiteConexion(this, Transacciones.DBName, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();

        String paisSeleccionado = spinnerPaises.getSelectedItem().toString();

        ContentValues valores = new ContentValues();
        valores.put(Transacciones.pais, paisSeleccionado);
        valores.put(Transacciones.nombre, nombre.getText().toString());
        valores.put(Transacciones.telefono, telefono.getText().toString());
        valores.put(Transacciones.nota, nota.getText().toString());

        long result = db.insert(Transacciones.TablaContactos, Transacciones.id_contacto, valores);

        if (result != -1) {
            Toast.makeText(getApplicationContext(), "Contacto ingresada correctamente. ID: " + result,
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Error al Registrar el contacto",
                    Toast.LENGTH_LONG).show();
        }
    }
}