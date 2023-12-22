package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ProgressBar;
import android.content.SharedPreferences;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DenunciaActivity extends AppCompatActivity{
    TextInputEditText editTextFecha, editTextTitulo, editTextMensaje;
    Button button_enviar;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncias);
        editTextFecha = findViewById(R.id.dateDen);
        editTextMensaje = findViewById(R.id.messageDen);
        editTextTitulo = findViewById(R.id.tituloDen);
        button_enviar = findViewById(R.id.btn_enviarDen);

        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);

        button_enviar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Denuncia denuncia = new Denuncia(editTextFecha.getText().toString(),editTextTitulo.getText().toString(), editTextMensaje.getText().toString(), sharedPreferences.getString("username", null));
                sendDenuncia(denuncia);
            }
        });
    }
    public void sendDenuncia(Denuncia denuncia){
        Call<Void> denunciaResponse = ApiClient.getService().addDenuncia(denuncia);
        denunciaResponse.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    String msg = "Success";
                    Toast.makeText(DenunciaActivity.this,msg,Toast.LENGTH_LONG).show();
                }else {
                    String msg = "Error";
                    Toast.makeText(DenunciaActivity.this,msg,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                String msg = "Error" + t.getMessage();
                Toast.makeText(DenunciaActivity.this,msg,Toast.LENGTH_LONG).show();
            }
        });
    }
}
