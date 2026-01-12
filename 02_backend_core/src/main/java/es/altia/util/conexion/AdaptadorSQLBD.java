package es.altia.util.conexion;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.technical.PortableContext;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException; 
import java.util.Properties;

/**
 * Clase abstracta encargada de servir de esqueleto a cada implementación
 * particular del OAD
 * <p>Título: </p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: Altia Consultores & AytosCPD</p>
 * @author Manuel Vera Silvestre
 * @version 1.0
 */
public class AdaptadorSQLBD implements AdaptadorSQL{

   private AdaptadorSQLBD instance = null;
   private String[] parametros = null;
   private String tipoGestor = "";
   private static final String JNDI_PREFIX = "java:comp/env/";
   public String COLLATE_LATIN = "";
   protected static Logger m_Log =
            LogManager.getLogger(AdaptadorSQLBD.class.getName());


   /**
    * Método encargada de inicializar la instancia única
    * @param params Array con los parametros de la conexion
    */
   public AdaptadorSQLBD(String[] params){
	parametros = params;	
       if (parametros[0].equalsIgnoreCase(ConstantesDatos.ORACLE)) {	
           instance = new AdaptadorSQLOracle();	
           tipoGestor = ConstantesDatos.ORACLE;	
       } else if (parametros[0].equalsIgnoreCase(ConstantesDatos.SQLSERVER)) {	
           COLLATE_LATIN = " COLLATE Latin1_General_CI_AI ";	
           instance = new AdaptadorSQLSqlServer();	
           tipoGestor = ConstantesDatos.SQLSERVER;	
       }

   }

   /**
    * Constructor por defecto
    */
   protected AdaptadorSQLBD(){
       m_Log = LogManager.getLogger(this.getClass().getName());
   }

   /**
    * Funcion para la adapter de datos
    * @param dato La constante o campo de la base de datos a convertir
    * @param tipo El tipo de datos al que hay que convertir el primer parámetro
    * @param fmt El formato en el que se mostaran los datos
    * @return El String adecuado para realizar la conversión
    */
   public String convertir(String dato, int tipo, String fmt){
	    return instance.convertir(dato,tipo,fmt);
   }

   /**
    * Funcion para concatenar una serie de cadenas
    * @param values Las cadenas a concatenar
    * @return El String concatenado con la función que corresponda
    */
   public String concat(String[] values){
	    return instance.concat(values);
   }

   /**
    * Función para el tratamiento de fechas
    * @param valor La cadena representando el valor de la fecha o el campo de
    * la base de datos a tratar en formato dd/MM/yyyy para fecha, dd/MM/yyyy
    * HH:mm:ss para fecha-hora y HH:mm:ss para hora
    * @param tipoCampo El tipo de valor fecha de que se trata
    * @return El String adecuado para tratar la fecha
    * @throws BDException
    */
   public String fechaHora(String valor, int tipoCampo) throws BDException{
	    return instance.fechaHora(valor,tipoCampo);
   }

   /**
    * Función encargada del tratamiento de cadenas
    * @param funcion El tipo de función de cadena a aplicar
    * @param params Los elementos que se necesitan para aplicar la función
    * seleccionada en el primer argumento
    * @return El String adecuado para llevar a cabo la función
    */
   public String funcionCadena(int funcion, String[] params){
	    return instance.funcionCadena(funcion,params);
   }

   /**
    * Se encarga de tratar las funciones de fecha
    * @param funcion El tipo de función de fecha a aplicar
    * @param params Los elementos que se necesitan para aplicar la función
    * seleccionada en el primer argumento
    * @return El String adecuado para ejecutar la funcion
    */
   public String funcionFecha(int funcion, String[] params){
	    try 
            {
                return instance.funcionFecha(funcion,params);
            }catch (Exception e){
                  e.printStackTrace();
            }
            return null;
   }

   /**
    * Se encarga de tratar las funciones matemáticas
    * @param funcion El tipo de función matemática:
    * @param params Los elementos que se necesitan para aplicar la función
    * seleccionada en el primer argumento
    * @return El String adecuado para ejecutar la función
    */
   public String funcionMatematica(int funcion, String[] params){
	   return instance.funcionMatematica(funcion,params);
   }

