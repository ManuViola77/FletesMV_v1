package com.bios.mv.fletesmv_v1.ui.transporte;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bios.mv.fletesmv_v1.R;
import com.bios.mv.fletesmv_v1.bd.Constantes;
import com.bios.mv.fletesmv_v1.bd.Vehiculo;
import com.bios.mv.fletesmv_v1.bd.converter.VehiculoConverter;

import org.json.JSONObject;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class IniciarTrasladoActivity extends AppCompatActivity {

    private TextView titulo;
    private TextView marca;
    private TextView modelo;
    private TextView matricula;
    private TextView chofer;
    private Button boton_iniciar_traslado;

    private int idTraslado;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_iniciar_traslado);

        // Mantengo activa la pantalla para que no tengan que estar presionando para que no se vaya.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // oculta teclado para que no este apenas se entra a la activity
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idTraslado = extras.getInt(Constantes.extra_iniciar_traslado);
        }

        titulo = findViewById(R.id.iniciar_traslado_titulo);
        titulo.setText(getResources().getString(R.string.iniciar_traslado_titulo,String.valueOf(idTraslado)));

        marca = findViewById(R.id.iniciar_traslado_vehiculo_marca);
        modelo = findViewById(R.id.iniciar_traslado_vehiculo_modelo);
        matricula = findViewById(R.id.iniciar_traslado_vehiculo_matricula);
        chofer = findViewById(R.id.iniciar_traslado_vehiculo_chofer);

        boton_iniciar_traslado = findViewById(R.id.boton_iniciar_traslado);

        boton_iniciar_traslado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String URL_transporte_info = Constantes.URL_TRANSPORTES+"/"+idTraslado;

                RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());

                Vehiculo vehiculo = new Vehiculo();

                vehiculo.setMarca(marca.getText().toString());
                vehiculo.setModelo(modelo.getText().toString());
                vehiculo.setMatricula(matricula.getText().toString());
                vehiculo.setChofer(chofer.getText().toString());

                JSONObject parameters = VehiculoConverter.convertVehiculoToJSONOBject(vehiculo);

                JsonObjectRequest solicitud = new JsonObjectRequest(
                        Request.Method.POST,
                        URL_transporte_info,
                        parameters,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                manejarRespuesta();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                manejarError(error);

                            }
                        }
                );

                requestQueue.add(solicitud);

            }
        });

    }

    private void manejarError(VolleyError error) {
        Log.e(Constantes.TAG_LOG,error.getMessage());
        Toast.makeText(this,"Error obteniendo datos del transporte",Toast.LENGTH_LONG).show();

        finish();
    }

    private void manejarRespuesta() {
        finish();
    }

}
