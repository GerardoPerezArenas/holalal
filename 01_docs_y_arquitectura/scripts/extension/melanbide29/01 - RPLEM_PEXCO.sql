CREATE TABLE RPLEM_PEXCO
( PEXCO_MUN 	NUMBER(3) NOT NULL, 
  PEXCO_EJE 	NUMBER (4) NOT NULL, 
  PEXCO_PRO		VARCHAR2(5) NOT NULL, 
  PEXCO_NUM		VARCHAR2(30) NOT NULL,
  PEXCO_NCON	NUMBER(6) NOT NULL, 
  PEXCO_CTPE	NUMBER(2) NOT NULL, 
  PEXCO_DOC 	VARCHAR2(25) NOT NULL, 
  PEXCO_TER_COD	NUMBER(12), 
  PEXCO_NOM 	VARCHAR2(80) NOT NULL, 
  PEXCO_AP1 	VARCHAR2(100) NOT NULL, 
  PEXCO_AP2 	VARCHAR2(100), 
  PEXCO_FNA 	DATE, 
  PEXCO_SEX 	NUMBER(2), 
  PEXCO_MIN 	VARCHAR2(1), 
  PEXCO_INM 	VARCHAR2(1), 
  PEXCO_PLD 	VARCHAR2(1), 
  PEXCO_RML 	VARCHAR2(1), 
  PEXCO_OTR 	VARCHAR2(1), 
  PEXCO_COL 	NUMBER(2), 
  PEXCO_MDE 	NUMBER(3), 
  PEXCO_NES 	NUMBER(3), 
  PEXCO_TCO 	NUMBER(3), 
  PEXCO_FAC 	DATE, 
  PEXCO_TJO 	VARCHAR2(5), 
  PEXCO_FFC 	DATE, 
  PEXCO_DCO 	NUMBER(5),
  PEXCO_CNOE 	VARCHAR2(4), 
  PEXCO_SAL 	NUMBER(12,2),
  PEXCO_IMP 	NUMBER(12,2), 
  PEXCO_FBE 	DATE, 
  PEXCO_PRJ 	NUMBER(5,2)
);
COMMENT ON TABLE RPLEM_PEXCO IS 'Personas por cada Linea de Contratos del Expediente';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_MUN 	IS 'Entorno 0 - Pruebas 1 - Produccion';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_EJE 	IS 'Ejercicio/Ano del Expediente';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_PRO 	IS 'Codigo del Procedimiento';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_NUM		IS 'Número del expediente Formateado ANO/PROC/NUM';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_NCON	IS 'Correlativo de Contratos para cada Expediente ';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_CTPE	IS 'Codigo de tipo de Persona dentro de la linea x contratos 1.P.Sustituida, 2.P.Contratada, 3.P.Cont.Adicional';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_DOC 	IS 'Documento de identidad de la persona (DNI, NIE, ... Etc)';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_TER_COD	IS 'Codigo del tercero , solo para nuevos registros dados de alta en Regexlan.';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_NOM 	IS 'Nombre';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_AP1 	IS 'Apellido 1';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_AP2 	IS 'Apellido 2';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_FNA 	IS 'Fecha de Nacimiento';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_SEX 	IS 'Sexo: 1.Hombre - 2.Mujer';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_MIN 	IS 'Indicador Booleano S/N de Persona Minusvalida';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_INM 	IS 'Indicador Booleano S/N de Persona Inmigrante';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_PLD 	IS 'Indicador Booleano S/N de Persona Parado de larga duración';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_RML 	IS 'Indicador Booleano S/N de Persona Reentrante en el mercado laboral';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_OTR 	IS 'Indicador Booleano S/N de Persona Otros caracteristicas especiales';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_COL 	IS 'Colectivo al que pertenece';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_MDE 	IS 'Numero de Meses en desempleo';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_NES 	IS 'Nivel de Estudios - Cod. desplegable';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_TCO 	IS 'Tipo de Contrato por Duracion del mismo - Cod. desplegable';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_FAC 	IS 'Fecha de alta o inicio del Contrato';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_TJO 	IS 'Tipo de jornada: COM: Completa PARC: Parcial';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_FFC 	IS 'Fecha de finalización del contrato';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_DCO 	IS 'Duración del contrato No. de dias. Calculado entre F.Alta P.Contratada y F. Baja de la Empresa de la P. Sustituida (PEXCO_FBE para PEXCO_CTPE=3).';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_CNOE 	IS 'CNOE - Codigo de la Clasificación nacional de ocupaciones en españa';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_SAL 	IS 'Retribución salarial anual bruta ';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_IMP 	IS 'Importe de la subvención concedida';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_FBE 	IS 'Fecha prevista de Baja de la persona sustituida, (Fecha de Jubilación o 65 cumpleaños)';
COMMENT ON COLUMN RPLEM_PEXCO.PEXCO_PRJ 	IS 'Porcentaje de jornada reducida - Persona Sustituida';

CREATE UNIQUE INDEX RPLEM_PEXCOP00 ON RPLEM_PEXCO
(PEXCO_MUN, PEXCO_EJE, PEXCO_PRO, PEXCO_NUM, PEXCO_NCON, PEXCO_CTPE, PEXCO_DOC);

CREATE SYNONYM RPLEM_PEXCOS00 FOR RPLEM_PEXCO;

ALTER TABLE RPLEM_PEXCO ADD (
  CONSTRAINT RPLEM_PEXCOP00
 PRIMARY KEY
 (PEXCO_MUN, PEXCO_EJE, PEXCO_PRO, PEXCO_NUM, PEXCO_NCON, PEXCO_CTPE, PEXCO_DOC)
);
