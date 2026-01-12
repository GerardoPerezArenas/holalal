/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide38.util;


import es.altia.flexia.integracion.moduloexterno.melanbide38.vo.ContratoVO;
import java.util.Date;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class ValidadorContratos 
{
            
    //Logger
    private static Logger log = LogManager.getLogger(ValidadorContratos.class);
    
    //Instancia
    private static ValidadorContratos instance = null;    
    
    private ValidadorContratos()
    {
        
    }
    
    public static ValidadorContratos getInstance()
    {
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null)
        {
            synchronized(ValidadorContratos.class)
            {
                instance = new ValidadorContratos();
            }
        }
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }           
            
            
    public boolean validarContrato(ContratoVO contrato)
    {
        boolean correcto = true;
        if(!validarDatosTrabajador(contrato))
        {
            correcto = false;
        }
        if(!validarDatosContrato(contrato))
        {
            correcto = false;
        }
        if(!validarImporteSubvencion(contrato))
        {
            correcto = false;
        }
        return correcto;
    }
            
    private boolean validarDatosTrabajador(ContratoVO contrato)
    {
        //Datos obligatorios
        String dni = contrato.getDoc();
        if(dni == null || dni.equals(""))
        {
            return false;
        }
        String nombre = contrato.getNom();
        if(nombre == null || nombre.equals(""))
        {
            return false;
        }
        String ape1 = contrato.getAp1();
        if(ape1 == null || ape1.equals(""))
        {
            return false;
        }
        Date feNac = contrato.getFna();
        if(feNac == null)
        {
            return false;
        }
        Integer sexo = contrato.getSex();
        if(sexo == null)
        {
            return false;
        }
        Long idTer = contrato.getTerCod();
        if(idTer == null)
        {
            return false;
        }
        //DNI
        if(!validarNif(dni))
        {
            return false;
        }
        //Fecha nacimiento
        Date today = new Date();
        if(today.before(feNac))
        {
            return false;
        }
        //Meses desempleo
        Integer meses = contrato.getMde();
        if(meses != null)
        {
            if(meses < 0 || meses > 999)
            {
                return false;
            }
        }
        //Nivel estudios
        
        return true;
    }
            
    private boolean validarDatosContrato(ContratoVO contrato)
    {
        //Fecha alta

        //Duracion contrato
        Integer meses = contrato.getDco();
        if(meses != null)
        {
            if(meses > 99999 || meses < 0)
            {
                return false;
            }
        }

        //Tipo Contrato
        

        //Tipo Jornada
        

        //Porcentaje jornada
        if(contrato.getPjt() != null)
        {
            Double porcentaje = contrato.getPjt().doubleValue();
            if(porcentaje > 100.0 || porcentaje < 0.0){
                return false;
            }
        }
        
        //Tipo Contrato por duración
        

        //Salario Bruto
        if(contrato.getSal() != null)
        {
            Double retrib = contrato.getSal().doubleValue();
            if(retrib >= 10000000000.0 || retrib < 0.0)
            {
                return false;
            }
        }
        
        
        //CNOE
        

        //Seguridad Social
        if(contrato.getCss() != null)
        {
            Double segSocial = contrato.getCss().doubleValue();
            if(segSocial >= 10000000000.0 || segSocial < 0.0)
            {
                return false;
            }
        }
        
        //Colectivo
        
        return true;
    }
            
    private boolean validarImporteSubvencion(ContratoVO contrato)
    {
        //Concedido
        if(contrato.getImp() != null)
        {
            Double concedido = contrato.getImp().doubleValue();
            if(concedido >= 10000000000.0 || concedido < 0.0)
            {
                return false;
            }
        }

        //Modificacion Resolucion
        if(contrato.getImr() != null)
        {
            Double modif = contrato.getImr().doubleValue();
            if(modif >= 10000000000.0 || modif < 0.0)
            {
                return false;
            }
        }

        //Recurso
        if(contrato.getIre() != null)
        {
            Double retrib = contrato.getIre().doubleValue();
            if(retrib >= 10000000000.0 || retrib < 0.0)
            {
                return false;
            }
        }
        
        return true;
    }

    private boolean validarNif(String dni)
    {
        String cadena = "TRWAGMYFPDXBNJZSQVHLCKET";
        char letra;
        int posicion;
        boolean correcto = true;
        int longitud = dni.length();
        String aux = dni.substring(longitud-1).toUpperCase();
        if ((longitud >= 8) && (longitud < 10))
        {
            Integer num = null;
            try
            {
                num = Integer.parseInt(aux);
            }
            catch(Exception ex)
            {
                
            }
            if (num == null)
            {	
                posicion = Integer.parseInt(dni.substring(0,longitud-1)) % 23;
                letra=cadena.charAt(posicion);
                try
                {
                    Integer.parseInt(dni.substring(0,longitud-1));
                }
                catch(Exception ex)
                {
                    return false;
                }
                if(!aux.equals(String.valueOf(letra))) 
                {
                        return false;
                }
            }
            else
            {
                posicion = Integer.parseInt(dni.substring(0,longitud)) % 23;
                letra=cadena.charAt(posicion);
                try
                {
                    Integer.parseInt(dni.substring(0,longitud));
                }
                catch(Exception ex)
                {
                    return false;
                }
                return true;
            }
        }
        else 
        {
          return false;
        }
        return correcto;
    }
}
