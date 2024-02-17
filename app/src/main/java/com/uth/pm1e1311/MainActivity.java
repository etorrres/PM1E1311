package com.uth.pm1e1311;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.Manifest;

import com.uth.pm1e1311.Configuracion.SQLiteConexion;
import com.uth.pm1e1311.Configuracion.Transacciones;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    EditText nombre, telefono, nota;
    Button btn_imagen, btn_salvarContacto, btn_contactosSalvados;
    String valorPais="";
    Spinner spinnerPaises;

    static final int peticion_camara = 100;
    static final int peticion_foto = 102;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerPaises = findViewById(R.id.list_paises);
        nombre = (EditText) findViewById(R.id.text_nombre);
        telefono = (EditText) findViewById(R.id.text_telefono);
        nota = (EditText) findViewById(R.id.text_notas);
        btn_salvarContacto = (Button) findViewById(R.id.btn_salvarContacto);
        btn_contactosSalvados = (Button) findViewById(R.id.btn_contactosSalvados);
        btn_imagen = (Button) findViewById(R.id.btn_camara);
        imageView = (ImageView) findViewById(R.id.img_contacto);

        btn_salvarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreTxt = nombre.getText().toString().trim();
                String telefonoTxt = telefono.getText().toString().trim();
                String notaTxt = nota.getText().toString().trim();
                Drawable img_contacto = imageView.getDrawable();

                File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File img_archivo = new File(directorio, "contacto.jpg");



                if (nombreTxt.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Debe escribir un nombre",
                            Toast.LENGTH_SHORT).show();
                } else if (telefonoTxt.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Debe escribir un numero de telefono",
                            Toast.LENGTH_SHORT).show();
                } else if (notaTxt.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Debe escribir una nota",
                            Toast.LENGTH_SHORT).show();
                } else if (imageView.getDrawable() == null){
                    Toast.makeText(getApplicationContext(), "Debe tomar una imagen",
                            Toast.LENGTH_SHORT).show();
                }else {
                    RegistrarContacto();
                    try {
                        FileOutputStream fos = new FileOutputStream(img_archivo);
                        Bitmap bitmap = ((BitmapDrawable) img_contacto).getBitmap();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                        fos.close();
                    } catch (IOException e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error al Guardar la Imagen",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_contactosSalvados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityListContact.class);
                startActivity(intent);
            }
        });

        btn_imagen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Permisos();
            }
        });

    }
    private void RegistrarContacto()
    {
        SQLiteConexion conn = new SQLiteConexion(this, Transacciones.DBName, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();

        String paisSeleccionado = spinnerPaises.getSelectedItem().toString();


        if (paisSeleccionado.equals("Honduras (504)")) {
            valorPais = "504";
        } else if (paisSeleccionado.equals("Belice (501)")) {
            valorPais = "501";
        } else if (paisSeleccionado.equals("Guatemala (502)")) {
            valorPais = "502";
        } else if (paisSeleccionado.equals("Nicaragua (505)")) {
            valorPais = "505";
        } else if (paisSeleccionado.equals("Costa Rica (506)")) {
            valorPais = "506";
        } else if (paisSeleccionado.equals("Pánama (507)")) {
            valorPais = "507";
        }

        ContentValues valores = new ContentValues();
        valores.put(Transacciones.pais, valorPais);
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

    private void Permisos ()
    {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    peticion_camara);
        }
        else
        {
            tomarfoto();
        }
    }

    private void tomarfoto ()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(intent, peticion_foto);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull
    int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == peticion_camara)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                tomarfoto();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Permiso denegado", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == peticion_foto && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap imagen = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imagen);
        }
    }


}