package com.crm.cronmetro;
import android.widget.Button;
import android.view.View;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ChronometerService chronometerService;
    private boolean isServiceBound = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ChronometerService.LocalBinder binder = (ChronometerService.LocalBinder) service;
            chronometerService = binder.getService();
            isServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Iniciar el servicio
        Intent serviceIntent = new Intent(this, ChronometerService.class);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        // Configurar botones y texto
        Button startButton = findViewById(R.id.startButton);
        Button stopButton = findViewById(R.id.stopButton);
        //Button resetButton = findViewById(R.id.resetButton);
        final TextView timeTextView = findViewById(R.id.timeTextView);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar el cronómetro
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Detener el cronómetro
            }
        });

        /*resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reiniciar el cronómetro
            }
        });*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isServiceBound) {
            unbindService(serviceConnection);
        }
    }
}
