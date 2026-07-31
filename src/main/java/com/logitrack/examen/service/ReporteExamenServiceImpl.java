package com.logitrack.examen.service;

import com.logitrack.examen.specification.MovimientoSpecification;
import com.logitrack.model.Auditoria;
import com.logitrack.model.MovimientoInventario;
import com.logitrack.model.TipoMovimiento;
import com.logitrack.repository.AuditoriaRepository;
import com.logitrack.repository.MovimientoInventarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReporteExamenServiceImpl implements ReporteExamenService {

    private final MovimientoInventarioRepository movimientoRepository;
    private final AuditoriaRepository auditoriaRepository;

    public ReporteExamenServiceImpl(
            MovimientoInventarioRepository movimientoRepository,
            AuditoriaRepository auditoriaRepository) {

        this.movimientoRepository = movimientoRepository;
        this.auditoriaRepository = auditoriaRepository;
    }

    @Override
public List<MovimientoInventario> obtenerMovimientos(
        Long bodegaId,
        Long productoId,
        TipoMovimiento tipoMovimiento,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin) {

    return movimientoRepository.findAll(
        MovimientoSpecification.filtrar(
                bodegaId,
                productoId,
                tipoMovimiento,
                fechaInicio,
                fechaFin
        )
);
}

    @Override
    public List<Auditoria> obtenerAuditorias(
            Long productoId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            String campoModificado) {

        return auditoriaRepository.buscarAuditoriasConFiltros(
                productoId,
                fechaInicio,
                fechaFin,
                campoModificado
        );
    }
}