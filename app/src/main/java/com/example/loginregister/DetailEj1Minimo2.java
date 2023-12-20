package com.example.loginregister;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailEj1Minimo2 extends AppCompatActivity {

    TextView nombre;

    //ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tienda);
        nombre = findViewById(R.id.Nombre);

        String nombreRecibido = getIntent().getExtras().getString("Nombre");
        nombre.setText("Description : " + nombreRecibido);
    }}