package com.uth.pm1e1311;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

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
    Integer posicionSeleccionada;
    Button btnActualizarContacto;
    Button btneliminarContacto;
    Button btnCompartir;
    Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contact);

        conexion = new SQLiteConexion(this, Transacciones.DBName, null, 1);
        listpersonas = findViewById(R.id.listpersons);
        buscarContacto = findViewById(R.id.searchContact);
        btnActualizarContacto = findViewById(R.id.btnactualizar);
        btneliminarContacto = findViewById(R.id.btneliminar);
        btnVolver = findViewById(R.id.btnatras);
        btnCompartir = findViewById(R.id.btncompartir);

        obtenerDatos();

        // Configurando el TextChangeListener para el SearchView
        buscarContacto.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Cuando el texto de búsqueda cambia, ejecutar la búsqueda
                buscarContacto(newText);
                return true;
            }
        });

        listpersonas.setOnItemClickListener((parent, view, position, id) -> {
            // Guardar la posición del elemento seleccionado
            posicionSeleccionada = position;
        });

        btnActualizarContacto.setOnClickListener(v -> {
            if (posicionSeleccionada != -1) {
                // Obtener el contacto seleccionado
                Contactos contactoSeleccionado = lista.get(posicionSeleccionada);
                //Enviar los datos a actualizar
                Intent intent = new Intent(ActivityListContact.this, ActivityActions.class);
                intent.putExtra("id", contactoSeleccionado.getId_contacto().toString());
                intent.putExtra("pais", contactoSeleccionado.getPais());
                intent.putExtra("nombre", contactoSeleccionado.getNombre());
                intent.putExtra("telefono", contactoSeleccionado.getTelefono());
                intent.putExtra("nota", contactoSeleccionado.getNota());

                startActivity(intent);
            } else {
                Toast.makeText(ActivityListContact.this, "Seleccione un contacto primero", Toast.LENGTH_SHORT).show();
            }
        });

        btneliminarContacto.setOnClickListener(view -> eliminarContacto());

        btnVolver.setOnClickListener(view -> volverAtras());

        //Enviar contacto a otras aplicaciones
        btnCompartir.setOnClickListener(view -> {
            if (posicionSeleccionada != null && posicionSeleccionada != -1) {
                // Obtener el contacto seleccionado
                Contactos contactoSeleccionado = lista.get(posicionSeleccionada);

                // Crear un Intent para compartir
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");

                // Construir el texto a compartir con los detalles del contacto
                String textoCompartir = "Nombre: " + contactoSeleccionado.getNombre() + "\n" +
                        "Teléfono: " + "+" + contactoSeleccionado.getPais() + contactoSeleccionado.getTelefono() + "\n" +
                        "Nota: " + contactoSeleccionado.getNota();

                intent.putExtra(Intent.EXTRA_TEXT, textoCompartir);

                // Iniciar la actividad para seleccionar una aplicación para compartir
                startActivity(Intent.createChooser(intent, "Compartir contacto con"));
            } else {
                Toast.makeText(ActivityListContact.this, "Seleccione un contacto primero", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //Boton para volver a pantalla principal
    private void volverAtras() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void eliminarContacto() {
        if (posicionSeleccionada != -1) {
            Contactos contactoEliminar = lista.get(posicionSeleccionada);
            SQLiteDatabase db = conexion.getWritableDatabase();
            // Condición de eliminación
            String whereClause = Transacciones.id_contacto + " = ?";
            // Argumentos de la condición
            String[] whereArgs = {String.valueOf(contactoEliminar.getId_contacto())};
            // Ejecutar la eliminación
            int result = db.delete(Transacciones.TablaContactos, whereClause, whereArgs);
            if (result > 0) {
                Toast.makeText(ActivityListContact.this, "Contacto eliminado correctamente", Toast.LENGTH_SHORT).show();
                obtenerDatos(); // Actualizar la lista después de eliminar el contacto
            } else {
                Toast.makeText(ActivityListContact.this, "Error al eliminar el contacto", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ActivityListContact.this, "Seleccione un contacto primero", Toast.LENGTH_SHORT).show();
        }
    }

    private void obtenerDatos() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Contactos contacto;
        lista = new ArrayList<>();

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
        Arreglo = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {
            Arreglo.add( lista.get(i).getNombre() + " | " +
                    lista.get(i).getPais() +
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
