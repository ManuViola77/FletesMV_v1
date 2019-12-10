package com.bios.mv.fletesmv_v1.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bios.mv.fletesmv_v1.Procedimientos;
import com.bios.mv.fletesmv_v1.bd.Constantes;
import com.bios.mv.fletesmv_v1.R;
import com.bios.mv.fletesmv_v1.ui.login.LoginActivity;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String usuarioLogueado = Procedimientos.getVariableSesionString(view.getContext(),Constantes.NOMBRE,Constantes.CODIGO_USUARIO);

        if (usuarioLogueado.isEmpty()) {
            Intent intent = new Intent(view.getContext(), LoginActivity.class);
            startActivity(intent);
        } else {
            homeViewModel.setText("Bienvenido/a "+usuarioLogueado+"!");
        }
    }
}