/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide73.vo;

/**
 *
 * @author pablo.bugia
 */
public enum ResultadoOperacion {
    ERROR(-1),
    CORRECT(1),
    REGISTRO_EXISTENTE(2),
    REGISTRO_INEXISTENTE(3),
    UOR_INACTIVA(4);

    private ResultadoOperacion(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
    
    
    private int codigo;
}
