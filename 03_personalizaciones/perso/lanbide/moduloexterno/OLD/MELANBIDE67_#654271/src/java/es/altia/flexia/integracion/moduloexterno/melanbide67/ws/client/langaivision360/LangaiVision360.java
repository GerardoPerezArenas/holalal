/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.ws.client.langaivision360;

/**
 *
 * @author pablo.bugia
 */
public interface LangaiVision360 extends java.rmi.Remote {

    public java.lang.String generarDocumentoPdf(java.lang.String tipoDoc, java.lang.String numDoc, java.lang.String documento, java.lang.String oferta, java.lang.String cifOferente, java.lang.String idSolicitud, java.lang.String idioma) throws java.rmi.RemoteException, Exception;
}
