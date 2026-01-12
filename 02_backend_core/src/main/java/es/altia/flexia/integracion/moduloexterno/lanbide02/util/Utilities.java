package es.altia.flexia.integracion.moduloexterno.lanbide02.util;

import es.altia.flexia.integracion.moduloexterno.lanbide02.vo.DocumentoLanbideVO;
import es.altia.flexia.integracion.moduloexterno.lanbide02.vo.ExpedienteLanbideVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.DocumentoTramitacionModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ExpedienteModuloIntegracionVO;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class Utilities {
    private static Logger log = LogManager.getLogger(Utilities.class);
    
    /**
     * Convierte un objeto de la clase ExpedienteModuloIntegracionVO en un objeto de la clase ExpedienteLanbideVO
     * @param doc: ExpedienteModuloIntegracionVO
     * @return Un objeto de la clase ExpedienteLanbideVO o null si no se puede hacer el mapeo
     */
    public static ExpedienteLanbideVO expedienteModuloIntegracionVOtoExpedienteLanbideVO(ExpedienteModuloIntegracionVO exp){
        ExpedienteLanbideVO elVO = null;
        if(exp!=null){
            elVO = new ExpedienteLanbideVO();
            elVO.setCodMunicipio(exp.getCodMunicipio());
            elVO.setCodProcedimiento(exp.getCodProcedimiento());
            elVO.setEjercicio(exp.getEjercicio());
            elVO.setNumExpediente(exp.getNumExpediente());
        }

        return elVO;
    }


    /**
     * Convierte un objeto de la clase DocumentoTramitacionModuloIntegracionVO en un objeto de la clase DocumentoLanbideVO
     * @param doc: DocumentoTramitacionModuloIntegracionVO
     * @return Un objeto de la clase DocumentoLanbideVO o null si no se puede hacer el mapeo
     */
    public static DocumentoLanbideVO documentoTramitacionModuloIntegraciontoDocumentoLanbideVO(DocumentoTramitacionModuloIntegracionVO doc){
        DocumentoLanbideVO elVO = null;
        if(doc!=null){
            elVO = new DocumentoLanbideVO();
            elVO.setCodMunicipio(doc.getCodMunicipio());
            elVO.setCodProcedimiento(doc.getCodProcedimiento());
            elVO.setCodTramite(doc.getCodTramite());
            elVO.setOcurrenciaTramite(doc.getOcuTramite());
            elVO.setNumExpediente(doc.getNumExpediente());
            elVO.setNumeroDocumento(doc.getNumDocumento());
            elVO.setNombreDocumento(doc.getNombreDocumento());
        }
        return elVO;
    }


    /**
     * Comprueba si una cadena de caracteres contiene una cadena vacía
     * @param dato: String
     * @return true si es cadena no vacía y false en otro caso
     */
    public static boolean cadenaNoVacia(String dato){
        boolean exito = false;
        if(dato!=null && !"".equals(dato) && dato.length()>0)
            exito = true;
        
        return exito;
    }


    /**
     * Devuelve un número decimal correctamente formateado para ser mostrada al usuario
     * @param numero: double
     * @return String
     */
    public static String formatoNumeroDecimales(double numero){    
        DecimalFormat df = new DecimalFormat("#,##0.00;(-#,##0.00)");
        return df.format(numero);
    }

  

    /**
     * Comprueba si String contiene un valor numérico de tipo entero
     * @param cadena: String
     * @return True si lo es y false en otro caso
     */
    public static boolean esNumeroEntero(String cadena){
        boolean exito = false;
        try{
            
            Integer.parseInt(cadena);
            exito = true;

        }catch(NumberFormatException e){
            e.printStackTrace();
        }

        return exito;
    }



   /**
     * Ejecuta un método set de una clase y devuelve a continuación el objeto
     * @param objeto
     * @param nombreMetodo
     * @param tipoParametros
     * @param valoresParametros
     * @return
     */
    public static Object ejecutarMetodo(Object objeto,String nombreMetodo,Class[] tipoParametros,Object[] valoresParametros){
        try{
            Class clase = Class.forName(objeto.getClass().getName());
            clase.newInstance();

            Method metodo = clase.getMethod(nombreMetodo,tipoParametros);
            metodo.invoke(objeto,valoresParametros);

        }catch(ClassNotFoundException e){
            log.error(e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return objeto;
    }


    public static boolean isDouble(String dato){
        boolean exito = false;
        try{
            Double.parseDouble(dato);
            exito = true;
        }catch(NumberFormatException e){
            exito = false;
        }

        return exito;
    }



    public static boolean existePosicion(String[] lista,int posicion){
        boolean exito = false;
        if(lista!=null){
            if(posicion>=0 && posicion<lista.length)
                exito = true;
        }

        return exito;
    }
    
}