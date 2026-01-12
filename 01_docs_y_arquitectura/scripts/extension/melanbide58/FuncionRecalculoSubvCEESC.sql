create or replace FUNCTION recalculoSubvCEESC(p_numExp IN VARCHAR2, p_numLinea IN VARCHAR2, p_anio_sol IN VARCHAR2) RETURN VARCHAR2 AS 

  V_Retorno            Varchar2(1000);
  Ex_Error             Exception;
  V_SMI_Diario         Melanbide58_Valor_Smi.Imp_Diario_Smi%Type;
																
  
  V_Idsmi              Melanbide58_Smi.Idsmi%Type;
  V_Num_Linea          Melanbide58_Smi.Num_Linea%Type;

  V_Idsmi2             Melanbide58_Smi.Idsmi%Type;
  V_Num_Exp2           Melanbide58_Smi.Num_Exp%Type;
  V_Num_Linea2         Melanbide58_Smi.Num_Linea%Type;
  V_Nif2         	   Melanbide58_Smi.Nif%Type;
  V_Apellidos2         Melanbide58_Smi.Apellidos%Type;
  V_Nombre2            Melanbide58_Smi.Nombre%Type;
  V_Num_Dias_Sin2      Melanbide58_Smi.Num_Dias_Sin%Type;
  V_Num_Dias2          Melanbide58_Smi.Num_Dias%Type;
  V_Causa2             Melanbide58_Smi.Causa%Type;
  V_Importe2           Melanbide58_Smi.Importe%Type;
  V_Fecha2             Melanbide58_Smi.Fecha%Type;
  V_Por_Jornada2       Melanbide58_Smi.Por_Jornada%Type;
  V_Por_Reduccion2     Melanbide58_Smi.Por_Reduccion%Type;
  V_Importe_Calculado2 Melanbide58_Smi.Importe_Calculado%Type;
  V_Importe_Lanbide2   Melanbide58_Smi.Importe_Lanbide%Type;
  V_Calculo_Formula    Melanbide58_Smi.Importe_Calculado%Type;
  V_Importe2_Aux       Melanbide58_Smi.Importe%Type;
  V_Idsmi2_Aux         Melanbide58_Smi.Idsmi%Type;
  
  V_IdsmiF             Melanbide58_Smi.Idsmi%Type;
  V_Num_ExpF           Melanbide58_Smi.Num_Exp%Type;
  V_Num_LineaF         Melanbide58_Smi.Num_Linea%Type;
  V_NifF         	   Melanbide58_Smi.Nif%Type;
  V_ApellidosF         Melanbide58_Smi.Apellidos%Type;
  V_NombreF            Melanbide58_Smi.Nombre%Type;
  V_Num_Dias_SinF      Melanbide58_Smi.Num_Dias_Sin%Type;
  V_Num_DiasF          Melanbide58_Smi.Num_Dias%Type;
  V_CausaF             Melanbide58_Smi.Causa%Type;
  V_ImporteF           Melanbide58_Smi.Importe%Type;
  V_FechaF             Melanbide58_Smi.Fecha%Type;
  V_Por_JornadaF       Melanbide58_Smi.Por_Jornada%Type;
  V_Por_Reduccionf     Melanbide58_Smi.Por_Reduccion%Type;
  V_Importe_Lanbidef   Melanbide58_Smi.Importe_Lanbide%Type;

  V_Num_Linea_Aux     Melanbide58_Smi.Num_Linea%Type;
  V_Idsmi_Aux         Melanbide58_Smi.Idsmi%Type;
  
  V_Max_Lin           Melanbide58_Smi.Num_Linea%Type;
  V_Idsmi_Lin         Melanbide58_Smi.Idsmi%Type;
  
  -- Variables para almacenar valores, segun las condiciones de MELANBIDE58_VALOR_SMI
  v_por_ayuda_smi 	  MELANBIDE58_VALOR_SMI.POR_AYUDA_SMI%TYPE;
  v_imp_diario_smi	  MELANBIDE58_VALOR_SMI.IMP_DIARIO_SMI%TYPE;
  v_mensaje			  Varchar2(1000);

Cursor C_Smi Is
                  Select Idsmi,
                         Num_Linea
                  From  Melanbide58_Smi
                  Where Num_Exp = P_Numexp
                  And   (Num_Linea = P_Numlinea Or P_Numlinea Is Null)
                  And   Num_linea Is Not Null
                  Order by Idsmi;
                  
