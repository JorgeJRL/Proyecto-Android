package com.example.proyectoandroid

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(R.layout.activity_main)

            ViewCompat.setOnApplyWindowInsetsListener(findViewById (R.id.main)) { v, insets -> val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(
                    systemBars.left, systemBars.top,
                    systemBars.right, systemBars.bottom
                )
                insets
            }

            val datoUsuario = findViewById<EditText>(R.id.usuario)
            val contraseña = findViewById<EditText>(R.id.contraseña)
            val botonL = findViewById<Button>(R.id.loginB)
            val botonS = findViewById<Button>(R.id.signIn)
            val botonB = findViewById<Button>(R.id.BorrarC)
            val checkM = findViewById<CheckBox>(R.id.modificarC)

            botonL.setOnClickListener() {
                val admin = AdminBD(this, "usuarios", null, 1)
                val bd = admin.writableDatabase
                if (datoUsuario.text.isEmpty() ||
                    contraseña.text.isEmpty()
                ) {
                    Toast.makeText(this, "Introduce algo", Toast.LENGTH_SHORT).show()
                } else {
                    val fila = bd.rawQuery("select * from usuarios where nombre = '${datoUsuario.text.toString()}'", null)

                    if (fila.moveToFirst()) {
                        datoUsuario.setText(fila.getString(0))
                        contraseña.setText(fila.getString(1))
                        val intento = Intent(this, Bienvenida::class.java)
                        intento.putExtra("usuario", datoUsuario.text.toString())
                        startActivity(intento)


                    } else {
                        Toast.makeText(this, "No existe ese usuario", Toast.LENGTH_SHORT).show()
                    }

                    bd.close()
                }
            }

            botonS.setOnClickListener() {
                val intento = Intent(this, Registrar::class.java)
                startActivity(intento)

            }

            botonB.setOnClickListener() {
                val admin = AdminBD(this, "usuarios", null, 1)
                val bd = admin.writableDatabase
                if (datoUsuario.text.isEmpty() ||
                    contraseña.text.isEmpty()
                ) {
                    Toast.makeText(this, "Introduce algo", Toast.LENGTH_SHORT).show()
                } else {
                    val borrado = bd.delete("usuarios", "nombre='${datoUsuario.text.toString()}'", null)
                    bd.close()
                    datoUsuario.setText("")
                    contraseña.setText("")
                    if (borrado == 1) {
                        Toast.makeText(this, "borrada con exito", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "no existe ese usuario", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            checkM.setOnClickListener() {
                if (checkM.isChecked) {
                    val admin = AdminBD(this, "usuarios", null, 1)
                    val bd = admin.writableDatabase
                    if (datoUsuario.text.isEmpty() ||
                        contraseña.text.isEmpty())
                    {
                        Toast.makeText(this, "Introduce algo", Toast.LENGTH_SHORT).show()
                    } else {
                        val actualizar = ContentValues()
                        actualizar.put("contraseña", contraseña.text.toString())
                        val listo = bd.update("usuarios", actualizar, "nombre='${datoUsuario.text.toString()}'", null)
                        bd.close()
                        if (listo > 0) {
                            Toast.makeText(this, "actualizada con exito ", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "no existe ese usuario ", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }
    }