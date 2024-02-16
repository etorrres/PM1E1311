package com.uth.pm1e1311;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import com.uth.pm1e1311.Configuracion.SQLiteConexion;

import java.util.ArrayList;

public class ActivityListContact extends AppCompatActivity {

    SQLiteConexion conexion;
    ListView listpersonas;

    ArrayList<String> Arreglo;
    EditText nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contact);
    }
}