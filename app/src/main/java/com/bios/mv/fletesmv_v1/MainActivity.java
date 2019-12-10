package com.bios.mv.fletesmv_v1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.bios.mv.fletesmv_v1.bd.Constantes;
import com.bios.mv.fletesmv_v1.ui.configuraciones.ConfiguracionesActivity;
import com.bios.mv.fletesmv_v1.ui.login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Mantengo activa la pantalla para que no tengan que estar presionando para que no se vaya.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Obtengo mi navigation view (la barra de abajo).
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Construyo la bottom app bar con mis fragmentos y la seteo.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_transporte, R.id.navigation_notifications, R.id.navigation_logout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onBackPressed() {
        // no dejar apretar para atrás (para no volver al login o registro).
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Agrego los 3 puntitos para el menú de configuraciones y cerrar sesión.
        getMenuInflater().inflate(R.menu.configuraciones_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Evento que se ejecuta cuando se selecciona alguna de las opciones del menú.
        switch (item.getItemId()){
            case R.id.Configuraciones:
                // Estoy en el caso de que presionaron Configuraciones.
                // Abre la actividad que contiene las Configuraciones de la aplicación.

                // Creo el intent de la actividad de configuraciones.
                Intent intent = new Intent(this, ConfiguracionesActivity.class);

                // Empiezo la actividad del intent.
                startActivity(intent);
                return true;

            case R.id.CerrarSesion:
                // Estoy en el caso de que presionaron Cerrar Sesión.
                // Abre un diálogo de confirmación.

                AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                        // Seteo título, mensaje, ícono y acciones positiva y negativa.
                        .setTitle("Confirmar Cierre de Sesión")
                        .setMessage("¿Confirma que desea cerrar sesión?")
                        .setIcon(R.drawable.confirmar)

                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Si confirma llamo al procedimiento que Cierra Sesión y cierro el diálogo.
                                CerrarSesion();
                                dialog.dismiss();
                            }

                        })

                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Si no confirma solamente cierro el diálogo.
                                dialog.dismiss();
                            }
                        })
                        .create();
                myQuittingDialogBox.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void CerrarSesion() {
        // Guardo en el parámetro de Sesión el Usuario el String vacío porque ya no tengo usuario logueado.
        Procedimientos.setVariableSesionString(
                this,
                Constantes.NOMBRE,
                Constantes.CODIGO_USUARIO,
                "");

        // Creo el intent de la actividad de login.
        Intent intent = new Intent(this, LoginActivity.class);
        // Empiezo la actividad del intent.
        startActivity(intent);
    }
}
