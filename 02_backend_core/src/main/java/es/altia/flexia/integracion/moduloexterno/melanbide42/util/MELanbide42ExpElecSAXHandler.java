/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide42.util;

import es.altia.flexia.integracion.moduloexterno.melanbide42.vo.MELanbide42FilaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide42.vo.MELanbide42ModuloVO;
import es.altia.flexia.integracion.moduloexterno.melanbide42.vo.MELanbide42TablaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide42.vo.MELanbide42XMLAltaExpediente;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author mikel
 */
public class MELanbide42ExpElecSAXHandler extends DefaultHandler {
    
    private final Map<String, String> variablesList = new LinkedHashMap<String, String>();
    private String currentField = "";
    private String currentFieldValue = "";
    private final ArrayList<MELanbide42ModuloVO> modulesList = new ArrayList<MELanbide42ModuloVO>();
    private MELanbide42ModuloVO currentModule = null;
    private MELanbide42TablaVO currentTable = null;
    private MELanbide42FilaVO currentRow = null;
    private String currentColumn = "";
    private String currentValue = "";
    
    private final static int TAG_CAMPOSVARIABLES_TYPE = 11;
    private final static int TAG_CAMPO_TYPE = 12;
    private final static int TAG_CODCAMPO_TYPE = 13;
    private final static int TAG_VALORCAMPO_TYPE = 14;
    private final static int TAG_MODULO_TYPE = 1;
    private final static int TAG_TABLA_TYPE = 2;
    private final static int TAG_FILA_TYPE = 3;
    private final static int TAG_COLUMNA_TYPE = 4;
    private final static int TAG_VALORCOLUMNA_TYPE = 5;
    private int currentTag = 0;
    
    private final static String TAG_CAMPOSVARIABLES_NAME = "CamposVariables";
    private final static String TAG_CAMPO_NAME = "Campo";
    private final static String TAG_CODCAMPO_NAME = "CodCampo";
    private final static String TAG_VALORCAMPO_NAME = "ValorCampo";
    private final static String TAG_MODULO_NAME = "CodigoModulo";
    private final static String TAG_TABLA_NAME = "Tabla";
    private final static String TAG_FILA_NAME = "Fila";
    private final static String TAG_COLUMNA_NAME = "Columna";
    private final static String TAG_VALORCOLUMNA_NAME = "ValorColumna";
    
    private final static String ATTRIBUTE_MODULOCOD_NAME = "cod";
    private final static String ATTRIBUTE_TABLANAME_NAME = "name";

    @Override
    public void startDocument() throws SAXException {
        //System.out.println("Starting SAX parser");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        
        // Cambia el tipo de elemento que se est� leyendo
        if (qName.equals(TAG_CAMPOSVARIABLES_NAME)){
            currentTag = TAG_CAMPOSVARIABLES_TYPE;
            
        } else if (qName.equals(TAG_CAMPO_NAME)){
            currentTag = TAG_CAMPO_TYPE;
            currentField = "";
            currentFieldValue = "";
        
        } else if (qName.equals(TAG_CODCAMPO_NAME)){
            currentTag = TAG_CODCAMPO_TYPE;
        
        } else if (qName.equals(TAG_VALORCAMPO_NAME)){
            currentTag = TAG_VALORCAMPO_TYPE;
        
        } else if (qName.equals(TAG_MODULO_NAME)){
            currentModule = new MELanbide42ModuloVO(attributes.getValue(ATTRIBUTE_MODULOCOD_NAME));
            modulesList.add(currentModule);
            currentTag = TAG_MODULO_TYPE;
        
        } else if (qName.equals(TAG_TABLA_NAME)){
            currentTable = new MELanbide42TablaVO(attributes.getValue(ATTRIBUTE_TABLANAME_NAME), currentModule);
            currentModule.addTabla(currentTable);
            currentTag = TAG_TABLA_TYPE;
        
        } else if (qName.equals(TAG_FILA_NAME)){
            currentRow = new MELanbide42FilaVO(currentTable);
            currentTable.addFila(currentRow);
            currentTag = TAG_FILA_TYPE;

        } else if (qName.equals(TAG_COLUMNA_NAME)){
            currentTag = TAG_COLUMNA_TYPE;

        } else if (qName.equals(TAG_VALORCOLUMNA_NAME)){
            currentTag = TAG_VALORCOLUMNA_TYPE;
        }
    
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (currentTag == TAG_CAMPOSVARIABLES_TYPE) {
            // Do nothing
        } else if (currentTag == TAG_CAMPO_TYPE){
            // Do nothing
        } else if (currentTag == TAG_CODCAMPO_TYPE){
            currentField += String.valueOf(ch).substring(start, (start + length)).trim();
        } else if (currentTag == TAG_VALORCAMPO_TYPE){
            currentFieldValue += String.valueOf(ch).substring(start, (start + length)).trim();
        } else if (currentTag == TAG_MODULO_TYPE){
            // Do nothing
        } else if (currentTag == TAG_TABLA_TYPE){
            // Do nothing
        } else if (currentTag == TAG_FILA_TYPE){
            // Do nothing
        } else if (currentTag == TAG_COLUMNA_TYPE){
            currentColumn += String.valueOf(ch).substring(start, (start + length)).trim();
        } else if (currentTag == TAG_VALORCOLUMNA_TYPE) {
            currentValue +=  String.valueOf(ch).substring(start, (start + length)).trim();
            currentRow.addCampo(currentColumn, currentValue);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (currentTag == TAG_CAMPOSVARIABLES_TYPE) {
            currentTag--;
        } else if (currentTag == TAG_CAMPO_TYPE){
            currentTag--;
        } else if (currentTag == TAG_CODCAMPO_TYPE){
            // Do nothing
        } else if (currentTag == TAG_VALORCAMPO_TYPE){
            // Debe ir siempre valorCampo tras codCampo en el XML
            variablesList.put(currentField, currentFieldValue);
        } else if (currentTag == TAG_MODULO_TYPE){
            currentTag--;
        } else if (currentTag == TAG_TABLA_TYPE){
            currentTag--;
        } else if (currentTag == TAG_FILA_TYPE){
            // TODO ñapa: pilla caracteres en blanco al final y añade un campo vacío a cada fila
            if (currentColumn.length() > 0){
                currentRow.addCampo(currentColumn, currentValue);
            }
            currentTag--;
        } else if (currentTag == TAG_COLUMNA_TYPE){
            currentTag--;
        } else if (currentTag == TAG_VALORCOLUMNA_TYPE) {
            // Se borra el campo actual
            currentColumn = "";
            currentValue = "";
            currentTag--;
        }
    }

    
    public ArrayList<MELanbide42ModuloVO> getModuleList(){
        return modulesList;
    }
    
    public Map<String, String> getVariablesList(){
        return variablesList;
    }
    
    public MELanbide42XMLAltaExpediente getXMLAltaExpediente(){
        return new MELanbide42XMLAltaExpediente(this.variablesList, this.modulesList);
    }
}