   /**
    * Se encarga del tratamientode las funciones del sistema
    * @param funcion El tipo de función de sistema:
    * @param params Los elementos que se necesitan para aplicar la función
    * seleccionada en el primer argumento
    * @return El String adecuado para ejecutar la función
    */
   public String funcionSistema(int funcion, String[] params){
	return instance.funcionSistema(funcion,params);
   }

   /**
     * Devuelve el simbolo empleado por la BD para representar la concatenacion de cadenas
     * @return El simbolo de concatenacion
     */
    public String getSymbolConcat() {
        return instance.getSymbolConcat();
    }

   /**
    * Función encargadada del tratamiento de agrupaciones
    * @param camposAgrupacion vector con parejas NOMBRE (del campo), POSICION
    * (dentro de la select)
    * @return El String adecuado para realizar la agrupación
    */
   public String group(String[] camposAgrupacion){
	   return instance.group(camposAgrupacion);
   }

   /**
    * Función encargadada del tratamiento de los rollup en la agrupaciones
    * @param camposAgrupacion vector con los NOMBRES (del campo)
    * @return El String adecuado para realizar la agrupación
    */    
   public String rollup(String[] camposAgrupacion){
       return instance.rollup(camposAgrupacion);
   }

  /**
   * @deprecated 
    * Se encarga de generar una línea de join por la izquierda
    * @param campoIzqda
    * @param campoDrcha
    * @return El String adecuado para ejecutar la función
    */
   public String joinLeft(String campoIzqda, String campoDrcha){
	   return instance.joinLeft(campoIzqda, campoDrcha);
   }

  /**
   * @deprecated 
    * Se encarga de generar tantas líneas de join por la izquierda como
    * parámetros se le pasan
    * @param camposIzqda
    * @param camposDrcha
    * @return El String adecuado para ejecutar la función
    */
   public String joinLeft(String camposIzqda[], String camposDrcha[]){
	   return instance.joinLeft(camposIzqda, camposDrcha);
   }

  /**
   * @deprecated 
    * Se encarga de generar una línea de join por la derecha
    * @param campoIzqda
    * @param campoDrcha
    * @return El String adecuado para ejecutar la función
    */
   public String joinRight(String campoIzqda, String campoDrcha){
	   return instance.joinRight(campoIzqda, campoDrcha);
   }

  /**
   * @deprecated 
    * Se encarga de generar tantas líneas de join por la derecha como
    * parámetros se le pasan
    * @param camposIzqda
    * @param camposDrcha 
    * @return El String adecuado para ejecutar la función
    */
   public String joinRight(String camposIzqda[], String camposDrcha[]){
	   return instance.joinRight(camposIzqda, camposDrcha);
   }


   /**
    * Función encargada de realizar los joins sobre tablas. De momento no
    * soporta joins anidados.
    * @param parteFrom String con la parte from de una sentencia (solo el nombre
    * de las tablas o los alias empleados)
    * @param parteWhere String con la parte where de una sentencia (solo la de
    * la sentencia, sin las condiciones de los joins)
    * @param params Corresponde a la siguiente secuencia de información:
    * Tabla principal, tipo de unión 1 ("Left" o "Inner"), tabla
    * secundaria 1, condición de unión 1, tipo de unión 2, tabla secundaria 2,
    * condición de unión 2, ... Deben ir OBLIGATORIAMENTE en ese orden. El
    * último parámetro tendrá el valor "true" si se anida y "false" en caso
    * contrario
    * @return El string adecuado para llevar a cabo el join
    * @throws BDException
    */
   public String join(String parteFrom, String parteWhere, String[] params)
	   throws BDException{
	   return instance.join(parteFrom,parteWhere,params);
   }

   /**
    * Función encargada de tratamiento de la cláusula like
    * @param campo El campo de base de datos o valor constante a comparar
    * @param valor Patrón de comparación. Puede contener los caracteres comodín
    * _ (para un solo caracter) y % (para 0 o más caracteres)
    * @param caracter El caracter de escape que queramos especificar
    * @return El String adecuado para agregar la cláusula like
    */
   public String like(String campo, String valor, String caracter){
	   return instance.like(campo,valor,caracter);
   }

