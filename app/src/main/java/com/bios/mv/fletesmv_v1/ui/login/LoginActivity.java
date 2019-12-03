package com.bios.mv.fletesmv_v1.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bios.mv.fletesmv_v1.MainActivity;
import com.bios.mv.fletesmv_v1.Procedimientos;
import com.bios.mv.fletesmv_v1.bd.Constantes;
import com.bios.mv.fletesmv_v1.R;
import com.bios.mv.fletesmv_v1.bd.Usuario;
import com.bios.mv.fletesmv_v1.bd.converter.UsuarioConverter;
import com.bios.mv.fletesmv_v1.ui.registro.RegistroActivity;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity  extends AppCompatActivity {

    private Button boton_registrarse;
    private Button boton_login;

    private EditText username;
    private EditText password;

    private RequestQueue requestQueue;

    private static final String URL_LOGIN = Constantes.getUrlLogin();
    private static final String NOMBRE = Constantes.getNOMBRE();
    private static final String CODIGO_USUARIO = Constantes.getCodigoUsuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        boton_registrarse = findViewById(R.id.boton_registrarse);
        boton_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarse(view.getContext());
            }
        });

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        username.setFocusableInTouchMode(true);
        password.setFocusableInTouchMode(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        boton_login = findViewById(R.id.boton_login);
        boton_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void registrarse(Context contexto) {
        Intent intent = new Intent(contexto, RegistroActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        username.clearFocus();
        password.clearFocus();
    }

    private void login(){
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        Usuario usuario = new Usuario();
        usuario.setEmail(username.getText().toString());
        usuario.setPassword(password.getText().toString());

        //Log.i(Constantes.getTagLog(),"Usuario: "+usuario);

        JSONObject parameters = UsuarioConverter.convertUsuarioToJSONOBject(usuario);

        //Log.i(Constantes.getTagLog(),"parameters: "+parameters);

        //Log.i(Constantes.getTagLog(),"URL_LOGIN: "+URL_LOGIN);

        // Crear solicitud
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL_LOGIN,
                parameters, // datos del post
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        manejarRespuesta(response);
                    } // codigo de success
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        manejarError(error);
                    } // codigo de error
                }
        );

        requestQueue.add(request);
    }

    private void manejarRespuesta(JSONObject respuesta) {

        Usuario usuario = UsuarioConverter.convertUsuario(respuesta);

        Procedimientos.setVariableSesionString(
                this,
                NOMBRE,
                CODIGO_USUARIO,
                usuario.getName());

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void manejarError(VolleyError volleyError) {
        Toast.makeText(this,
                "No se pudo realizar el registro, "+volleyError.getMessage(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        // no dejar apretar para atras
    }
}
