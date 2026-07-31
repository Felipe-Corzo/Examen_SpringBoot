package com.logitrack.examen.dto;

import com.logitrack.model.TipoMovimiento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoReporteDTO {

    private Long id;
    private LocalDateTime fecha;
    private TipoMovimiento tipoMovimiento;
    private String usuarioNombre;
    private Long bodegaOrigenId;
    private String bodegaOrigenNombre;
    private Long bodegaDestinoId;
    private String bodegaDestinoNombre;
    private List<DetalleReporteDTO> detalles;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetalleReporteDTO {
        private Long productoId;
        private String productoNombre;
        private String productoCategoria;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }
}

