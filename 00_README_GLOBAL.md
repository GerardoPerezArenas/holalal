# 00_README_GLOBAL - CONTEXTO DEL PROYECTO LEGACY (FLEXIA)

## 📌 INSTRUCCIONES CRÍTICAS PARA LA IA (NotebookLM)
Este Notebook contiene un **único proyecto Java monolítico** subido en varias partes (ZIPs) debido a su tamaño.
**IMPORTANTE:**
1. **NO trates los ZIPs como proyectos distintos.** Son módulos del mismo WAR.
2. **Codificación:** Los archivos originales están en **ISO-8859-1**. Si encuentras caracteres extraños, interprétalos asumiendo esta codificación.
3. **Internal Libs:** Hay muchas dependencias `es.altia.*` y `es.lanbide.*`. Si falta su código fuente, infiere su función basándote en los nombres de los métodos y el contexto de uso.

## 🏗 ARQUITECTURA TÉCNICA (Inferida del pom.xml)
El sistema es un aplicativo web (WAR) "Legacy" con la siguiente pila tecnológica:

* **Java Version:** Java 8 (1.8).
* **Framework MVC (Frontend):** Apache Struts 1.x (Usa ActionForms, ActionMappings y JSPs).
* **Backend & DI:** Spring Framework 2.5.6 (Configurado vía XML, probable `applicationContext.xml`).
* **Web Services (SOAP):** Apache Axis 1.4 y Axis2 1.5.2 (Uso intensivo de XML-RPC y WSDLs).
* **Base de Datos:** Oracle (ojdbc8) con gestión de pools `commons-dbcp`.
* **Build System:** Maven (pero con estructura de carpetas antigua).

## 🧩 MAPA DE MÓDULOS (Estructura de subida)
El código se ha fragmentado lógicamente para su análisis:

* **01_docs_y_arquitectura:** Configuración global (`pom.xml`, `web.xml`, contextos de Spring).
* **02_backend_core:** Código fuente Java (`src/java`). Lógica de negocio, Servicios y DAOs.
* **03_frontend_jsp:** Capa de presentación (Archivos JSP, tags de Struts).
* **04_recursos_sql:** Modelos de datos y scripts.

## 🔑 FUNCIONALIDADES PRINCIPALES DETECTADAS
Al analizar el código, presta atención a estas áreas críticas:
1.  **Generación de Documentos:** Se usa JasperReports (v6), iText y Apache POI (Excel/Word).
2.  **Firma Electrónica:** Integraciones con ViaFirma, @firma y certificados digitales (Librerías `dss`, `xmlsec`, `bouncycastle`).
3.  **Integración LANBIDE:** Conexiones con servicios internos del Servicio Vasco de Empleo (ver perfiles maven `lanbide`).

---
**OBJETIVO DEL ANÁLISIS:**
El usuario solicitará ayuda para comprender flujos antiguos, refactorizar código o solucionar errores. Basa tus respuestas en la interacción entre los Action de Struts y los Servicios de Spring.