# Tarea: asociación múltiple de registro a expediente (`asociarexpregistro`)

## Objetivo
Permitir asociar un mismo registro de entrada a **N expedientes**, dejando trazabilidad en histórico.

## Contexto funcional
- El flujo `WsTramitacion.asociarExpRegistro()` no permitía el caso de múltiples asociaciones para un mismo registro.
- Se implementa un procedure PL/SQL reutilizable para ejecutar la asociación desde BD y auditar el movimiento.

## Artefacto implementado
- Script: `asociarexpregistro.sql`
- Ruta: `01_docs_y_arquitectura/scripts/extension/melanbide77/asociarexpregistro.sql`

## Lógica implementada
1. Valida registro existente y en estado asociable (`res_est IN (0,1)` para soportar segunda/tercera asociación).
2. Valida expediente existente.
3. Evita duplicado del mismo par expediente/registro (`e_exr` + `hist_e_exr`).
4. Inserta asociación en `e_exr`.
5. Actualiza `r_res.res_est = 1`.
6. Inserta histórico en `r_historico` y `r_historico_prev` con `tipomov = 14`.
7. Devuelve `resultado` y `mensaje` (`OK` / `KO`).

## Ajustes realizados durante validación
- Se detectó que `EXR_TOP` puede variar según el registro (ej.: `0` y `1` en datos reales); el procedure reutiliza el `EXR_TOP` existente para ese registro y, si no hay asociaciones previas, usa `0` por defecto.
- Se detectó KO en segunda asociación por validación estricta de pendiente; se cambió la validación de `res_est = 0` a `res_est IN (0,1)`.

## Evidencias de prueba funcional en BD
### 1) Control de duplicados (esperado: KO)
- Caso probado: `2026/197` + `2025/RGI/000004`
- Resultado: `KO`
- Mensaje: `El expediente 2025/RGI/000004 ya está asociado al registro 2026/197`

### 2) Asociación adicional del mismo registro (esperado: OK)
- Caso probado: `2026/197` + `2006/CEI/000004`
- Resultado: `OK`
- Mensaje: `==> Asociado el expediente 2006/CEI/000004 al registro 2026/197`

### 3) Verificación de múltiples asociaciones en `e_exr`
- `2026 | 197 | 2006/CEI/000004 | E | 1 | FLEXIA`
- `2026 | 197 | 2025/RGI/000003 | E | 1 | FLEXIA`
- `2026 | 197 | 2025/RGI/000004 | E | 1 | FLEXIA`

### 4) Verificación de histórico
- Último movimiento observado para `1/0/E/2026/197`: `TIPOMOV = 14` (fecha 08/04/2026).

## Estado final
- Requisito funcional validado: **un registro puede asociarse a varios expedientes**, con control de duplicado por par expediente/registro y trazabilidad en histórico.

## Pasos sencillos para probar en SQL
1. **Compilar el procedure**
   ```sql
   @01_docs_y_arquitectura/scripts/extension/melanbide77/asociarexpregistro.sql
   SHOW ERRORS PROCEDURE asociarexpregistro;
   ```

2. **Verificar que existe y está válido**
   ```sql
   SELECT object_name, status
   FROM user_objects
   WHERE object_type = 'PROCEDURE'
     AND object_name = 'ASOCIAREXPREGISTRO';
   ```

3. **Preparar variables de salida**
   ```sql
   SET SERVEROUTPUT ON;
   VAR v_resultado VARCHAR2(10);
   VAR v_mensaje   VARCHAR2(4000);
   ```

4. **Llamar al procedure** (ajustar valores)
   ```sql
   BEGIN
     FLBPRU.asociarexpregistro(
       ejercicio     => 2026,
       numregistro   => 197,
       numexpediente => '2006/CEI/000004',
       resultado     => :v_resultado,
       mensaje       => :v_mensaje
     );
   END;
   /
   ```

5. **Ver resultado funcional**
   ```sql
   PRINT v_resultado;
   PRINT v_mensaje;
   ```
   - Esperado: `OK` si la asociación es nueva.
   - Esperado: `KO` si ese expediente ya estaba asociado al registro.

6. **Verificar datos insertados**
   ```sql
   SELECT exr_ejr, exr_nre, exr_num, exr_tip, exr_top, exr_origen
   FROM e_exr
   WHERE exr_ejr = 2026 AND exr_nre = 197 AND exr_tip = 'E'
   ORDER BY exr_num;

   SELECT codigo, fecha, tipomov, codentidad
   FROM r_historico_prev
   WHERE codentidad LIKE '%/E/2026/197'
   ORDER BY fecha DESC;
   ```
