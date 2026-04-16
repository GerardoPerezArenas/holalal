create or replace PROCEDURE asociarexpregistro (
    ejercicio      IN NUMBER,
    numregistro    IN NUMBER,
    numexpediente  IN VARCHAR2,
    resultado      OUT VARCHAR2,
    mensaje        OUT VARCHAR2
) IS
    rel_exr          NUMBER(1,0) DEFAULT 0;
    rel_hist_exr     NUMBER(1,0) DEFAULT 0;
    registro_ok      NUMBER(1,0) DEFAULT 0;
    expediente_ok    NUMBER(1,0) DEFAULT 0;

    departamento     NUMBER(3,0);
    uor              NUMBER(5,0);
    municipio        NUMBER(3,0);
    procedimiento    VARCHAR2(5 BYTE);
    ejercicio_exp    NUMBER(4,0);
    tipo_operacion   VARCHAR2(1 BYTE) DEFAULT '0';

    codigo           NUMBER(10,0);
    usuario          NUMBER(4,0) DEFAULT 5;
    fecha            DATE;
    tipoentidad      VARCHAR2(12 BYTE) DEFAULT 'ANOTACION';
    codentidad       VARCHAR2(30 BYTE);
    tipomov          NUMBER(2,0) DEFAULT 14;
    detalles         CLOB;

    e_noregistro     EXCEPTION;
    e_noexpediente   EXCEPTION;
    e_yarelacion     EXCEPTION;
    e_noasociado     EXCEPTION;
    e_historico      EXCEPTION;

BEGIN
    dbms_output.put_line('asociarExpRegistro:BEGIN');

-- comprobar registro asociable
    SELECT COUNT(*)
      INTO registro_ok
      FROM r_res
     WHERE res_eje = ejercicio
       AND res_num = numregistro
       AND res_tip = 'E'
       AND res_est IN (0,1);

    IF registro_ok = 0 THEN
        RAISE e_noregistro;
    END IF;

-- comprobar expediente
    SELECT COUNT(*)
      INTO expediente_ok
      FROM e_exp
     WHERE exp_num = numexpediente;

    IF expediente_ok = 0 THEN
        RAISE e_noexpediente;
    END IF;

-- comprobar relación ya existente (activo o histórico)
    SELECT COUNT(*)
      INTO rel_exr
      FROM e_exr
     WHERE exr_num = numexpediente
       AND exr_ejr = ejercicio
       AND exr_nre = numregistro
       AND exr_tip = 'E';

    SELECT COUNT(*)
      INTO rel_hist_exr
      FROM hist_e_exr
     WHERE exr_num = numexpediente
       AND exr_ejr = ejercicio
       AND exr_nre = numregistro
       AND exr_tip = 'E';

    IF rel_exr > 0 OR rel_hist_exr > 0 THEN
        RAISE e_yarelacion;
    END IF;

-- recuperar datos necesarios para la asociación
    SELECT exp_mun, exp_pro, exp_eje
      INTO municipio, procedimiento, ejercicio_exp
      FROM e_exp
     WHERE exp_num = numexpediente;

    SELECT res_dep, res_uor
      INTO departamento, uor
      FROM r_res
     WHERE res_eje = ejercicio
       AND res_num = numregistro
       AND res_tip = 'E';

-- conservar EXR_TOP del registro; si no tiene asociaciones previas usar '0'
    SELECT NVL(MAX(exr_top), '0')
      INTO tipo_operacion
      FROM e_exr
     WHERE exr_ejr = ejercicio
       AND exr_nre = numregistro
       AND exr_tip = 'E';

-- insertar relación en E_EXR
    INSERT INTO e_exr (
        exr_dep, exr_uor, exr_tip, exr_ejr, exr_nre,
        exr_ori, exr_eje, exr_num, exr_top, exr_mun,
        exr_pro, exr_origen
    ) VALUES (
        departamento, uor, 'E', ejercicio, numregistro,
        1, ejercicio_exp, numexpediente, tipo_operacion, municipio,
        procedimiento, 'FLEXIA'
    );

    IF SQL%ROWCOUNT = 0 THEN
        RAISE e_noasociado;
    END IF;

    dbms_output.put_line('==> Asociado el expediente ' || numexpediente || ' al registro ' || ejercicio || '/' || numregistro);

