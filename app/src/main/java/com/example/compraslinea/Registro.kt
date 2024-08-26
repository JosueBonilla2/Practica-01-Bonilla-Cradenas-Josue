package com.example.compraslinea

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class Registro : AppCompatActivity() {

    private val textTitle = "Datos Bancarios Registrados Correctamente"

    private lateinit var banco : Spinner
    private lateinit var nombre : EditText
    private lateinit var bin : EditText
    private lateinit var fecha : EditText
    private lateinit var cvv : EditText

    private lateinit var registrar : Button

    private var bancoSel : String = "BANCOS"
    private val usuarios = arrayOf("BBVA", "Banamex", "Santander", "Banorte", "HSBC")

    private val CHANNEL_ID = "canal_notificacion"
    private val notificationId = 100

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        createNotificationChannel()

        banco = findViewById(R.id.sp_banco)
        nombre = findViewById(R.id.txt_nombre)
        bin = findViewById(R.id.txt_bin)
        fecha = findViewById(R.id.txt_fecha)
        cvv = findViewById(R.id.txt_cvv)

        setSupportActionBar(findViewById(R.id.toolbar))

        registrar = findViewById(R.id.btn_registrat)

        registrar.setOnClickListener{notificacionToque()}

        val lstbancos = resources.getStringArray(R.array.lst_bancos)
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, lstbancos)
        banco.adapter = adaptador
        banco.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                bancoSel = lstbancos[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun notificacionToque() {
        val nombreTexto = nombre.text.toString().trim()
        val binTexto = bin.text.toString().trim()
        val fechaTexto = fecha.text.toString().trim()
        val cvvTexto = cvv.text.toString().trim()
        val bancoSeleccionado = bancoSel

        if (nombreTexto.isEmpty() || binTexto.isEmpty() || fechaTexto.isEmpty() || cvvTexto.isEmpty() || bancoSeleccionado == "BANCOS") {
            showToast("Por favor, llena todos los campos.")
            return
        }

        if (!cvvTexto.matches("\\d{3}".toRegex())) {
            showToast("El CVV debe ser una cadena de 3 dígitos numéricos.")
            return
        }

        if (!binTexto.matches("\\d{16}".toRegex())) {
            showToast("El BIN debe ser una cadena de 16 dígitos numéricos.")
            return
        }

        if (!fechaTexto.matches("\\d{2}/\\d{2}".toRegex())) {
            showToast("La fecha debe estar en formato MM/YY.")
            return
        } else {
            val (mes, año) = fechaTexto.split("/").map { it.toInt() }

            if (mes !in 1..12) {
                showToast("El mes debe estar entre 01 y 12.")
                return
            }

            if (año + 2000 > 2030) {
                showToast("El año debe ser menor o igual a 2030.")
                return
            }
        }

        val intent = Intent(this, Direccion::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(textTitle)
            .setContentText("Toque la notificación para continuar con el registro de direccion")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(notificationId, builder.build())
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "My Channel"
        val descriptionText = "Descripcion del canal"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, Compra::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        return super.onOptionsItemSelected(item)
    }
}