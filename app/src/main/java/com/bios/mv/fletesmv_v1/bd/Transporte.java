package com.bios.mv.fletesmv_v1.bd;

public class Transporte implements Comparable<Transporte>{

    private int id;
    private String estado;
    private String fecha;
    private String origen_direccion;
    private double origen_latitud;
    private double origen_longitud;

    private String destino_direccion;
    private double destino_latitud;
    private double destino_longitud;

    private Vehiculo vehiculo;
    private Recepcion recepcion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getOrigen_direccion() {
        return origen_direccion;
    }

    public void setOrigen_direccion(String origen_direccion) {
        this.origen_direccion = origen_direccion;
    }

    public double getOrigen_latitud() {
        return origen_latitud;
    }

    public void setOrigen_latitud(double origen_latitud) {
        this.origen_latitud = origen_latitud;
    }

    public double getOrigen_longitud() {
        return origen_longitud;
    }

    public void setOrigen_longitud(double origen_longitud) {
        this.origen_longitud = origen_longitud;
    }

    public String getDestino_direccion() {
        return destino_direccion;
    }

    public void setDestino_direccion(String destino_direccion) {
        this.destino_direccion = destino_direccion;
    }

    public double getDestino_latitud() {
        return destino_latitud;
    }

    public void setDestino_latitud(double destino_latitud) {
        this.destino_latitud = destino_latitud;
    }

    public double getDestino_longitud() {
        return destino_longitud;
    }

    public void setDestino_longitud(double destino_longitud) {
        this.destino_longitud = destino_longitud;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Recepcion getRecepcion() {
        return recepcion;
    }

    public void setRecepcion(Recepcion recepcion) {
        this.recepcion = recepcion;
    }

    @Override
    public int compareTo(Transporte transporte) {
        if (this.id < transporte.id) {
            return -1;
        }
        if (this.id > transporte.id) {
            return 1;
        }
        return 0;
    }
}
