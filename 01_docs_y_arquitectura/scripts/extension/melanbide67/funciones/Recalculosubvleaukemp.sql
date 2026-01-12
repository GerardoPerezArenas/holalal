create or replace
Function Recalculosubvleaukemp(P_Numexp In Varchar2, P_Anio_Sol In Varchar2, P_Id Number) Return Varchar2 As 


  V_Retorno             Varchar2(1000);
  V_Porcentaje          Melanbide67_Puestos_Trab.Porcentaje%Type := 0;
  V_String_Calculo      VARCHAR2(4000);
  Ex_Error              Exception;
  
  
  --Variables referentes a los importes subvencionados (tabla Melanbide67_Subv_Empresas).
  V_Retribucion               Melanbide67_Subv_Empresas.Retribucion%Type;
  V_Contr_Pract_6_Meses       Melanbide67_Subv_Empresas.Contr_Pract_6_Meses%Type;
  V_Contr_Pract_12_Meses      Melanbide67_Subv_Empresas.Contr_Pract_12_Meses%Type;
  V_Contr_Indef               Melanbide67_Subv_Empresas.Contr_Indef%Type;
  V_Incr_Mujer_Subrepres      Melanbide67_Subv_Empresas.Incr_Mujer_Subrepres%Type;
  V_Porc_Pago_1               Melanbide67_Subv_Empresas.Porc_Pago_1%Type;
  V_Porc_Pago_2               Melanbide67_Subv_Empresas.Porc_Pago_1%Type;
  
  --Titulacion 1 (Titulación Superior o Grado Universitario)
  V_Retribucion1              Melanbide67_Subv_Empresas.Retribucion%Type;
  V_Contr_Pract_6_Meses1      Melanbide67_Subv_Empresas.Contr_Pract_6_Meses%Type;
  V_Contr_Pract_12_Meses1     Melanbide67_Subv_Empresas.Contr_Pract_12_Meses%Type;
  V_Contr_Indef1              Melanbide67_Subv_Empresas.Contr_Indef%Type;
  V_Incr_Mujer_Subrepres1     Melanbide67_Subv_Empresas.Incr_Mujer_Subrepres%Type;
  --Titulacion 2 (Titulación de Grado Medio)
  V_Retribucion2              Melanbide67_Subv_Empresas.Retribucion%Type;
  V_Contr_Pract_6_Meses2      Melanbide67_Subv_Empresas.Contr_Pract_6_Meses%Type;
  V_Contr_Pract_12_Meses2     Melanbide67_Subv_Empresas.Contr_Pract_12_Meses%Type;
  V_Contr_Indef2              Melanbide67_Subv_Empresas.Contr_Indef%Type;
  V_Incr_Mujer_Subrepres2     Melanbide67_Subv_Empresas.Incr_Mujer_Subrepres%Type;
  --Titulacion 3 (Ciclos Formativos de Grado Superior)
  V_Retribucion3              Melanbide67_Subv_Empresas.Retribucion%Type;
  V_Contr_Pract_6_Meses3      Melanbide67_Subv_Empresas.Contr_Pract_6_Meses%Type;
  V_Contr_Pract_12_Meses3     Melanbide67_Subv_Empresas.Contr_Pract_12_Meses%Type;
  V_Contr_Indef3              Melanbide67_Subv_Empresas.Contr_Indef%Type;
  V_Incr_Mujer_Subrepres3     Melanbide67_Subv_Empresas.Incr_Mujer_Subrepres%Type;
  --Titulacion 4 (Ciclos Formativos de Grado Medio (FP I) y Certificado de Profesionalidad)
  V_Retribucion4              Melanbide67_Subv_Empresas.Retribucion%Type;
  V_Contr_Pract_6_Meses4      Melanbide67_Subv_Empresas.Contr_Pract_6_Meses%Type;
  V_Contr_Pract_12_Meses4     Melanbide67_Subv_Empresas.Contr_Pract_12_Meses%Type;
  V_Contr_Indef4              Melanbide67_Subv_Empresas.Contr_Indef%Type;
  V_Incr_Mujer_Subrepres4     Melanbide67_Subv_Empresas.Incr_Mujer_Subrepres%Type;
  
  --Variables para la tabla Melanbide67_Empresas
  V_Id                        Melanbide67_Empresas.Id%Type;
  V_Cod_Puesto                Melanbide67_Empresas.Cod_Puesto%Type;
  V_Nombre_Puesto_Sol         Melanbide67_Empresas.Nombre_Puesto_Sol%Type;
  V_Apellido1                 Melanbide67_Empresas.Apellido1%Type;
  V_Apellido2                 Melanbide67_Empresas.Apellido2%Type;
  V_Nombre                    Melanbide67_Empresas.Nombre%Type;
  V_Importe_Subv_Sol          Melanbide67_Empresas.Importe_Subv_Sol%Type;
  V_Importe_Subv_Est          Melanbide67_Empresas.Importe_Subv_Est%Type;
  V_Importe_Subv_Ofe          Melanbide67_Empresas.Importe_Subv_Ofe%Type;
  V_Reintegros                Melanbide67_Empresas.Reintegros%Type;
  V_Importe_Total             Melanbide67_Empresas.Importe_Total%Type;
  V_Salario_Sol               Melanbide67_Empresas.Salario_Sol%Type;
  V_Salario_Ofe               Melanbide67_Empresas.Salario_Ofe%Type;
  V_Sexo_Sol                  Melanbide67_Empresas.Sexo_Sol%Type;
  V_Sexo_Ofe                  Melanbide67_Empresas.Sexo_Ofe%Type;
  V_Dpto_Sol                  Melanbide67_Empresas.Dpto_Sol%Type;
  V_Dpto_Ofe                  Melanbide67_Empresas.Dpto_Ofe%Type;
  V_Titulacion_Sol            Melanbide67_Empresas.Titulacion_Sol%Type;
  V_Titulacion_Ofe            Melanbide67_Empresas.Titulacion_Ofe%Type;
  V_Modalidad_Sol             Melanbide67_Empresas.Modalidad_Sol%Type;
  V_Modalidad_Ofe             Melanbide67_Empresas.Modalidad_Ofe%Type;
  V_Jornada_Lab_Sol           Melanbide67_Empresas.Jornada_Lab_Sol%Type;
  V_Jornada_Lab_Ofe           Melanbide67_Empresas.Jornada_Lab_Ofe%Type;
  V_Centro_Trab_Sol           Melanbide67_Empresas.Centro_Trab_Sol%Type;
  V_Centro_Trab_Ofe           Melanbide67_Empresas.Centro_Trab_Ofe%Type;
  V_Cta_Cotiz_Sol             Melanbide67_Empresas.Cta_Cotiz_Sol%Type;
  V_Cta_Cotiz_Ofe             Melanbide67_Empresas.Cta_Cotiz_Ofe%Type;
  V_Fec_Ini_Contr_Sol         Melanbide67_Empresas.Fec_Ini_Contr_Sol%Type;
  V_Fec_Ini_Contr_Ofe         Melanbide67_Empresas.Fec_Ini_Contr_Ofe%Type;
  V_Fec_Fin_Contr_Sol         Melanbide67_Empresas.Fec_Fin_Contr_Sol%Type;
  V_Fec_Fin_Contr_Ofe         Melanbide67_Empresas.Fec_Fin_Contr_Ofe%Type;
  V_Grupo_Cotiz_Sol           Melanbide67_Empresas.Grupo_Cotiz_Sol%Type;
  V_Grupo_Cotiz_Ofe           Melanbide67_Empresas.Grupo_Cotiz_Ofe%Type;
  V_Convenio_Col_Sol          Melanbide67_Empresas.Convenio_Col_Sol%Type;
  V_Convenio_Col_Ofe          Melanbide67_Empresas.Convenio_Col_Ofe%Type;
  V_Observaciones             Melanbide67_Empresas.Observaciones%Type;
  
