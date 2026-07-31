package com.logitrack.examen.controller;

import com.logitrack.examen.dto.AuditoriaReporteDTO;
import com.logitrack.examen.dto.MovimientoReporteDTO;
import com.logitrack.examen.service.ReporteExamenService;
import com.logitrack.model.TipoMovimiento;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReporteExamenController {

    private final ReporteExamenService reporteExamenService;

    public ReporteExamenController(ReporteExamenService reporteExamenService) {
        this.reporteExamenService = reporteExamenService;
    }

    /**
     * GET /api/reportes/movimientos
     * Devuelve movimientos de inventario filtrados por:
     * - bodega (opcional)
     * - producto (opcional)
     * - tipoMovimiento (opcional: ENTRADA, SALIDA, AJUSTE)
     * - rango de fechas (fechaInicio, fechaFin, opcional)
     */
    @GetMapping("/movimientos")
    public ResponseEntity<List<MovimientoReporteDTO>> obtenerMovimientos(
            @RequestParam(required = false) Long bodega,
            @RequestParam(required = false) Long producto,
            @RequestParam(required = false) TipoMovimiento tipoMovimiento,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        List<MovimientoReporteDTO> resultados = reporteExamenService.obtenerMovimientosFiltrados(
                bodega, producto, tipoMovimiento, fechaInicio, fechaFin);
        return ResponseEntity.ok(resultados);
    }

    /**
     * GET /api/reportes/auditoria
     * Devuelve registros de auditoría filtrados por:
     * - producto (opcional)
     * - fechaCambio (rango opcional: fechaInicio, fechaFin)
     * - campoModificado (opcional)
     */
    @GetMapping("/auditoria")
    public ResponseEntity<List<AuditoriaReporteDTO>> obtenerAuditoria(
            @RequestParam(required = false) Long producto,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam(required = false) String campoModificado) {

        List<AuditoriaReporteDTO> resultados = reporteExamenService.obtenerAuditoriasFiltradas(
                producto, fechaInicio, fechaFin, campoModificado);
        return ResponseEntity.ok(resultados);
    }
}
