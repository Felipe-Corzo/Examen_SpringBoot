package com.logitrack.examen.controller;

import com.logitrack.examen.service.ReporteExamenService;
import com.logitrack.model.Auditoria;
import com.logitrack.model.MovimientoInventario;
import com.logitrack.model.TipoMovimiento;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReporteExamenController {

    private final ReporteExamenService reporteExamenService;

    public ReporteExamenController(ReporteExamenService reporteExamenService) {
        this.reporteExamenService = reporteExamenService;
    }

    @GetMapping("/movimientos")
    public ResponseEntity<List<MovimientoInventario>> obtenerMovimientos(

            @RequestParam(required = false)
            Long bodegaId,

            @RequestParam(required = false)
            Long productoId,

            @RequestParam(required = false)
            TipoMovimiento tipoMovimiento,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fechaInicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fechaFin) {

        return ResponseEntity.ok(
                reporteExamenService.obtenerMovimientos(
                        bodegaId,
                        productoId,
                        tipoMovimiento,
                        fechaInicio,
                        fechaFin
                )
        );
    }

    @GetMapping("/auditoria")
    public ResponseEntity<List<Auditoria>> obtenerAuditorias(

            @RequestParam(required = false)
            Long productoId,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fechaInicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fechaFin,

            @RequestParam(required = false)
            String campoModificado) {

        return ResponseEntity.ok(
                reporteExamenService.obtenerAuditorias(
                        productoId,
                        fechaInicio,
                        fechaFin,
                        campoModificado
                )
        );
    }
}