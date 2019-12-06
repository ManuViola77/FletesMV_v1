package com.bios.mv.fletesmv_v1.ui.registro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class RegistroActivity extends AppCompatActivity {

    private RequestQueue requestQueue;

    private EditText nombre;
    private EditText apellido;
    private EditText mail;
    private EditText contrasena;

    private Button boton_confirmar_registro;

    private static final String URL_REGISTRO = Constantes.getUrlRegistro();
    private static final String NOMBRE = Constantes.getNOMBRE();
    private static final String CODIGO_USUARIO = Constantes.getCodigoUsuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nombre = findViewById(R.id.nombre);
        apellido = findViewById(R.id.apellido);
        mail = findViewById(R.id.mail);
        contrasena = findViewById(R.id.contrasena);

        nombre.setFocusableInTouchMode(true);
        apellido.setFocusableInTouchMode(true);
        mail.setFocusableInTouchMode(true);
        contrasena.setFocusableInTouchMode(true);

        // oculta teclado para que no este apenas se entra a la activity
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        boton_confirmar_registro = findViewById(R.id.boton_confirmar_registro);
        boton_confirmar_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inicializo cola de solicitudes
                requestQueue = Volley.newRequestQueue(getApplicationContext());

                Usuario usuario = new Usuario();
                usuario.setName(nombre.getText().toString());
                usuario.setLast_name(apellido.getText().toString());
                usuario.setEmail(mail.getText().toString());
                usuario.setPassword(contrasena.getText().toString());

                //Log.i(Constantes.getTagLog(),"Usuario: "+usuario);

                JSONObject parameters = UsuarioConverter.convertUsuarioToJSONOBject(usuario);

                //Log.i(Constantes.getTagLog(),"parameters: "+parameters);

                //Log.i(Constantes.getTagLog(),"URL_REGISTRO: "+URL_REGISTRO);

                // Crear solicitud
                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        URL_REGISTRO,
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
        });
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
    protected void onResume() {
        super.onResume();
        nombre.clearFocus();
        apellido.clearFocus();
        mail.clearFocus();
        contrasena.clearFocus();
    }
}
