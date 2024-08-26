package com.example.compraslinea

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class Compra : AppCompatActivity() {

    private val CHANNEL_ID = "canal_notificacion"
    private val notificationId = 100


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compra)

        createNotificationChannel()

        val btn_comprar: View = findViewById(R.id.btn_comprar)
        val btn_comprar2: View = findViewById(R.id.btn_comprar2)
        val btn_comprar3: View = findViewById(R.id.btn_comprar3)

        btn_comprar.setOnClickListener{notificacionAccion()}
        btn_comprar2.setOnClickListener{notificacionAccion()}
        btn_comprar3.setOnClickListener{notificacionAccion()}

    }

    private fun notificacionAccion() {
        val accionSi = Intent(this, Registro::class.java).apply {
            putExtra("accion", 1)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val accionNo = Intent(this, MainActivity::class.java).apply {
            putExtra("accion", 2)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntentSi: PendingIntent = PendingIntent.getActivity(this, 0, accionSi, PendingIntent.FLAG_IMMUTABLE)
        val pendingIntentNo: PendingIntent = PendingIntent.getActivity(this, 0, accionNo, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Compra Detectada")
            .setContentText("Quiere ingresar sus datos bancarios pra hacer la compra?")
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
}
