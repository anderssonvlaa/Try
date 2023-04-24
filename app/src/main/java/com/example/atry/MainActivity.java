package com.example.atry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.atry.API.ConexionAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText filtro;
    Button btnBuscar, btnAgregar;
    RecyclerView lista;
    ConexionAPI objAPI = new ConexionAPI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filtro = findViewById(R.id.txtFiltro);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnAgregar = findViewById(R.id.btnAgregar);
        lista = findViewById(R.id.rcvTareas);
        lista.setLayoutManager(new LinearLayoutManager(this));

        listarTareas();

        btnAgregar.setOnClickListener(view -> {
            Intent intent = new Intent(this, AgregarTarea.class);
            startActivity(intent);
        });

        btnBuscar.setOnClickListener(view -> {
            if (filtro.getText() != null) {
                listarTareas();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        listarTareas();
    }

    private void listarTareas() {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        try {
            StringRequest request = new StringRequest(Request.Method.POST, objAPI.URL, response -> {
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray jsonArrayTareas = json.getJSONArray("resultado");
                    TareasAdapter adapter = new TareasAdapter(jsonArrayTareas, this);
                    lista.setAdapter(adapter);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
            }, error -> {
                Toast.makeText(this, "Error " + error.getMessage(), Toast.LENGTH_LONG).show();
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("accion", "listar");
                    params.put("filtro", filtro.getText().toString());
                    return params;
                }
            };

            queue.add(request);
        } catch (Exception e) {
            Toast.makeText(this, "Error en tiempo de ejecución" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public class TareasAdapter extends RecyclerView.Adapter<TareasAdapter.ViewHolder> implements View.OnClickListener {

        private JSONArray jsonArrayTareas;
        private View.OnClickListener clickListener;
        private Context context;

        public TareasAdapter(JSONArray jsonArrayContactos, Context context) {
            this.jsonArrayTareas = jsonArrayContactos;
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onClick(view);
            }
        }

        @NonNull
        @Override
        public TareasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_tarea, parent, false);
            view.setOnClickListener(this);
            return new TareasAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TareasAdapter.ViewHolder holder, int position) {
            try {
                JSONObject jsonContacto = jsonArrayTareas.getJSONObject(position);
                final String id_tarea = jsonContacto.getString("id_tarea");
                final String nombre = jsonContacto.getString("nombre");
                final String descripcion = jsonContacto.getString("descripcion");
                final String fecha = jsonContacto.getString("fecha");
                final String status = jsonContacto.getString("status");

                holder.tvNombre.setText("Nombre de la tarea: " + nombre);
                holder.tvDescripcion.setText("Descripcion: " + descripcion);
                holder.tvFecha.setText("Fecha: " + fecha);
                holder.tvStatus.setText("Estado: " + status);

                holder.btnVerTarea.setOnClickListener(view -> {
                    Intent intent = new Intent(context, ModificarTarea.class);
                    intent.putExtra("id_tarea", id_tarea);
                    intent.putExtra("nombre", nombre);
                    intent.putExtra("descripcion", descripcion);
                    intent.putExtra("fecha", fecha);
                    intent.putExtra("status", status);
                    context.startActivity(intent);
                });

                holder.btnEliminarTarea.setOnClickListener(view -> {
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    try {
                        StringRequest request = new StringRequest(Request.Method.POST, objAPI.URL, response -> {
                            try {
                                JSONObject json = new JSONObject(response);
                                String result = json.getString("resultado");
                                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                            }catch (Exception e) {
                                Log.e("Error", e.getMessage());
                            }
                        }, error -> {
                            Toast.makeText(MainActivity.this, "Error "+error.getMessage(), Toast.LENGTH_LONG).show();
                        }){
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("accion", "eliminar");
                                params.put("id_tarea", id_tarea);;
                                return params;
                            }
                        };

                        queue.add(request);
                        listarTareas();
                    }catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Error en tiempo de ejecución: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                holder.btnTerminar.setOnClickListener(view -> {
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    try {
                        StringRequest request = new StringRequest(Request.Method.POST, objAPI.URL, response -> {
                            try {
                                JSONObject json = new JSONObject(response);
                                String result = json.getString("resultado");
                                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                            }catch (Exception e) {
                                Log.e("Error", e.getMessage());
                            }
                        }, error -> {
                            Toast.makeText(MainActivity.this, "Error "+error.getMessage(), Toast.LENGTH_LONG).show();
                        }){
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("accion", "actualizar");
                                params.put("id_tarea",id_tarea);
                                params.put("nombre", nombre);
                                params.put("descripcion", descripcion);
                                params.put("fecha", fecha);
                                params.put("status", "Finalizada");
                                return params;
                            }
                        };

                        queue.add(request);
                        listarTareas();
                    }catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Error en tiempo de ejecución: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (JSONException e) {
                Log.e("Error Json", e.getMessage());
            }
        }

        @Override
        public int getItemCount() {
            return (jsonArrayTareas != null) ? jsonArrayTareas.length() : 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvNombre;
            private TextView tvDescripcion;
            private TextView tvFecha;
            private TextView tvStatus;
            private Button btnVerTarea;
            private Button btnEliminarTarea;

            private Button btnTerminar;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvNombre = itemView.findViewById(R.id.tvNombreTarea);
                tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
                tvFecha = itemView.findViewById(R.id.tvFecha);
                tvStatus = itemView.findViewById(R.id.tvStatus);
                btnVerTarea = itemView.findViewById(R.id.btnVerTarea);
                btnEliminarTarea = itemView.findViewById(R.id.btnEliminar);
                btnTerminar = itemView.findViewById(R.id.btnTerminar);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listarTareas();
    }
}