CREATE TABLE CNEE_SOXEX
( 
  SOXEX_MUN		NUMBER(3) 		NOT NULL,
  SOXEX_EJE       NUMBER (4) 	NOT NULL,
  SOXEX_PRO       VARCHAR2(5) 	NOT NULL,
  SOXEX_NUM       VARCHAR2(30) 	NOT NULL,
  SOXEX_NCON      NUMBER(6) 	NOT NULL,
  SOXEX_DOC       VARCHAR2(25) 	NOT NULL,
  SOXEX_TER_COD   NUMBER(12),
  SOXEX_NOM       VARCHAR2(80) 	NOT NULL,
  SOXEX_AP1       VARCHAR2(100) NOT NULL,
  SOXEX_AP2       VARCHAR2(100),
  SOXEX_FNA       DATE,
  SOXEX_SEX       NUMBER(2),
  SOXEX_MIN       VARCHAR2(1),
  SOXEX_INM       VARCHAR2(1),
  SOXEX_PLD       VARCHAR2(1),
  SOXEX_RML       VARCHAR2(1),
  SOXEX_OTR       VARCHAR2(1),
  SOXEX_COL       NUMBER(2),
  SOXEX_MDE       NUMBER(3),
  SOXEX_NES       NUMBER(3),
  SOXEX_TCO       NUMBER(3),
  SOXEX_TIC       NUMBER(3),
  SOXEX_FAC       DATE,
  SOXEX_TJO       VARCHAR2(5),
  SOXEX_PJT       NUMBER(5,2),
  SOXEX_FFC       DATE,
  SOXEX_DCO       NUMBER(5),
  SOXEX_CNOE      VARCHAR2(4),
  SOXEX_SAL       NUMBER(12,2),
  SOXEX_CSS       NUMBER(12,2),
  SOXEX_IMP       NUMBER(12,2),
  SOXEX_IMR       NUMBER(12,2),
  SOXEX_IRE       NUMBER(12,2)
);

COMMENT ON TABLE CNEE_SOXEX IS 'Socios por Expediente de Creación de Nuevas Estructuras Empresariales';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_MUN	 IS 'Entorno 0 - Pruebas 1 - Producción';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_EJE IS 'Ejercicio/Ano del Expediente';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_PRO IS 'Código del Procedimiento';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_NUM IS 'Número del expediente Formateado ANO/PROC/NUM';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_NCON IS 'Correlativo de Contratos para cada Expediente ';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_DOC IS 'Documento de identidad de la persona (DNI, NIE, ... Etc)';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_TER_COD IS 'Código del tercero, solo para nuevos registros dados de alta en Regexlan.';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_NOM IS 'Nombre';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_AP1 IS 'Apellido 1';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_AP2 IS 'Apellido 2';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_FNA IS 'Fecha de Nacimiento';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_SEX IS 'Sexo: 1.Hombre - 2.Mujer';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_MIN IS 'Indicador Booleano S/N de Persona Minusválida';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_INM IS 'Indicador Booleano S/N de Persona Inmigrante';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_PLD IS 'Indicador Booleano S/N de Persona Parado de larga duración';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_RML IS 'Indicador Booleano S/N de Persona Reentrante en el mercado laboral';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_OTR IS 'Indicador Booleano S/N de Persona Otros características especiales';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_COL IS 'Colectivo al que pertenece – Cod Desplegable';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_MDE IS 'Numero de Meses en desempleo';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_NES IS 'Nivel de Estudios - Cod. desplegable';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_TCO IS 'Tipo de Contrato por Duración del mismo - Cod. desplegable (1)INDEFINIDO, (2) TEMPORAL < 1 MES, ...';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_TIC IS 'Tipo de Contrato - Cod. desplegable  - (1)Prácticas, (2) Formación, (3)Indefinido, ...';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_FAC IS 'Fecha de alta o inicio del Contrato';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_TJO IS 'Tipo de jornada: COM: Completa PARC: Parcial';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_PJT IS 'Porcentaje de jornada de Trabajo ';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_FFC IS 'Fecha de finalización del contrato';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_DCO IS 'Duración del contrato No. Meses. ';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_CNOE IS 'CNOE - Código de la Clasificación nacional de ocupaciones en España';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_SAL IS 'Retribución salarial anual bruta ';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_CSS IS 'Total Coste Anual de la Seguridad Social';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_IMP IS 'Importe de la subvención concedida';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_IMR IS 'Importe por Modificación de resolución.';
COMMENT ON COLUMN CNEE_SOXEX.SOXEX_IRE IS 'Importe por Imposición de Recursos.';

CREATE UNIQUE INDEX CNEE_SOXEXP00 ON CNEE_SOXEX
(SOXEX_MUN, SOXEX_EJE, SOXEX_PRO, SOXEX_NUM, SOXEX_NCON, SOXEX_DOC);


CREATE SYNONYM CNEE_SOXEXS00 FOR CNEE_SOXEX;

ALTER TABLE CNEE_SOXEX ADD (
  CONSTRAINT CNEE_SOXEXP00
 PRIMARY KEY
 (SOXEX_MUN, SOXEX_EJE, SOXEX_PRO, SOXEX_NUM, SOXEX_NCON, SOXEX_DOC)
);
