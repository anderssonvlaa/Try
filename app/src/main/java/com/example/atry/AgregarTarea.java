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

public class AgregarTarea extends AppCompatActivity {
    Button btnGuardar, btnRegresar;
    EditText txtNombre, txtDescripcion;
    private ConexionAPI objAPI = new ConexionAPI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarea);

        btnGuardar = findViewById(R.id.btnActualizar);
        btnRegresar = findViewById(R.id.btnCancelar);

        txtDescripcion = findViewById(R.id.txtDescripcionActualizar);
        txtNombre = findViewById(R.id.txtNombreActualizar);

        btnGuardar.setOnClickListener(view -> {
            guardarTarea();
        });

        btnRegresar.setOnClickListener(view -> {
            finish();
        });
    }

    private void guardarTarea() {
        RequestQueue queue = Volley.newRequestQueue(AgregarTarea.this);
        try {
            StringRequest request = new StringRequest(Request.Method.POST, objAPI.URL, response -> {
                try {
                    JSONObject json = new JSONObject(response);
                    String result = json.getString("resultado");
                    Toast.makeText(AgregarTarea.this, result, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
            }, error -> {
                Toast.makeText(AgregarTarea.this, "Error " + error.getMessage(), Toast.LENGTH_LONG).show();
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    LocalDateTime fechaActual = LocalDateTime.now();
                    DateTimeFormatter formatoFechaHora = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String fechaHoraActual = fechaActual.format(formatoFechaHora);
                    Map<String, String> params = new HashMap<>();
                    params.put("accion", "insertar");
                    params.put("nombre", txtNombre.getText().toString());
                    params.put("descripcion", txtDescripcion.getText().toString());
                    params.put("fecha", fechaHoraActual);
                    params.put("status", "Pendiente");
                    return params;
                }
            };

            queue.add(request);
            finish();
        } catch (Exception e) {
            Toast.makeText(AgregarTarea.this, "Error en tiempo de ejecución: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}