Cursor C_Max_Idsmi Is
                      Select Max(Idsmi) maxIdsmi
                      From  Melanbide58_Smi
                      Where Num_Exp = P_Numexp;
                      
                      V_Max_Idsmi_Idsmi Melanbide58_Smi.Idsmi%Type;
                      Reg_Max_Idsmi    C_Max_Idsmi%Rowtype;
                      
Cursor C_Lineas_Lin Is
                 Select Distinct  A.Idsmi,
                                  A.Num_Exp,
                                  A.Num_Linea,
								  A.Nif,
                                  A.Apellidos,
                                  A.Nombre,
                                  A.Num_Dias_Sin,
                                  A.Num_Dias,
                                  A.Causa,
                                  A.Importe,
                                  A.Fecha,
                                  A.Por_Jornada,
                                  A.Por_Reduccion,
                                  A.Importe_Calculado,
                                  A.Importe_Lanbide
                  From  melanbide58_smi a,
                        (select b.idsmi, 
                                b.num_exp  
                          From Melanbide58_Smi B 
                          Where B.Num_Exp = P_Numexp
                          And   B.num_linea = V_Num_Linea_Aux) idsmiinicio,
                        (Select C.Idsmi 
                         From Melanbide58_Smi C 
                         Where C.Num_Exp = P_Numexp
                         And   C.Num_Linea >= V_Num_Linea_Aux
                         And   (Nvl(C.Num_Linea, V_Num_Linea_Aux) <= V_Num_Linea_Aux + 1)) Idsmifin
                  Where A.Idsmi >= Idsmiinicio.Idsmi
                  And   A.Idsmi <= Idsmifin.Idsmi--(+)
                  And   A.Num_Exp = Idsmiinicio.Num_Exp
                  And   A.Idsmi <= V_Max_Idsmi_Idsmi
                  Order By A.idsmi;
                  
Cursor C_Count_Lin Is
                 Select Count(Distinct  A.Idsmi) cont_lin
                  From  melanbide58_smi a,
                        (select b.idsmi, 
                                b.num_exp  
                          From Melanbide58_Smi B 
                          Where B.Num_Exp = P_Numexp
                          And   B.num_linea = V_Num_Linea_Aux) idsmiinicio,
                        (Select C.Idsmi 
                         From Melanbide58_Smi C 
                         Where C.Num_Exp = P_Numexp
                         And   C.Num_Linea >= V_Num_Linea_Aux
                         And   (Nvl(C.Num_Linea, V_Num_Linea_Aux) <= V_Num_Linea_Aux + 1)) Idsmifin
                  Where A.idsmi >= idsmiinicio.idsmi
                  And   A.Idsmi <= Idsmifin.Idsmi
                  And   A.Num_Exp = Idsmiinicio.Num_Exp
                  And   A.Idsmi <= V_Max_Idsmi_Idsmi;
                  
                  V_Count_Line     Number(10);
                  V_Contador       Number(10);
                  Reg_Count_Lin    C_Count_Lin%Rowtype;
                  
Cursor C_Last_Line Is
                  Select Idsmi,
                         Num_Exp,
                         Num_Linea,
						 Nif,
                         Apellidos,
                         Nombre,
                         Num_Dias_Sin,
                         Num_Dias,
                         Causa,
                         Importe,
                         Fecha,
                         Por_Jornada,
                         Por_Reduccion,
                         Importe_Calculado,
                         Importe_Lanbide
                  From  Melanbide58_Smi
                  Where Num_Exp = P_Numexp
                  And   Idsmi >= V_Idsmi_Lin
                  And   Idsmi <= V_Max_Idsmi_Idsmi
                  Order by Idsmi;   
                  
                  Reg_Last_Line    C_Last_Line%Rowtype;
                  
Begin
    V_Retorno := 'OK';
    
