package com.example.proyectoandroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PerfilUsuario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val switch = findViewById<Switch>(R.id.cambiarT)
        val nombre = findViewById<TextView>(R.id.nombreP)
        val correo = findViewById<TextView>(R.id.correoP)
        val botonS = findViewById<TextView>(R.id.botonS)
        val numP2 = findViewById<TextView>(R.id.numP)
        val volver = findViewById<Button>(R.id.volver)




        val bundle = intent.extras
        val dato1 = bundle?.getString("usuario")


        nombre.setText(dato1)
        correo.setText(cogerDatos(dato1!!))

        numP2.setText(cogerPedidos(dato1!!).toString())

        botonS.setOnClickListener(){
            finishAffinity()
        }

        volver.setOnClickListener(){
            val intento = Intent(this, Bienvenida::class.java)
            intento.putExtra("usuario", dato1)
            startActivity(intento)

        }
        switch.setOnClickListener(){
            if (switch.isChecked){
                cambiarTema(0)
            }else{
                cambiarTema(1)
            }
            recreate()
        }

    }



    fun cambiarTema(modo : Int){
        if (modo==0){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            val prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE)
            prefs.edit().putInt("theme_mode", modo).apply()
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            val prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE)
            prefs.edit().putInt("theme_mode", modo).apply()
        }
    }


    fun cogerDatos(dato : String) :String {
        val admin = AdminBD(this, "usuarios", null, 1)
        val bd = admin.writableDatabase
        val fila = bd.rawQuery("select correo from usuarios where nombre = '${dato}'", null)
        var resultado="correo no encontrado"
        if (fila.moveToFirst()) {
            resultado = fila.getString(fila.getColumnIndexOrThrow("correo"))
        }
        fila.close()
        bd.close()

        return resultado
    }


    fun cogerPedidos (dato : String) :Int{
        val admin = AdminBD(this, "usuarios", null, 1)
        val bd = admin.writableDatabase
        val fila = bd.rawQuery("select id from usuarios where nombre = '${dato}'", null)
        var resultado=""
        var resultado2=0
        if (fila.moveToFirst()) {
            resultado = fila.getString(fila.getColumnIndexOrThrow("id"))
            val admin = AdminBD(this, "servicios", null, 1)
            val bd = admin.writableDatabase
            val fila2 = bd.rawQuery("select count(idCliente) from servicios where idCliente = '${resultado.toInt()}'", null)
            if (fila2.moveToFirst()) {
                resultado2 = fila2.getInt(0)

            }
            fila2.close()
        }
        fila.close()
        bd.close()

        return resultado2
    }
}