Cursor C_Empresas Is
                  Select Id,
                         Cod_Puesto,
                         Nombre_Puesto_Sol,
                         Apellido1,
                         Apellido2,
                         Nombre,
                         Importe_Subv_Sol,
                         Importe_Subv_Est,
                         Importe_Subv_Ofe,
                         Reintegros,
                         Importe_Total,
                         Salario_Sol,
                         Salario_Ofe,
                         Sexo_Sol,
                         Sexo_Ofe,
                         Dpto_Sol,
                         Dpto_Ofe,
                         Titulacion_Sol,
                         Titulacion_Ofe,
                         Modalidad_Sol,
                         Modalidad_Ofe,
                         Jornada_Lab_Sol,
                         Jornada_Lab_Ofe,
                         Centro_Trab_Sol,
                         Centro_Trab_Ofe,
                         Cta_Cotiz_Sol,
                         Cta_Cotiz_Ofe,
                         Fec_Ini_Contr_Sol,
                         Fec_Ini_Contr_Ofe,
                         Fec_Fin_Contr_Sol,
                         Fec_Fin_Contr_Ofe,
                         Grupo_Cotiz_Sol,
                         Grupo_Cotiz_Ofe,
                         Convenio_Col_Sol,
                         Convenio_Col_Ofe,
                         Observaciones
                  From  Melanbide67_Empresas
                  Where Num_Exp = P_Numexp
                  And   Id      = Nvl(P_Id, Id)
                  Order by Id;
                  
                                         
