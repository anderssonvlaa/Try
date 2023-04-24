package com.example.atry;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.atry.API.ConexionAPI;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ModificarTarea extends AppCompatActivity {
    Button btnCancelar, btnActualizar;
    EditText txtNombreActualizar, txtDescripcionActualizar;
    private ConexionAPI objAPI = new ConexionAPI();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_tarea);

        btnActualizar = findViewById(R.id.btnActualizar);
        btnCancelar = findViewById(R.id.btnCancelar);

        txtDescripcionActualizar = findViewById(R.id.txtDescripcionActualizar);
        txtNombreActualizar = findViewById(R.id.txtNombreActualizar);

        txtNombreActualizar.setText(getIntent().getExtras().getString("nombre"));
        txtDescripcionActualizar.setText(getIntent().getExtras().getString("descripcion"));
        btnActualizar.setOnClickListener(view -> {
            actualizarTarea();
        });

        btnCancelar.setOnClickListener(view -> {
            finish();
        });
    }

    private void actualizarTarea(){
        RequestQueue queue = Volley.newRequestQueue(ModificarTarea.this);
        try {
            StringRequest request = new StringRequest(Request.Method.POST, objAPI.URL, response -> {
                try {
                    JSONObject json = new JSONObject(response);
                    String result = json.getString("resultado");
                    Toast.makeText(ModificarTarea.this, result, Toast.LENGTH_SHORT).show();
                }catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
            }, error -> {
                Toast.makeText(ModificarTarea.this, "Error "+error.getMessage(), Toast.LENGTH_LONG).show();
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    LocalDateTime fechaActual = LocalDateTime.now();
                    DateTimeFormatter formatoFechaHora = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String fechaHoraActual = fechaActual.format(formatoFechaHora);

                    Map<String, String> params = new HashMap<>();
                    params.put("accion", "actualizar");
                    params.put("id_tarea", getIntent().getExtras().getString("id_tarea"));
                    params.put("nombre", txtNombreActualizar.getText().toString());
                    params.put("descripcion", txtDescripcionActualizar.getText().toString());
                    params.put("fecha", fechaHoraActual);
                    params.put("status", "Pendiente");
                    return params;
                }
            };

            queue.add(request);
        }catch (Exception e) {
            Toast.makeText(ModificarTarea.this, "Error en tiempo de ejecución: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}