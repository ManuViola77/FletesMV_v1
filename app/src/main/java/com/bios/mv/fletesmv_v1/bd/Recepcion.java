package com.bios.mv.fletesmv_v1.bd;

public class Recepcion {

    String nombre_receptor;
    String observacion;
    String fecha;
    double latitud;
    double longitud;

    public String getNombre_receptor() {
        return nombre_receptor;
    }

    public void setNombre_receptor(String nombre_receptor) {
        this.nombre_receptor = nombre_receptor;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
