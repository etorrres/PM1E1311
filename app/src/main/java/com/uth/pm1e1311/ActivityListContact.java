package com.uth.pm1e1311;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.uth.pm1e1311.Configuracion.SQLiteConexion;
import com.uth.pm1e1311.Configuracion.Transacciones;

import java.util.ArrayList;

import Models.Contactos;

public class ActivityListContact extends AppCompatActivity {

    SQLiteConexion conexion;
    ListView listpersonas;
    ArrayList<Contactos> lista;
    ArrayList<String> Arreglo;
    SearchView buscarContacto;
    String contactoBuscado;
    Button btnActualizarContacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contact);

        conexion = new SQLiteConexion(this, Transacciones.DBName, null, 1);
        listpersonas = findViewById(R.id.listpersons);
        buscarContacto = findViewById(R.id.searchContact);
        btnActualizarContacto = findViewById(R.id.btnactualizar);

        obtenerDatos();

        // Configurar el TextChangeListener para el SearchView
        buscarContacto.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // No necesitas implementar nada aquí
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Cuando el texto de búsqueda cambia, ejecutar la búsqueda
                buscarContacto(newText);
                return true;
            }
        });

        listpersonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtener la persona seleccionada
                Contactos personaSeleccionada = lista.get(position);

                Intent intent = new Intent(ActivityListContact.this, ActivityActions.class);

                // Pasar la información a través del Intent
                intent.putExtra("id", personaSeleccionada.getId_contacto().toString());
                intent.putExtra("nombre", personaSeleccionada.getNombre());
                intent.putExtra("telefono", personaSeleccionada.getTelefono().toString());
                intent.putExtra("nota", personaSeleccionada.getNota());

                // Iniciar la nueva actividad
                startActivity(intent);
                finish();
            }
        });
    }

    private void obtenerDatos() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Contactos contacto = null;
        lista = new ArrayList<Contactos>();

        //Cursor de base de datos para recorrer los datos
        Cursor cursor = db.rawQuery(Transacciones.SelectAllContactos, null);

        while (cursor.moveToNext()) {
            contacto = new Contactos();
            contacto.setId_contacto(cursor.getInt(0));
            contacto.setPais(cursor.getString(1));
            contacto.setNombre(cursor.getString(2));
            contacto.setTelefono(cursor.getString(3));
            contacto.setNota(cursor.getString(4));

            lista.add(contacto);
        }

        cursor.close();
        LlenarData();
    }

    private void LlenarData() {
        Arreglo = new ArrayList<String>();
        for (int i = 0; i < lista.size(); i++) {
            Arreglo.add( lista.get(i).getNombre() + " | " +
                    lista.get(i).getPais() + "" +
                    lista.get(i).getTelefono() + "\n" +
                    lista.get(i).getNota() + "\n");
        }

        // Configurar el adaptador para la lista
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Arreglo);
        listpersonas.setAdapter(adapter);
    }

    private void buscarContacto(String textoBuscado) {
        SQLiteDatabase db = conexion.getReadableDatabase();
        lista.clear(); // Limpiar la lista actual para agregar los nuevos resultados de búsqueda

        // Consulta SQL para buscar coincidencias en cualquiera de los campos
        String consulta = "SELECT * FROM " + Transacciones.TablaContactos +
                " WHERE " + Transacciones.nombre + " LIKE '%" + textoBuscado + "%' OR " +
                Transacciones.pais + " LIKE '%" + textoBuscado + "%' OR " +
                Transacciones.telefono + " LIKE '%" + textoBuscado + "%' OR " +
                Transacciones.nota + " LIKE '%" + textoBuscado + "%'";

        Cursor cursor = db.rawQuery(consulta, null);

        while (cursor.moveToNext()) {
            Contactos contacto = new Contactos();
            contacto.setId_contacto(cursor.getInt(0));
            contacto.setPais(cursor.getString(1));
            contacto.setNombre(cursor.getString(2));
            contacto.setTelefono(cursor.getString(3));
            contacto.setNota(cursor.getString(4));

            lista.add(contacto);
        }

        cursor.close();
        LlenarData(); // Actualizar la lista con los resultados de búsqueda
    }
}