   /**
    * Esta función resuelve las ordenaciones de sentencias
    * @param params vector con parejas NOMBRE (del campo), POSICION
    * (dentro de la select)
    * @return El String adecuado para la cláusula ORDERBY
    */
   public String orderUnion(String[] params){
	   return instance.orderUnion(params);
   }

    /**
     * Método que se encarga de devolver el numero correspondiente de la secuencia, aumentandolo en 1 en la bd
     * @param parametros los parametros de conexion a la BD
     * @param secuencia el nombre de la secuencia
     * @throws BDException
     */
    public long devolverNextValSecuencia(String[] parametros, String secuencia) throws BDException {
        return instance.devolverNextValSecuencia(parametros, secuencia);
    }

    /**
     * Función que introduce las comillas simples a un campo que es de tipo String y además comprueba que no tenga otras
     * comillas simples por el medio
     * @param dato El valor sin las comillas simples
     * @return El valor con las comillas simples
     */
    public String addString(String dato){
        return instance.addString(dato);
    }

   /**
     * Función que realiza la operacion MINUS
     * @param sentencia Consulta original a la que se le desea añadir el operador
     * @param condicionSelect Consulta con las tuplas a restar
     * @return String a añadir al final de la consulta original
     */
    public String minus(String sentencia, String condicionSelect){
        return instance.minus(sentencia,condicionSelect);
    }

    /**
     * Función que realiza la operacion INTERSECT
     * @param sentencia Consulta original a la que se le desea añadir el operador
     * @param condicionSelect Consulta con las tuplas que se desean intersecar
     * @return String a añadir al final de la consulta original
     */
    public String intersect(String sentencia, String condicionSelect){
        return instance.intersect(sentencia,condicionSelect);
    }


   /**
    * Función que nos devuelve una conexión a la base de datos
    * @return El Connection correspondiente
    * @throws BDException
    */
   public Connection getConnection() throws BDException{
	Connection con = null;
                    if (m_Log.isErrorEnabled()) m_Log.debug("datasource: " + parametros[6]);
	if(parametros[6] == null){//no hay DataSource
	   String nombredriver = parametros[1];
	   if (m_Log.isDebugEnabled()) m_Log.debug("driver: " + nombredriver);
	   String url = parametros[2];
	   if (m_Log.isDebugEnabled()) m_Log.debug("url" + url);
	   String usuario = parametros[3];
	   if (m_Log.isDebugEnabled()) m_Log.debug("usuario: " + usuario);
	   String password = parametros[4];
	   if (m_Log.isDebugEnabled()) m_Log.debug("password" + password);
	   String ficheroLog = parametros[5];
	   if (m_Log.isDebugEnabled()) m_Log.debug("fichero de log: " + ficheroLog);
	   try{
		Driver driver = (Driver)Class.forName(nombredriver).newInstance();
		Properties p = new Properties();
		p.put("user",usuario);
		p.put("password",password);
		con = driver.connect(url,p);
		if (m_Log.isDebugEnabled()) m_Log.debug("Conexión adquirida en OAD");
	   }
	   catch(SQLException sqle){
		if (m_Log.isDebugEnabled()) m_Log.error("*** AdaptadorSQLBDOracle." + sqle.toString());
		throw new BDException(100,"Error, no se pudo obtener la conexión a " +
					    " la base de datos en la función getConnection "
					    + " sin DataSource ", sqle.toString());
	   }
	   catch(Exception e){
		if(m_Log.isErrorEnabled()) m_Log.error("*** AdaptadorSQLBDOracle." + e.toString());
		throw new BDException(100,"Error, excepcion capturada en la funcion " +
					    " getConnection ",e.toString());
	   }
	}
	else{
	   try{
		con = getDataSource(parametros[6]).getConnection();
		m_Log.debug("Conexión adquirida en OAD");
	   }
	   catch(SQLException sqle){
		if(m_Log.isErrorEnabled()) m_Log.error("*** AdaptadorSQLBD: " + sqle.toString());
		throw new BDException(100,"Error, no se pudo obtener la conexión a " +
					    " la base de datos en la función getConnection "
					    + " con DataSource ", sqle.toString());
	   }
	}
	return con;
   }

