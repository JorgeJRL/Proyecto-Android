package com.example.proyectoandroid

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class Pedidos : AppCompatActivity() {
    var imagen: ImageButton? = null
    var datapicker: DatePicker? = null
    private val SPEECH_REQUEST_CODE = 1
    lateinit var nombre: EditText
    lateinit var producto: EditText
    lateinit var peso: EditText
    lateinit var lugar: EditText
    lateinit var fecha: EditText
    private var dato: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pedidos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        nombre = findViewById(R.id.nombreCliente)
        producto = findViewById(R.id.producto)
        peso = findViewById(R.id.peso)
        lugar = findViewById(R.id.lugar)
        fecha = findViewById(R.id.fecha)

        val bundle = intent.extras
        dato = bundle?.getString("usuario")
        imagen = findViewById(R.id.imagen)
        datapicker = findViewById(R.id.dpFecha)
        val enviar = findViewById<Button>(R.id.enviar)
        val sonido = findViewById<ImageButton>(R.id.sonidoP)
        val borrar = findViewById<Button>(R.id.borrar)




        fecha?.setText(getFecha())

        datapicker?.setOnDateChangedListener { datapicker, anio, mes, dia ->
            fecha?.setText(getFecha())

        }
        sonido.setOnClickListener() {

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )

                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

                putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora para transcribir tu voz")
            }
            try {
                startActivityForResult(intent, SPEECH_REQUEST_CODE)
            } catch (e: Exception) {
                Toast.makeText(
                    this, "El reconocimiento de voz no está disponible",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        borrar.setOnClickListener(){
            borrarTodo()
        }


        enviar.setOnClickListener() {
            enviarDatos(dato!!)
        }

    }

    fun getFecha(): String {
        var dia = datapicker?.dayOfMonth.toString().padStart(2, '0')
        var mes = (datapicker!!.month + 1).toString().padStart(2, '0')
        var anio = datapicker?.year.toString().padStart(4, '0')

        return "$anio-$mes-$dia"
    }

    fun borrarTodo() {
        nombre.setText(" ")
        producto.setText(" ")
        peso.setText(" ")
        lugar.setText(" ")
        fecha.setText(" ")
    }

    fun getIdCliente(dato: String): Int {
        val admin = AdminBD(this, "usuarios", null, 1)
        val bd = admin.writableDatabase
        val fila = bd.rawQuery("select id from usuarios where nombre = '${dato}'", null)
        var resultado = ""
        if (fila.moveToFirst()) {
            resultado = fila.getString(fila.getColumnIndexOrThrow("id"))

        }
        fila.close()

        bd.close()
        return resultado.toInt()
    }

    fun enviarDatos(dato: String) {
        val mensaje = StringBuffer()
        if (nombre.text.isEmpty()) {
            mensaje.append("Rellena el nombre" + '\n')
        }
        if (producto.text.isEmpty()) {
            mensaje.append("Rellena el producto" + '\n')
        }
        if (peso.text.isEmpty()) {
            mensaje.append("Rellena el peso" + '\n')
        }
        if (lugar.text.isEmpty()) {
            mensaje.append("Rellena el lugar" + '\n')
        }


        if (mensaje.isEmpty()) {
            val admin = AdminBD(this, "servicios", null, 1)
            val bd = admin.writableDatabase
            val registro = ContentValues()


            var id = getIdCliente(dato)

            registro.put("nombreCliente", nombre.text.toString())
            registro.put("producto", producto.text.toString())
            registro.put("peso", peso.text.toString())
            registro.put("lugar", lugar.text.toString())
            registro.put("fechaEntrega", fecha.text.toString())
            registro.put("idCliente", id)

            var resultado = bd.insert("servicios", null, registro)
            bd.close()

            nombre.setText("")
            producto.setText("")
            peso.setText("")
            lugar.setText("")




            if (resultado > 0) {
                Toast.makeText(this, "Pedido realizado correctamente", Toast.LENGTH_SHORT)
                    .show()

                val intento = Intent(this, Bienvenida::class.java)
                intento.putExtra("usuario", dato)
                startActivity(intento)

            } else {
                Toast.makeText(this, "No se pudo insertar", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(this, "Di 'realizar pedido' para enviar el pedido", Toast.LENGTH_SHORT).show()
        Toast.makeText(this, "Di 'borrar todo' para borrar el pedido", Toast.LENGTH_SHORT).show()
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            result?.let {

                if (it[0].equals("realizar pedido")) {
                    enviarDatos(dato!!)
                } else if (it[0].equals("borrar todo")){
                    borrarTodo()
                }
            }

        }

    }
}