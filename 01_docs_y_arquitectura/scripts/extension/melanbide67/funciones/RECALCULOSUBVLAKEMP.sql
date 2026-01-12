create or replace Function Recalculosubvlakemp(P_Anio_Sol In Varchar2, P_Cod_Puesto In Varchar2, P_Titulacion In Varchar2, P_Modalidad_Contr In Varchar2, P_SexoTrab In Varchar2, P_Jornada In Number) Return Varchar2 As 

  V_Retorno             Varchar2(10000);
  Ex_Error              Exception;

  --Variables referentes a los importes subvencionados (tabla Melanbide67_Subv_Empresas).
  V_Retribucion               Melanbide67_Subv_Empresas.Retribucion%Type;
  V_Contr_Pract_6_Meses       Melanbide67_Subv_Empresas.Contr_Pract_6_Meses%Type;
  V_Contr_Pract_12_Meses      Melanbide67_Subv_Empresas.Contr_Pract_12_Meses%Type;
  V_Contr_Indef               Melanbide67_Subv_Empresas.Contr_Indef%Type;
  V_Incr_Mujer_Subrepres      Melanbide67_Subv_Empresas.Incr_Mujer_Subrepres%Type;
  V_Porc_Pago_1               Melanbide67_Subv_Empresas.Porc_Pago_1%Type;
  V_Porc_Pago_2               Melanbide67_Subv_Empresas.Porc_Pago_1%Type;
  V_Indicador                 Melanbide67_Puestos_Trab.Indicador%Type;

  --Titulacion 1 (Titulación Superior o Grado Universitario) - Viene de E_DES_VAL con código TITL
  --Las modalidades de contrato --Viene de E_DES_VAL con código MDLE
  V_Retribucion1              Melanbide67_Subv_Empresas.Retribucion%Type;
  V_Contr_Pract_6_Meses1      Melanbide67_Subv_Empresas.Contr_Pract_6_Meses%Type;
  V_Contr_Pract_12_Meses1     Melanbide67_Subv_Empresas.Contr_Pract_12_Meses%Type;
  V_Contr_Indef1              Melanbide67_Subv_Empresas.Contr_Indef%Type;
  V_Incr_Mujer_Subrepres1     Melanbide67_Subv_Empresas.Incr_Mujer_Subrepres%Type;
  --Titulacion 2 (Titulación de Grado Medio) - Viene de E_DES_VAL con código TITL
  --Las modalidades de contrato --Viene de E_DES_VAL con código MDLE
  V_Retribucion2              Melanbide67_Subv_Empresas.Retribucion%Type;
  V_Contr_Pract_6_Meses2      Melanbide67_Subv_Empresas.Contr_Pract_6_Meses%Type;
  V_Contr_Pract_12_Meses2     Melanbide67_Subv_Empresas.Contr_Pract_12_Meses%Type;
  V_Contr_Indef2              Melanbide67_Subv_Empresas.Contr_Indef%Type;
  V_Incr_Mujer_Subrepres2     Melanbide67_Subv_Empresas.Incr_Mujer_Subrepres%Type;
  --Titulacion 3 (Ciclos Formativos de Grado Superior) - Viene de E_DES_VAL con código TITL
  --Las modalidades de contrato --Viene de E_DES_VAL con código MDLE
  V_Retribucion3              Melanbide67_Subv_Empresas.Retribucion%Type;
  V_Contr_Pract_6_Meses3      Melanbide67_Subv_Empresas.Contr_Pract_6_Meses%Type;
  V_Contr_Pract_12_Meses3     Melanbide67_Subv_Empresas.Contr_Pract_12_Meses%Type;
  V_Contr_Indef3              Melanbide67_Subv_Empresas.Contr_Indef%Type;
  V_Incr_Mujer_Subrepres3     Melanbide67_Subv_Empresas.Incr_Mujer_Subrepres%Type;
  --Titulacion 4 (Ciclos Formativos de Grado Medio (FP I) FP Básica y Certificado de Profesionalidad) - Viene de E_DES_VAL con código TITL
  --Las modalidades de contrato --Viene de E_DES_VAL con código MDLE
  V_Retribucion4              Melanbide67_Subv_Empresas.Retribucion%Type;
  V_Contr_Pract_6_Meses4      Melanbide67_Subv_Empresas.Contr_Pract_6_Meses%Type;
  V_Contr_Pract_12_Meses4     Melanbide67_Subv_Empresas.Contr_Pract_12_Meses%Type;
  V_Contr_Indef4              Melanbide67_Subv_Empresas.Contr_Indef%Type;
  V_Incr_Mujer_Subrepres4     Melanbide67_Subv_Empresas.Incr_Mujer_Subrepres%Type;

  V_Importe_Total             NUMBER(15,2);
  V_Importe_Pago_1            NUMBER(15,2);
  V_Importe_Pago_2            NUMBER(15,2);