   /**
    * Método que se encarga de liberar la conexión con la base de datos
    * @param conexion la conexión a liberar
    * @throws BDException
    */
   public void devolverConexion(Connection conexion) throws BDException{
	try{
	   if(conexion != null && !conexion.isClosed()) conexion.close();
	   m_Log.debug("Conexión cerrada en el OAD");
	}
	catch(SQLException sqle){
	   if(m_Log.isErrorEnabled()) m_Log.error("*** ConexionBD: "+ sqle.toString());
	   throw new BDException(999,"Error, no se pudo cerrar la conexion en " +
					 "el método devolverConexion", sqle.toString());
	}
   }

   /**
    * Método que ha de llamarse antes de ejecutar cualquier transacción
    * @param conexion La conexion sobre la que vamos a operar
    * @throws BDException
    */
   public void inicioTransaccion(Connection conexion) throws BDException{
	try {
	   conexion.setAutoCommit(false);
	}
	catch (SQLException e) {
	   if(m_Log.isErrorEnabled()) m_Log.error("*** AdaptadorSQLBD: " + e.toString());
	   throw new BDException(999,"Error, imposible iniciar transacción en " +
					 "el método inicioTransacción", e.toString());
	}
   }

   /**
    * Método que hay que llamar al finalizar la transacción ya que salva los
    * cambios realizados en la base de datos
    * @param conexion La conexión utilizada
    * @throws BDException
    */
   public void finTransaccion(Connection conexion) throws BDException{
	try{
	   conexion.commit();
	   conexion.setAutoCommit(true);
	}
	catch (SQLException e) {
	   if(m_Log.isErrorEnabled()) m_Log.error("*** AdaptadorSQLBD: " + e.toString());
	   throw new BDException(999,"Error, imposible finalizar transacción " +
					 "en el método finTransacción", e.toString());
	}
   }

   public void cambiarNivelTransaccion(Connection conexion,int nivel) throws BDException{
	try{
	   conexion.setTransactionIsolation(nivel);
	}
	catch (SQLException e) {
	   if(m_Log.isErrorEnabled()) m_Log.error("*** AdaptadorSQLBD: " + e.toString());
	   throw new BDException(999,"Error, imposible imposible cambiar el nivel de " +
	   "transacción en cambiarNivelTransaccion", e.toString());
	}
   }

   /**
    * Cancela la transacción actual.
    * @param conexion La conexión utilizada
    * @throws BDException
    */
   public void rollBack(Connection conexion) throws BDException{
	try{
	   conexion.rollback();
	   conexion.setAutoCommit(true);
	}
	catch (SQLException e){
	   if(m_Log.isErrorEnabled()) m_Log.error("*** AdaptadorSQLBD: " + e.toString());
	   throw new BDException(999,"Error, imposible hacer rollback en el " +
					 "método rollBack", e.toString());
	}
   }

