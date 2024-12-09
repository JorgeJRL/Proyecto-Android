package com.example.proyectoandroid

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Registrar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nombre = findViewById<EditText>(R.id.nombreR)
        val correo = findViewById<EditText>(R.id.correoR)
        val contraseña = findViewById<EditText>(R.id.contraseñaR)
        val boton = findViewById<Button>(R.id.registrar)

        boton.setOnClickListener {
            val admin = AdminBD(this, "usuarios", null, 1)
            val bd = admin.writableDatabase
            val registro = ContentValues()
            var insertado = false

            if (nombre.text.isEmpty() || contraseña.text.isEmpty() || correo.text.isEmpty()) {
                Toast.makeText(this, "Introduce algo", Toast.LENGTH_SHORT).show()
            } else {

                val cursor = bd.rawQuery(
                    "SELECT * FROM usuarios WHERE nombre = ?",
                    arrayOf(nombre.text.toString())
                )

                if (cursor.moveToFirst()) {
                    Toast.makeText(this, "El usuario ya está registrado", Toast.LENGTH_SHORT).show()
                    cursor.close()
                } else {

                    registro.put("nombre", nombre.text.toString())
                    registro.put("contraseña", contraseña.text.toString())
                    registro.put("correo", correo.text.toString())

                    bd.insert("usuarios", null, registro)
                    bd.close()
                    cursor.close()
                    nombre.setText("")
                    contraseña.setText("")
                    correo.setText("")
                    insertado = true
                }
            }

            if (insertado) {
                Toast.makeText(this, "Insertado con éxito", Toast.LENGTH_SHORT).show()

                val intento = Intent(this, MainActivity::class.java)
                startActivity(intento)


            } else {
                Toast.makeText(this, "No se pudo insertar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}