package com.example.loginregister;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button button_Tienda, button_perfil, button_insignias, button_language;

    TextView username;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);
        button_Tienda = findViewById(R.id.btn_tienda);
        button_perfil = findViewById(R.id.btn_perfil);
        button_insignias = findViewById(R.id.btn_insignias);
        button_language = findViewById(R.id.btn_language);
        username = findViewById(R.id.username);



        sharedPreferences = getSharedPreferences("user_info",MODE_PRIVATE);
        username.setText(sharedPreferences.getString("username",null));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));
        button_Tienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,TiendaActivity.class));
            }
        });

        button_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PerfilActivity.class));
            }
        });

        button_insignias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Ej1Minimo2Activity.class));
            }
        });
        //Boton cambiar idioma
        button_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                changeLanguageDialog();
            }
        });
    }
    //Funcion para camniar de idioma
    private void changeLanguageDialog(){
        final String[] listLanguages = {"Español", "English"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setSingleChoiceItems(listLanguages, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        setLocale("Es");
                        setLang(sharedPreferences.getString("mail", null), "Es");
                        recreate();
                        break;
                    case 1:
                        setLocale("En");
                        setLang(sharedPreferences.getString("mail", null), "En");
                        recreate();
                        break;
                }

                dialogInterface.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
    //Cambio en las variables locales para el idioma
    private void setLocale(String lang){
        SharedPreferences.Editor editor = getSharedPreferences("user_info", MODE_PRIVATE).edit();
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        editor.putString("lang", lang);
        editor.apply();
    }
    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("user_info",MODE_PRIVATE);
        LoginRequest log = new LoginRequest();
        String lang = prefs.getString("lang", null);
        log.setEmail(prefs.getString("mail", null));
        log.setPassword(prefs.getString("password", null));
        getLanguage(log);
        setLocale(lang);
    }
    public void getLanguage(LoginRequest loginRequest){
        Call<UsuarioResponse> usuarioResponseCall = ApiClient.getService().loginUser(loginRequest);
        usuarioResponseCall.enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                if (response.isSuccessful()){
                    String message = "Successful";
                    Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String langC = sharedPreferences.getString("lang", null);
                    editor.putString("lang", response.body().getLang());
                    editor.commit();
                    String lang = sharedPreferences.getString("lang", null);
                    setLocale(lang);
                    if(langC != lang){
                        recreate();
                    }
                } else {
                    String message = "An error occurred";
                    Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
            }
        });
    }
    private void setLang(String mail,String newLang) {
        Call<Void> setLangResponse = ApiClient.getService().setLang(mail, newLang);
        setLangResponse.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    String message = "Successful";
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    String message = "An error occurred";
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                String message = "Error: " + t.getMessage(); // Agrega esta línea para obtener más detalles
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}