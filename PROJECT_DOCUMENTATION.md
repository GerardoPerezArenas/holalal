# Documentación Técnica del Proyecto Flexia (Lanbide)

Este documento proporciona una visión técnica detallada del proyecto Flexia, una personalización del producto AGORA para Lanbide. Está diseñado para facilitar la comprensión del sistema a desarrolladores y agentes de IA.

## 1. Visión General
Flexia es una aplicación web monolítica (WAR) "Legacy" diseñada para la gestión de:
*   **Registro**: Entradas y salidas de documentos.
*   **SGE (Sistema de Gestión de Expedientes)**: Tramitación de expedientes administrativos.
*   **Terceros**: Gestión de personas físicas y jurídicas.
*   **Administración**: Configuración del sistema, usuarios y permisos.

El sistema se basa en el framework propietario **AGORA** (paquete `es.altia.agora`) y contiene personalizaciones específicas para Lanbide en el paquete `es.altia.flexia`.

## 2. Stack Tecnológico
*   **Java Version**: Java 8 (1.8).
*   **Frontend**: Apache Struts 1.x (JSP, ActionForms, ActionMappings, Tiles).
*   **Backend**: Arquitectura propietaria basada en patrones Singleton (Managers). Se menciona Spring 2.5.6 en dependencias pero la inyección de dependencias principal es manual o gestionada por el framework Agora.
*   **Persistencia**: JDBC Manual (DAO Pattern) con construcción de SQL dinámica.
*   **Base de Datos**: Oracle (driver `ojdbc8`).
*   **Servicios Web**: Apache Axis 1.4 y Axis2 (SOAP/WSDL).
*   **Codificación**: **ISO-8859-1** (o ISO-8859-15). Es crítico respetar esto para evitar corrupción de caracteres.

## 3. Arquitectura del Código

### 3.1 Estructura de Directorios (Lógica)
El código fuente se encuentra en `02_backend_core`.
*   `src/main/java`: Código fuente Java.
*   `src/main/webapp`: Recursos Web (JSP, CSS, WEB-INF).
*   `src/main/resources`: Archivos de propiedades y configuración.

### 3.2 Paquetes Principales
*   **`es.altia.agora.*`**: **NÚCLEO (Core)**. Contiene la lógica base del producto.
    *   `interfaces.user.web.*`: Capa de presentación (Struts Actions y Forms).
    *   `business.*`: Capa de negocio (Managers y Value Objects).
        *   `business.registro`: Lógica de Registro.
        *   `business.sge`: Lógica de Expedientes.
        *   `business.terceros`: Lógica de Terceros.
    *   `business.*.persistence.manual`: Capa de acceso a datos (DAOs).
*   **`es.altia.flexia.*`**: **PERSONALIZACIÓN (Lanbide)**.
    *   `flexia.registro.digitalizacion`: Módulos de digitalización específicos.
    *   `flexia.tramitacion`: Extensiones de la lógica de tramitación.

## 4. Patrones de Diseño Implementados

### 4.1 Capa de Presentación (MVC - Struts)
El flujo es controlado por `struts-config.xml` (`src/main/webapp/WEB-INF/struts/`).
1.  **Request**: Llega al `ActionServlet` (o `ControllerPrincipal`).
2.  **Action**: Se ejecuta una clase `Action` (ej. `TramitacionAction`).
3.  **Form**: Los datos viajan en `ActionForm` (ej. `TramitacionForm`).
4.  **View**: Se renderiza un JSP con Taglibs de Struts (`<bean:write>`, `<html:text>`).

### 4.2 Capa de Negocio (Business Delegate / Singleton)
No se usa `@Autowired` ni inyección estándar de Spring.
*   Patrón: **Singleton Manager**.
*   Acceso: `TramitacionManager.getInstance().metodo()`.
*   Función: Orquestar lógica, validar reglas de negocio y llamar a los DAOs.

### 4.3 Capa de Persistencia (Manual DAO)
*   Patrón: **Singleton DAO**.
*   Acceso: `AnotacionRegistroDAO.getInstance().insertar(...)`.
*   **Manejo de SQL**: Concatenación de Strings manual.
    ```java
    String sql = "INSERT INTO TABLA (CAMPO) VALUES (" + valor + ")";
    ```
*   **Transacciones**: Gestión manual mediante `SigpGeneralOperations.commit()` y `rollback()`.
*   **Helper**: Uso de `AdaptadorSQLBD` para abstracción de funciones de base de datos (fechas, nulos).

## 5. Configuración y Arranque

### 5.1 `web.xml` (`01_docs_y_arquitectura/.../web.xml`)
*   Define el **`ApplicationInitContextListener`**: Responsable de inicializar el entorno "Agora" (Configuración, Cachés, etc.).
*   Filtro `ISO885915Filter`: Fuerza la codificación **ISO-8859-15** en todas las peticiones y respuestas HTTP.
*   DataSources JNDI: `jdbc_flexia_generico`, `jdbc_flexia_organizacion`.

### 5.2 Configuración Propietaria
El sistema utiliza archivos `.properties` y una tabla de configuración (probablemente en BD o archivos cifrados) accedidos vía `ConfigServiceHelper`.

## 6. Flujos Críticos

### 6.1 Alta de Registro
1.  **Vista**: `altaRE.jsp`.
2.  **Action**: `MantAnotacionRegistroAction`.
3.  **Manager**: `AnotacionRegistroManager`.
4.  **DAO**: `AnotacionRegistroDAO`. Inserta en tablas como `R_RES` (Registro Entrada/Salida).

### 6.2 Tramitación de Expedientes
1.  **Vista**: `tramitacionExpedientes.jsp`.
2.  **Action**: `TramitacionExpedientesAction`.
3.  **Manager**: `TramitacionExpedientesManager`.
4.  **Modelo de Datos**: Tablas complejas de expedientes, trámites, fases y documentos.

## 7. Guía para el Desarrollador (IA)

### Cómo crear una nueva funcionalidad
1.  **BD**: Crear la tabla o campos necesarios (Script SQL).
2.  **DAO**: Crear/Modificar el DAO en `persistence.manual`. Añadir método con SQL manual (`StringBuffer` o concatenación). **Importante**: Escapar inputs para evitar inyección SQL básica si no se usa `PreparedStatement` (aunque se recomienda usarlo).
3.  **Manager**: Crear/Modificar método en el Manager correspondiente. Gestionar la conexión (`con`) y transacción.
4.  **Struts**:
    *   Crear `ActionForm` si hay datos nuevos.
    *   Crear `Action` para manejar la petición.
    *   Registrar en `struts-config.xml`.
5.  **JSP**: Crear la vista usando tags de Struts.

### Cómo depurar errores
*   **Logs**: Buscar trazas de `Log4j` (`m_Log.error(...)`).
*   **Encoding**: Si los acentos fallan, verificar que el archivo JSP tiene encoding ISO-8859-15 (declarado en la directiva `<%@ page contentType="..." pageEncoding="ISO-8859-15" %>`) y que las cabeceras HTTP del navegador también usen ISO-8859-15.
*   **NullPointer**: Común en este código legacy si los objetos VO (Value Objects) no se inicializan correctamente.

## 8. Integraciones Externas
*   **Portafirmas**: Módulo complejo en `es.altia.flexia.portafirmas`.
*   **Lanbide**: Integraciones específicas en `es.lanbide.*` (generalmente clientes de Web Services).
*   **Digitalización**: Escaneo y OCR, lógica en `es.altia.flexia.registro.digitalizacion`.