-- marcar registro como asociado
    UPDATE r_res
       SET res_est = 1
     WHERE res_eje = ejercicio
       AND res_num = numregistro
       AND res_tip = 'E';

    dbms_output.put_line('==> Marcado como asociado el registro ' || ejercicio || '/' || numregistro);

    IF SQL%ROWCOUNT = 0 THEN
        RAISE e_noasociado;
    END IF;

-- grabar en histórico
    BEGIN
        SELECT res_dep INTO departamento
          FROM r_res
         WHERE res_eje = ejercicio
           AND res_num = numregistro
           AND res_tip = 'E';

        SELECT res_uor INTO uor
          FROM r_res
         WHERE res_eje = ejercicio
           AND res_num = numregistro
           AND res_tip = 'E';

        codigo := getmaximocodigohistorico;
        fecha := sysdate;
        codentidad := departamento || '/' || uor || '/E/' || ejercicio || '/' || numregistro;
        detalles := '<DescripcionMovimiento><AdjuntarExpediente><expedienteAdjunto>'
                    || numexpediente
                    || '</expedienteAdjunto></AdjuntarExpediente></DescripcionMovimiento>';

        INSERT INTO r_historico
        VALUES (codigo, usuario, fecha, tipoentidad, codentidad, tipomov, detalles);

        IF SQL%ROWCOUNT = 0 THEN
            RAISE e_historico;
        END IF;

        INSERT INTO r_historico_prev
        VALUES (codigo, usuario, fecha, tipoentidad, codentidad, tipomov);

        IF SQL%ROWCOUNT = 0 THEN
            RAISE e_historico;
        END IF;

        resultado := 'OK';
        mensaje := ' ==> Asociado el expediente ' || numexpediente || ' al registro ' || ejercicio || '/' || numregistro;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            resultado := 'KO';
            dbms_output.put_line('  =====> No se ha recuperado el departamento o UOR del registro ' || ejercicio || '/' || numregistro);
            mensaje := 'No se ha recuperado el departamento o UOR del registro ' || ejercicio || '/' || numregistro;
    END;

    dbms_output.put_line('asociarExpRegistro:END');

EXCEPTION
    WHEN e_noregistro THEN
        dbms_output.put_line('  =====> El registro ' || ejercicio || '/' || numregistro || ' no existe o no está en estado asociable');
        resultado := 'KO';
        mensaje := 'El registro ' || ejercicio || '/' || numregistro || ' no existe o no está en estado asociable';
    WHEN e_noexpediente THEN
        dbms_output.put_line('  =====> No existe el expediente ' || numexpediente);
        resultado := 'KO';
        mensaje := 'No existe el expediente ' || numexpediente;
    WHEN e_yarelacion THEN
        dbms_output.put_line('  =====> El expediente ' || numexpediente || ' ya tiene relación con el registro ' || ejercicio || '/' || numregistro);
        resultado := 'KO';
        mensaje := 'El expediente ' || numexpediente || ' ya tiene relación con el registro ' || ejercicio || '/' || numregistro;
    WHEN e_noasociado THEN
        dbms_output.put_line('  =====> No se ha podido asociar el expediente ' || numexpediente || ' al registro ' || ejercicio || '/' || numregistro);
        resultado := 'KO';
        mensaje := 'No se ha podido asociar el expediente ' || numexpediente || ' al registro ' || ejercicio || '/' || numregistro;
    WHEN e_historico THEN
        dbms_output.put_line('  =====> ERROR al insertar movimiento en el histórico del registro ' || ejercicio || '/' || numregistro);
        resultado := 'KO';
        mensaje := 'ERROR al insertar movimiento en el histórico del registro ' || ejercicio || '/' || numregistro;
    WHEN OTHERS THEN
        dbms_output.put_line('  =====> ERROR al asociar el expediente ' || numexpediente || ' al registro ' || ejercicio || '/' || numregistro || ' : ' || sqlcode);
        dbms_output.put_line(sqlerrm);
        dbms_output.put_line('------');
        resultado := 'KO';
        mensaje := 'ERROR al asociar el expediente ' || numexpediente || ' al registro ' || ejercicio || '/' || numregistro || ' : ' || sqlerrm;
END;
/