--    Begin
--        Select Imp_Diario_Smi
--        Into  V_Smi_Diario
--        From  Melanbide58_Valor_Smi
--        Where anio = p_anio_sol;
--    Exception
--          When No_Data_Found Then
--                  V_Retorno := 'NOT_FOUND_SMI';
--                  Raise Ex_Error;
--          When Others Then
--                  V_Retorno := Sqlerrm;
--                  Raise Ex_Error;
--    End;
    
    For Reg_Max_Idsmi In C_Max_Idsmi
     Loop
        V_Max_Idsmi_Idsmi := Reg_Max_Idsmi.Maxidsmi;
     End Loop;

     Open C_Smi;
        LOOP
            FETCH c_smi
            INTO  V_Idsmi,
                  V_Num_Linea;
            Exit When C_Smi%Notfound;
            
            V_Idsmif          := Null;
            V_Num_LineaF      := Null;
			V_NifF			  := Null;
            V_ApellidosF      := Null;
            V_NombreF         := Null;
            V_Num_Dias_SinF   := Null;
            V_Num_Diasf       := Null;
            V_CausaF          := Null;
            V_ImporteF        := Null;
            V_FechaF          := Null;
            V_Por_JornadaF    := Null;
            V_Por_ReduccionF  := Null;
            V_Calculo_Formula := Null;
            V_Contador        := 0;
            V_Importe2_Aux    := Null;
            V_Idsmi2_Aux      := Null;
            
            If V_Num_Linea Is Not Null Then
              V_Num_Linea_Aux := V_Num_Linea;
              V_Idsmi_Aux     := V_Idsmi;
            END IF;
            
            For Reg_Count_Lin In C_Count_Lin
            Loop
                Declare
                  v_hay_lineas  NUMBER(15) := 0;
                Begin
                    Select COUNT(1)
                      Into  v_hay_lineas
                      From Melanbide58_Smi S
                      Where S.Num_Exp = p_numExp
                      And S.Num_Linea > V_Num_Linea_Aux;
                      
                      If v_hay_lineas >= 1 Then      
                         V_Count_Line := Reg_Count_Lin.Cont_Lin - 1;
                      Else
                          V_Count_Line := Reg_Count_Lin.Cont_Lin;
                      End If;
                End;
            End Loop;
            
            Open C_Lineas_Lin;
            LOOP
                Fetch C_Lineas_Lin
                Into  V_Idsmi2,
                      V_Num_Exp2,
                      V_Num_Linea2,
					  V_Nif2,
                      V_Apellidos2,
                      V_Nombre2,
                      V_Num_Dias_Sin2,
                      V_Num_Dias2,
                      V_Causa2,
                      V_Importe2,
                      V_Fecha2,
                      V_Por_Jornada2,
                      V_Por_Reduccion2,
                      V_Importe_Calculado2,
                      V_importe_Lanbide2;
                Exit When C_Lineas_Lin%Notfound;
                
                V_Contador := V_Contador + 1;
				
				dbms_output.put_line('recalculoSubvCEESC  V_Num_Dias_Sin2--> '|| V_Num_Dias_Sin2);				
                
                --Borra los importes siempre que no sea la última línea a tratar.
                If V_Idsmi2 < V_Max_Idsmi_Idsmi And Nvl(V_Num_Linea2, 0) <= V_Num_Linea_Aux Then
                   --Borra los importes calculados de la línea.
                  Begin
                      --Si el importe Lanbide es nulo se recalcula en el campo Importe calculado. Esto es debido
                      --a que el usuario no quiere modificar la línea. Si el usuario quiere modificar la línea sin
                      --subsanación se regraba el importe calculado en importe Lanbide, con lo cuál el campo ya no es nulo
                      --y el recálculo se graba en el importe Lanbide.
                      If V_Importe_Lanbide2 Is Null Then
                         Update Melanbide58_Smi
                           Set Importe_Calculado = null
                           Where Idsmi = V_Idsmi2;
                      Else
                         Update Melanbide58_Smi
                           Set Importe_Lanbide = Null
                           Where Idsmi = V_Idsmi2;
                      End If;
                      
                      COMMIT;
                  Exception
                       When Others Then
                                V_Retorno := 'UPDATE_RECALCULO';
                                Raise Ex_Error;
                  End;
                End If;
                
                
                If V_Importe2 Is Not Null And V_Importe2 != 0 Then
                   V_Importe2_Aux := V_Importe2;
                   V_Idsmi2_Aux   := V_Idsmi2;
                End If;

                --Mientras sean todos los datos de la misma línea
                If V_Num_Linea2 = V_Num_Linea_Aux Or V_Num_Linea2 Is Null Then
                   
                   If V_Num_Dias_Sin2 Is Not Null Then                     
                      V_Num_Dias_SinF := V_Num_Dias_Sin2;
                   End If;
                
                   If V_Num_Dias2 Is Not Null Then                     
                      V_Num_DiasF := V_Num_Dias2;
                   End If;
                
                   If V_Importe2 Is Not Null Then                     
                      V_ImporteF := V_Importe2;
                   End If;
                   
                   If V_Por_Jornada2 Is Not Null Then                     
                      V_Por_JornadaF := V_Por_Jornada2;
                   End If;
                   
                   If V_Por_Reduccion2 Is Not Null Then                     
                      V_Por_ReduccionF := V_Por_Reduccion2;
                   End If;
                   
                   If V_Importe_Lanbide2 Is Not Null Then                     
                      V_Importe_Lanbidef := V_Importe_Lanbide2;
                   End If;
                   
                   If V_Contador = V_Count_Line Or 
                      (V_Idsmi2 = V_Max_Idsmi_Idsmi And V_Num_Dias_Sinf Is Not Null) Or
                      ((V_Num_Dias_Sinf Is Not Null And V_Num_Dias_Sinf != 0) And (V_Importe2_Aux Is Not Null And V_Importe2_Aux != 0)) Then
                     --Nº días sin incidencia * (SMI diario/2) * (%Jornada/100) * (%Reduc. Jornada/100)
					 
					 --obtenerPorcentajeAyudaSUBCEESC (p_anio in number, p_num_exp in varchar2, p_nif in varchar2, p_por_ayuda_smi out number, p_imp_diario_smi out number, mensaje out varchar2)
					 
					   -- Variables para almacenar valores, segun las condiciones de MELANBIDE58_VALOR_SMI
					 obtenerPorcentajeAyudaSUBCEESC (p_anio_sol, p_numExp, V_Nif2, v_por_ayuda_smi, v_imp_diario_smi , v_mensaje);
					 
					 
					 
					-- Cambio 27/01/2020
					-- Se cambia columna % reduccion de jornada por % rendimiento
					-- Formula queda de la siguiente manera;
					-- Subvención = Nº días sin incidencia * (SMI diario*(% subvención a aplicar /100)) * (%Jornada/100) * (100 - %Rendimiento/100)
					 
                     --V_Calculo_Formula := Round(Nvl(V_Num_Dias_Sinf, 0) * (v_imp_diario_smi * (Nvl(v_por_ayuda_smi, 1))) * (Nvl(V_Por_Jornadaf, 100)/100) * (Nvl(V_Por_Reduccionf, 100)/100), 2);

