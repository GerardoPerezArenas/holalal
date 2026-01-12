/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.vo;

import es.altia.flexia.registro.digitalizacion.lanbide.vo.GeneralComboVO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pbugia
 */
public class DetallesCampoSuplementarioVO {
    private String valor;
    private String pcaTda;
    private String pcaTam;
    private String pcaBloq;
    private String pcaOculto;
    private String pcaDesplegable;
    private List<GeneralComboVO> desplegable;

    public DetallesCampoSuplementarioVO(String valor, String pcaTda, String pcaTam, String pcaBloq,
            String pcaOculto, String pcaDesplegable) {
        this.valor = valor;
        this.pcaTda = pcaTda;
        this.pcaTam = pcaTam;
        this.pcaBloq = pcaBloq;
        this.pcaOculto = pcaOculto;
        this.pcaDesplegable = pcaDesplegable;
        this.desplegable = new ArrayList<GeneralComboVO>();
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getPcaTda() {
        return pcaTda;
    }

    public void setPcaTda(String pcaTda) {
        this.pcaTda = pcaTda;
    }

    public String getPcaTam() {
        return pcaTam;
    }

    public void setPcaTam(String pcaTam) {
        this.pcaTam = pcaTam;
    }

    public String getPcaBloq() {
        return pcaBloq;
    }

    public void setPcaBloq(String pcaBloq) {
        this.pcaBloq = pcaBloq;
    }

    public String getPcaOculto() {
        return pcaOculto;
    }

    public void setPcaOculto(String pcaOculto) {
        this.pcaOculto = pcaOculto;
    }

    public String getPcaDesplegable() {
        return pcaDesplegable;
    }

    public void setPcaDesplegable(String pcaDesplegable) {
        this.pcaDesplegable = pcaDesplegable;
    }

    public List<GeneralComboVO> getDesplegable() {
        return desplegable;
    }

    public void setDesplegable(List<GeneralComboVO> desplegable) {
        this.desplegable = desplegable;
    }
    
    
}
