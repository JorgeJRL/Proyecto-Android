package com.example.proyectoandroid

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminBD(
    context: Context,
    name: String,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table usuarios(nombre text unique, contraseña text, correo text,id INTEGER PRIMARY KEY AUTOINCREMENT)")
        db.execSQL("create table servicios(idServicio INTEGER PRIMARY KEY AUTOINCREMENT,nombreCliente text, producto text, peso Integer, " +
                "lugar text, fechaEntrega date, idCliente Integer, FOREIGN KEY (idCliente) REFERENCES usuarios (id) )")
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }


}