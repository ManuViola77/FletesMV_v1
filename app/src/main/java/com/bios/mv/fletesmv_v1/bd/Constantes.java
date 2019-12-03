package com.bios.mv.fletesmv_v1.bd;

/**
 *  Esta clase se creó para obtener el valor de constantes que se utilizan en la aplicación.
 *
 *  Se crean las variables y sus Getters.
 **/

public class Constantes {

    private static final String NOMBRE = "com.bios.mv.fletesmv_v1";
    private static final String CODIGO_USUARIO = "com.bios.mv.fletesmv_v1.usuario_logueado";
    private static final String TRANSPORTE_KEY = "com.bios.mv.fletesmv_v1.TRANSPORTE_KEY";

    private static final String URL_BD = "https://bios-fletes-api.herokuapp.com/api/student/47491070/";
    private static final String URL_REGISTRO = URL_BD+"users/register";
    private static final String URL_LOGIN = URL_BD+"users/login";
    //private static final String URL_TRANSPORTES = URL_BD+"transportations";
    private static final String URL_TRANSPORTES = "https://bios-fletes-api.herokuapp.com/api/student/1/transportations";

    private static final String TAG_LOG = "FletesMV_LOG";

    private static final String conf_dias_noti_key = "conf_dias_noti";
    private static final String conf_dias_noti_defecto = "1";


    public static String getNOMBRE() {
        return NOMBRE;
    }

    public static String getCodigoUsuario() {
        return CODIGO_USUARIO;
    }

    public static String getUrlBd() {
        return URL_BD;
    }

    public static String getUrlRegistro() {
        return URL_REGISTRO;
    }

    public static String getTagLog() {
        return TAG_LOG;
    }

    public static String getUrlLogin() { return URL_LOGIN; }

    public static String getUrlTransportes() {
        return URL_TRANSPORTES;
    }

    public static String getConf_dias_noti_key() {
        return conf_dias_noti_key;
    }

    public static String getConf_dias_noti_defecto() {
        return conf_dias_noti_defecto;
    }

    public static String getTransporteKey() {
        return TRANSPORTE_KEY;
    }
}
