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
import com.bios.mv.fletesmv_v1.Procedimientos;
import com.bios.mv.fletesmv_v1.R;
import com.bios.mv.fletesmv_v1.bd.Constantes;
import com.bios.mv.fletesmv_v1.bd.Recepcion;
import com.bios.mv.fletesmv_v1.bd.converter.RecepcionConverter;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;


import androidx.appcompat.app.AppCompatActivity;

public class FinalizarTrasladoActivity extends AppCompatActivity {

    private TextView titulo;
    private TextInputLayout nombre_receptor;
    private TextInputLayout observacion;
    private Button boton_finalizar_traslado;

    private int idTraslado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_traslado);

        // Mantengo activa la pantalla para que no tengan que estar presionando para que no se vaya.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // oculta teclado para que no este apenas se entra a la activity
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idTraslado = extras.getInt(Constantes.extra_finalizar_traslado);
        }

        titulo = findViewById(R.id.finalizar_traslado_titulo);
        titulo.setText(getResources().getString(R.string.finalizar_traslado_titulo,String.valueOf(idTraslado)));

        nombre_receptor = findViewById(R.id.finalizar_traslado_nombre_receptor);
        observacion = findViewById(R.id.finalizar_traslado_observacion);

        boton_finalizar_traslado = findViewById(R.id.boton_finalizar_traslado);
        boton_finalizar_traslado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizarTraslado(v);
            }
        });

        nombre_receptor.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validarNombreReceptor(nombre_receptor);
                }
            }
        });

        observacion.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validarObservacion(observacion);
                }
            }
        });
    }

    public void finalizarTraslado(View view) {
        if (Procedimientos.tieneConexionInternet(this)) {
            if (validarNombreReceptor(nombre_receptor) &
                    validarObservacion(observacion)) {

                String URL_transporte_info = Constantes.URL_TRANSPORTES + "/" + idTraslado;

                RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());

                Recepcion recepcion = new Recepcion();

                recepcion.setNombre_receptor(nombre_receptor.getEditText().getText().toString());
                recepcion.setObservacion(observacion.getEditText().getText().toString());
                recepcion.setFecha(Procedimientos.getFechaActual());
                recepcion.setLongitud(-34);
                recepcion.setLatitud(34);

                JSONObject parameters = RecepcionConverter.convertRecepcionToJSONOBject(recepcion);

                Log.i(Constantes.TAG_LOG, "parameters: " + parameters);

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
        } else {
            Toast.makeText(this,"No se puede finalizar el traslado debido a que no hay internet",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void manejarError(VolleyError error) {
        Log.e(Constantes.TAG_LOG,"error: "+error.getMessage());
        Toast.makeText(this,"Error guardando datos del transporte",Toast.LENGTH_LONG).show();

        finish();
    }

    private void manejarRespuesta() {
        finish();
    }

    private boolean validarNombreReceptor(TextInputLayout editText) {

        if (editText.getEditText().getText().toString().isEmpty()) {
            editText.setError("Debe ingresar el nombre del receptor");

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            return false;
        }

        editText.setError(null);

        return true;
    }

    private boolean validarObservacion(TextInputLayout editText) {

        if (editText.getEditText().getText().toString().isEmpty()) {
            editText.setError("Debe ingresar una observación");

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            return false;
        }

        editText.setError(null);

        return true;
    }

}

