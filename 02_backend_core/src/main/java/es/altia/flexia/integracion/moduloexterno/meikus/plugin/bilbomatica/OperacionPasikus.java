/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica;

import java.sql.Date;

/**
 *
 * @author pablo.bugia
 */
public class OperacionPasikus {
    private String codError;
    private String msgCodError;

    public OperacionPasikus() {
    }

    public String getCodError() {
        return codError;
    }

    public void setCodError(String codError) {
        this.codError = codError;
    }

    public String getMsgCodError() {
        return msgCodError;
    }

    public void setMsgCodError(String msgCodError) {
        this.msgCodError = msgCodError;
    }  
    
}
