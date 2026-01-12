package es.altia.flexia.integracion.moduloexterno.melanbide62.vo;

import es.altia.flexia.integracion.moduloexterno.melanbide62.util.MeLanbide62Utils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class InfoTramiteVO {
    private int codTramite;
    private int ocurrencia;
    // Propiedades que guardan valores de campos suplementarios del tr�mite
    private Date fSolPago;
    private Date fDesdePago;
    private Date fHastaPago;
    private double importePago;
    private String refIkusPago;
    // Propiedades que almacenan valores que se crean a partir de las otras propiedades
    private String fSolPagoStr;
    private String fDesdePagoStr;
    private String fHastaPagoStr;
    private long diasSolicitud;
    private String importePagoFormat;
  

    /**
     * @return the codTramite
     */
    public int getCodTramite() {
        return codTramite;
    }

    /**
     * @param codTramite the codTramite to set
     */
    public void setCodTramite(int codTramite) {
        this.codTramite = codTramite;
    }

    /**
     * @return the ocurrencia
     */
    public int getOcurrencia() {
        return ocurrencia;
    }

    /**
     * @param ocurrencia the ocurrencia to set
     */
    public void setOcurrencia(int ocurrencia) {
        this.ocurrencia = ocurrencia;
    }

    /**
     * @return the fSolPago
     */
    public Date getfSolPago() {
        return fSolPago;
    }
    
    /**
     * @param formato el formato que queremos que tenga el string de fSolPago
     */
    public void setfSolPagoStr(String formato) {
        SimpleDateFormat format = new SimpleDateFormat(formato);
        if(fSolPago!=null)
            fSolPagoStr = format.format(fSolPago);
        else fSolPagoStr = "-";
    }
    
    /**
     * @return el string de fSolPago 
     */
    public String getfSolPagoStr() {
        return fSolPagoStr;
    }

    /**
     * @param fSolPago the fSoliPago to set
     */
    public void setfSolPago(Date fSolPago) {
        this.fSolPago = fSolPago;
    }

    /**
     * @return the fDesdePago
     */
    public Date getfDesdePago() {
        return fDesdePago;
    }
    
    /**
     * @param formato el formato que queremos que tenga el string de fDesdePago
     */
    public void setfDesdePagoStr(String formato) {
        SimpleDateFormat format = new SimpleDateFormat(formato);
        if(fDesdePago!=null)
            fDesdePagoStr = format.format(fDesdePago);
        else fDesdePagoStr = "-";
    }
    
    /**
     * @return el string de fDesdePago
     */
    public String getfDesdePagoStr() {
        return fDesdePagoStr;
    }

    /**
     * @param fDesdePago the fDesdePago to set
     */
    public void setfDesdePago(Date fDesdePago) {
        this.fDesdePago = fDesdePago;
    }

    /**
     * @return the fHastaPago
     */
    public Date getfHastaPago() {
        return fHastaPago;
    }
    
    /**
     * @param formato el formato que queremos que tenga el string de fHastaPago
     */
    public void setfHastaPagoStr(String formato) {
        SimpleDateFormat format = new SimpleDateFormat(formato);
        if(fHastaPago!=null)
            fHastaPagoStr = format.format(fHastaPago);
        else fHastaPagoStr = "-";
    }
    
    /**
     * @return el string de fHastaPago 
     */
    public String getfHastaPagoStr() {
        return fHastaPagoStr;
    }
    
    /**
     * Establece la propiedad diasSolicitud al valor de la diferencia en d�as entre fDesdePago y fHastaPago
     */
    public void setDiasSolPago(){
       // long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000; //Milisegundos al d�a 
        
        long diferencia = 0;
        if(fHastaPago!=null && fDesdePago!=null){
            
         //  diferencia =  fHastaPago.getTime() - fDesdePago.getTime() ;      
           // TimeUnit unidad = TimeUnit.DAYS;
            
           //  diasSolicitud = unidad.convert(diferencia, TimeUnit.MILLISECONDS);
           GregorianCalendar dateIni = new GregorianCalendar();
           
           dateIni.setTime(fDesdePago);
            GregorianCalendar dateFin = new GregorianCalendar();
            dateFin.setTime(fHastaPago); 
            
            int anioFecha1= dateIni.get(GregorianCalendar.YEAR);
            int anioFecha2= dateFin.get(GregorianCalendar.YEAR);
             if(anioFecha1!=anioFecha2){
                 //Calcular los dias de la fecha inicio hast ael 31/12 de ese anio
                GregorianCalendar dateIniDici = new GregorianCalendar(anioFecha1, 11, 31);
                int diasFecha1 = dateIniDici.get(GregorianCalendar.DAY_OF_YEAR) - dateIni.get(GregorianCalendar.DAY_OF_YEAR);
                diasFecha1++;
             // dateIniDici.set(anioFecha1, 11, 31);
             // int diasFEcha1 = (dateIniDici.get(GregorianCalendar.DAY_OF_YEAR) - dateFin.get(GregorianCalendar.DAY_OF_YEAR)  );
                GregorianCalendar dateFinEne = new GregorianCalendar(anioFecha2, 0, 1);
                int diasFecha2 = dateFin.get(GregorianCalendar.DAY_OF_YEAR) - dateFinEne.get(GregorianCalendar.DAY_OF_YEAR);
                diasFecha2++;
             // dateFinEne.set(anioFecha2, 0, 1);
             // int diasFEcha2 = (dateFin.get(GregorianCalendar.DAY_OF_YEAR) - dateFinEne.get(GregorianCalendar.DAY_OF_YEAR) );
                int disAniosINtermedios = 0;
                for(int anioIntermedio =(anioFecha1+1);anioIntermedio<anioFecha2;anioIntermedio++ ){
                    GregorianCalendar dateI = new GregorianCalendar();
                    dateI.set(anioIntermedio, 0, 1);
                    GregorianCalendar dateF = new GregorianCalendar();
                    dateF.set(anioIntermedio, 11, 31);
                  //disAniosINtermedios += (dateF.get(GregorianCalendar.DAY_OF_YEAR) - dateI.get(GregorianCalendar.DAY_OF_YEAR) + 1);
                    disAniosINtermedios +=((dateF.get(GregorianCalendar.DAY_OF_YEAR) - dateI.get(GregorianCalendar.DAY_OF_YEAR)));
                    disAniosINtermedios++;

                }
                diasSolicitud = diasFecha1 + disAniosINtermedios + diasFecha2;
            }else{
                diasSolicitud = (dateFin.get(GregorianCalendar.DAY_OF_YEAR) - dateIni.get(GregorianCalendar.DAY_OF_YEAR));
                diasSolicitud++;
            }
            
        }
    }
    /**
     * @return la diferencia en d�as entre fDesdePago y fHastaPago contenido en la propiedad diasSolicitud
     */
    public long getDiasSolPago(){
        return diasSolicitud;
    }
    
    /**
     * Devuelde la diferencia en d�as entre fDesdePago y fHastaPago como String
     */
    public String getDiasSolPagoStr(){
        long dif = getDiasSolPago();
        
        if(fHastaPago==null || fDesdePago==null) return "-";
        return String.valueOf(dif);
    }
    
    

    /**
     * @param fHastaPago the fHastaPago to set
     */
    public void setfHastaPago(Date fHastaPago) {
        this.fHastaPago = fHastaPago;
    }

    /**
     * @return the importePago
     */
    public double getImportePago() {
        return importePago;
    }

    /**
     * @param importePago the importePago to set
     */
    public void setImportePago(double importePago) {
        this.importePago = importePago;
    }

    /**
     * @return the refIkusPago
     */
    public String getRefIkusPago() {
        if(refIkusPago!=null)
            return refIkusPago;
        return "-";
    }

    /**
     * @param refIkusPago the refIkusPago to set
     */
    public void setRefIkusPago(String refIkusPago) {
        this.refIkusPago = refIkusPago;
    }

    /**
     * @return the importePagoFormat
     */
    public String getImportePagoFormat() {
        return importePagoFormat;
    }

    /**
     * formatea el importe del pago a un string con , decimal y . separanndo miles
     */
    public void setImportePagoFormat() {
        this.importePagoFormat = MeLanbide62Utils.doubleToFormattedString(this.importePago);
    }
    
    
}

