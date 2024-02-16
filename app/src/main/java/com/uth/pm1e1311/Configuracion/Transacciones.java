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

    public static final String imagen = "imagen";

    //CREACION
    public static final String CreateTableContactos = "Create table "+ TablaContactos +" ("+
            "id_contacto PRIMARY KEY AUTOINCREMENT, nombre TEXT, telefono LONG, nota TEXT, imagen BLOB)";

    //DROP
    public static final String DropTableContactos = "DROP TABLE IF EXISTS "+TablaContactos;

    //SELECT
    public static final String SelectAllContactos = "SELECT * FROM "+ TablaContactos;


}
