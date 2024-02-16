package com.uth.pm1e1311.Configuracion;

public class Transacciones
{
    public static final String DBName = "PM1E1311";

    // ESTRUCTURA DE LA TABLA CONTACTOS
    public static final String TablaContactos = "contactos";
    public static final String id_contacto = "id_contacto";
    public static final String nombre = "nombre";
    public static final Long telefono = Long.valueOf("telefono");
    public static final String nota = "nota";
    public static final String imagen_id = "imagen_id";

    // ESTRUCTURA DE LA TABLA IMAGEN
    public static final String TablaImagenes = "imagen";
    public static final String id_imagen = "id_imagen";
    public static final String imagen = "imagen";

    //CREACION
    public static final String CreateTableContactos = "Create table "+ TablaContactos +" ("+
            "id_contacto PRIMARY KEY AUTOINCREMENT, nombre TEXT, telefono LONG, nota TEXT)";
    public static final String CreateTableImagen = "Create table "+ TablaImagenes +" ("+
            "id_imagen PRIMARY KEY AUTOINCREMENT, imagen BLOB)";

    //DROP
    public static final String DropTableContactos = "DROP TABLE IF EXISTS "+TablaContactos;
    public static final String DropTableImagenes = "DROP TABLE IF EXISTS "+TablaImagenes;

    //SELECT
    public static final String SelectAllContactos = "SELECT * FROM "+ TablaContactos;
    public static final String SelectAllImagenes = "SELECT * FROM "+ TablaImagenes;

}
