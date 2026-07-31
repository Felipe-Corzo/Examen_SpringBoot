package com.logitrack.examen.service;

import com.logitrack.examen.dto.AuditoriaReporteDTO;
import com.logitrack.examen.dto.MovimientoReporteDTO;
import com.logitrack.examen.specification.AuditoriaSpecification;
import com.logitrack.examen.specification.MovimientoInventarioSpecification;
import com.logitrack.model.Auditoria;
import com.logitrack.model.MovimientoDetalle;
import com.logitrack.model.MovimientoInventario;
import com.logitrack.model.TipoMovimiento;
import com.logitrack.repository.AuditoriaRepository;
import com.logitrack.repository.MovimientoInventarioRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReporteExamenService {

    private final MovimientoInventarioRepository movimientoRepository;
    private final AuditoriaRepository auditoriaRepository;

    public ReporteExamenService(MovimientoInventarioRepository movimientoRepository,
                                AuditoriaRepository auditoriaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.auditoriaRepository = auditoriaRepository;
    }

    /**
     * Obtiene movimientos de inventario filtrados por los parámetros proporcionados.
     * Todos los parámetros son opcionales.
     * <p>
     * Utiliza JPA Criteria API (Specification) en lugar de JPQL con
     * {@code (? IS NULL OR column = ?)} para evitar el error de PostgreSQL:
     * {@code could not determine data type of parameter}.
     * </p>
     */
    public List<MovimientoReporteDTO> obtenerMovimientosFiltrados(
            Long bodegaId,
            Long productoId,
            TipoMovimiento tipoMovimiento,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin) {

        List<MovimientoInventario> movimientos = movimientoRepository.findAll(
                MovimientoInventarioSpecification.conFiltros(
                        bodegaId, productoId, tipoMovimiento, fechaInicio, fechaFin),
                Sort.by(Sort.Direction.DESC, "fecha"));

        if (movimientos == null || movimientos.isEmpty()) {
            return Collections.emptyList();
        }

        return movimientos.stream()
                .map(this::convertirMovimientoADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene registros de auditoría filtrados por los parámetros proporcionados.
     * Todos los parámetros son opcionales.
     * <p>
     * Utiliza JPA Criteria API (Specification) en lugar de JPQL con
     * {@code (? IS NULL OR column = ?)} para evitar el error de PostgreSQL:
     * {@code could not determine data type of parameter}.
     * </p>
     */
    public List<AuditoriaReporteDTO> obtenerAuditoriasFiltradas(
            Long productoId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            String campoModificado) {

        List<Auditoria> auditorias = auditoriaRepository.findAll(
                AuditoriaSpecification.conFiltros(
                        productoId, campoModificado, fechaInicio, fechaFin),
                Sort.by(Sort.Direction.DESC, "fechaHora"));

        if (auditorias == null || auditorias.isEmpty()) {
            return Collections.emptyList();
        }

        return auditorias.stream()
                .map(this::convertirAuditoriaADTO)
                .collect(Collectors.toList());
    }

    private MovimientoReporteDTO convertirMovimientoADTO(MovimientoInventario m) {
        List<MovimientoReporteDTO.DetalleReporteDTO> detallesDTO = Optional.ofNullable(m.getDetalles())
                .orElse(Collections.emptyList())
                .stream()
                .map(this::convertirDetalleADTO)
                .collect(Collectors.toList());

        return MovimientoReporteDTO.builder()
                .id(m.getId())
                .fecha(m.getFecha())
                .tipoMovimiento(m.getTipoMovimiento())
                .usuarioNombre(m.getUsuario() != null ? m.getUsuario().getUsername() : null)
                .bodegaOrigenId(m.getBodegaOrigen() != null ? m.getBodegaOrigen().getId() : null)
                .bodegaOrigenNombre(m.getBodegaOrigen() != null ? m.getBodegaOrigen().getNombre() : null)
                .bodegaDestinoId(m.getBodegaDestino() != null ? m.getBodegaDestino().getId() : null)
                .bodegaDestinoNombre(m.getBodegaDestino() != null ? m.getBodegaDestino().getNombre() : null)
                .detalles(detallesDTO)
                .build();
    }

    private MovimientoReporteDTO.DetalleReporteDTO convertirDetalleADTO(MovimientoDetalle d) {
        BigDecimal precioUnitario = d.getProducto() != null && d.getProducto().getPrecio() != null
                ? d.getProducto().getPrecio()
                : BigDecimal.ZERO;
        Integer cantidad = d.getCantidad() != null ? d.getCantidad() : 0;
        BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

        return MovimientoReporteDTO.DetalleReporteDTO.builder()
                .productoId(d.getProducto() != null ? d.getProducto().getId() : null)
                .productoNombre(d.getProducto() != null ? d.getProducto().getNombre() : null)
                .productoCategoria(d.getProducto() != null ? d.getProducto().getCategoria() : null)
                .cantidad(cantidad)
                .precioUnitario(precioUnitario)
                .subtotal(subtotal)
                .build();
    }

    private AuditoriaReporteDTO convertirAuditoriaADTO(Auditoria a) {
        return AuditoriaReporteDTO.builder()
                .id(a.getId())
                .tipoOperacion(a.getTipoOperacion())
                .fechaHora(a.getFechaHora())
                .usuarioNombre(a.getUsuario() != null ? a.getUsuario().getUsername() : null)
                .usuarioId(a.getUsuario() != null ? a.getUsuario().getId() : null)
                .entidadAfectada(a.getEntidadAfectada())
                .entidadId(a.getEntidadId())
                .campoModificado(a.getCampoModificado())
                .valoresAnteriores(a.getValoresAnteriores())
                .valoresNuevos(a.getValoresNuevos())
                .build();
    }
}
