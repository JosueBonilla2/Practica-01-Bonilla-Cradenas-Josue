package com.example.compraslinea

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var usuario: EditText
    private lateinit var password: EditText

    private val usuariosYContraseñas = mapOf(
        "Josue" to "Josue123",
        "Ivan" to "Ivan123",
        "Jesus" to "Jesus123",
        "Andrea" to "Andrea123",
        "Josh" to "Josh123"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        password = findViewById(R.id.edt_password)
        usuario = findViewById(R.id.edt_usuario)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_ingresar -> ingresar()
            R.id.btn_salir -> salir()
        }
    }

    private fun ingresar() {
        val usuarioIngresado = usuario.text.toString()
        val passwordIngresado = password.text.toString()

        if (usuariosYContraseñas[usuarioIngresado] == passwordIngresado) {
            val intent = Intent(this, Compra::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun salir() {
        finish()
    }
}