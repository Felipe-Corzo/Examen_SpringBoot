package com.logitrack.examen.dto;

import com.logitrack.model.TipoOperacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditoriaReporteDTO {

    private Long id;
    private TipoOperacion tipoOperacion;
    private LocalDateTime fechaHora;
    private String usuarioNombre;
    private Long usuarioId;
    private String entidadAfectada;
    private Long entidadId;
    private String campoModificado;
    private String valoresAnteriores;
    private String valoresNuevos;
}

