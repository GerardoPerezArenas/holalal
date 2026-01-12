/**
 * RGIServices.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package rgi.lanbide.net;

public interface RGIServices extends java.rmi.Remote {
    public net.lanbide.rgi.services.type.ResultadoVO retroceder(java.lang.String numExpedienteAdmin, java.lang.String tramiteAnterior) throws java.rmi.RemoteException;
    public net.lanbide.rgi.services.type.ResultadoVO finalizarExpediente(java.lang.String numExpedienteAdmin) throws java.rmi.RemoteException;
    public net.lanbide.rgi.services.type.ResultadoVO desasociarEntradaRegistro(java.lang.String numeroRegistro, java.lang.String ejercicioRegistro, java.lang.String fechaDesasociacionRegistro, java.lang.String fechaPresentacionRegistro, java.lang.String expedienteRelacionado, java.lang.String usuario) throws java.rmi.RemoteException;
    public java.lang.String getCodigoTramiteEXTResultadoAlegacionesRetrocedible() throws java.rmi.RemoteException;
    public void setCodigoTramiteEXTResultadoAlegacionesRetrocedible(java.lang.String codigoTramiteEXTResultadoAlegacionesRetrocedible) throws java.rmi.RemoteException;
    public java.lang.String getCodigoTramiteSUTResultadoAlegacionesRetrocedible() throws java.rmi.RemoteException;
    public void setCodigoTramiteSUTResultadoAlegacionesRetrocedible(java.lang.String codigoTramiteSUTResultadoAlegacionesRetrocedible) throws java.rmi.RemoteException;
}