-- 01/12/2020
                    -- en los casos en los que el concepto sea ATR o INC o si es VAC y son genéricas dni=999 no se realiza el calculo, se copia el importe,
                    IF V_Nif2 = '88888888' OR V_Nif2 = '99999999' THEN
                        V_Calculo_Formula :=  Nvl(V_Importe2_Aux, 0);
					Elsif V_Causa2 = 'ATR' Or V_Causa2 = 'INC' Then 
                        V_Calculo_Formula :=  Nvl(V_Importe2_Aux, 0);                    
                    Else
                     V_Calculo_Formula := Round(Nvl(V_Num_Dias_Sinf, 0) * (v_imp_diario_smi * (Nvl(v_por_ayuda_smi, 1))) * (Nvl(V_Por_Jornadaf, 100)/100) * ((100-Nvl(V_Por_Reduccionf, 0))/100), 2); 					 
                    End If;
                    
                     Begin
                          --Si el importe Lanbide es nulo se recalcula en el campo Importe calculado. Esto es debido
                          --a que el usuario no quiere modificar la línea. Si el usuario quiere modificar la línea sin
                          --subsanación se regraba el importe calculado en importe Lanbide, con lo cuál el campo ya no es nulo
                          --y el recálculo se graba en el importe Lanbide.
                          If V_Importe_Lanbidef Is Null Then
                          
                             Update Melanbide58_Smi
                              Set Importe = Nvl(V_Importe2_Aux, 0),
                                  Importe_Calculado = V_Calculo_Formula
                              Where Idsmi = Nvl(V_Idsmi2_Aux, V_Idsmi2);  
                          Else
                              Update Melanbide58_Smi
                                Set Importe = Nvl(V_Importe2_Aux, 0),
                                    Importe_Lanbide = V_Calculo_Formula
                                Where Idsmi = Nvl(V_Idsmi2_Aux, V_Idsmi2); 
                          End If;
                          
                          COMMIT;
                     Exception
                          When Others Then
                                   V_Retorno := 'UPDATE_RECALCULO';
                                   RAISE ex_error;
                     End;
                   End If;
                 End If;
                 
                 If (V_Num_Linea2 Is Not Null And V_Num_Linea2 > V_Num_Linea_Aux) Or V_Idsmi2 = V_Max_Idsmi_Idsmi Then
                   Exit;
                 End If;               
            End Loop;
            Close C_Lineas_Lin;           
        End Loop;
        Close C_Smi;
        
        Begin
            Select Max(Num_Linea)
            Into  V_Max_Lin
            From Melanbide58_Smi
            Where Num_Exp = P_Numexp;
        Exception
            When No_Data_Found Then
                            Null;  
        End;
        
        Begin
            Select Max(Idsmi)
            Into  V_Idsmi_Lin
            From Melanbide58_Smi
            Where Num_Exp = P_Numexp
            And   Num_Linea = V_Max_Lin;
        Exception
            When No_Data_Found Then
                            Null;  
        End;
        
        If V_Idsmi_Lin < V_Max_Idsmi_Idsmi Then
        
            V_Idsmif          := Null;
            V_Num_LineaF      := Null;
			V_NifF			  := Null;
            V_ApellidosF      := Null;
            V_NombreF         := Null;
            V_Num_Dias_SinF   := Null;
            V_Num_Diasf       := Null;
            V_CausaF          := Null;
            V_ImporteF        := Null;
            V_FechaF          := Null;
            V_Por_JornadaF    := Null;
            V_Por_ReduccionF  := Null;
            V_Calculo_Formula := Null;
            V_Contador        := 0;
            V_Importe2_Aux    := Null;
            V_Idsmi2_Aux      := Null;
            
            For Reg_Last_Line In C_Last_Line
             Loop
                V_Idsmi2             := Reg_Last_Line.Idsmi;
                V_Num_Exp2           := Reg_Last_Line.Num_Exp;
                V_Num_Linea2         := Reg_Last_Line.Num_Linea;
				V_Nif2		         := Reg_Last_Line.Nif;
                V_Apellidos2         := Reg_Last_Line.Apellidos;
                V_Nombre2            := Reg_Last_Line.Nombre;
                V_Num_Dias_Sin2      := Reg_Last_Line.Num_Dias_Sin;
                V_Num_Dias2          := Reg_Last_Line.Num_Dias;
                V_Causa2             := Reg_Last_Line.Causa;
                V_Importe2           := Reg_Last_Line.Importe;
                V_Fecha2             := Reg_Last_Line.Fecha;
                V_Por_Jornada2       := Reg_Last_Line.Por_Jornada;
                V_Por_Reduccion2     := Reg_Last_Line.Por_Reduccion;
                V_Importe_Calculado2 := Reg_Last_Line.Importe_Calculado;
                V_Importe_Lanbide2   := Reg_Last_Line.Importe_Lanbide;
                
                --Borra los importes siempre que no sea la última línea a tratar.
                If V_Idsmi2 <= V_Max_Idsmi_Idsmi Then
                   --Borra los importes calculados de la línea.
                  Begin
                      If V_importe_Lanbide2 Is Null Then
                      
                         Update Melanbide58_Smi
                          Set Importe_Calculado = Null
                          Where Idsmi = V_Idsmi2;
                      Else
                          Update Melanbide58_Smi
                            Set Importe_Lanbide = Null
                            Where Idsmi = V_Idsmi2;
                      End If;
                      COMMIT;
                  Exception
                       When Others Then
                                V_Retorno := 'UPDATE_RECALCULO';
                                Raise Ex_Error;
                  End;
                End If;
                
                If V_Importe2 Is Not Null And V_Importe2 != 0 Then
                   V_Importe2_Aux := V_Importe2;
                   V_Idsmi2_Aux   := V_Idsmi2;
                End If;
               
               If V_Num_Dias_Sin2 Is Not Null Then                     
                  V_Num_Dias_SinF := V_Num_Dias_Sin2;
               End If;
            
               If V_Num_Dias2 Is Not Null Then                     
                  V_Num_DiasF := V_Num_Dias2;
               End If;
            
               If V_Importe2 Is Not Null Then                     
                  V_ImporteF := V_Importe2;
               End If;
               
               If V_Por_Jornada2 Is Not Null Then                     
                  V_Por_JornadaF := V_Por_Jornada2;
               End If;
               
               If V_Por_Reduccion2 Is Not Null Then                     
                  V_Por_ReduccionF := V_Por_Reduccion2;
               End If;
                   
               If V_Importe_Lanbide2 Is Not Null Then                     
                  V_Importe_Lanbidef := V_Importe_Lanbide2;
               End If;
               
               If (V_Idsmi2 = V_Max_Idsmi_Idsmi And V_Num_Dias_Sinf Is Not Null) Or
                  ((V_Num_Dias_Sinf Is Not Null And V_Num_Dias_Sinf != 0) And (V_Importe2_Aux Is Not Null And V_Importe2_Aux != 0)) Then
                 --Nº días sin incidencia * (SMI diario/2) * (%Jornada/100) * (%Reduc. Jornada/100)
                 --V_Calculo_Formula := Round(Nvl(V_Num_Dias_Sinf, 0) * (V_Smi_Diario/2) * (Nvl(V_Por_Jornadaf, 100)/100) * (Nvl(V_Por_Reduccionf, 100)/100), 2);
				 
				-- Variables para almacenar valores, segun las condiciones de MELANBIDE58_VALOR_SMI
				v_por_ayuda_smi := 0;
				v_imp_diario_smi := 0;
				v_mensaje := 0;
				obtenerPorcentajeAyudaSUBCEESC (p_anio_sol, p_numExp, V_Nif2, v_por_ayuda_smi, v_imp_diario_smi , v_mensaje);

				-- Cambio 27/01/2020
				-- Se cambia columna % reduccion de jornada por % rendimiento
				-- Formula queda de la siguiente manera;
				-- Subvención = Nº días sin incidencia * (SMI diario*(% subvención a aplicar /100)) * (%Jornada/100) * (100 - %Rendimiento/100)

				--V_Calculo_Formula := Round(Nvl(V_Num_Dias_Sinf, 0) * (v_imp_diario_smi * (Nvl(v_por_ayuda_smi, 1))) * (Nvl(V_Por_Jornadaf, 100)/100) * --(Nvl(V_Por_Reduccionf, 0))/100), 2);
				-- 01/12/2020
                -- en los casos en los que el concepto sea ATR o INC o si es VAC y son genéricas dni=999 no se realiza el calculo, se copia el importe,
                IF V_Nif2 = '88888888' OR V_Nif2 = '99999999' THEN
                    V_Calculo_Formula :=  Nvl(V_Importe2_Aux, 0);
                Elsif V_Causa2 = 'ATR' Or V_Causa2 = 'INC' Then 
                    V_Calculo_Formula :=  Nvl(V_Importe2_Aux, 0);                    
                Else
                 V_Calculo_Formula := Round(Nvl(V_Num_Dias_Sinf, 0) * (v_imp_diario_smi * (Nvl(v_por_ayuda_smi, 1))) * (Nvl(V_Por_Jornadaf, 100)/100) * ((100-Nvl(V_Por_Reduccionf, 0))/100), 2); 					 
                End If;
                
                 Begin
                      If V_Importe_Lanbide2 Is Null Then
                         Update Melanbide58_Smi
                          Set Importe = Nvl(V_Importe2_Aux, 0),
                              Importe_Calculado = V_Calculo_Formula
                          Where Idsmi = Nvl(V_Idsmi2_Aux, V_Idsmi2);
                      Else
                          Update Melanbide58_Smi
                            Set Importe = Nvl(V_Importe2_Aux, 0),
                                Importe_Lanbide = V_Calculo_Formula
                            Where Idsmi = Nvl(V_Idsmi2_Aux, V_Idsmi2);
                      End If;
                      
                      COMMIT;
                 Exception
                      When Others Then
                               V_Retorno := 'UPDATE_RECALCULO';
                               RAISE ex_error;
                 End;
               End If;
          End Loop;
        End If;
  
        COMMIT;
        Return V_Retorno;
        
Exception
      When ex_error Then
                ROLLBACK;
                Return(V_Retorno);  
      When Others Then
                    ROLLBACK;
                    V_Retorno := Sqlerrm;
                    RETURN(V_Retorno);
        
END recalculoSubvCEESC;