Begin
    --Se obtienen los importes de subvención por titulación.  
    For I In 0..3 Loop
      Begin
          Select Retribucion,
                 Contr_Pract_6_Meses,
                 Contr_Pract_12_Meses,
                 Contr_Indef,
                 Incr_Mujer_Subrepres,
                 Porc_Pago_1,
                 Porc_Pago_2
          Into  V_Retribucion,
                V_Contr_Pract_6_Meses,
                V_Contr_Pract_12_Meses,
                V_Contr_Indef,
                V_Incr_Mujer_Subrepres,
                V_Porc_Pago_1,
                V_Porc_Pago_2
          From  Melanbide67_Subv_Empresas
          Where Anio = P_Anio_Sol
          And   Titulacion = I + 1;

       Exception
            When No_Data_Found Then
                    V_Retorno := 'NOT_FOUND_IMP_LAK';
                    Raise Ex_Error;
            When Others Then
                    V_Retorno := Sqlerrm;
                    Raise Ex_Error;
       End;

       If I = 0 Then
        V_Contr_Pract_6_Meses1 := V_Contr_Pract_6_Meses;
        V_Contr_Pract_12_Meses1:= V_Contr_Pract_12_Meses;
        V_Contr_Indef1         := V_Contr_Indef;
        V_Incr_Mujer_Subrepres1:= V_Incr_Mujer_Subrepres;
       Elsif I = 1 Then
        V_Contr_Pract_6_Meses2 := V_Contr_Pract_6_Meses;
        V_Contr_Pract_12_Meses2:= V_Contr_Pract_12_Meses;
        V_Contr_Indef2         := V_Contr_Indef;
        V_Incr_Mujer_Subrepres2:= V_Incr_Mujer_Subrepres;
       Elsif I = 2 Then
        V_Contr_Pract_6_Meses3 := V_Contr_Pract_6_Meses;
        V_Contr_Pract_12_Meses3:= V_Contr_Pract_12_Meses;
        V_Contr_Indef3         := V_Contr_Indef;
        V_Incr_Mujer_Subrepres3:= V_Incr_Mujer_Subrepres;
       Elsif I = 3 Then
        V_Contr_Pract_6_Meses4 := V_Contr_Pract_6_Meses;
        V_Contr_Pract_12_Meses4:= V_Contr_Pract_12_Meses;
        V_Contr_Indef4         := V_Contr_Indef;
        V_Incr_Mujer_Subrepres4:= V_Incr_Mujer_Subrepres;
       End If;
    End Loop; 

    --Se obtiene el indicador de si la mujer está subrepresentada en la profesión (SI = Subrepresentada, NO = Representación > 50%)
    --Para obtener el incremento porcentual de subvención (variables V_Incr_Mujer_Subrepres) el indicador debe ser "SI"

    Begin
      Select Indicador
      Into   V_Indicador
      From Melanbide67_Puestos_Trab
      Where Codigo = P_Cod_Puesto;
    Exception
       When No_Data_Found Then
                    V_Retorno := 'NOT_FOUND_PUESTO_TRAB';
    End;

    --Calcula el importe estimado en función de los datos de la solicitud para saber si lo informado por al empresa es correcto
    --Tiene en cuenta el % de jornada laboral informado. Si no está informado se asume un 100% (Jornada completa)
    If P_Titulacion = 1 Then--Titulación Superior Universitaria.
       If P_Modalidad_Contr = 1 Then
          V_Importe_Total := V_Contr_Pract_6_Meses1 * Nvl(P_Jornada, 100) / 100;
       Elsif P_Modalidad_Contr = 2 Then
          V_Importe_Total := V_Contr_Pract_12_Meses1 * Nvl(P_Jornada, 100) / 100;
       Elsif P_Modalidad_Contr = 3 Then
          V_Importe_Total := V_Contr_Indef1 * Nvl(P_Jornada, 100) / 100;
       End If;
       V_Incr_Mujer_Subrepres := V_Incr_Mujer_Subrepres1;
   Elsif P_Titulacion = 2 Then--Titulación Grado Medio
       If P_Modalidad_Contr = 1 Then
          V_Importe_Total := V_Contr_Pract_6_Meses2 * Nvl(P_Jornada, 100) / 100;
       Elsif P_Modalidad_Contr = 2 Then
          V_Importe_Total := V_Contr_Pract_12_Meses2 * Nvl(P_Jornada, 100) / 100;
       Elsif P_Modalidad_Contr = 3 Then
          V_Importe_Total := V_Contr_Indef2 * Nvl(P_Jornada, 100) / 100;
       End If;
       V_Incr_Mujer_Subrepres := V_Incr_Mujer_Subrepres2;
    Elsif P_Titulacion = 3 Then--Titulación FPII
       If P_Modalidad_Contr = 1 Then
          V_Importe_Total := V_Contr_Pract_6_Meses3 * Nvl(P_Jornada, 100) / 100;
       Elsif P_Modalidad_Contr = 2 Then
          V_Importe_Total := V_Contr_Pract_12_Meses3 * Nvl(P_Jornada, 100) / 100;
       Elsif P_Modalidad_Contr = 3 Then
          V_Importe_Total := V_Contr_Indef3 * Nvl(P_Jornada, 100) / 100;
       End If;
       V_Incr_Mujer_Subrepres := V_Incr_Mujer_Subrepres3;
     Elsif P_Titulacion = 4 Then--Titulación FPI y FP básica
       If P_Modalidad_Contr = 1 Then
          V_Importe_Total := V_Contr_Pract_6_Meses4 * Nvl(P_Jornada, 100) / 100;
       Elsif P_Modalidad_Contr = 2 Then
          V_Importe_Total := V_Contr_Pract_12_Meses4 * Nvl(P_Jornada, 100) / 100;
       Elsif P_Modalidad_Contr = 3 Then
          V_Importe_Total := V_Contr_Indef4 * Nvl(P_Jornada, 100) / 100;
       End If;
       V_Incr_Mujer_Subrepres := V_Incr_Mujer_Subrepres4;
    End If;

    --Mira si el indicador de si la mujer está subrepresentada en la profesión (SI = Subrepresentada, NO = Representación > 50%)
    --Para obtener el incremento porcentual de subvención (variables V_Incr_Mujer_Subrepres) el indicador debe ser "SI"
    -- En 2020  se incrementa un 10% las subvenciones concedidas a todas las mujeres estén o no subrepresentadas 
    If TO_NUMBER(p_anio_sol) < 2020 Then
        If V_Indicador = 'SI' And P_SexoTrab = '2' Then
          V_Importe_Total := V_Importe_Total + (V_Importe_Total * Nvl(V_Incr_Mujer_Subrepres, 0) / 100);
        End If;
    Else 
        If P_SexoTrab = '2' Then
            V_Importe_Total := V_Importe_Total + (V_Importe_Total * Nvl(V_Incr_Mujer_Subrepres, 0) / 100);
        End If;
    End If;
    V_Importe_Pago_1 := V_Importe_Total * V_Porc_Pago_1 / 100;
    V_Importe_Pago_2 := V_Importe_Total * V_Porc_Pago_2 / 100;

    V_Retorno := V_Importe_Total || '|' || V_Importe_Pago_1 || '|' || V_Importe_Pago_2;

    Return V_Retorno;

Exception
      When ex_error Then
                Return(V_Retorno);  
      When Others Then
                    V_Retorno := Sqlerrm;
                    Return(V_Retorno);

END RecalculoSubvlakemp;

