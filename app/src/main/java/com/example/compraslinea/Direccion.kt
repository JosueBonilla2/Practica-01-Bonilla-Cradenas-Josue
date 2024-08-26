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

class Direccion: AppCompatActivity() {

    private lateinit var estado : Spinner
    private lateinit var calle : EditText
    private lateinit var cruce : EditText
    private lateinit var exterior : EditText
    private lateinit var municipio : EditText
    private lateinit var colonia : EditText
    private lateinit var cp : EditText
    private lateinit var indicaciones : EditText

    private lateinit var registrard : Button

    private var estadoSel : String = "Estados"

    private val CHANNEL_ID = "canal_notificacion"
    private val notificationId = 100

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direccion)

        createNotificationChannel()

        setSupportActionBar(findViewById(R.id.toolbar))

        estado = findViewById(R.id.sp_estado)

        calle = findViewById(R.id.txt_calle)
        cruce = findViewById(R.id.txt_cruce)
        exterior = findViewById(R.id.txt_num)
        municipio = findViewById(R.id.txt_municipio)
        colonia = findViewById(R.id.txt_colonia)
        cp = findViewById(R.id.txt_cp)
        indicaciones = findViewById(R.id.txt_indicaciones)

        registrard = findViewById(R.id.btn_registrard)

        registrard.setOnClickListener{notificacionAccion()}

        val lstestados = resources.getStringArray(R.array.estados)
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, lstestados)
        estado.adapter = adaptador
        estado.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                estadoSel = lstestados[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun notificacionAccion() {
        val estadoTexto = estado.selectedItem.toString()
        val calleTexto = calle.text.toString().trim()
        val cruceTexto = cruce.text.toString().trim()
        val exteriorTexto = exterior.text.toString().trim()
        val municipioTexto = municipio.text.toString().trim()
        val coloniaTexto = colonia.text.toString().trim()
        val cpTexto = cp.text.toString().trim()

        if (estadoTexto == "Estados" || calleTexto.isEmpty() || cruceTexto.isEmpty() || exteriorTexto.isEmpty() ||
            municipioTexto.isEmpty() || coloniaTexto.isEmpty() || cpTexto.isEmpty()) {
            showToast("Por favor, llena todos los campos obligatorios.")
            return
        }

        if (!exteriorTexto.matches("\\d{1,3}".toRegex())) {
            showToast("El número exterior debe ser un número de hasta 3 dígitos.")
            return
        }

        if (!cpTexto.matches("\\d{5}".toRegex())) {
            showToast("El código postal debe ser un número de 5 dígitos.")
            return
        }

        val accionSi = Intent(this, Informacion::class.java).apply {
            putExtra("accion", 1)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val accionNo = Intent(this, Compra::class.java).apply {
            putExtra("accion", 2)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntentSi: PendingIntent = PendingIntent.getActivity(this, 0, accionSi, PendingIntent.FLAG_IMMUTABLE)
        val pendingIntentNo: PendingIntent = PendingIntent.getActivity(this, 0, accionNo, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Registro Completado")
            .setContentText("¿Desea concretar esta compra?")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.ic_accept, "SI", pendingIntentSi)
            .addAction(R.drawable.ic_decline, "NO", pendingIntentNo)
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