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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class IniciarTrasladoActivity extends AppCompatActivity {

    private TextView titulo;
    private TextInputLayout marca;
    private TextInputLayout modelo;
    private TextInputLayout matricula;
    private TextInputLayout chofer;
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
                iniciarTraslado(view);
            }
        });

        marca.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validarMarca();
                }
            }
        });

        modelo.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validarModelo();
                }
            }
        });

        matricula.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validarMatricula();
                }
            }
        });

        chofer.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validarChofer();
                }
            }
        });

    }

    private void iniciarTraslado(View view){
        if (validarMarca() & validarModelo() & validarMatricula() & validarChofer()) {

            String URL_transporte_info = Constantes.URL_TRANSPORTES+"/"+idTraslado;

            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());

            Vehiculo vehiculo = new Vehiculo();

            vehiculo.setMarca(marca.getEditText().getText().toString());
            vehiculo.setModelo(modelo.getEditText().getText().toString());
            vehiculo.setMatricula(matricula.getEditText().getText().toString());
            vehiculo.setChofer(chofer.getEditText().getText().toString());

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
    }

    private boolean validarMarca() {
        if (marca.getEditText().getText().toString().isEmpty()) {
            marca.setError("Debe ingresar la marca");

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            return false;
        }

        marca.setError(null);

        return true;
    }

    private boolean validarModelo() {
        if (modelo.getEditText().getText().toString().isEmpty()) {
            modelo.setError("Debe ingresar el modelo");

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            return false;
        }

        modelo.setError(null);

        return true;
    }

    private boolean validarMatricula() {
        if (matricula.getEditText().getText().toString().isEmpty()) {
            matricula.setError("Debe ingresar la matrícula");

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            return false;
        }

        matricula.setError(null);

        return true;
    }

    private boolean validarChofer() {
        if (chofer.getEditText().getText().toString().isEmpty()) {
            chofer.setError("Debe ingresar el chofer");

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            return false;
        }

        chofer.setError(null);

        return true;
    }

    private void manejarError(VolleyError error) {
        Log.e(Constantes.TAG_LOG,"error: "+error.getMessage());
        Toast.makeText(this,"Error guardando datos del transporte",Toast.LENGTH_LONG).show();

        finish();
    }

    private void manejarRespuesta() {
        finish();
    }

}
