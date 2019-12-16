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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class RegistroActivity extends AppCompatActivity {

    private RequestQueue requestQueue;

    private TextInputLayout nombre;
    private TextInputLayout apellido;
    private TextInputLayout mail;
    private TextInputLayout contrasena;

    private Button boton_confirmar_registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // oculta teclado para que no este apenas se entra a la activity
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        nombre = findViewById(R.id.nombre);
        apellido = findViewById(R.id.apellido);
        mail = findViewById(R.id.mail);
        contrasena = findViewById(R.id.contrasena);

        // oculta teclado para que no este apenas se entra a la activity
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        boton_confirmar_registro = findViewById(R.id.boton_confirmar_registro);
        boton_confirmar_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar(view);
            }
        });

        nombre.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validarNombre();
                }
            }
        });

        apellido.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validarApellido();
                }
            }
        });

        mail.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validarMail();
                }
            }
        });

        contrasena.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validarContrasena();
                }
            }
        });
    }

    private void registrar(View view) {
        if (Procedimientos.tieneConexionInternet(this)) {
            if (validarNombre() & validarApellido() & validarMail() & validarContrasena()) {
                boton_confirmar_registro.setEnabled(false);

                // Inicializo cola de solicitudes
                requestQueue = Volley.newRequestQueue(getApplicationContext());

                Usuario usuario = new Usuario();
                usuario.setName(nombre.getEditText().getText().toString());
                usuario.setLast_name(apellido.getEditText().getText().toString());
                usuario.setEmail(mail.getEditText().getText().toString());
                usuario.setPassword(contrasena.getEditText().getText().toString());

                JSONObject parameters = UsuarioConverter.convertUsuarioToJSONOBject(usuario);

                // Crear solicitud
                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.URL_REGISTRO,
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
        } else {
            Toast.makeText(this,
                    "No se pudo realizar el registro debido a que no hay internet",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void manejarRespuesta(JSONObject respuesta) {

        Usuario usuario = UsuarioConverter.convertUsuario(respuesta);

        Procedimientos.setVariableSesionString(
                this,
                Constantes.NOMBRE,
                Constantes.CODIGO_USUARIO,
                usuario.getName());

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void manejarError(VolleyError volleyError) {
        Toast.makeText(this,
                "No se pudo realizar el registro, "+volleyError.getMessage(),
                Toast.LENGTH_LONG).show();
    }

    private boolean validarNombre() {
        if (nombre.getEditText().getText().toString().isEmpty()) {
            nombre.setError("Debe ingresar el nombre");

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            return false;
        }

        nombre.setError(null);

        return true;
    }

    private boolean validarApellido() {
        if (apellido.getEditText().getText().toString().isEmpty()) {
            apellido.setError("Debe ingresar el apellido");

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            return false;
        }

        apellido.setError(null);

        return true;
    }

    private boolean validarMail() {
        if (mail.getEditText().getText().toString().isEmpty()) {
            mail.setError("Debe ingresar el mail");

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            return false;
        }

        mail.setError(null);

        return true;
    }

    private boolean validarContrasena() {
        if (contrasena.getEditText().getText().toString().isEmpty()) {
            contrasena.setError("Debe ingresar la contraseña");

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            return false;
        }

        contrasena.setError(null);

        return true;
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
