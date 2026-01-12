/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.util;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.DesplegableVO;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide67Utils {
//Logger
    private static Logger log = LogManager.getLogger(MeLanbide67Utils.class);
    public static Integer getEjercicioDeExpediente(String numExpediente) {
        try {
            String[] datos = numExpediente.split(ConstantesMeLanbide67.BARRA_SEPARADORA);
            return Integer.parseInt(datos[0]);
        } catch (Exception ex) {
            return null;
        }
    }

    public static boolean evaluarExpresionRegular(String exp, String valor) {
        try {
            Pattern patron = Pattern.compile(exp);
            Matcher encaja = patron.matcher(valor);
            return encaja.matches();
        } catch (Exception ex) {
            return false;
        }
    }

    public static String getCodigoCampoSuplementario(String campo) {
        return ConfigurationParameter.getParameter(campo, ConstantesMeLanbide67.FICHERO_PROPIEDADES);
    }

    public static Integer calcularEdad(Date feNac) {
        if (feNac != null) {
            try {
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(feNac);
                int dia = cal.get(Calendar.DAY_OF_MONTH);
                int mes = cal.get(Calendar.MONTH);
                int ano = cal.get(Calendar.YEAR);

                Date fechaHoy = new Date();
                GregorianCalendar cal2 = new GregorianCalendar();
                cal2.setTime(fechaHoy);
                int ahoraDia = cal2.get(Calendar.DAY_OF_MONTH);
                int ahoraMes = cal2.get(Calendar.MONTH);
                int ahoraAno = cal2.get(Calendar.YEAR);

                int edad = (ahoraAno + 1900) - ano;

                if (ahoraMes < (mes - 1)) {
                    edad--;
                }
                if (((mes - 1) == ahoraMes) && (ahoraDia < dia)) {
                    edad--;
                }
                if (edad > 1900) {
                    edad -= 1900;
                }

                return edad;
            } catch (Exception ex) {

            }
        }
        return null;
    }

    public static Integer calcularEdadFormulaLanb(Date feNac, Date fecIni) {
        if (feNac != null) {
            try {
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                String hoy = formato.format(fecIni);
                String naci = formato.format(feNac);
                String[] dat1 = naci.split("/");
                String[] dat2 = hoy.split("/");
                int anos = Integer.parseInt(dat2[2]) - Integer.parseInt(dat1[2]);
                int mes = Integer.parseInt(dat2[1]) - Integer.parseInt(dat1[1]);
                if (mes < 0) {
                    anos = anos - 1;
                } else if (mes == 0) {
                    //int dia = Integer.parseInt(dat1[0]) - Integer.parseInt(dat2[0]);
                    if (Integer.parseInt(dat2[0]) < Integer.parseInt(dat1[0])) {
                        anos = anos - 1;
                    }
                }
                return anos;
            } catch (Exception ex) {

            }
        }
        return null;
    }

    public static Integer calcularEdad2(Date feNac, Date fecIni) {
        if (feNac != null) {
            try {
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(feNac);
                int dia = cal.get(Calendar.DAY_OF_MONTH);
                int mes = cal.get(Calendar.MONTH);
                int ano = cal.get(Calendar.YEAR);

                /*Date fechaHoy = new Date();
                GregorianCalendar cal2 = new GregorianCalendar();
                cal2.setTime(fechaHoy);*/
                GregorianCalendar cal2 = new GregorianCalendar();
                cal2.setTime(fecIni);
                int ahoraDia = cal2.get(Calendar.DAY_OF_MONTH);
                int ahoraMes = cal2.get(Calendar.MONTH);
                int ahoraAno = cal2.get(Calendar.YEAR);

                int edad = (ahoraAno + 1900) - ano;

                if (ahoraMes < (mes - 1)) {
                    edad--;
                }
                if (((mes - 1) == ahoraMes) && (ahoraDia < dia)) {
                    edad--;
                }
                if (edad > 1900) {
                    edad -= 1900;
                }

                return edad;
            } catch (Exception ex) {

            }
        }
        return null;
    }

    public static Integer calcularMeses(Date feIni, Date feFin) {
        if (feIni != null && feFin != null) {
            try {
                Double numMeses = 0.0;

                long diferencia = Math.abs(feFin.getTime() - feIni.getTime());

                double anos = Math.floor(diferencia / (1000 * 60 * 60 * 24 * 365));
                diferencia -= anos * (1000 * 60 * 60 * 24 * 365);
                double meses = Math.floor(diferencia / (1000 * 60 * 60 * 24 * 30.4375));

                numMeses = anos * 12 + meses;

                return numMeses.intValue();
            } catch (Exception ex) {

            }
        }
        return null;
    }

    public static String redondearDecimalesString(String num, int numDecimales) {
        if (num == null) {
            return null;
        } else {
            try {
                BigDecimal bd = new BigDecimal(num);
                return redondearDecimalesString(bd, numDecimales);
            } catch (Exception ex) {

            }
            return "";
        }
    }

    public static String redondearDecimalesString(BigDecimal num, int numDecimales) {
        if (num == null) {
            return null;
        } else {
            try {
                String strCero = "0.";
                for (int i = 0; i < numDecimales; i++) {
                    strCero += "0";
                }
                BigDecimal cero = new BigDecimal(strCero);
                num = num.stripTrailingZeros();
                num = num.add(cero);
                num = num.setScale(numDecimales, RoundingMode.HALF_EVEN);
                return num.toPlainString();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return "";
        }
    }

    public static BigDecimal redondearDecimalesStringBigDecimal(String num, int numDecimales) {
        if (num == null) {
            return null;
        } else {
            try {
                BigDecimal bd = new BigDecimal(num);
                return redondearDecimalesBigDecimal(bd, numDecimales);
            } catch (Exception ex) {

            }
            return null;
        }
    }

    public static BigDecimal redondearDecimalesBigDecimal(BigDecimal num, int numDecimales) {
        if (num == null) {
            return null;
        } else {
            try {
                String strCero = "0.";
                for (int i = 0; i < numDecimales; i++) {
                    strCero += "0";
                }
                BigDecimal cero = new BigDecimal(strCero);
                num = num.stripTrailingZeros();
                num = num.add(cero);
                num.setScale(numDecimales, RoundingMode.DOWN);
                return num;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

    public static List<String> getValoresCalculo(String retorno) {
        List<String> valores = new ArrayList<String>();
        try {
            String[] valor = retorno.split(ConstantesMeLanbide67.PIPE);
            valores.add(valor[0]);
            valores.add(valor[1]);
            valores.add(valor[2]);
            return valores;
        } catch (Exception ex) {
            return null;
        }
    }// getValoresRetorno

    /**
     * Método que extrae la descripción de los desplegables en el idioma del
     * usuario, en BBDD están en un campo separadas por Pipeline |
     *
     * @param request
     * @param desplegable
     * @return la lista en el idioma de usu
     */
    public List<DesplegableVO> traducirDesplegable(HttpServletRequest request, List<DesplegableVO> desplegable) {
        for (DesplegableVO d : desplegable) {
            if (d.getDes_nom() != null && !d.getDes_nom().isEmpty()) {
                d.setDes_nom(getDescripcionDesplegable(request, d.getDes_nom()));
            }
        }
        return desplegable;
    }

    /**
     * Método que retorna el valor de un desplegable en el idiona del usuario
     *
     * @param request
     * @param descripcionCompleta
     * @return String con el valor
     */
    public String getDescripcionDesplegable(HttpServletRequest request, String descripcionCompleta) {
        String descripcion = descripcionCompleta;
        String barraIdioma = ConfigurationParameter.getParameter(ConstantesMeLanbide67.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide67.FICHERO_PROPIEDADES);
//                log.debug("Descripcion complets: " + descripcion);
//                log.debug("Barra: "+ barraIdioma);
        try {
            if (!descripcion.isEmpty()) {
                String[] descripcionDobleIdioma = descripcion.split(barraIdioma);
                if (descripcionDobleIdioma != null) {
//                    log.debug("Cast:" + descripcionDobleIdioma[0]);
//                    log.debug("Eusk:" + descripcionDobleIdioma[1]);
                    if (getIdioma(request) == ConstantesMeLanbide67.CODIGO_IDIOMA_EUSKERA) {
                        descripcion = descripcionDobleIdioma[1];
                    } else {
                        // Cogemos la primera posicion que deberia ser castellano
                        descripcion = descripcionDobleIdioma[0];
                    }
                }
            } else {
                descripcion = "-";
            }
            return descripcion;
        } catch (Exception e) {
            return descripcion;
        }
    }

    /**
     * Método que recupera el Idioma de la request para la gestion de
     * Desplegables
     *
     * @param request
     * @return int idioma 1-castellano 4-euskera
     */
    private int getIdioma(HttpServletRequest request) {
        // Recuperamos el Idioma de la request para la gestion de Desplegables
        UsuarioValueObject usuario = new UsuarioValueObject();
        int idioma = ConstantesMeLanbide67.CODIGO_IDIOMA_CASTELLANO;  // Por Defecto 1 Castellano
        try {
            if (request != null && request.getSession() != null) {
                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                }
            }
        } catch (Exception ex) {
            idioma = ConstantesMeLanbide67.CODIGO_IDIOMA_CASTELLANO;
        }
        return idioma;
    }

    /**
     * Método llamado para devolver un String en formato JSON al cliente que ha
     * realiza la petición a alguna de las operaciones de este action
     *
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a través del cual se
     * devuelve la salida al cliente que ha realizado la solicitud
     */
    public void retornarJSON(String json, HttpServletResponse response) {
        try {
            if (json != null) {
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
        }
    }
}
