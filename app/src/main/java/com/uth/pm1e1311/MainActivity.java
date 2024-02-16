package com.uth.pm1e1311;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.uth.pm1e1311.Configuracion.SQLiteConexion;
import com.uth.pm1e1311.Configuracion.Transacciones;


public class MainActivity extends AppCompatActivity {

    EditText nombre, telefono, nota;
    Button btn_salvarContacto, btn_contactosSalvados, btn_foto;

    String mensaje="";

    Boolean validacion = false;

    static final int peticion_camara = 100;
    static final int peticion_foto = 102;

    ImageView foto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombre = (EditText) findViewById(R.id.text_nombre);
        telefono = (EditText) findViewById(R.id.text_telefono);
        nota = (EditText) findViewById(R.id.text_notas);
        btn_salvarContacto = (Button) findViewById(R.id.btn_salvarContacto);
        btn_contactosSalvados = (Button) findViewById(R.id.btn_contactosSalvados);
        btn_foto = (Button) findViewById(R.id.btn_add_foto);

        foto = (ImageView) findViewById(R.id.img_contacto);

        btn_salvarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrarContacto();
            }
        });

        btn_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarPermiso();
            }
        });

    }
    private void ValidarPermiso () {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    peticion_camara);
        }else{
            tomarFoto();
        }
    }

    private void tomarFoto(){
        Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, peticion_foto);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull
    int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == peticion_camara)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                tomarFoto();
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
            foto.setImageBitmap(imagen);
        }
    }

    private void RegistrarContacto()
    {
        SQLiteConexion conn = new SQLiteConexion(this, Transacciones.DBName, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();

        ContentValues valores = new ContentValues();

        valores.put(Transacciones.nombre, nombre.getText().toString());
        valores.put(Transacciones.telefono, telefono.getText().toString());
        valores.put(Transacciones.nota, nota.getText().toString());
        //valores.put(Transacciones.imagen, foto);

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