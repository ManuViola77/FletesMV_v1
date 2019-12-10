package com.bios.mv.fletesmv_v1.ui.logout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bios.mv.fletesmv_v1.Procedimientos;
import com.bios.mv.fletesmv_v1.R;
import com.bios.mv.fletesmv_v1.bd.Constantes;
import com.bios.mv.fletesmv_v1.ui.login.LoginActivity;

public class LogoutFragment extends Fragment {
    private LogoutViewModel logoutViewModel;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        logoutViewModel =
                ViewModelProviders.of(this).get(LogoutViewModel.class);
        root = inflater.inflate(R.layout.fragment_logout, container, false);

        final TextView textView = root.findViewById(R.id.text_logout);
        logoutViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        final Button boton_logout = root.findViewById(R.id.boton_logout);
        boton_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog myQuittingDialogBox = new AlertDialog.Builder(root.getContext())
                        //set message, title, and icon
                        .setTitle("Confirmar Cierre de Sesión")
                        .setMessage("¿Confirma que desea cerrar sesión?")
                        .setIcon(R.drawable.confirmar)

                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                CerrarSesion();
                                dialog.dismiss();
                            }

                        })

                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        })
                        .create();
                myQuittingDialogBox.show();
            }
        });
        return root;
    }

    private void CerrarSesion() {
        Procedimientos.setVariableSesionString(
                root.getContext(),
                Constantes.NOMBRE,
                Constantes.CODIGO_USUARIO,
                "");

        Intent intent = new Intent(root.getContext(), LoginActivity.class);
        startActivity(intent);
    }
}