   /**
     * The global method escape, as per ECMA-262 15.1.2.4.
     * Includes code for the 'mask' argument supported by the C escape
     * method, which used to be part of the browser imbedding.  Blame
     * for the strange constant names should be directed there.
     */
   public static String js_escape(String entrada) {
    if(entrada == null) return entrada;
    final int
	URL_XALPHAS = 1, URL_XPALPHAS = 2, URL_PATH = 4;
	String s = entrada;
	int mask = URL_XALPHAS | URL_XPALPHAS | URL_PATH;
	StringBuffer R = new StringBuffer();
	for (int k = 0; k < s.length(); k++) {
	   int c = s.charAt(k), d;
	   if (mask != 0 && ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') ||
				   (c >= 'a' && c <= 'z') || c == '@' || c == '*' ||
				   c == '_' || c == '-' || c == '.' ||
				   ((c == '/' || c == '+') && mask > 3)))
		R.append((char)c);
	   else if (c < 256) {
		if (c == ' ' && mask == URL_XPALPHAS) {
		   R.append('+');
		}
		else {
		   R.append('%');
		   R.append(hex_digit_to_char(c >>> 4));
		   R.append(hex_digit_to_char(c & 0xF));
		}
	   }
	   else {
		R.append('%');
		R.append('u');
		R.append(hex_digit_to_char(c >>> 12));
		R.append(hex_digit_to_char((c & 0xF00) >>> 8));
		R.append(hex_digit_to_char((c & 0xF0) >>> 4));
		R.append(hex_digit_to_char(c & 0xF));
	   }
	}
	return R.toString();
    }



    /**
     * The global unescape method, as per ECMA-262 15.1.2.5.
     */
    public static String js_unescape(String entrada){
      if(entrada == null) return entrada;
      String s = entrada;
	  int firstEscapePos = s.indexOf('%');
	  if (firstEscapePos >= 0) {
		int L = s.length();
		char[] buf = s.toCharArray();
		int destination = firstEscapePos;
		for (int k = firstEscapePos; k != L;) {
		    char c = buf[k];
		    ++k;
		    if (c == '%' && k != L) {
			  int end, start;
			  if (buf[k] == 'u') {
				start = k + 1;
				end = k + 5;
			  }
			  else {
				start = k;
				end = k + 2;
			  }
			  if (end <= L) {
				int x = 0;
				for (int i = start; i != end; ++i) {
				    x = (x << 4) | xDigitToInt(buf[i]);
				}
				if (x >= 0) {
				    c = (char)x;
				    k = end;
				}
			  }
		    }
		    buf[destination] = c;
		    ++destination;
		}
		s = new String(buf, 0, destination);
	  }
	  return s;
    }

    
    public String valorJoin() {
        return instance.valorJoin();
    }
    
    /**
     * Method hex_digit_to_char
     * @param    x                   an int
     * @return   a char
     */
    private static char hex_digit_to_char(int x) {
	 return (char)(x <= 9 ? x + '0' : x + ('A' - 10));
    }

    /**
     * Method xDigitToInt
     * @param    c                   an int
     * @return   an int
     *
     */
    private static int xDigitToInt(int c) {
	 if ('0' <= c && c <= '9') { return c - '0'; }
	 if ('a' <= c && c <= 'f') { return c - ('a' - 10); }
	 if ('A' <= c && c <= 'F') { return c - ('A' - 10); }
	 return -1;
    }

   public DataSource getDataSource(String jndi){
	DataSource ds = null;
	synchronized (this) {
	   try{
		PortableContext pc = PortableContext.getInstance();
		if (m_Log.isDebugEnabled()) m_Log.debug("He cogido el jndi: " + jndi);
		//ds = (DataSource)pc.lookup(JNDI_PREFIX + jndi, DataSource.class);
		ds = (DataSource)pc.lookup(jndi, DataSource.class);
	   }
	   catch(TechnicalException te){
		if(m_Log.isErrorEnabled()) m_Log.error("*** AdaptadorSQLBD: " + te.toString());
	   }
	}
	return ds;
   }

    public String consultaSinAcentos(Connection conexion, String[] params) {
         return instance.consultaSinAcentos(conexion,params);
    }

   public String whereEsNumero(String cadena) {
       return instance.whereEsNumero(cadena);
   }

   public String whereNoEsNumero(String cadena) {
       return instance.whereNoEsNumero(cadena);
   }
   
   /** Esta funci?n no se utiliza en oracle */
   public String castFecha(String dato){
       return "";
   }

   public String consultaSinTabla(){
       return instance.consultaSinTabla();
   }
   
      public String tamanoTexto(String dato){
       return instance.tamanoTexto( dato);
   }

    /**
        * Recupera un substring dentro de otro
        * @param value_expression: String sobre el que se realiza la búsqueda
        * @param start_expression: Posición a partir de la cual se comienza a buscar
        * @param length_expression: Posición hasta la que se busca
        * @return
        */
    public String substr(String value_expression, String start_expression, String length_expression ){
        return instance.substr(value_expression, start_expression, length_expression);
   }

    /**
     *  Posición de una determinada subcadena dentro de otra cadena
     * @param buscar: Cadena a buscar
     * @param contenedor: String que contiene la cadena buscada y referencia por el parámetro buscar
     * @param posicion: Posició a partir de la cual se comienza a buscar
     * @param ocurrencia: Primera ocurrencia a buscar
     * @return String
     */
   public String charIndex(String buscar,String contenedor,String posicion, String ocurrencia){
       return instance.charIndex(buscar, contenedor, posicion, ocurrencia);
   }
   
   
    public String diferenciaMeses(String fechaI, String fechaF){
       return instance.diferenciaMeses(fechaI, fechaF);
   }

    public String[] getParametros(){
        return this.parametros;
    }
    
     public String getTipoGestor() {	
        return this.tipoGestor;	
    }
}