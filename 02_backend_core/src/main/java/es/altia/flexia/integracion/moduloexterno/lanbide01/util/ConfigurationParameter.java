package es.altia.flexia.integracion.moduloexterno.lanbide01.util;
import java.util.ResourceBundle;

public class ConfigurationParameter {    
    private static String SQL_GESTOR   = "/SQL.gestor";
    private static String SQL_DRIVER   = "/SQL.driver";
    private static String SQL_URL      = "/SQL.url";
    private static String SQL_USUARIO  = "/SQL.usuario";
    private static String SQL_PASSWORD = "/SQL.password";

    public static String getParameter(String property,String FICHERO_CONFIGURACION){
        String valor = "";
        try{
            ResourceBundle bundle = ResourceBundle.getBundle(FICHERO_CONFIGURACION);
            valor = bundle.getString(property);
        }catch(Exception e){
            e.printStackTrace();
        }

        return valor;
    }


    public static String[] getParamsBD(String codOrganizacion,String FICHERO_CONFIGURACION){
        String[] paramsBD = new String[7];

        try{
            paramsBD[0] = getParameter(codOrganizacion + SQL_GESTOR,FICHERO_CONFIGURACION);
            paramsBD[1] = getParameter(codOrganizacion + SQL_DRIVER,FICHERO_CONFIGURACION);
            paramsBD[2] = getParameter(codOrganizacion + SQL_URL,FICHERO_CONFIGURACION);
            paramsBD[3] = getParameter(codOrganizacion + SQL_USUARIO,FICHERO_CONFIGURACION);
            paramsBD[4] = getParameter(codOrganizacion + SQL_PASSWORD,FICHERO_CONFIGURACION);
            
        }catch(Exception e){
            e.printStackTrace();
        }

        return paramsBD;
    }
}