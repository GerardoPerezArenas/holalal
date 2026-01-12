/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.lanbide.merkalan.exception;

import javax.xml.rpc.ServiceException;

/**
 *
 * @author pablo.bugia
 */
public class RemoteServiceException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public RemoteServiceException() {
        super();
    }

    public RemoteServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteServiceException(String message) {
        super(message);
    }

    public RemoteServiceException(Throwable cause) {
        super(cause);
    }
}
