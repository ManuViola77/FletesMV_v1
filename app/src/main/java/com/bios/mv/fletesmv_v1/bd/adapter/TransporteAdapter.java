package com.bios.mv.fletesmv_v1.bd.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bios.mv.fletesmv_v1.R;
import com.bios.mv.fletesmv_v1.bd.Transporte;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransporteAdapter extends
        RecyclerView.Adapter<TransporteAdapter.TransporteViewHolder> {

    private TransporteSelectedListener transporteSelectedListener;

    private List<Transporte> transportes = new ArrayList<>();

    @NonNull
    @Override
    public TransporteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater =
                LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.list_item_transporte,
                parent, false);

        return new TransporteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransporteViewHolder holder, int position) {
        final Transporte transporte = transportes.get(position);

        holder.id.setText(String.valueOf(transporte.getId()));
        //holder.estado.setText(transporte.getEstado());
        //holder.fecha.setText(transporte.getFecha());
        holder.origen_direccion.setText(transporte.getOrigen_direccion());
        //holder.origen_latitud.setText(String.valueOf(transporte.getOrigen_latitud()));
        //holder.origen_longitud.setText(String.valueOf(transporte.getOrigen_longitud()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transporteSelectedListener != null) {
                    transporteSelectedListener.onTransporteSelected(transporte);
                }
            }
        });

        int color = 0;
        switch (transporte.getEstado()) {
            case "pendiente":
                color = holder.itemView.getResources().getColor(R.color.estado_pendiente);
                break;

            case "iniciado":
                color = holder.itemView.getResources().getColor(R.color.estado_iniciado);
                break;

            case "cargando":
                color = holder.itemView.getResources().getColor(R.color.estado_cargando);
                break;

            case "viajando":
                color = holder.itemView.getResources().getColor(R.color.estado_viajando);
                break;

            case "descargado":
                color = holder.itemView.getResources().getColor(R.color.estado_descargado);
                break;

            case "finalizado":
                color = holder.itemView.getResources().getColor(R.color.estado_finalizado);
                break;

        }

        holder.relativeLayout.setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return transportes.size();
    }

    public void setTransportes(List<Transporte> transportes) {
       Collections.sort(transportes);
        this.transportes = transportes;
    }

    public void setTransporteSelectedListener(TransporteSelectedListener transporteSelectedListener) {
        this.transporteSelectedListener = transporteSelectedListener;
    }

    public interface TransporteSelectedListener {
        void onTransporteSelected(Transporte transporte);
    }

    static class TransporteViewHolder
            extends RecyclerView.ViewHolder {

        TextView id;
        TextView estado;
        TextView fecha;
        TextView origen_direccion;
        TextView origen_latitud;
        TextView origen_longitud;

        RelativeLayout relativeLayout;
        public TransporteViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.transporte_id);
            //estado = itemView.findViewById(R.id.transporte_estado);
            //fecha = itemView.findViewById(R.id.transporte_fecha);
            origen_direccion = itemView.findViewById(R.id.transporte_origen_direccion);
            //origen_latitud = itemView.findViewById(R.id.transporte_origen_latitud);
           //origen_longitud = itemView.findViewById(R.id.transporte_origen_longitud);

            relativeLayout = itemView.findViewById(R.id.transporte_rl);
        }
    }

}

