package com.example.proyectoandroid

import Servicios
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Bienvenida : AppCompatActivity() {
    val listaS: ArrayList<Servicios> = ArrayList()
    var usuarioActual : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bienvenida)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val boton = findViewById<Button>(R.id.pedir)
        val b = findViewById<Button>(R.id.b)
        val bundle = intent.extras
        usuarioActual = bundle?.getString("usuario")!!
        val usuario = findViewById<TextView>(R.id.usuarioB)






        usuario.setText("$usuarioActual")

        b.setOnClickListener() {
            val intent = Intent(this, PerfilUsuario::class.java)
            intent.putExtra("usuario", usuarioActual)
            startActivity(intent)
        }


        boton.setOnClickListener() {
            val intento = Intent(this, Pedidos::class.java)
            intento.putExtra("usuario", usuarioActual)
            startActivity(intento)

        }
        val eliminar = findViewById<Button>(R.id.eliminar)
        eliminar.setOnClickListener() {
            val tabla = findViewById<TableLayout>(R.id.tabla)
            for (j in tabla.childCount - 1 downTo 0 ) {
                val registro = findViewById<TableLayout>(R.id.tabla).getChildAt(j)
                val checkBox = registro.findViewById<CheckBox>(R.id.checkBox)
                if (checkBox.isChecked) {
                    var idservicio = checkBox.tag.toString().toInt()
                    val admin = AdminBD(this, "servicios", null, 1)
                    val bd = admin.writableDatabase

                    val borrado = bd.delete("servicios", "idServicio='${idservicio}'", null)
                    Toast.makeText(this, "el id es " + idservicio, Toast.LENGTH_SHORT).show()
                    bd.close()
                    if (borrado > 0) {
                        Toast.makeText(this, "borrada con exito", Toast.LENGTH_SHORT).show()
                        tabla.removeViewAt(j)

                    } else {
                        Toast.makeText(this, "ha ocurrido un error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


    }


    override fun onResume() {
        super.onResume()
        listaS.clear()
        val bundle2 = intent.extras
        val dato = bundle2?.getString("usuario")
        meterDatos(dato!!)
        actualizarTabla()


    }


    fun actualizarTabla() {

        val tabla = findViewById<TableLayout>(R.id.tabla)

        tabla.removeAllViews()

        if (listaS.size == 0) {
            Toast.makeText(this, "Haz tu primer pedido no dudes", Toast.LENGTH_LONG).show()
        } else {
            for (i in 0 until listaS.size) {
                val registro =
                    LayoutInflater.from(this).inflate(R.layout.registro_tabla, null, false)
                val columnaN = registro.findViewById<View>(R.id.colNombre) as TextView
                val columnaP = registro.findViewById<View>(R.id.colProducto) as TextView
                val columnaPeso = registro.findViewById<View>(R.id.colPeso) as TextView
                val columnaL = registro.findViewById<View>(R.id.colLugar) as TextView
                val columnaF = registro.findViewById<View>(R.id.colFecha) as TextView
                val checkBox = registro.findViewById<View>(R.id.checkBox) as CheckBox

                columnaN.text = listaS[i].nombre
                columnaP.text = listaS[i].producto
                columnaPeso.text = listaS[i].peso.toString()
                columnaL.text = listaS[i].lugar
                columnaF.text = listaS[i].fecha



                checkBox.tag = listaS[i].id

                tabla.addView(registro)
            }
        }

    }


    fun meterDatos(dato: String): ArrayList<Servicios> {

        if (dato.isNullOrBlank()) {
        } else {


            val admin = AdminBD(this, "usuarios", null, 1)
            val bd = admin.writableDatabase
            val fila = bd.rawQuery("select id from usuarios where nombre = '${dato}'", null)
            var resultado = ""
            if (fila.moveToFirst()) {
                resultado = fila.getString(fila.getColumnIndexOrThrow("id"))
                val admin = AdminBD(this, "servicios", null, 1)
                val bd = admin.writableDatabase
                val fila2 = bd.rawQuery(
                    "select * from servicios where idCliente = '${resultado.toInt()}'", null
                )
                if (fila2.moveToFirst()) {
                    do {
                        val nombre = fila2.getString(1)
                        val producto = fila2.getString(2)
                        val peso = fila2.getInt(3)
                        val lugar = fila2.getString(4)
                        val fecha = fila2.getString(5)
                        val id = fila2.getInt(6)

                        listaS.add(
                            Servicios(
                                id,
                                nombre,
                                producto,
                                peso,
                                lugar,
                                fecha
                            )
                        )
                    } while (fila2.moveToNext())

                }
                fila2.close()
            }
            fila.close()

        }
        return listaS
    }
}