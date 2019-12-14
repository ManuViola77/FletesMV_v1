package com.bios.mv.fletesmv_v1.bd;

/**
 *  Esta clase se creó para obtener el valor de constantes que se utilizan en la aplicación.
 *
 *  Se crean las variables y sus Getters.
 **/

public class Constantes {

    public static final String NOMBRE = "com.bios.mv.fletesmv_v1";
    public static final String CODIGO_USUARIO = "com.bios.mv.fletesmv_v1.usuario_logueado";
    public static final String TRANSPORTE_KEY = "com.bios.mv.fletesmv_v1.TRANSPORTE_KEY";

    public static final String URL_BD = "https://bios-fletes-api.herokuapp.com/api/student/47491070/";
    public static final String URL_REGISTRO = URL_BD+"users/register";
    public static final String URL_LOGIN = URL_BD+"users/login";
    //public static final String URL_TRANSPORTES = URL_BD+"transportations";
    public static final String URL_TRANSPORTES = "https://bios-fletes-api.herokuapp.com/api/student/1/transportations";
    public static final String URL_GPS = "https://bios-fletes-api.herokuapp.com/api/student/1/transportations";

    public static final String TAG_LOG = "FletesMV_LOG";

    public static final String conf_dias_noti_key = "conf_dias_noti";
    public static final String conf_dias_noti_defecto = "1";

    public static final String extra_transporte_origen_latitud = "com.bios.mv.fletesmv_v1.extra_transporte_origen_latitud";
    public static final String extra_transporte_origen_longitud = "com.bios.mv.fletesmv_v1.extra_transporte_origen_longitud";
    public static final String extra_transporte_destino_latitud = "com.bios.mv.fletesmv_v1.extra_transporte_destino_latitud";
    public static final String extra_transporte_destino_longitud = "com.bios.mv.fletesmv_v1.extra_transporte_destino_longitud";
    public static final String extra_transporte_ultima_latitud = "com.bios.mv.fletesmv_v1.extra_transporte_ultima_latitud";
    public static final String extra_transporte_ultima_longitud = "com.bios.mv.fletesmv_v1.extra_transporte_ultima_longitud";
    public static final String extra_transporte_modo = "com.bios.mv.fletesmv_v1.extra_transporte_modo";

    public static final String extra_iniciar_traslado = "com.bios.mv.fletesmv_v1.extra_iniciar_traslado";
    public static final String extra_finalizar_traslado = "com.bios.mv.fletesmv_v1.extra_finalizar_traslado";

    public static final String pendiente = "pendiente";
    public static final String iniciado = "iniciado";
    public static final String cargando = "cargando";
    public static final String viajando = "viajando";
    public static final String descargando = "descargando";
    public static final String finalizado = "finalizado";

}
