create or replace
Function Recalculosubvleaukcen(P_Numexp In Varchar2, P_Anio_Sol In Varchar2, P_Id Number) Return Varchar2 As 


  V_Retorno             Varchar2(1000);
  V_Cociente            Pls_Integer := 0;
  V_Resto               Pls_Integer := 0;
  V_String_Calculo      VARCHAR2(4000);
  V_Imp_Mas_20_Contr    Melanbide67_Subv_Centros.Imp_Ofer_Captadas_Reg%Type;
  V_Imp_Mas_20_Ofer     Melanbide67_Subv_Centros.Imp_Ofer_Gestionadas%Type;
  Ex_Error              Exception;
  
  
  --Variables referentes a los importes subvencionados (tabla Melanbide67_Subv_Centros).
  V_Imp_Ofer_Captadas_Reg     Melanbide67_Subv_Centros.Imp_Ofer_Captadas_Reg%Type;
  V_Imp_Ofer_Gestionadas      Melanbide67_Subv_Centros.Imp_Ofer_Gestionadas%Type;
  V_Num_Contratos_Incr        Melanbide67_Subv_Centros.Num_Contratos_Incr%Type;
  V_Porc_Incremento           Melanbide67_Subv_Centros.Porc_Incremento%Type;
  
  --Variables para la tabla Melanbide67_Empresas
  V_Id                        Melanbide67_Centros.Id%Type;
  V_Oferta_Empleo             Melanbide67_Centros.Oferta_Empleo%Type;
  V_Num_Ofertas_Sol           Melanbide67_Centros.Num_Ofertas_Sol%Type;
  V_Num_Ofertas_Con           Melanbide67_Centros.Num_Ofertas_Con%Type;
  V_Num_Contr_Sol             Melanbide67_Centros.Num_Contr_Sol%Type;
  V_Num_Contr_Con             Melanbide67_Centros.Num_Contr_Con%Type;
  V_Importe_Subv              Melanbide67_Centros.Importe_Subv%Type;
  V_Importe_Subv_Sol          Melanbide67_Centros.Importe_Subv_Sol%Type;
  V_Importe_Subv_Con          Melanbide67_Centros.Importe_Subv_Con%Type;
  V_Observaciones             Melanbide67_Centros.Observaciones%Type;
  
Cursor C_Centros Is
                  Select Id,
                         Oferta_Empleo,
                         Num_Ofertas_Sol,
                         Num_Ofertas_Con,
                         Num_Contr_Sol,
                         Num_Contr_Con,
                         Importe_Subv,
                         Importe_Subv_Sol,
                         Importe_Subv_Con,
                         Observaciones
                  From  Melanbide67_Centros
                  Where Num_Exp = P_Numexp
                  And   Id      = Nvl(P_Id, Id)
                  Order by Id;                         
Begin
    V_Retorno := 'OK';
    --Se obtienen los importes de subvención por tipo de gestión del centro.  
      Begin
          Select Imp_Ofer_Captadas_Reg,
                 Imp_Ofer_Gestionadas,
                 Num_Contratos_Incr,
                 Porc_Incremento
          Into  V_Imp_Ofer_Captadas_Reg,
                V_Imp_Ofer_Gestionadas,
                V_Num_Contratos_Incr,
                V_Porc_Incremento
          From  Melanbide67_Subv_Centros
          Where Anio = P_Anio_Sol;
          
       Exception
            When No_Data_Found Then
                    V_Retorno := 'NOT_FOUND_IMP_LEAUK';
                    Raise Ex_Error;
            When Others Then
                    V_Retorno := Sqlerrm;
                    Raise Ex_Error;
       End;
       
  --Abre cursor de centros para calcular la subvención por tipo de gestión realizada por el centro colaborador.
   Open C_Centros;
      Loop
          Fetch C_Centros
          Into  V_Id,
                V_Oferta_Empleo,
                V_Num_Ofertas_Sol,
                V_Num_Ofertas_Con,
                V_Num_Contr_Sol,
                V_Num_Contr_Con,
                V_Importe_Subv,
                V_Importe_Subv_Sol,
                V_Importe_Subv_Con,
                V_Observaciones;
          Exit When C_Centros%Notfound;

            V_String_Calculo := Null;  

            --Por cada 20 (campo Num_Contratos_Incr) contratos firmados se incrementa un % (campo Porc_Incremento) el importe de la subvención.
            --El resto de la división por 20 se subvencionan con el importe establecido (campo Importe_Subv).
            If V_Num_Contr_Con >= 20 Then
                  V_Cociente := Trunc(V_Num_Contr_Con / V_Num_Contratos_Incr);--Cociente del número de contratos entre el número de contratos de la promoción
                  V_Resto    := Mod(V_Num_Contr_Con, V_Num_Contratos_Incr);--Resto de la división del número de contratos entre el número de contratos de la promoción
                  V_Importe_Subv_Con := V_Importe_Subv * V_Resto;--Se calcula el importe de los contratos del resto (no llegan al número que indica el campo Num_Contratos_Incr)
                  V_String_Calculo := 'El cálculo realizado es el siguiente para ' || V_Num_Contr_Con ||' contratos -->' || V_Importe_Subv || ' * ' || V_Resto || ' = ' || V_Importe_Subv_Con;
                  V_Importe_Subv := (V_Importe_Subv * V_Cociente * V_Porc_Incremento / 100) + V_Importe_Subv;--Se calcula el incremento porcentual de la subvención.
                  V_Importe_Subv_Con :=  V_Importe_Subv_Con + (V_Importe_Subv * (V_Num_Contr_Con - V_Resto));--Se multiplica por el número de contratos y se le suma la anterior cantidad
                  V_String_Calculo := V_String_Calculo || ' + ' || V_Importe_Subv  || '(importe unitario incrementado en un ' || To_Char(V_Cociente * V_Porc_Incremento) ||'%) * ' || To_Char((V_Num_Contr_Con - V_Resto)) || ' = ' || V_Importe_Subv_Con;
            Else--Número de contratos por importe establecido (campo Importe_Subv).
              V_Importe_Subv_Con := V_Importe_Subv * V_Num_Contr_Con;
            End If;
              
            Begin
              Update Melanbide67_Centros
              Set Importe_Subv_Con = V_Importe_Subv_Con,
                  Observaciones = Decode(V_String_Calculo, Null, Observaciones, Decode(Observaciones, Null, V_String_Calculo, Observaciones || Chr(13) || V_String_Calculo))
              Where Id = V_Id;
            Exception
              When Others Then
                    V_Retorno := Sqlerrm;
            End;
      End Loop;
      Close C_Centros;
      
      Commit;
      Return v_retorno;
        
Exception
      When ex_error Then
                Return(V_Retorno);  
      When Others Then
                    V_Retorno := Sqlerrm;
                    Return(V_Retorno);
        
END Recalculosubvleaukcen;