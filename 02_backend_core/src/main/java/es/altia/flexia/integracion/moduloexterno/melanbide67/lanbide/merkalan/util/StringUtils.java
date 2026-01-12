/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.lanbide.merkalan.util;

/**
 *
 * @author pablo.bugia
 */
public abstract class StringUtils {

    public static final String EMPTY_STRING = "";

    public static boolean isNullOrBlank(final String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isNotNullOrBlank(final String value) {
        return !isNullOrBlank(value);
    }

    public static String toNonNullValue(final String value) {
        return value != null ? value : "";
    }

    public static String concatValues(String defaultValueIfEmpty, Character separator, String... values) {
        String def = defaultValueIfEmpty != null ? defaultValueIfEmpty : StringUtils.EMPTY_STRING;

        StringBuilder sb = new StringBuilder();

        for (String str : values) {
            if (StringUtils.isNotNullOrBlank(str)) {
                if (sb.length() > 0) {
                    sb.append(" ").append(separator).append(" ");
                }
                sb.append(str.trim());
            }
        }

        String retStr = sb.toString();
        return StringUtils.isNotNullOrBlank(retStr) ? retStr : def;
    }
}
