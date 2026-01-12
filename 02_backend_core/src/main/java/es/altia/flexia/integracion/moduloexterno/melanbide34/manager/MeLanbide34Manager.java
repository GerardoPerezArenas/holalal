/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide34.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide34.dao.MeLanbide34DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide34.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide34.util.ConstantesMeLanbide34;
import es.altia.flexia.integracion.moduloexterno.melanbide34.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide34.vo.FilaMinimisVO;
import es.altia.flexia.integracion.moduloexterno.melanbide34.vo.S75PagosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide34.vo.SelectItem;
import es.altia.flexia.integracion.moduloexterno.melanbide34.vo.ValoresCalculo;
import es.altia.flexia.integracion.moduloexterno.melanbide34.vo.ValoresPago;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide34Manager 
{
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide34Manager.class);
    
    //Instancia
    private static MeLanbide34Manager instance = null;
    
    private MeLanbide34Manager()
    {
        
    }
    
    public static MeLanbide34Manager getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide34Manager.class)
            {
                instance = new MeLanbide34Manager();
            }
        }
        return instance;
    }    
    
    public BigDecimal getValorCampoNumerico(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide34DAO meLanbide34DAO = MeLanbide34DAO.getInstance();
            return meLanbide34DAO.getValorCampoNumerico(codOrganizacion, numExp, ejercicio, codigoCampo, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando el valor para el campo suplementario " + codigoCampo, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando el valor para el campo suplementario " + codigoCampo, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexiÃ³n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public String getValorCampoDesplegable(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide34DAO meLanbide34DAO = MeLanbide34DAO.getInstance();
            return meLanbide34DAO.getValorCampoDesplegable(codOrganizacion, numExp, ejercicio, codigoCampo, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando el valor para el campo suplementario " + codigoCampo, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando el valor para el campo suplementario " + codigoCampo, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexiÃ³n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public BigDecimal getValorCampoNumericoTramite(int codOrganizacion, String numExp, String ejercicio, String codigoTramite, String codigoCampo, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide34DAO meLanbide34DAO = MeLanbide34DAO.getInstance();
            Long codTram = meLanbide34DAO.getCodigoInternoTramite(codOrganizacion, codigoTramite, con);
            return meLanbide34DAO.getValorCampoNumericoTramite(codOrganizacion, ejercicio, numExp, codTram, codigoCampo, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando el valor para el campo suplementario " + codigoCampo, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando el valor para el campo suplementario " + codigoCampo, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexiÃ³n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public Date getValorCampoFechaTramite(int codOrganizacion, String numExp, String ejercicio, String codigoTramite, String codigoCampo, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide34DAO meLanbide34DAO = MeLanbide34DAO.getInstance();
            Long codTram = meLanbide34DAO.getCodigoInternoTramite(codOrganizacion, codigoTramite, con);
            return meLanbide34DAO.getValorCampoFechaTramite(codOrganizacion, ejercicio, numExp, codTram, codigoCampo, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando el valor para el campo suplementario " + codigoCampo, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando el valor para el campo suplementario " + codigoCampo, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexiÃ³n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    
    public boolean guardarValoresCalculo(int codOrganizacion, ValoresCalculo valoresCalculo, String numExpediente, String ejercicio, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        boolean transactionStarted = false;
        try
        {
            int result = 0;
            Integer anoExp = Integer.parseInt(ejercicio);
            Integer anoPago = anoExp;
            Date d = new Date();
            Calendar cal = new GregorianCalendar();
            Integer anoAct = cal.get(Calendar.YEAR);
            if(anoPago.compareTo(anoAct) < 0)
            {
                anoPago = anoAct;
            }
            con = adaptador.getConnection();
            MeLanbide34DAO meLanbide34DAO = MeLanbide34DAO.getInstance();
            BigDecimal pago1 = new BigDecimal(valoresCalculo.getPago1());
            BigDecimal pago2 = new BigDecimal(valoresCalculo.getPago2());
            BigDecimal pago3 = new BigDecimal(valoresCalculo.getPago3());
            boolean nuevo = false;
            adaptador.inicioTransaccion(con);
            transactionStarted = true;
            
            Long codTram = meLanbide34DAO.getCodigoInternoTramite(codOrganizacion, ConstantesMeLanbide34.CODIGO_TRAM_ETECNICO, con);
            
            BigDecimal limitePorc = meLanbide34DAO.getValorCampoNumericoTramite(codOrganizacion, ejercicio, numExpediente, codTram, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_LIMITEPORC, con);
            if(limitePorc == null)
            {
                nuevo = true;
            }
            limitePorc = new BigDecimal(valoresCalculo.getPorcentaje());
            result = meLanbide34DAO.guardarValorCampoNumericoTramite(codOrganizacion, ejercicio, numExpediente, codTram, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_LIMITEPORC, limitePorc, nuevo, con);
            if(result != 1)
            {
                adaptador.rollBack(con);
                return false;
            }
            
            nuevo = false;
            BigDecimal limiteMax = meLanbide34DAO.getValorCampoNumericoTramite(codOrganizacion, ejercicio, numExpediente, codTram, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_LIMITEMAX, con);
            if(limiteMax == null)
            {
                nuevo = true;
            }
            limiteMax = new BigDecimal(valoresCalculo.getLimite());
            result = meLanbide34DAO.guardarValorCampoNumericoTramite(codOrganizacion, ejercicio, numExpediente, codTram, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_LIMITEMAX, limiteMax, nuevo, con);
            if(result != 1)
            {
                adaptador.rollBack(con);
                return false;
            }
            
            nuevo = false;
            BigDecimal cuanlim = meLanbide34DAO.getValorCampoNumericoTramite(codOrganizacion, ejercicio, numExpediente, codTram, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_CUANLIM, con);
            if(cuanlim == null)
            {
                nuevo = true;
            }
            cuanlim = new BigDecimal(valoresCalculo.getLimitePorc());
            result = meLanbide34DAO.guardarValorCampoNumericoTramite(codOrganizacion, ejercicio, numExpediente, codTram, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_CUANLIM, cuanlim, nuevo, con);
            if(result != 1)
            {
                adaptador.rollBack(con);
                return false;
            }
            
            BigDecimal p1 = meLanbide34DAO.getValorCampoNumericoTramite(codOrganizacion, ejercicio, numExpediente, codTram, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_P1,con);
            if(p1 == null)
            {
                nuevo = true;
            }
            p1 = new BigDecimal(valoresCalculo.getP1());
            result = meLanbide34DAO.guardarValorCampoNumericoTramite(codOrganizacion, ejercicio, numExpediente, codTram, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_P1, p1, nuevo, con);
            if(result != 1)
            {
                adaptador.rollBack(con);
                return false;
            }
            
            nuevo = false;
            BigDecimal p2 = meLanbide34DAO.getValorCampoNumericoTramite(codOrganizacion, ejercicio, numExpediente, codTram, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_P2,con);
            if(p2 == null)
            {
                nuevo = true;
            }
            p2 = new BigDecimal(valoresCalculo.getP2());
            result = meLanbide34DAO.guardarValorCampoNumericoTramite(codOrganizacion, ejercicio, numExpediente, codTram, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_P2, p2, nuevo, con);
            if(result != 1)
            {
                adaptador.rollBack(con);
                return false;
            }
            
            nuevo = false;
            BigDecimal p3 = meLanbide34DAO.getValorCampoNumericoTramite(codOrganizacion, ejercicio, numExpediente, codTram, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_P3,con);
            if(p3 == null)
            {
                nuevo = true;
            }
            p3 = new BigDecimal(valoresCalculo.getP3());
            result = meLanbide34DAO.guardarValorCampoNumericoTramite(codOrganizacion, ejercicio, numExpediente, codTram, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_P3, p3, nuevo, con);
            if(result != 1)
            {
                adaptador.rollBack(con);
                return false;
            }
            
            nuevo = false;
            BigDecimal p4 = meLanbide34DAO.getValorCampoNumericoTramite(codOrganizacion, ejercicio, numExpediente, codTram, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_P4,con);
            if(p4 == null)
            {
                nuevo = true;
            }
            p4 = new BigDecimal(valoresCalculo.getP4());
            result = meLanbide34DAO.guardarValorCampoNumericoTramite(codOrganizacion, ejercicio, numExpediente, codTram, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_P4, p4, nuevo, con);
            if(result != 1)
            {
                adaptador.rollBack(con);
                return false;
            }
            
            nuevo = false;
            BigDecimal sumpond = meLanbide34DAO.getValorCampoNumericoTramite(codOrganizacion, ejercicio, numExpediente, codTram, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_SUMPOND, con);
            if(sumpond == null)
            {
                nuevo = true;
            }
            sumpond = new BigDecimal(valoresCalculo.getTotal());
            result = meLanbide34DAO.guardarValorCampoNumericoTramite(codOrganizacion, ejercicio, numExpediente, codTram, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_SUMPOND, sumpond, nuevo, con);
            if(result != 1)
            {
                adaptador.rollBack(con);
                return false;
            }
            
            nuevo = false;
            BigDecimal propuesta = meLanbide34DAO.getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_PROPUESTA, con);
            if(propuesta == null)
            {
                nuevo = true;
            }
            propuesta = new BigDecimal(valoresCalculo.getPropuesta());
            result = meLanbide34DAO.guardarValorCampoNumerico(codOrganizacion, ejercicio, numExpediente, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_PROPUESTA, propuesta, nuevo, con);
            if(result != 1)
            {
                adaptador.rollBack(con);
                return false;
            }
            
            nuevo = false;
            BigDecimal concedida = meLanbide34DAO.getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_CONCEDIDA, con);
            if(concedida == null)
            {
                nuevo = true;
            }
            concedida = new BigDecimal(valoresCalculo.getConcedida());
            result = meLanbide34DAO.guardarValorCampoNumerico(codOrganizacion, ejercicio, numExpediente, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_CONCEDIDA, concedida, nuevo, con);
            if(result != 1)
            {
                adaptador.rollBack(con);
                return false;
            }
            
            nuevo = false;
            S75PagosVO pago1VO = meLanbide34DAO.getPagosExpediente(codOrganizacion, numExpediente, ejercicio, 1, con);
            if(pago1VO == null)
            {
                nuevo = true;
                pago1VO = new S75PagosVO();
                pago1VO.setPagAnoPago(anoPago.toString());
                pago1VO.setPagConcep(ConstantesMeLanbide34.CODIGO_CONCEPTO);
                pago1VO.setPagEje(Long.valueOf(ejercicio));
                pago1VO.setPagMun((long)codOrganizacion);
                pago1VO.setPagNum(numExpediente);
                pago1VO.setPagNumpago(1L);
                pago1VO.setPagPro(ConstantesMeLanbide34.CODIGO_PROCEDIMIENTO_MELANBIDE34);
            }
            pago1VO.setPagImpcon(pago1);
            result = meLanbide34DAO.guardarPago(pago1VO, nuevo, con);
            if(result != 1)
            {
                adaptador.rollBack(con);
                return false;
            }
            
            nuevo = false;
            BigDecimal impcona1 = meLanbide34DAO.getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_IMPCONA1, con);
            if(impcona1 == null)
            {
                nuevo = true;
            }
            impcona1 = pago1;
            result = meLanbide34DAO.guardarValorCampoNumerico(codOrganizacion, ejercicio, numExpediente, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_IMPCONA1, impcona1, nuevo, con);
            if(result != 1)
            {
                adaptador.rollBack(con);
                return false;
            }
            
            nuevo = false;
            S75PagosVO pago2VO = meLanbide34DAO.getPagosExpediente(codOrganizacion, numExpediente, ejercicio, 2, con);
            if(pago2VO == null)
            {
                nuevo = true;
                pago2VO = new S75PagosVO();
                pago2VO.setPagAnoPago(anoPago.toString());
                pago2VO.setPagConcep(ConstantesMeLanbide34.CODIGO_CONCEPTO);
                pago2VO.setPagEje(Long.valueOf(ejercicio));
                pago2VO.setPagMun((long)codOrganizacion);
                pago2VO.setPagNum(numExpediente);
                pago2VO.setPagNumpago(2L);
                pago2VO.setPagPro(ConstantesMeLanbide34.CODIGO_PROCEDIMIENTO_MELANBIDE34);
            }
            pago2VO.setPagImpcon(pago2);
            result = meLanbide34DAO.guardarPago(pago2VO, nuevo, con);
            if(result != 1)
            {
                adaptador.rollBack(con);
                return false;
            }
            
            nuevo = false;
            BigDecimal impcona2 = meLanbide34DAO.getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_IMPCONA2, con);
            if(impcona2 == null)
            {
                nuevo = true;
            }
            impcona2 = pago2;
            result = meLanbide34DAO.guardarValorCampoNumerico(codOrganizacion, ejercicio, numExpediente, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_IMPCONA2, impcona2, nuevo, con);
            if(result != 1)
            {
                adaptador.rollBack(con);
                return false;
            }
            
            nuevo = false;
            S75PagosVO pago3VO = meLanbide34DAO.getPagosExpediente(codOrganizacion, numExpediente, ejercicio, 3, con);
            if(pago3VO == null)
            {
                nuevo = true;
                pago3VO = new S75PagosVO();
                pago3VO.setPagAnoPago(anoPago.toString());
                pago3VO.setPagConcep(ConstantesMeLanbide34.CODIGO_CONCEPTO);
                pago3VO.setPagEje(Long.valueOf(ejercicio));
                pago3VO.setPagMun((long)codOrganizacion);
                pago3VO.setPagNum(numExpediente);
                pago3VO.setPagNumpago(3L);
                pago3VO.setPagPro(ConstantesMeLanbide34.CODIGO_PROCEDIMIENTO_MELANBIDE34);
            }
            pago3VO.setPagImpcon(pago3);
            result = meLanbide34DAO.guardarPago(pago3VO, nuevo, con);
            if(result != 1)
            {
                adaptador.rollBack(con);
                return false;
            }
            
            nuevo = false;
            BigDecimal impcona3 = meLanbide34DAO.getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_IMPCONA3, con);
            if(impcona3 == null)
            {
                nuevo = true;
            }
            impcona3 = pago3;
            result = meLanbide34DAO.guardarValorCampoNumerico(codOrganizacion, ejercicio, numExpediente, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_IMPCONA3, impcona3, nuevo, con);
            if(result != 1)
            {
                adaptador.rollBack(con);
                return false;
            }
            
            //fecestudio
            nuevo = false;
            Date fecestudio = meLanbide34DAO.getValorCampoFechaTramite(codOrganizacion,  ejercicio, numExpediente, codTram, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_FECETECNICO, con);
            if(fecestudio == null)
            {
                nuevo = true;
            }
            
            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
            try {
                if (!valoresCalculo.getFecEstudio().equals(""))
                fecestudio = formatoDelTexto.parse(valoresCalculo.getFecEstudio());
                else fecestudio=null;
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            if (fecestudio!=null)
                result = meLanbide34DAO.guardarValorCampoFechaTramite(codOrganizacion, ejercicio, numExpediente, codTram, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_FECETECNICO, fecestudio, nuevo, con);
            else { 
                result = meLanbide34DAO.eliminarValorCampoFechaTramite(codOrganizacion, ejercicio, numExpediente, codTram, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_FECETECNICO, con);
            }
            if((fecestudio!=null && result != 1) || (fecestudio==null && result<0))
            {
                adaptador.rollBack(con);
                return false;
            }
            //resul subv
            nuevo = false;
            String resultado = meLanbide34DAO.getValorCampoDesplegable(codOrganizacion,   numExpediente, ejercicio, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_RESULSUBV, con);
            if(resultado == null)
            {
                nuevo = true;
            }       
            resultado = valoresCalculo.getResulSubv();
            if (!resultado.equals("")){
                result = meLanbide34DAO.guardarValorCampoDesplegable(codOrganizacion, ejercicio, numExpediente, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_RESULSUBV, resultado, nuevo, con);
                if(result != 1)
                {
                    adaptador.rollBack(con);
                    return false;
                }
            }
            //MOTIVO DENEGACION1 
            nuevo = false;
            String motden1 = meLanbide34DAO.getValorCampoDesplegable(codOrganizacion,  numExpediente, ejercicio, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_MOTDEN1, con);
            if(motden1 == null)
            {
                nuevo = true;
            }       
            motden1 = valoresCalculo.getMotivoDen1();
            if (!motden1.equals("")){
                result = meLanbide34DAO.guardarValorCampoDesplegable(codOrganizacion, ejercicio, numExpediente, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_MOTDEN1, motden1, nuevo, con);
                if(result != 1)
                {
                    adaptador.rollBack(con);
                    return false;
                }
            }
            //MOTIVO DENEGACION2 
            nuevo = false;
            String motden2 = meLanbide34DAO.getValorCampoDesplegable(codOrganizacion,  numExpediente, ejercicio, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_MOTDEN2, con);
            if(motden2 == null)
            {
                nuevo = true;
            }       
            motden2 = valoresCalculo.getMotivoDen2();
            if (!motden2.equals("")){
                result = meLanbide34DAO.guardarValorCampoDesplegable(codOrganizacion, ejercicio, numExpediente, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_MOTDEN2, motden2, nuevo, con);
                if(result != 1)
                {
                    adaptador.rollBack(con);
                    return false;
                }
            }
            //MOTIVO DENEGACION3 
            nuevo = false;
            String motden3 = meLanbide34DAO.getValorCampoDesplegable(codOrganizacion,  numExpediente, ejercicio, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_MOTDEN3, con);
            if(motden3 == null)
            {
                nuevo = true;
            }       
            motden3 = valoresCalculo.getMotivoDen3();
            if (!motden3.equals("")){
                result = meLanbide34DAO.guardarValorCampoDesplegable(codOrganizacion, ejercicio, numExpediente, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_MOTDEN3, motden3, nuevo, con);
                if(result != 1)
                {
                    adaptador.rollBack(con);
                    return false;
                }
            }
            
            adaptador.finTransaccion(con);
            return true;
        }
        catch(BDException e)
        {
            if(transactionStarted)
            {
                adaptador.rollBack(con);
            }
            log.error("Se ha producido una excepciÃ³n en la BBDD guardando los pagos para el expediente " + numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            if(transactionStarted)
            {
                adaptador.rollBack(con);
            }
            log.error("Se ha producido una excepciÃ³n en la BBDD guardando los pagos para el expediente " + numExpediente, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexiÃ³n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public Long getCodigoUltimoTramiteAbierto(int codOrganizacion, String ejercicio, String numExp, Long ocurrencia, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide34DAO meLanbide34DAO = MeLanbide34DAO.getInstance();
            return meLanbide34DAO.getCodigoUltimoTramiteAbierto(codOrganizacion, ejercicio, numExp, ocurrencia, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando el tramite abierto ("+numExp+")", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando el tramite abierto ("+numExp+")", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexiÃ³n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    
    public boolean guardarValoresPago(int codOrganizacion, ValoresPago valoresPago, String numExpediente, String ejercicio, int pago, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        boolean transactionStarted = false;
        try
        {
            int result = 0;
            Integer anoExp = Integer.parseInt(ejercicio);
            Integer anoPago = anoExp;
            Date d = new Date();
            Calendar cal = new GregorianCalendar();
            Integer anoAct = cal.get(Calendar.YEAR);
            if(anoPago.compareTo(anoAct) < 0)
            {
                anoPago = anoAct;
            }
            con = adaptador.getConnection();
            MeLanbide34DAO meLanbide34DAO = MeLanbide34DAO.getInstance();           
            boolean nuevo = false;
            adaptador.inicioTransaccion(con);
            transactionStarted = true;
            
            String tramite="";
            String campoAPagar="";
            String campoDescuento="";
            String campoFechaPago="";
            switch (pago){
                    case 1: 
                            campoAPagar = ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_IMPPAGAR1;
                            campoDescuento = ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_DESCUENTO1;
                            tramite=ConstantesMeLanbide34.CODIGO_TRAM_PRIMERPAGO;
                            campoFechaPago = ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_FECPRIMERPAGO;                
                            break;
                    case 2:                    
                            tramite=ConstantesMeLanbide34.CODIGO_TRAM_SEGUNDOPAGO;
                            campoAPagar = ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_IMPPAGAR2;
                            campoDescuento = ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_DESCUENTO2;
                            campoFechaPago = ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_FECSEGUNDOPAGO;      
                            break;
                    case 3: 
                            tramite=ConstantesMeLanbide34.CODIGO_TRAM_TERCERPAGO;
                            campoAPagar = ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_IMPPAGAR3;
                            campoDescuento = ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_DESCUENTO3;
                            campoFechaPago = ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_FECTERCERPAGO;      
                            break;
                 }   
            
            Long codTram = meLanbide34DAO.getCodigoInternoTramite(codOrganizacion, tramite, con);
            
            nuevo = false;
            BigDecimal impPagar = meLanbide34DAO.getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, campoAPagar, con);
            if(impPagar == null)
            {
                nuevo = true;
            }
            impPagar = new BigDecimal(valoresPago.getImporteAPagar());
            result = meLanbide34DAO.guardarValorCampoNumerico(codOrganizacion, ejercicio, numExpediente, campoAPagar, impPagar, nuevo, con);
            if(result != 1)
            {
                adaptador.rollBack(con);
                return false;
            }
            
            nuevo = false;
            BigDecimal descuento = meLanbide34DAO.getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, campoDescuento, con);
            if(descuento == null)
            {
                nuevo = true;
            }
            descuento = new BigDecimal(valoresPago.getDescuento());
            result = meLanbide34DAO.guardarValorCampoNumerico(codOrganizacion, ejercicio, numExpediente, campoDescuento, descuento, nuevo, con);
            if(result != 1)
            {
                adaptador.rollBack(con);
                return false;
            }
            
             //fecpago
            nuevo = false;
            Date fecpago = meLanbide34DAO.getValorCampoFechaTramite(codOrganizacion,  ejercicio, numExpediente, codTram, campoFechaPago, con);
            if(fecpago == null)
            {
                nuevo = true;
            }
            
            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
            try {
                 if (!valoresPago.getFecPago().equals(""))
                         fecpago = formatoDelTexto.parse(valoresPago.getFecPago());
                 else fecpago=null;
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            if (fecpago!=null){
                result = meLanbide34DAO.guardarValorCampoFechaTramite(codOrganizacion, ejercicio, numExpediente, codTram, campoFechaPago, fecpago, nuevo, con);
                if(result != 1)
                {
                    adaptador.rollBack(con);
                    return false;
                }
            }
            
            //viable
           if (pago ==2){
               nuevo = false;
           
                String resultado = meLanbide34DAO.getValorCampoDesplegable(codOrganizacion,   numExpediente, ejercicio, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_VIABLE, con);
                if(resultado == null)
                {
                    nuevo = true;
                }       
                resultado = valoresPago.getViable();
                if (!resultado.equals("")){
                    result = meLanbide34DAO.guardarValorCampoDesplegable(codOrganizacion, ejercicio, numExpediente, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_VIABLE, resultado, nuevo, con);
                    if(result != 1)
                    {
                        adaptador.rollBack(con);
                        return false;
                    }
                }
           }
            
            
            nuevo = false;
            S75PagosVO pago1VO = meLanbide34DAO.getPagosExpediente(codOrganizacion, numExpediente, ejercicio, pago, con);
            /*if(pago1VO == null)
            {
                nuevo = true;
                pago1VO = new S75PagosVO();
                pago1VO.setPagAnoPago(anoPago.toString());
                pago1VO.setPagConcep(ConstantesMeLanbide34.CODIGO_CONCEPTO);
                pago1VO.setPagEje(Long.valueOf(ejercicio));
                pago1VO.setPagMun((long)codOrganizacion);
                pago1VO.setPagNum(numExpediente);
                pago1VO.setPagNumpago(1L);
                pago1VO.setPagPro(ConstantesMeLanbide34.CODIGO_PROCEDIMIENTO_MELANBIDE34);
            }
            pago1VO.setPagImpcon(pago1);
            */
            pago1VO.setPagImppag(impPagar);
            pago1VO.setPagImpanu(descuento);
            pago1VO.setPagFecpag(fecpago);
            result = meLanbide34DAO.guardarPago(pago1VO, nuevo, con);
            if(result != 1)
            {
                adaptador.rollBack(con);
                return false;
            }
            
            adaptador.finTransaccion(con);
            return true;
        }
        catch(BDException e)
        {
            if(transactionStarted)
            {
                adaptador.rollBack(con);
            }
            log.error("Se ha producido una excepciÃ³n en la BBDD guardando los pagos para el expediente " + numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            if(transactionStarted)
            {
                adaptador.rollBack(con);
            }
            log.error("Se ha producido una excepciÃ³n en la BBDD guardando los pagos para el expediente " + numExpediente, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexiÃ³n a la BBDD: " + e.getMessage());
            }
        }
    }
    
     public List<SelectItem> getListaDesplegable(AdaptadorSQLBD adaptador, String idLista)  throws Exception {
         Connection con = null;
        List<SelectItem> lista=null;
        MeLanbide34DAO meLanbide34DAO = MeLanbide34DAO.getInstance();
        try{
            con = adaptador.getConnection();
            lista = meLanbide34DAO.getListaDesplegable(con, idLista);
        }catch(BDException e)
        {
            log.error("Se ha producido una excepcion recuperando lista resultado Final", e);
            throw new Exception(e);
        }catch(Exception e){
            log.debug("Error al obtener lista resultado Final."+e.getMessage());
            throw new Exception(e);
        }finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return lista;
    }
           public List<FilaMinimisVO> getDatosMinimis(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide34DAO meLanbide34DAO = MeLanbide34DAO.getInstance();
            lista = meLanbide34DAO.getDatosMinimis(numExp, codOrganizacion, con);
            //recuperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableAdmonLocalVO> listaEstado = MeLanbide34Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide34.COD_DES_DTSV, ConstantesMeLanbide34.FICHERO_PROPIEDADES), adapt);
       
            for (FilaMinimisVO cont : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaEstado) {
                    if (valordesp.getDes_val_cod().equals(cont.getEstado())) {
                        cont.setDesEstado(valordesp.getDes_nom());
                        break;
}
                }
            }         
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepci0n en la BBDD recuperando datos sobre las minimis ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre las minimis ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
            public FilaMinimisVO getMinimisPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide34DAO meLanbide34DAO = MeLanbide34DAO.getInstance();
            return meLanbide34DAO.getMinimisPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una minimis:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una minimis:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarMinimis(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide34DAO meLanbide34DAO = MeLanbide34DAO.getInstance();
            return meLanbide34DAO.eliminarMinimis(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepci?n en la BBDD al eliminar una minimis:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n en la BBDD al eliminar una minimis:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexi?n a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevoMinimis(FilaMinimisVO nuevaMinimis, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide34DAO meLanbide34DAO = MeLanbide34DAO.getInstance();
            insertOK = meLanbide34DAO.crearNuevoMinimis(nuevaMinimis, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepci?n en la BBDD creando una minimis : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n en la BBDD creando una minimis : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexi?n a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }

    public boolean modificarMinimis(FilaMinimisVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide34DAO meLanbide34DAO = MeLanbide34DAO.getInstance();
            insertOK = meLanbide34DAO.modificarMinimis(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepci?n en la BBDD actualizando una minimis : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n en la BBDD actualizando una minimis : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexi?n a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }
    public List<DesplegableAdmonLocalVO> getValoresDesplegablesAdmonLocalxdes_cod(String des_cod, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide34DAO meLanbide34DAO = MeLanbide34DAO.getInstance();
            return meLanbide34DAO.getValoresDesplegablesAdmonLocalxdes_cod(des_cod, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepci?n en la BBDD recuperando valores de desplegable : " + des_cod, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n en la BBDD recuperando valores de desplegable :  " + des_cod, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexi?n a la BBDD: " + e.getMessage());
            }
        }
    }    
}
