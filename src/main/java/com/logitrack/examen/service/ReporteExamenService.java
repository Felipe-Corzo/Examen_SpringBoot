package com.logitrack.examen.service;

import com.logitrack.model.Auditoria;
import com.logitrack.model.MovimientoInventario;
import com.logitrack.model.TipoMovimiento;

import java.time.LocalDateTime;
import java.util.List;

public interface ReporteExamenService {

    List<MovimientoInventario> obtenerMovimientos(
            Long bodegaId,
            Long productoId,
            TipoMovimiento tipoMovimiento,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin
    );

    List<Auditoria> obtenerAuditorias(
            Long productoId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            String campoModificado
    );
}