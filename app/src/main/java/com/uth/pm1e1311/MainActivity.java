package com.uth.pm1e1311;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uth.pm1e1311.Configuracion.SQLiteConexion;
import com.uth.pm1e1311.Configuracion.Transacciones;

public class MainActivity extends AppCompatActivity {

    EditText nombre, telefono, nota;
    Button btn_salvarContacto, btn_contactosSalvados;

    String mensaje="";

    Boolean validacion = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombre = (EditText) findViewById(R.id.text_nombre);
        telefono = (EditText) findViewById(R.id.text_telefono);
        nota = (EditText) findViewById(R.id.text_notas);

        btn_salvarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrarContacto();
            }
        });

    }
    private void RegistrarContacto()
    {
        SQLiteConexion conn = new SQLiteConexion(this, Transacciones.DBName, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Transacciones.nombre, nombre.getText().toString());
        valores.put(String.valueOf(Transacciones.telefono), telefono.getText().toString());
        valores.put(Transacciones.nota, nota.getText().toString());

        if (nombre == null){
          mensaje = "Debes ingresar un nombre";
          validacion = false;
        } else{
            if (telefono == null){
                mensaje = "Debes ingresar un telefono";
                validacion = false;
            }else{
                if (nota == null){
                    mensaje = "Debes ingresar una nota";
                    validacion = false;
                }else{
                    mensaje = "Contacto Registrado Correctamente";
                    validacion = true;
                }
            }
        }

        if(validacion){
            Long result = db.insert(Transacciones.TablaContactos, Transacciones.id_contacto, valores);

            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();

            db.close();
        }else{
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        }

    }
}