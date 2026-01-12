package es.altia.flexia.integracion.moduloexterno.melanbide03.vo;

public class CertRd34VO {

    private String numExpediente;

    private String nif;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String fec_nacimiento;
    private String sexo;
    private String nacion;
    private String ccaa;
    private String provincia;
    private String localidad;
    private String domicilio;
    private String cp;
    private String telefono_fijo;
    private String movil;
    private String mail;
    private String provincia_registro;
    private String anio_registro;
    private String organismo;
    private String provincia_otorgamiento;
    private String fecha_otorgamiento;
    private String modo_gestion;
    private String codigo_certificado;
    //private String convocatoria;//va vacío
    //private String num_registro;//va vacío, falta en ejemplo de prueba, parece que viene en la respuesta del ws, es correlativo por comunidad autónoma y por año (00000001, 00000002, ...)
    private String fecha_registro;
    private String itinerario;//1 total, 2 parcial
    /*
    CODIGOS VIA-ACCESO
        01 Convocatoria RD 1224/2009 ? Formación no formal
        02 Convocatoria RD 1224/2009 ? Experiencia laboral
        04 Transitoria 4. No válida actualmente.
        05 Transitoria 5. No válida actualmente.
        06 Certificado de profesionalidad DEROGADO.
        07 Transitoria 7. No válida actualmente.
        08 Equivalencia sistema educativo.
        09 Acción Formativa certificado de profesionalidad.
        10 Sentencia judicial fi rme o resolución administrativa.
        11 Formación no formal y experiencia laboral .
    */
    private String via_acceso;//09 - Acción Formativa certificado de profesionalidad.
    private String fecha_acceso;//LanF pondrá en vista la de inicio de la primera formación
    //private String nombre_empresa;//va vacío
    //private String codigo_ef;//va vacío
    //private String codigo_af;//va vacío
    //private String fecha_inicio;//va vacío
    //private String fecha_final;//va vacío
    //private String uc;//va vacío
    //private String modalidad_imparticion;//va vacío
    /*
    CODIGOS MODULO
        01 Asociado a certi ficado de profesionalidad COMPLETO
        03 Exento por experiencia laboral
        04 Exento por alternancia con el empleo
        05 Módulo de Formación Práctica en centros de trabajo NO EXIGIBLE
        06 Independiente, de acumulación de acreditaciones parciales
    */
    //private String modulo;//va vacío
    private String fecha_modulo;
    //private String tipo_registro;//falta en ejemplo de prueba, se supone que siempre será "A" (alta) por defecto en el ws


    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getFec_nacimiento() {
        return fec_nacimiento;
    }

    public void setFec_nacimiento(String fec_nacimiento) {
        this.fec_nacimiento = fec_nacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNacion() {
        return nacion;
    }

    public void setNacion(String nacion) {
        this.nacion = nacion;
    }

    public String getCcaa() {
        return ccaa;
    }

    public void setCcaa(String ccaa) {
        this.ccaa = ccaa;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getTelefono_fijo() {
        return telefono_fijo;
    }

    public void setTelefono_fijo(String telefono_fijo) {
        this.telefono_fijo = telefono_fijo;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getProvincia_registro() {
        return provincia_registro;
    }

    public void setProvincia_registro(String provincia_registro) {
        this.provincia_registro = provincia_registro;
    }

    public String getAnio_registro() {
        return anio_registro;
    }

    public void setAnio_registro(String anio_registro) {
        this.anio_registro = anio_registro;
    }

    public String getOrganismo() {
        return organismo;
    }

    public void setOrganismo(String organismo) {
        this.organismo = organismo;
    }

    public String getProvincia_otorgamiento() {
        return provincia_otorgamiento;
    }

    public void setProvincia_otorgamiento(String provincia_otorgamiento) {
        this.provincia_otorgamiento = provincia_otorgamiento;
    }

    public String getFecha_otorgamiento() {
        return fecha_otorgamiento;
    }

    public void setFecha_otorgamiento(String fecha_otorgamiento) {
        this.fecha_otorgamiento = fecha_otorgamiento;
    }

    public String getModo_gestion() {
        return modo_gestion;
    }

    public void setModo_gestion(String modo_gestion) {
        this.modo_gestion = modo_gestion;
    }

    public String getCodigo_certificado() {
        return codigo_certificado;
    }

    public void setCodigo_certificado(String codigo_certificado) {
        this.codigo_certificado = codigo_certificado;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public String getItinerario() {
        return itinerario;
    }

    public void setItinerario(String itinerario) {
        this.itinerario = itinerario;
    }

    public String getVia_acceso() {
        return via_acceso;
    }

    public void setVia_acceso(String via_acceso) {
        this.via_acceso = via_acceso;
    }

    public String getFecha_acceso() {
        return fecha_acceso;
    }

    public void setFecha_acceso(String fecha_acceso) {
        this.fecha_acceso = fecha_acceso;
    }

    public String getFecha_modulo() {
        return fecha_modulo;
    }

    public void setFecha_modulo(String fecha_modulo) {
        this.fecha_modulo = fecha_modulo;
    }
}//class