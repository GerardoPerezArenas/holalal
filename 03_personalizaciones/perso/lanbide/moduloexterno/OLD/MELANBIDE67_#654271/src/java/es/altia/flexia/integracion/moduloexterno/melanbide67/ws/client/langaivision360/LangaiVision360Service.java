/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.ws.client.langaivision360;

import javax.xml.rpc.Service;

/**
 *
 * @author pablo.bugia
 */
public interface LangaiVision360Service extends Service {

    public java.lang.String getLangaiVision360PortAddress();

    public LangaiVision360 getLangaiVision360Port() throws javax.xml.rpc.ServiceException;

    public LangaiVision360 getLangaiVision360Port(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