Begin
    V_Retorno := 'OK';
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
                    V_Retorno := 'NOT_FOUND_IMP_LEAUK';
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
       
  --Abre cursor de empresas para calcular la subvención por puesto una vez obtenido el importe por tipo de titulacion académica.
   Open C_Empresas;
      Loop
          Fetch C_Empresas
          Into  V_Id,
                V_Cod_Puesto,
                V_Nombre_Puesto_Sol,
                V_Apellido1,
                V_Apellido2,
                V_Nombre,
                V_Importe_Subv_Sol,
                V_Importe_Subv_Est,
                V_Importe_Subv_Ofe,
                V_Reintegros,
                V_Importe_Total,
                V_Salario_Sol,
                V_Salario_Ofe,
                V_Sexo_Sol,
                V_Sexo_Ofe,
                V_Dpto_Sol,
                V_Dpto_Ofe,
                V_Titulacion_Sol,
                V_Titulacion_Ofe,
                V_Modalidad_Sol,
                V_Modalidad_Ofe,
                V_Jornada_Lab_Sol,
                V_Jornada_Lab_Ofe,
                V_Centro_Trab_Sol,
                V_Centro_Trab_Ofe,
                V_Cta_Cotiz_Sol,
                V_Cta_Cotiz_Ofe,
                V_Fec_Ini_Contr_Sol,
                V_Fec_Ini_Contr_Ofe,
                V_Fec_Fin_Contr_Sol,
                V_Fec_Fin_Contr_Ofe,
                V_Grupo_Cotiz_Sol,
                V_Grupo_Cotiz_Ofe,
                V_Convenio_Col_Sol,
                V_Convenio_Col_Ofe,
                V_Observaciones;
          Exit When C_Empresas%Notfound;
            
            V_String_Calculo := Null;    
            
            Begin
              Select Porcentaje
              Into   V_Porcentaje
              From Melanbide67_Puestos_Trab
              --Where Codigo = Substr(V_Nombre_Puesto_Sol, 1, 4);
              Where Upper(descripcion) = Upper(V_Nombre_Puesto_Sol);
            Exception
               When No_Data_Found Then
                            V_Porcentaje := 0;
            End;
            
            --Calcula el importe estimado en función de los datos de la solicitud para saber si lo informado por al empresa es correcto
            --Tiene en cuenta el % de jornada laboral informado. Si no está informado se asume un 100% (Jornada completa)
            If V_Titulacion_Sol = 1 Then--Titulación Superior Universitaria.
               If V_Modalidad_Sol = 1 Then
                  V_Importe_Subv_Est := V_Contr_Pract_6_Meses1 * Nvl(V_Jornada_Lab_Sol, 100) / 100;
               Elsif V_Modalidad_Sol = 2 Then
                  V_Importe_Subv_Est := V_Contr_Pract_12_Meses1 * Nvl(V_Jornada_Lab_Sol, 100) / 100;
               Elsif V_Modalidad_Sol = 3 Then
                  V_Importe_Subv_Est := V_Contr_Indef1 * Nvl(V_Jornada_Lab_Sol, 100) / 100;
               End If;
           Elsif V_Titulacion_Sol = 2 Then--Titulación Grado Medio
               If V_Modalidad_Sol = 1 Then
                  V_Importe_Subv_Est := V_Contr_Pract_6_Meses2 * Nvl(V_Jornada_Lab_Sol, 100) / 100;
               Elsif V_Modalidad_Sol = 2 Then
                  V_Importe_Subv_Est := V_Contr_Pract_12_Meses2 * Nvl(V_Jornada_Lab_Sol, 100) / 100;
               Elsif V_Modalidad_Sol = 3 Then
                  V_Importe_Subv_Est := V_Contr_Indef2 * Nvl(V_Jornada_Lab_Sol, 100) / 100;
               End If;
            Elsif V_Titulacion_Sol = 3 Then--Titulación FPII
               If V_Modalidad_Sol = 1 Then
                  V_Importe_Subv_Est := V_Contr_Pract_6_Meses3 * Nvl(V_Jornada_Lab_Sol, 100) / 100;
               Elsif V_Modalidad_Sol = 2 Then
                  V_Importe_Subv_Est := V_Contr_Pract_12_Meses3 * Nvl(V_Jornada_Lab_Sol, 100) / 100;
               Elsif V_Modalidad_Sol = 3 Then
                  V_Importe_Subv_Est := V_Contr_Indef3 * Nvl(V_Jornada_Lab_Sol, 100) / 100;
               End If;
             Elsif V_Titulacion_Sol = 4 Then--Titulación FPI
               If V_Modalidad_Sol = 1 Then
                  V_Importe_Subv_Est := V_Contr_Pract_6_Meses4 * Nvl(V_Jornada_Lab_Sol, 100) / 100;
               Elsif V_Modalidad_Sol = 2 Then
                  V_Importe_Subv_Est := V_Contr_Pract_12_Meses4 * Nvl(V_Jornada_Lab_Sol, 100) / 100;
               Elsif V_Modalidad_Sol = 3 Then
                  V_Importe_Subv_Est := V_Contr_Indef4 * Nvl(V_Jornada_Lab_Sol, 100) / 100;
               End If;
            End If;
            
            --Calcula el importe real en función de los datos de la oferta y/o contrato
            If V_Titulacion_Ofe = 1 Then--Titulación Superior Universitaria.
               If V_Modalidad_Ofe = 1 Then
                  V_Importe_Subv_Ofe := V_Contr_Pract_6_Meses1 * Nvl(V_Jornada_Lab_Ofe, 100) / 100;
               Elsif V_Modalidad_Ofe = 2 Then
                  V_Importe_Subv_Ofe := V_Contr_Pract_12_Meses1 * Nvl(V_Jornada_Lab_Ofe, 100) / 100;
               Elsif V_Modalidad_Ofe = 3 Then
                  V_Importe_Subv_Ofe := V_Contr_Indef1 * Nvl(V_Jornada_Lab_Ofe, 100) / 100;
               End If;
           Elsif V_Titulacion_Ofe = 2 Then--Titulación Grado Medio
               If V_Modalidad_Ofe = 1 Then
                  V_Importe_Subv_Ofe := V_Contr_Pract_6_Meses2 * Nvl(V_Jornada_Lab_Ofe, 100) / 100;
               Elsif V_Modalidad_Ofe = 2 Then
                  V_Importe_Subv_Ofe := V_Contr_Pract_12_Meses2 * Nvl(V_Jornada_Lab_Ofe, 100) / 100;
               Elsif V_Modalidad_Ofe = 3 Then
                  V_Importe_Subv_Ofe := V_Contr_Indef2 * Nvl(V_Jornada_Lab_Ofe, 100) / 100;
               End If;
            Elsif V_Titulacion_Ofe = 3 Then--Titulación FPII
               If V_Modalidad_Ofe = 1 Then
                  V_Importe_Subv_Ofe := V_Contr_Pract_6_Meses3 * Nvl(V_Jornada_Lab_Ofe, 100) / 100;
               Elsif V_Modalidad_Ofe = 2 Then
                  V_Importe_Subv_Ofe := V_Contr_Pract_12_Meses3 * Nvl(V_Jornada_Lab_Ofe, 100) / 100;
               Elsif V_Modalidad_Ofe = 3 Then
                  V_Importe_Subv_Ofe := V_Contr_Indef3 * Nvl(V_Jornada_Lab_Ofe, 100) / 100;
               End If;
             Elsif V_Titulacion_Ofe = 4 Then--Titulación FPI
               If V_Modalidad_Ofe = 1 Then
                  V_Importe_Subv_Ofe := V_Contr_Pract_6_Meses4 * Nvl(V_Jornada_Lab_Ofe, 100) / 100;
               Elsif V_Modalidad_Ofe = 2 Then
                  V_Importe_Subv_Ofe := V_Contr_Pract_12_Meses4 * Nvl(V_Jornada_Lab_Ofe, 100) / 100;
               Elsif V_Modalidad_Ofe = 3 Then
                  V_Importe_Subv_Ofe := V_Contr_Indef4 * Nvl(V_Jornada_Lab_Ofe, 100) / 100;
               End If;
            End If;
            
            --Mira si el porcentaje de participación de la mujer es mayor que el 50% (0,5) si es así aumenta un 10% la cantidad subvencionada.
            
            If V_Porcentaje < 0.5 And V_Sexo_Sol = '1' Then
               V_Importe_Subv_Est := V_Importe_Subv_Est + (V_Importe_Subv_Est * Nvl(V_Incr_Mujer_Subrepres, 0) / 100);
               --V_String_Calculo := '(Cálculo Estimado) Se añade el 10% a la subvención por ser mujer y estar subrepresentado el género en la profesión: ' || V_Importe_Subv_Est;
            End If;
            
            If V_Porcentaje < 0.5 And V_Sexo_Ofe = '1' Then  
                  V_Importe_Subv_Ofe := V_Importe_Subv_Ofe + (V_Importe_Subv_Ofe * Nvl(V_Incr_Mujer_Subrepres, 0) / 100);
                  --V_String_Calculo := '(Cálculo Real) Se añade el 10% a la subvención por ser mujer y estar subrepresentado el género en la profesión: ' || V_Importe_Subv_Ofe;
            End If;
            
            Begin
              Update Melanbide67_Empresas
              Set Importe_Subv_Est = V_Importe_Subv_Est,
                  Importe_Subv_Ofe = V_Importe_Subv_Ofe,
                  Importe_Total = Nvl(V_Importe_Subv_Ofe, 0) - Nvl(V_Reintegros, 0),
                  Imp_Pago_1 = Round(Nvl(V_Porc_Pago_1, 0) / 100 * V_Importe_Subv_Ofe, 2),
                  Imp_Pago_2 = Round(Nvl(V_Porc_Pago_2, 0) / 100 * V_Importe_Subv_Ofe, 2),
                  Observaciones = Decode(V_String_Calculo, Null, Observaciones, Decode(Observaciones, Null, V_String_Calculo, Observaciones || Chr(13) || V_String_Calculo))
              Where Id = V_Id;
            Exception
              When Others Then
                    V_Retorno := 'UPDATE_RECAL_LEAUK';
                    RAISE ex_error;
            End;
      End Loop;
      Close C_Empresas;
      
      Commit;
      Return v_retorno;
        
Exception
      When ex_error Then
                Return(V_Retorno);  
      When Others Then
                    V_Retorno := Sqlerrm;
                    Return(V_Retorno);
        
END recalculoSubvLEAUKEMP;
